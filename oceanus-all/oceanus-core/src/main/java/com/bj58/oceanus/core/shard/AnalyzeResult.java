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
package com.bj58.oceanus.core.shard;

import java.util.Collection;

import com.bj58.oceanus.core.context.StatementType;

/**
 * 解析结果约束
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface AnalyzeResult extends Cloneable{
	
	public StatementType getStatementType();

	public Object getTreeNode();

	public Collection<TableInfo> getTableInfos();
	
	public TableInfo[] getShardTables();

	/**
	 * where 中的条件语句 或者join的条件语句
	 * 
	 * @return
	 */
	public Collection<TableColumn> getConditionColumns();

	public Collection<TableColumn> getResultColumns();
	
	public Collection<TableColumn> getAppendResultColumns();

	public Collection<TableColumn> getOrderByColumns();

	public Collection<TableColumn> getGroupByColumns();
	
	public Collection<TableColumn> getAggregateColumns();

	public SqlValueItem getLimit();

	public SqlValueItem getOffset();

	public boolean isDistinct();
	
	Collection<AnalyzerCallback> getAnalyzerCallbacks();
	
	public HavingInfo getHavingInfo();
	
	
	public class SqlValueItem{
		Integer value;
		
		int parameterIndex;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public int getParameterIndex() {
			return parameterIndex;
		}

		public void setParameterIndex(int parameterIndex) {
			this.parameterIndex = parameterIndex;
		}
		
		
		
		
	}

}
