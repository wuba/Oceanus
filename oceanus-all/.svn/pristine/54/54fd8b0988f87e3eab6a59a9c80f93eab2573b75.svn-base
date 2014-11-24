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
package com.bj58.oceanus.core.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.bj58.oceanus.core.interceptor.InterceptorRef;
import com.bj58.oceanus.core.shard.Function;
import com.bj58.oceanus.core.shard.ShardType;

/**
 * Table 描述实例
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TableDescription {
	
	final String tableName;
	
	final List<NameNodeHolder> nameNodes = new ArrayList<NameNodeHolder>();
	
	final List<InterceptorRef> interceptors = new ArrayList<InterceptorRef>();
	
	Function function;
	
	String columns[];
	
	ShardType shardType;
	
	boolean differentName;
	
	ThreadPoolExecutor threadPool;

	public TableDescription(String tableName) {
		this.tableName = tableName;
	}

	public NameNodeHolder getNameNode(int i) {
		return (NameNodeHolder) nameNodes.get(i);
	}

	public List<NameNodeHolder> getNameNodes() {
		return nameNodes;
	}

	public void addNameNode(NameNodeHolder nameNode) {
		nameNodes.add(nameNode);
	}

	public String getTableName() {
		return tableName;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public ShardType getShardType() {
		return shardType;
	}

	public void setShardType(ShardType shardType) {
		this.shardType = shardType;
	}

	public boolean isDifferentName() {
		return differentName;
	}

	public void setDifferentName(boolean differentName) {
		this.differentName = differentName;
	}

	public List<InterceptorRef> getInterceptors() {
		return interceptors;
	}

	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}

	public NameNodeHolder findNameNodeHolder(String tableName) {
		for (NameNodeHolder holder : nameNodes) {
			if (holder.getTableName() != null
					&& holder.getTableName().equalsIgnoreCase(tableName)) {
				return holder;
			}
		}
		return null;
	}

}
