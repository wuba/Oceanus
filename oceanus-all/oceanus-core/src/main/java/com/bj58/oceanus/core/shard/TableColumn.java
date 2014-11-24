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

/**
 * 表列信息
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TableColumn {
	
	String schema;
	
	String table;
	
	String name;

	String aliasName;

	int resultIndex;

	boolean distinct;
	OrderByType orderByType;

	Integer preparedIndex;
	String aggregate;
	Object aggregateNode;
	String aggregateNodeContent;
	/**
	 * 在执行过程中的值,sql解析中持有的值,in的话会一个一个拆分
	 */
	Object value;

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTable() {
		return table;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public Integer getPreparedIndex() {
		return preparedIndex;
	}

	public void setPreparedIndex(Integer preparedIndex) {
		this.preparedIndex = preparedIndex;
	}

	public int getResultIndex() {
		return resultIndex;
	}

	public void setResultIndex(int resultIndex) {
		this.resultIndex = resultIndex;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public OrderByType getOrderByType() {
		return orderByType;
	}

	public void setOrderByType(OrderByType orderByType) {
		this.orderByType = orderByType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getAggregate() {
		return aggregate;
	}

	public void setAggregate(String aggregate) {
		this.aggregate = aggregate;
	}

	public Object getAggregateNode() {
		return aggregateNode;
	}

	public void setAggregateNode(Object aggregateNode) {
		this.aggregateNode = aggregateNode;
	}

	public String getAggregateNodeContent() {
		return aggregateNodeContent;
	}

	public void setAggregateNodeContent(String aggregateNodeContent) {
		aggregateNodeContent=aggregateNodeContent.replaceAll("[(]", "\\[(]");
		aggregateNodeContent=aggregateNodeContent.replaceAll("[)]", "\\[)]");
		this.aggregateNodeContent = aggregateNodeContent;
		
	}

	@Override
	public String toString() {
		return "TableColumn [schema=" + schema + ", table=" + table + ", name="
				+ name + ", aliasName=" + aliasName + ", resultIndex="
				+ resultIndex + ", distinct=" + distinct + ", orderByType="
				+ orderByType + ", preparedIndex=" + preparedIndex + ", value="
				+ value + "]";
	}

}
