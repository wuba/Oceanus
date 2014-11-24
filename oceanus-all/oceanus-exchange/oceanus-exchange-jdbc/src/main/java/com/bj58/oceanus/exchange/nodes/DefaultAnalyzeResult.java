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
package com.bj58.oceanus.exchange.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.AnalyzerCallback;
import com.bj58.oceanus.core.shard.HavingInfo;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;

/**
 * 默认解析结果
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DefaultAnalyzeResult implements AnalyzeResult {
	final Collection<TableInfo> tableInfos = new ArrayList<TableInfo>();
	final Collection<TableColumn> conditionColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> resultColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> aggregateColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> appendResultColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> orderByColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> groupByColumns = new ArrayList<TableColumn>();
	TableInfo shardTables[];
	HavingInfo havingInfo;
	final Object treeNode;
	StatementType statementType;
	SqlValueItem limit;
	SqlValueItem offset;
	boolean distinct;
	List<AnalyzerCallback> analyzerCallbacks = new ArrayList<AnalyzerCallback>();

	public DefaultAnalyzeResult() {
		this(null);
	}

	public DefaultAnalyzeResult(Object treeNode) {
		this.treeNode = treeNode;
	}

	public Collection<TableInfo> getTableInfos() {
		return tableInfos;
	}

	public Collection<TableColumn> getConditionColumns() {
		return conditionColumns;
	}

	public Collection<TableColumn> getResultColumns() {
		return resultColumns;
	}

	public Collection<TableColumn> getAppendResultColumns() {
		return appendResultColumns;
	}

	public Collection<TableColumn> getOrderByColumns() {
		return orderByColumns;
	}

	public Collection<TableColumn> getGroupByColumns() {
		return groupByColumns;
	}
	@Override
	public Collection<TableColumn> getAggregateColumns() { 
		return aggregateColumns;
	}
	public void addTables(Collection<TableInfo> tables) {
		tableInfos.addAll(tables);
	}

	public void addConditionColumns(Collection<TableColumn> columns) {
		conditionColumns.addAll(columns);
	}

	public void addResultColumns(Collection<TableColumn> columns) {
		resultColumns.addAll(columns);
	}

	public void addAppendResultColumns(TableColumn column) {
		appendResultColumns.add(column);
	}

	public void addOrderByColumns(Collection<TableColumn> columns) {
		orderByColumns.addAll(columns);
	}

	public void addGroupByColumns(Collection<TableColumn> columns) {
		groupByColumns.addAll(columns);
	}

	

	public SqlValueItem getLimit() {
		return limit;
	}

	public void setLimit(SqlValueItem limit) {
		this.limit = limit;
	}

	public SqlValueItem getOffset() {
		return offset;
	}

	public void setOffset(SqlValueItem offset) {
		this.offset = offset;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public Object getTreeNode() {
		return treeNode;
	}

	@Override
	public StatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(StatementType statementType) {
		this.statementType = statementType;
	}

	public Collection<AnalyzerCallback> getAnalyzerCallbacks() {
		return analyzerCallbacks;
	}

	public void addAnalyzerCallback(AnalyzerCallback analyzerCallback) {
		analyzerCallbacks.add(analyzerCallback);
	}
	@Override
	public TableInfo[] getShardTables() { 
		return shardTables;
	}
	
	public void setShardTables(TableInfo[] shardTables) {
		this.shardTables = shardTables;
	}
	@Override
	public HavingInfo getHavingInfo() { 
		return havingInfo;
	}


	public void setHavingInfo(HavingInfo havingInfo) {
		this.havingInfo = havingInfo;
	}

	@Override
	public String toString() {
		return "DefaultAnalyzeResult [tableInfos=" + tableInfos
				+ ", conditionColumns=" + conditionColumns + ", resultColumns="
				+ resultColumns + ", orderByColumns=" + orderByColumns
				+ ", groupByColumns=" + groupByColumns + "]";
	}

	
	public AnalyzeResult clone(){
		DefaultAnalyzeResult result=new DefaultAnalyzeResult();
		return null;
		
	}

	

}
