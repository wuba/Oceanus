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
package com.bj58.oceanus.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bj58.oceanus.core.shard.ShardType;

/**
 * Table配置实体
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TableConfig implements Config {

	private static final long serialVersionUID = 1L;

	String name;

	String columns[];

	boolean differName;
	FunctionConfig function;
	ShardType shardType;
	String threadPoolId;

	final List<NameNodeReferenceConfig> referenceList = new ArrayList<NameNodeReferenceConfig>();

	public BaseNameNodeConfig getNameNode(int index) {
		NameNodeReferenceConfig reference = referenceList.get(index);
		return Configurations.getInstance().getNameNodeConfig(
				reference.getRef());

	}

	public String getThreadPoolId() {
		return threadPoolId;
	}

	public void setThreadPoolId(String threadPoolId) {
		this.threadPoolId = threadPoolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public FunctionConfig getFunction() {
		return function;
	}

	public void setFunction(FunctionConfig function) {
		this.function = function;
	}

	public ShardType getShardType() {
		return shardType;
	}

	public void setShardType(ShardType shardType) {
		this.shardType = shardType;
	}

	public List<NameNodeReferenceConfig> getReferenceList() {
		return referenceList;
	}

	public void addNode(NameNodeReferenceConfig node) {
		referenceList.add(node);
	}

	public void addAllNode(List<NameNodeReferenceConfig> nodeList) {
		referenceList.addAll(nodeList);
	}

	public boolean isDifferName() {
		return differName;
	}

	public void setDifferName(boolean differName) {
		this.differName = differName;
	}

	@Override
	public String toString() {
		return "TableConfig [name=" + name + ", columns="
				+ Arrays.toString(columns) + ", differName=" + differName
				+ ", function=" + function + ", shardType=" + shardType
				+ ", referenceList=" + referenceList + "]";
	}

}
