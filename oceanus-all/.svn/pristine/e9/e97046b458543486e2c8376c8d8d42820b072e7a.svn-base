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
package com.bj58.oceanus.core.context;

import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.jdbc.StatementCallback;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.shard.TableInfo;

/**
 * 一个statement处理的上下文
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class StatementContext {
	
	static final ThreadLocal<StatementContext> threadLocal = new ThreadLocal<StatementContext>();
	
	/**
	 * sql批信息
	 */
	final List<BatchItem> baches = new LinkedList<BatchItem>();
	
	/**
	 * 创建statement的callback
	 */
	StatementCallback statementCreateCallback;
	
	final List<Statement> statements = new LinkedList<Statement>();
	
	/**
	 * 对statement的包装
	 */
	Statement statementWrapper;
	
	/**
	 * 是否是批次，在statement中通过addbatch标记
	 */
	boolean isBatch = false;

	/**
	 * 当前的sql信息
	 */
	BatchItem currentBatch = new BatchItem();
	
	int batchIndex = 0;

	/**
	 * 执行信息
	 */
	final Map<BatchItem, Set<RouteTarget>> executeInfosMap = new LinkedHashMap<BatchItem, Set<RouteTarget>>();

	public StatementContext() {
		baches.add(currentBatch);
		currentBatch.setIndex(batchIndex);
	}

	public static StatementContext getContext() {
		return threadLocal.get();
	}

	public Statement getStatementWrapper() {
		return statementWrapper;
	}

	public void setStatementWrapper(Statement statementWrapper) {
		this.statementWrapper = statementWrapper;
	}

	public static void setContext(StatementContext context) {
		threadLocal.set(context);
	}

	public StatementCallback getStatementCreateCallback() {
		return statementCreateCallback;
	}

	public void setStatementCreateCallback(
			StatementCallback statementCreateCallback) {
		this.statementCreateCallback = statementCreateCallback;
	}

	public void addTarget(RouteTarget target) {
		Set<RouteTarget> targets = executeInfosMap.get(target.getBatchItem());
		if (targets == null) {
			targets = new LinkedHashSet<RouteTarget>();
			executeInfosMap.put(target.getBatchItem(), targets);
		}
		targets.add(target);
	}

	public void addTargets(Set<RouteTarget> targets) {
		for (RouteTarget target : targets) {
			this.addTarget(target);
		}
	}

	public Map<BatchItem, Set<RouteTarget>> getExecuteInfosMap() {
		return executeInfosMap;
	}

	public void addParameterCallback(ParameterCallback<?> callback) {
		getCurrentBatch().add(callback);
	}

	public List<BatchItem> getBaches() {
		return baches;
	}

	public boolean isBatch() {
		return isBatch;
	}

	public BatchItem getCurrentBatch() {
		return currentBatch;
	}

	/**
	 * 默认创建了一个batch， 当第一次调用的时候不用添加，将isBatch＝true，并为下一次操作做准备 currentBatch = new
	 * BatchItem();
	 */
	public void addBatch() {

		if (!isBatch) {
			isBatch = true;
		} else {
			baches.add(currentBatch);
		}
		String sql = currentBatch.sql;
		/**
		 * 操作后为下一次操作做准备
		 */
		currentBatch = new BatchItem();
		currentBatch.sql = sql;
		currentBatch.setIndex(++batchIndex);
	}

	public void addBatch(String sql) {
		currentBatch.setSql(sql);
		if (!isBatch) {
			isBatch = true;
		} else {
			baches.add(currentBatch);
		}
		/**
		 * 操作后为下一次操作做准备
		 */
		currentBatch = new BatchItem();
		currentBatch.setIndex(++batchIndex);
	}

	public void clearBatch() {
		baches.clear();
		currentBatch = new BatchItem();
		this.baches.add(currentBatch);
		batchIndex = 0;
		isBatch = false;
	}

	public void release() {
		StatementContext.setContext(null);
		baches.clear();

		// TODO release
	}

	 
	/**
	 * 一个批次项，每次执行不管是不是batch都会创建一个 三种方式： 1.callback 2.event 3.statement
	 * 一个BatchItem代表一个语句或者一个批次， 在分库分表里面，一条语句可能在多个库或者表里执行
	 * 
	 * @author luolishu
	 * 
	 */
	public static class BatchItem {
		/**
		 * 序号,标记第几条语句,从0开始
		 */
		int index;
		final Map<Integer, ParameterCallback<?>> callbacksMap = new LinkedHashMap<Integer, ParameterCallback<?>>();
		String sql;

		/**
		 * sql解析的treeNode
		 */
		private Object statementTreeNode;

		private AnalyzeResult analyzeResult;
		/**
		 * 匹配的表
		 */
		private TableInfo matchTable;

		public Object getStatementTreeNode() {
			return statementTreeNode;
		}

		public void setStatementTreeNode(Object statementTreeNode) {
			this.statementTreeNode = statementTreeNode;
		}

		public Collection<ParameterCallback<?>> getCallbacks() {
			return callbacksMap.values();
		}

		public ParameterCallback<?> getCallback(int index) {
			return callbacksMap.get(index);
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public AnalyzeResult getAnalyzeResult() {
			return analyzeResult;
		}

		public void setAnalyzeResult(AnalyzeResult analyzeResult) {
			this.analyzeResult = analyzeResult;
		}

		public void add(ParameterCallback<?> callback) {
			this.callbacksMap.put(callback.parameterIndex(), callback);
		}

		public void addAll(Collection<ParameterCallback<?>> callbacks) {
			for (ParameterCallback<?> callback : callbacks) {
				this.add(callback);
			}
		}

		public void clearCallbacks() {
			callbacksMap.clear();
		}

		public TableInfo getMatchTable() {
			return matchTable;
		}

		public void setMatchTable(TableInfo matchTable) {
			this.matchTable = matchTable;
		}

		@Override
		public String toString() {
			return "BatchItem [index=" + index + ", callbacksMap="
					+ callbacksMap + ", sql=" + sql + ", statementTreeNode="
					+ statementTreeNode + ", analyzeResult=" + analyzeResult
					+ "]";
		}

	}
}
