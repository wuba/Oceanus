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
package com.bj58.oceanus.result.merger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.jdbc.aggregate.Aggregate;
import com.bj58.oceanus.core.jdbc.aggregate.Avg;
import com.bj58.oceanus.core.jdbc.aggregate.Count;
import com.bj58.oceanus.core.jdbc.aggregate.Max;
import com.bj58.oceanus.core.jdbc.aggregate.Min;
import com.bj58.oceanus.core.jdbc.aggregate.Sum;
import com.bj58.oceanus.core.jdbc.result.ColumnFinder;
import com.bj58.oceanus.core.jdbc.result.GroupByKeyGenerator;
import com.bj58.oceanus.core.jdbc.result.RowSet;
import com.bj58.oceanus.core.jdbc.result.RowSetComparator;
import com.bj58.oceanus.core.jdbc.result.RowSetCreator;
import com.bj58.oceanus.core.jdbc.result.RowSetsResultSet;
import com.bj58.oceanus.core.jdbc.result.SimpleMergedResultSet;
import com.bj58.oceanus.core.script.InterpretedScriptExecutor;
import com.bj58.oceanus.core.script.ScriptExecutor;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.HavingInfo;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.utils.StringUtils;

/**
 * max(name),min(name),avg(name),count(name),sum(name),count(*),name
 * from table group by name 对聚合函数来说按照以下的逻辑：
 * 
 * 1.group by，当前结果
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings("rawtypes")
public class DefaultResultSetMerger implements ResultSetMerger {
	static Logger logger = LoggerFactory
			.getLogger(DefaultResultSetMerger.class);
	RowSetCreator rowSetCreator = new DefaultRowSetCreator();
	ScriptExecutor<Boolean> scriptExecutor = new InterpretedScriptExecutor<Boolean>();

	@Override
	public ResultSet merge(final ResultSet[] resultSets,
			StatementContext context) throws SQLException {
		final AnalyzeResult analyzeResult = context.getCurrentBatch()
				.getAnalyzeResult();
		final Collection<TableColumn> groupByColumns = analyzeResult
				.getGroupByColumns();
		final Collection<TableColumn> resultColumns = analyzeResult
				.getResultColumns();
		final Collection<TableColumn> orderByColumns = analyzeResult
				.getOrderByColumns();
		final ResultSetMetaData metaData = resultSets[0].getMetaData();
		if (groupByColumns.isEmpty() && orderByColumns.isEmpty()
				&& analyzeResult.getLimit() == null
				&& analyzeResult.getAggregateColumns().isEmpty()
				&& !analyzeResult.isDistinct()) {
			return new SimpleMergedResultSet(Arrays.asList(resultSets));
		}
		GroupByKeyGenerator groupByKeyGenerator = null;
		RowSetComparator comparator = null;
		if (!groupByColumns.isEmpty() || analyzeResult.isDistinct()) {

			if (analyzeResult.isDistinct()) {
				groupByKeyGenerator = new GroupByKeyGenerator(
						resultColumns.toArray(new TableColumn[] {}));
			} else {

				groupByKeyGenerator = new GroupByKeyGenerator(
						groupByColumns.toArray(new TableColumn[] {}));
			}

		}
		ResultSetColumnFinder columnFinder = new ResultSetColumnFinder(
				resultSets[0]);
		for (TableColumn column : groupByColumns) {
			if (column.getAliasName() != null
					&& column.getAliasName().trim().length() > 0) {
				column.setResultIndex(columnFinder.findIndex(column
						.getAliasName()));
			} else {
				column.setResultIndex(columnFinder.findIndex(column.getName()));
			}
		}
		for (TableColumn column : orderByColumns) {
			if (column.getAliasName() != null
					&& column.getAliasName().trim().length() > 0) {
				column.setResultIndex(columnFinder.findIndex(column
						.getAliasName()));
			} else {
				column.setResultIndex(columnFinder.findIndex(column.getName()));
			}
		}
		if (!orderByColumns.isEmpty()) {
			comparator = new RowSetComparator(
					orderByColumns.toArray(new TableColumn[] {}));
		}

		Map<Object, RowSet> groupByMap = new LinkedHashMap<Object, RowSet>();
		Map<String, Aggregate> aggregateMap = new HashMap<String, Aggregate>();
		List<RowSet> results = new LinkedList<RowSet>();
		int rowCounts[] = new int[resultSets.length];// 记录每个结果集的行数，通过行数判断，假如每个结果集的行数是1，并且包含了聚集函数，没有group
														// by情况下，则合并成一行
		for (int i = 0; i < resultSets.length; i++) {// 不同的resultset进行处理，每个resultset的结果需要合并一下
			final ResultSet resultSet = resultSets[i];

			while (resultSet.next()) {
				rowCounts[i]++;
				RowSet rowSet = rowSetCreator.create(resultSet, columnFinder);
				String key = "";
				if (groupByKeyGenerator != null) {
					Object groupKey = groupByKeyGenerator.getKey(rowSet);
					RowSet groupedRowset = groupByMap.get(groupKey);

					if (groupedRowset != null) {

						if (analyzeResult.isDistinct()) {// distinct
															// 情况下直接跳过，去除,当前resultset的结果不需要合并
							continue;
						}
						processAggregate(rowSet, resultSet, analyzeResult,
								groupKey, aggregateMap);// 处理聚合函数
						continue;
					}
					groupByMap.put(groupKey, rowSet);
					if (groupKey != null) {
						key = groupKey.toString();
					}
				}
				processAggregate(rowSet, resultSet, analyzeResult, key,
						aggregateMap);// 处理聚合函数
				results.add(rowSet);
			}
			resultSet.close();
		}
		if (comparator != null) {
			Collections.sort(results, comparator);
		}
		results = processLimit(results, analyzeResult);
		if (analyzeResult.getGroupByColumns().isEmpty()
				&& analyzeResult.getAggregateColumns().size() > 0) {// 在查询结果中有聚集函数，而且每个结果集返回<=1
			boolean mergeToOne = true;
			for (int i = 0; i < rowCounts.length; i++) {
				if (rowCounts[i] > 1) {
					mergeToOne = false;
					break;
				}
			}
			if (mergeToOne) {
				results = results.subList(0, 1);
			}
		}
		this.processHaving(results, analyzeResult);
		return new RowSetsResultSet(results, metaData);
	}

	private List<RowSet> processLimit(List<RowSet> results,
			AnalyzeResult analyzeResult) {
		if (analyzeResult.getLimit() == null) {
			return results;
		}
		int limit = analyzeResult.getLimit().getValue();
		int offset = 0;
		if (analyzeResult.getOffset() != null) {
			offset = analyzeResult.getOffset().getValue();
		}
		if(offset>=results.size()){
			return Collections.EMPTY_LIST;
		}
		int pos=offset + limit;
		if(pos>results.size()){
			pos=results.size(); 
		}
		return results.subList(offset, pos);

	}

	private List<RowSet> processHaving(List<RowSet> results,
			AnalyzeResult analyzeResult) {
		if (analyzeResult.getHavingInfo() == null || results == null
				|| results.isEmpty()) {
			return results;
		}
		Map<String, TableColumn> columnsMap = new HashMap<String, TableColumn>();
		for (TableColumn column : analyzeResult.getResultColumns()) {
			String key = this.getTableColumnKey(column);
			columnsMap.put(column.getName(), column);
			columnsMap.put(key, column);
//			key = column.getAggregateNodeContent();
//			if (!columnsMap.containsKey(key)) {
//				columnsMap.put(key, column);
//			}
		}
		HavingInfo havingInfo = analyzeResult.getHavingInfo();
		for (TableColumn column : havingInfo.getAggregateColumns()) {
			String key = this.getTableColumnKey(column);
			TableColumn resultColumn = columnsMap.get(key);
			if (resultColumn == null) {
				key = column.getAggregateNodeContent();
				resultColumn = columnsMap.get(key);
			}
			column.setResultIndex(resultColumn.getResultIndex());
			column.setValue(resultColumn.getValue());
		}
		Iterator<RowSet> iterator = results.iterator();
		String scriptExp = getScriptExp(havingInfo);
		while (iterator.hasNext()) {
			RowSet row = iterator.next();
			Map<String, Object> contextMap = this.createScriptContextMap(
					columnsMap, row);
			try {
				if (!scriptExecutor.execute(scriptExp, contextMap)) {
					iterator.remove();
				}
			} catch (ScriptException e) {
				logger.error("script execute error!", e);
				throw new ShardException("having error!havingInfo="
						+ havingInfo, e);
			}
		}
		return results;

	}
	
	private String getTableColumnKey(TableColumn column){
		StringBuilder builder = new StringBuilder(column.getName());
		
		if(column.getTable() != null){
			builder.insert(0, ".");
			builder.insert(0, column.getTable());
		}
		
		if(column.getAggregate() != null){
//			builder.insert(0, column.getAggregate());
//			builder.insert(column.getAggregate().length(), "(");
//			builder.append(")");
			builder.delete(0, builder.length());
			builder.append(column.getAggregateNodeContent().replaceAll("[\\[|\\]]", ""));
		}
		return builder.toString();
	}

	private Map<String, Object> createScriptContextMap(
			Map<String, TableColumn> columnsMap, RowSet row) {
		Map<String, Object> context = new HashMap<String, Object>();
		for (Iterator<Entry<String, TableColumn>> it = columnsMap.entrySet().iterator();
				it.hasNext();) {
			Entry<String, TableColumn> entry = it.next();
			Object value;
			try {
				if(row.getObject(entry.getKey()) != null){
					value = row.getObject(entry.getValue().getResultIndex());
					
					context.put(entry.getKey().toUpperCase(), value);
					context.put("$" + entry.getValue().getResultIndex(), value);
				}
			} catch (SQLException e) {
				logger.error("get value error!", e);
				throw new ShardException("having error!column=" + entry.getValue(), e);
			}
		}

		return context;

	}

	private String getScriptExp(HavingInfo havingInfo) {
		String exp = havingInfo.getExpression().toUpperCase();
		for (TableColumn column : havingInfo.getAggregateColumns()) {
			exp = exp.replaceAll(column.getAggregateNodeContent(),
					"\\$" + column.getResultIndex());
			if (StringUtils.isNotBlank(column.getAliasName())) {
				exp = exp.replaceAll(column.getAliasName().toUpperCase(), "\\$"
						+ column.getResultIndex());
			}
		}
		exp = MergeUtils.getScriptExp(exp);
		return exp;

	}

	boolean havingCondition(String exp, Map<String, Object> contextMap) {

		return false;
	}

	private void processAggregate(RowSet rowSet, ResultSet resultSet,
			AnalyzeResult analyzeResult, Object groupKey,
			Map<String, Aggregate> aggregateMap) throws SQLException {
		Collection<TableColumn> aggregateColumns = analyzeResult
				.getAggregateColumns();
		List<TableColumn> avgColumns = new ArrayList<TableColumn>();
		Set<Aggregate> aggregates = new HashSet<Aggregate>();
		for (TableColumn column : aggregateColumns) {
			if (column.getAggregate().toUpperCase().startsWith("AVG")
					|| column.getAggregate().toUpperCase()
							.startsWith("AVERAGE")) {// avg延后初始化
				avgColumns.add(column);
				continue;
			}
			Aggregate function = this.createAggregate(column, groupKey,
					aggregateMap);
			if (!aggregates.contains(function)) {// 假如sql中存在多个聚集函数，过滤掉，select
													// max(age),max(Age) from t;
				aggregates.add(function);
				function.addRow(resultSet, rowSet);
			}
			rowSet.setAggregate(column.getResultIndex(), function);
		}
		for (TableColumn column : avgColumns) {
			Aggregate function = this.createAggregate(column, groupKey,
					aggregateMap);
			function.addRow(resultSet, rowSet);
			rowSet.setAggregate(column.getResultIndex(), function);
		}
	}

	private String getAggregateKey(TableColumn column, Object groupKey) {
		String key = groupKey + column.getAggregate().toUpperCase()
				+ column.getName();
		return key.toUpperCase();
	}

	private Aggregate createAggregate(TableColumn column, Object groupKey,
			Map<String, Aggregate> aggregateMap) {
		String aggregate = column.getAggregate();
		String key = this.getAggregateKey(column, groupKey);
		Aggregate function = aggregateMap.get(key);
		if (function != null) {
			return function;
		}

		if (aggregate.toUpperCase().startsWith("COUNT")) {
			function = new Count(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("MAX")) {
			function = new Max(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("MIN")) {
			function = new Min(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("SUM")) {
			function = new Sum(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("AVG")
				|| aggregate.toUpperCase().startsWith("AVERAGE")) {
			String sumKey = (groupKey + "SUM" + column.getName()).toUpperCase();
			String countKey = (groupKey + "COUNT" + column.getName())
					.toUpperCase();
			Sum sum = (Sum) aggregateMap.get(sumKey);
			Count count = (Count) aggregateMap.get(countKey);
			function = new Avg(sum, count, key, column.getResultIndex());

		}
		if (!aggregateMap.containsKey(key)) {
			aggregateMap.put(key, function);
		}
		return function;
	}

	class ResultSetColumnFinder implements ColumnFinder {
		ResultSet resultSet;
		ResultSetMetaData metaData;
		Map<String, Integer> values = new HashMap<String, Integer>();

		public ResultSetColumnFinder(ResultSet rs) throws SQLException {
			this.resultSet = rs;
			this.metaData = rs.getMetaData();
			init();
		}

		private void init() throws SQLException {
			int count = metaData.getColumnCount();
			for (int i = 1; i <= count; i++) {
				String label = metaData.getColumnLabel(i);
				String name = metaData.getColumnName(i);
				String key = label.toUpperCase();
				values.put(key, i);
				key = name.toUpperCase();
				if (!values.containsKey(key)) {
					values.put(key, i);
				}
			}
		}

		@Override
		public int findIndex(String columnName) throws SQLException {

			String key = columnName.toUpperCase();
			if (values.containsKey(key)) {
				return values.get(key);
			}
			Integer value = resultSet.findColumn(columnName);
			values.put(key, value);
			return value;
		}

	};

}
