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
package com.bj58.oceanus.exchange.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.InsertNode;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.RowsResultSetNode;

/**
 * InsertStatementContextHandler
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class InsertStatementContextHandler implements
		StatementContextHandler<InsertNode> {
	static Logger logger = LoggerFactory
			.getLogger(InsertStatementContextHandler.class);

	@Override
	public StatementContext handle(InsertNode statementNode,
			StatementContext context) {

		BatchItem batchItem = context.getCurrentBatch();
	
		if (statementNode.getResultSetNode() instanceof RowsResultSetNode) {
			// insert into users(id, name, age) values(1, 'zhangsan', 20),(2, 'lisi', 19)
			// 这种插入语句在sharding场景中无法保证路由唯一，通过约束要求不予支持
			throw new ShardException("Do not support muti-insert in one sql! SQL:"+batchItem.getSql());
			
			/*
			// 写入多列，先不考虑性能，这里会将所有rows从新拆分，然后按照batch的方式处理
			Collection<ParameterCallback<?>> callbacks = batchItem
					.getCallbacks();
			RowsResultSetNode rowsNode = (RowsResultSetNode) statementNode
					.getResultSetNode();
			List<RowResultSetNode> rows = rowsNode.getRows();
			List<RowResultSetNode> tempRows = new ArrayList<RowResultSetNode>(
					rows);
			context.clearBatch();
			for (int i = 0; i < tempRows.size(); i++) {
				rows.clear();
				rows.add(tempRows.get(i));
				NodeTreeToSql sqlTree = new NodeTreeToSql();
				try {
					String sql = sqlTree.toString(statementNode);
					SQLParser parser = StatementHelper.createSQLParser();
					InsertNode insertNode = (InsertNode) parser
							.parseStatement(sql); 
					batchItem = context.getCurrentBatch();
					batchItem.addAll(callbacks);
					batchItem.setSql(sql);
					batchItem.setStatementTreeNode(insertNode);
					this.parseTableInfo(batchItem, insertNode);
					context.addBatch(sql);

				} catch (Exception e) {
					logger.error("generate sql error!", e);
				}
			}
			*/
		} else {
			/**
			 * 解析表信息
			 */
			this.parseTableInfo(batchItem, statementNode);
			batchItem.setStatementTreeNode(statementNode);
		}

		if (context.getBaches().size() > 1) {
			this.resetCallbacks(context.getBaches());
		}
		
		return context;
	}

	void resetCallbacks(List<BatchItem> batches) {
		int start = 0;
		for (BatchItem batchItem : batches) {
			this.resetCallBack(batchItem, start);
			start += batchItem.getCallbacks().size();
		}
	}

	void resetCallBack(BatchItem batchItem, int start) {
		List<ParameterCallback<?>> callbacks = new ArrayList<ParameterCallback<?>>();
		Collection<TableColumn> columns = batchItem.getAnalyzeResult()
				.getConditionColumns();
		int i = 1;
		for (TableColumn column : columns) {
			if (column.getPreparedIndex() != null) {
				int index = start + column.getPreparedIndex();
				ParameterCallback<?> callback = batchItem.getCallback(index);
				callback.setParameterIndex(i++);
				callbacks.add(callback);
			}
		}
		batchItem.clearCallbacks();
		batchItem.addAll(callbacks);
	}

	void parseTableInfo(BatchItem batchItem, InsertNode statementNode) {

		/**
		 * 获取当前sql的数据库表信息
		 */
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode.getNodeType());
		AnalyzeResult result = analyzer.analyze(statementNode);
		result.getConditionColumns().addAll(result.getResultColumns());
		batchItem.setAnalyzeResult(result);

	}

	

}
