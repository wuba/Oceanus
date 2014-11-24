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
package com.bj58.oceanus.exchange.executors.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.ExecuteHandler;
import com.bj58.oceanus.exchange.executors.Executor;
import com.bj58.oceanus.exchange.executors.handler.DeleteExecuteHandler;
import com.bj58.oceanus.exchange.executors.handler.InsertExecuteHandler;
import com.bj58.oceanus.exchange.executors.handler.QueryExecuteHandler;
import com.bj58.oceanus.exchange.executors.handler.UpdateExecuteHandler;
import com.bj58.oceanus.result.merger.MergerBuilder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleExecutor implements Executor {
	static Logger logger = LoggerFactory.getLogger(SimpleExecutor.class);
	static final ExecuteHandler<Integer> deleteHandler = new DeleteExecuteHandler();
	static final ExecuteHandler<Integer> insertHandler = new InsertExecuteHandler();
	static final ExecuteHandler<Integer> updateHandler = new UpdateExecuteHandler();
	static final ExecuteHandler<ResultSet> queryHandler = new QueryExecuteHandler();

	@Override
	public Object execute(StatementContext context, ExecuteCallback callback)
			throws SQLException {

		switch (context.getCurrentBatch().getAnalyzeResult().getStatementType()) {
		case SELECT:
			return this.doQuery(context, callback);
		case INSERT:
		case UPDATE:
		case DELETE:
			return doUpdate(context, callback);
		default:
			break;
		}
		return null;
	}

	ExecuteHandler<?> getHandler(StatementType statementType) {
		switch (statementType) {
		case SELECT:
			return queryHandler;
		case INSERT:
			return insertHandler;
		case UPDATE:
			return updateHandler;
		case DELETE:
			return deleteHandler;
		default:
			break;
		}
		return null;
	}

	private Integer doUpdate(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		Map<BatchItem, Set<RouteTarget>> batchItems = context
				.getExecuteInfosMap();
		StatementType statementType = context.getCurrentBatch()
				.getAnalyzeResult().getStatementType();
		ExecuteHandler<Integer> hanlder = (ExecuteHandler<Integer>) getHandler(statementType);
		Set<RouteTarget> targets = batchItems.get(context.getCurrentBatch());
		return this.doUpdate(hanlder, targets, context, callback);

	}

	private Integer doUpdate(ExecuteHandler<Integer> hanlder,
			Set<RouteTarget> targets, StatementContext context,
			ExecuteCallback callback) throws SQLException {
		Integer sumResult = 0;
		List<FutureUpdateExecuteCallback> futureHolders = new ArrayList<FutureUpdateExecuteCallback>();
		int i = 0;
		int n = (targets.size() + 1);
		CyclicBarrier barrier = new CyclicBarrier(n);
		boolean asyn = (targets.size() > 1);
		boolean autoClosed=(targets.size() > 1);

		for (RouteTarget target : targets) {
			FutureUpdateExecuteCallback futureCallback = this
					.createUpdateCallback(null, barrier, callback, asyn,autoClosed);
			futureHolders.add(futureCallback);
			Integer result = hanlder.handle(target, context, futureCallback);
			if (result != null) {
				sumResult += result;
			}
		}
		if (asyn) {
			try {
				barrier.await();
				for (i = 0; i < futureHolders.size(); i++) {
					sumResult += (Integer) futureHolders.get(i).future.get();
				}
			} catch (Exception e) {
				logger.error("asyn execute error!targets=" + targets, e);
				throw new ShardException("asyn execute error!", e);
			}

		}
		return sumResult;
	}

	public ResultSet doQuery(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		Map<BatchItem, Set<RouteTarget>> batchItems = context
				.getExecuteInfosMap();
		StatementType statementType = context.getCurrentBatch()
				.getAnalyzeResult().getStatementType();
		ExecuteHandler<ResultSet> hanlder = (ExecuteHandler<ResultSet>) getHandler(statementType);
		Set<RouteTarget> targets = batchItems.get(context.getCurrentBatch());
		return this.doQuery(hanlder, targets, context, callback);
	}

	private ResultSet doQuery(final ExecuteHandler<ResultSet> hanlder,
			final Set<RouteTarget> targets, final StatementContext context,
			final ExecuteCallback callback) throws SQLException {
		if(targets.isEmpty()){
			throw new ShardException("no target found!batch="+context.getCurrentBatch());
		}
		ResultSet[] results = new ResultSet[targets.size()];
		List<FutureQueryExecuteCallback> futureHolders = new ArrayList<FutureQueryExecuteCallback>();
		int i = 0;
		int n = (targets.size() + 1);
		CyclicBarrier barrier = new CyclicBarrier(n);
		boolean asyn = (targets.size() > 1); 


		for (RouteTarget target : targets) {
			FutureQueryExecuteCallback futureCallback = this.createCallback(
					target.getBatchItem().getMatchTable(), barrier, callback, asyn);
			futureHolders.add(futureCallback);
			results[i++] = hanlder.handle(target, context, futureCallback);
		}
		if (asyn) {
			try {
				barrier.await();
				for (i = 0; i < results.length; i++) {
					results[i] = (ResultSet) futureHolders.get(i).future.get();
				}
			} catch (Exception e) {
				logger.error("asyn execute error!targets=" + targets, e);
				throw new ShardException("asyn execute error!", e);
			}

		}
		if (results.length > 1) {
			ResultSet mergeResult = MergerBuilder.buildResultSetMerger(context)
					.merge(results, context);
			return mergeResult;
		} else {
			return results[0];
		}
	}

	FutureUpdateExecuteCallback createUpdateCallback(TableInfo tableInfo,
			CyclicBarrier barrier, final ExecuteCallback callback, boolean asyn,boolean autoClosed) {

		final ThreadPoolExecutor threadPool = Configurations.getInstance()
				.getThreadPool();
		FutureUpdateExecuteCallback resultCallback = new FutureUpdateExecuteCallback(
				threadPool, asyn, callback, barrier, ConnectionContext
						.getContext().getTransaction(),autoClosed);
		return resultCallback;
	}

	FutureQueryExecuteCallback createCallback(TableInfo tableInfo,
			CyclicBarrier barrier, final ExecuteCallback callback, boolean asyn) {

		final ThreadPoolExecutor threadPool = Configurations.getInstance()
				.getThreadPool();
		FutureQueryExecuteCallback resultCallback = new FutureQueryExecuteCallback(
				threadPool, asyn, callback, barrier, ConnectionContext
						.getContext().getTransaction());
		return resultCallback;
	}

}
