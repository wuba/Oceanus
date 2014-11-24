/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.oceanus.exchange.nodes.dml.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.resource.TableDescription;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.AnalyzeResult.SqlValueItem;
import com.bj58.oceanus.core.shard.AnalyzerCallback;
import com.bj58.oceanus.core.shard.HavingInfo;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.exchange.nodes.AbstractNodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.NodeHelper;
import com.bj58.oceanus.exchange.unparse.NodeTreeToSql;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.AggregateNode;
import com.bj58.sql.parser.ColumnReference;
import com.bj58.sql.parser.CursorNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.NumericConstantNode;
import com.bj58.sql.parser.ParameterNode;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.ResultColumn;
import com.bj58.sql.parser.SelectNode;
import com.bj58.sql.parser.ValueNode;

/**
 * SQL节点解析器:CursorNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CursorNodeAnalyzer extends
		AbstractNodeAnalyzer<CursorNode, AnalyzeResult> {
	static Logger logger = LoggerFactory.getLogger(CursorNodeAnalyzer.class);
	final NodeTreeToSql nodeToStringBuilder = new NodeTreeToSql();
	int[] nodeTypes = { NodeTypes.CURSOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(CursorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(node);
		result.setStatementType(StatementType.SELECT);
		QueryTreeNode treeNode = node.getOrderByList();

		if (treeNode != null) {// parse order by
			AnalyzeResult orderByResult = Analyzers.get(treeNode.getNodeType())
					.analyze(treeNode);
			result.addOrderByColumns(orderByResult.getOrderByColumns());

		}
		SelectNode selectNode = (SelectNode) node.getResultSetNode();
		result.setDistinct(selectNode.isDistinct());
		treeNode = selectNode.getGroupByList();
		if (treeNode != null) {
			AnalyzeResult groupByResult = Analyzers.get(treeNode.getNodeType())
					.analyze(treeNode);
			result.addGroupByColumns(groupByResult.getGroupByColumns());

		}

		treeNode = selectNode.getResultColumns();
		if (treeNode != null) {
			AnalyzeResult resultCols = Analyzers.get(treeNode.getNodeType())
					.analyze(treeNode);
			result.addResultColumns(resultCols.getResultColumns());
		}
		this.analyzeAndMergeResult(result, selectNode);
		if (node.getFetchFirstClause() != null) {
			result.setLimit(parseLimitOffset(node.getFetchFirstClause()));
		}
		if (node.getOffsetClause() != null) {
			result.setOffset(parseLimitOffset(node.getOffsetClause()));
		}
		this.processAverageAndLimit(result, node);
		this.processOrderByAndGroupByIndex(result);
		this.processAggregate(result);
		ValueNode havingNode = selectNode.getHavingClause();
		if (havingNode != null) {
			HavingInfo havingInfo = null;
			try {
				havingInfo = this.parseHavingInfo(havingNode, selectNode);
			} catch (StandardException e) {
				logger.error("parsing having error!", e);
				throw new ShardException("shard error!", e);
			}
			result.setHavingInfo(havingInfo);
		}
		return result;
	}

	/**
	 * 处理数值情况
	 * 
	 * @param node
	 * @return
	 */
	private SqlValueItem parseLimitOffset(ValueNode node) {
		if (node instanceof NumericConstantNode) {
			return parseLimitOffset((NumericConstantNode) node);
		}
		if (node instanceof ParameterNode) {
			return parseLimitOffset((ParameterNode) node);
		}
		throw new IllegalArgumentException("node=" + node);
	}

	/**
	 * 处理数值情况
	 * 
	 * @param node
	 * @return
	 */
	private SqlValueItem parseLimitOffset(NumericConstantNode node) {
		SqlValueItem item = new SqlValueItem();
		item.setValue((Integer) node.getValue());
		return item;
	}

	private SqlValueItem parseLimitOffset(ParameterNode node) {
		SqlValueItem item = new SqlValueItem();
		item.setParameterIndex(node.getParameterNumber()+1);
		return item;
	}

	private void processAggregate(AnalyzeResult result) {
		Collection<TableColumn> resultColumns = result.getResultColumns();
		Collection<TableColumn> appendColumns = result.getAppendResultColumns();
		for (TableColumn column : resultColumns) {
			if (column.getAggregate() != null
					&& column.getAggregate().trim().length() > 0) {
				result.getAggregateColumns().add(column);
			}
		}
		for (TableColumn column : appendColumns) {
			if (column.getAggregate() != null
					&& column.getAggregate().trim().length() > 0) {
				result.getAggregateColumns().add(column);
			}
		}

	}

	private TableInfo getTableInfo(DefaultAnalyzeResult result) {
		return result.getTableInfos().iterator().next();
	}

	private void processOrderByAndGroupByIndex(DefaultAnalyzeResult result) {
		TableInfo tableInfo = this.getTableInfo(result);

		// 如果table不在配置中，就不做order by和group by的字段校验
		TableDescription desc = Configurations.getInstance()
				.getTableDescription(tableInfo.getName());
		if (desc == null) {
			return;
		}

		Collection<TableColumn> orderByColumns = result.getOrderByColumns();

		Collection<TableColumn> groupByColumns = result.getGroupByColumns();
		if ((!orderByColumns.isEmpty() || !groupByColumns.isEmpty())
				&& result.getTableInfos().size() > 1) {
			throw new ShardException("do not support muti table operation!");
		}

		if (!orderByColumns.isEmpty()) {
			for (TableColumn column : orderByColumns) {
				TableColumn resultColumn = getResultColumn(tableInfo,
						column.getName(), result);
				if (resultColumn == null) {
					throw new ShardException(
							"order by column not found!column name="
									+ column.getName());
				}
				column.setResultIndex(resultColumn.getResultIndex());
			}
		}
		if (!groupByColumns.isEmpty()) {
			for (TableColumn column : groupByColumns) {
				TableColumn resultColumn = getResultColumn(tableInfo,
						column.getName(), result);
				if (resultColumn == null) {
					throw new ShardException(
							"order by column not found!column name="
									+ column.getName());
				}
				column.setResultIndex(resultColumn.getResultIndex());
			}
		}

	}

	private TableColumn getResultColumn(TableInfo table, String name,
			DefaultAnalyzeResult result) {
		Collection<TableColumn> resultColumns = result.getResultColumns();
		if (resultColumns.size() == 1) {
			TableColumn c = resultColumns.iterator().next();
			if ("*".equalsIgnoreCase(c.getName())) {
				try {
					resultColumns = Arrays.asList(NodeHelper.getColumns(table));
				} catch (SQLException e) {
					logger.error("get table columns error!", e);
				}
			}
		}
		for (TableColumn item : resultColumns) {
			if (name.equalsIgnoreCase(item.getName())
					|| name.equalsIgnoreCase(item.getAliasName())
					|| name.equalsIgnoreCase(item.getAggregate())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 查询语句中存在以下情况，则会改变原有语句 1.
	 * 
	 * @param result
	 * @param statementNode
	 */
	void processAverageAndLimit(final DefaultAnalyzeResult result,
			final CursorNode statementNode) {
		Collection<TableColumn> resultColumns = result.getResultColumns();
		Map<String, TableColumn> columnsHolder = new HashMap<String, TableColumn>(
				resultColumns.size());
		List<TableColumn> averageList = new ArrayList<TableColumn>();
		Iterator<TableColumn> it = resultColumns.iterator();
		while (it.hasNext()) {
			TableColumn column = it.next();
			if (column.getAggregate() != null) {

				if ("AVG".equalsIgnoreCase(column.getAggregate())
						|| "AVERAGE".equalsIgnoreCase(column.getAggregate())) {
					averageList.add(column);
				} else {
					String key = (column.getName() + "." + column
							.getAggregate()).toUpperCase();
					columnsHolder.put(key, column);
				}
			}
		}
		final List<ResultColumn> appendColumns = new ArrayList<ResultColumn>();
		if (!averageList.isEmpty()) {
			int index = result.getResultColumns().size();
			for (TableColumn column : averageList) {

				String countKey = (column.getName() + "." + "COUNT")
						.toUpperCase();
				String sumKey = (column.getName() + "." + "SUM").toUpperCase();
				TableColumn countColumn = columnsHolder.get(countKey);
				TableColumn sumColumn = columnsHolder.get(sumKey);
				if (countColumn == null) {
					try {
						final ResultColumn countNode = this.create(
								column.getName(), "CountAggregateDefinition",
								"COUNT");

						appendColumns.add(countNode);
						TableColumn appendColumn = NodeHelper
								.parseResultColumn(countNode);
						appendColumn.setResultIndex(++index);
						columnsHolder.put(countKey, appendColumn);
						result.addAppendResultColumns(appendColumn);
					} catch (Exception e) {
						logger.error("append node error!", e);
					}
				}
				if (sumColumn == null) {
					try {
						final ResultColumn sumNode = this.create(
								column.getName(), "SumAvgAggregateDefinition",
								"SUM");
						appendColumns.add(sumNode);
						TableColumn appendColumn = NodeHelper
								.parseResultColumn(sumNode);
						appendColumn.setResultIndex(++index);
						columnsHolder.put(sumKey, appendColumn);
						result.addAppendResultColumns(appendColumn);
					} catch (Exception e) {
						logger.error("append node error!", e);
					}
				}

			}
		}
		AnalyzerCallback callback = new AnalyzerCallback() {

			@Override
			public void call() {
				for (ResultColumn columnNode : appendColumns) {
					statementNode.getResultSetNode().getResultColumns()
							.addResultColumn(columnNode);
				}
				ValueNode limitNode=statementNode.getFetchFirstClause();
				ValueNode offsetNode=statementNode.getOffsetClause();
				if (limitNode!= null&&limitNode instanceof NumericConstantNode) {//处理分页逻辑，假如是数值的话，修改limit值为limit＋offset
					NumericConstantNode valueNode = (NumericConstantNode) statementNode
							.getFetchFirstClause();
					int limit = result.getLimit().getValue();
					if (statementNode.getOffsetClause() != null) {
						limit += result.getOffset().getValue();
					}
					valueNode.setValue(limit);
				}
				if ( offsetNode!= null&&offsetNode instanceof NumericConstantNode) {//处理分页逻辑，假如是数值的话，修改offset值为0
					NumericConstantNode valueNode = (NumericConstantNode) statementNode
							.getOffsetClause();
					valueNode.setValue(0);
				}

				// TODO: 在多库路由的情况下，ParameterNode 不能明确

			}

		};
		result.addAnalyzerCallback(callback);

	}

	ResultColumn create(String columnName, String aggregateClaz,
			String aggregateName) throws Exception {
		ResultColumn resultColumn = new ResultColumn();
		AggregateNode aggregateNode = new AggregateNode();
		aggregateNode.setNodeType(NodeTypes.AGGREGATE_NODE);// SumAvgAggregateDefinition,MaxMinAggregateDefinition,CountAggregateDefinition
		aggregateNode.init(null, aggregateClaz, false, aggregateName);
		resultColumn.init(null, aggregateNode);
		resultColumn.setNodeType(NodeTypes.RESULT_COLUMN);
		if (columnName != null) {
			ColumnReference ref = new ColumnReference();
			ref.init(columnName, null);
			ref.setNodeType(NodeTypes.COLUMN_REFERENCE);
			aggregateNode.setOperand(ref);
		}

		return resultColumn;
	}

	HavingInfo parseHavingInfo(ValueNode havingNode, SelectNode selectNode)
			throws StandardException {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(havingNode);
		HavingInfo havingInfo = new DefaultHavingInfo(havingNode, selectNode);
		if (analyzer == null) {
			logger.error("analyzer is null!node=" + havingNode);
		} else {
			AnalyzeResult havingResult = analyzer.analyze(havingNode);
			havingInfo.getAggregateColumns().addAll(
					havingResult.getAggregateColumns());

		}
		return havingInfo;

	}

	class DefaultHavingInfo implements HavingInfo {
		Collection<TableColumn> columns = new ArrayList<TableColumn>();
		final ValueNode node;
		final SelectNode selectNode;
		final AnalyzerCallback callback;
		final String expression;

		DefaultHavingInfo(ValueNode node, SelectNode selectNode)
				throws StandardException {
			this.node = node;
			this.selectNode = selectNode;
			callback = new AnalyzerCallback() {

				@Override
				public void call() {
					DefaultHavingInfo.this.selectNode.setHavingClause(null);
				}
			};
			this.expression = nodeToStringBuilder.toString(node);

		}

		@Override
		public Collection<TableColumn> getAggregateColumns() {
			return columns;
		}

		@Override
		public String getExpression() {
			return expression;
		}

		@Override
		public AnalyzerCallback getCallback() {
			return callback;
		}

	}
}
