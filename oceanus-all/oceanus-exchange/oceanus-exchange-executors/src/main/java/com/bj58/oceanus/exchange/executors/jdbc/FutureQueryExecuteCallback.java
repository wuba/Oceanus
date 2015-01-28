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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bj58.oceanus.core.tx.Transaction;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FutureQueryExecuteCallback extends ExecuteCallback {
	
	final ThreadPoolExecutor threadPool;
	final boolean asyn;
	final ExecuteCallback callback;
	final CyclicBarrier barriar;
	final Transaction trasaction;
	Future future;

	FutureQueryExecuteCallback(ThreadPoolExecutor threadPool, boolean asyn,
			ExecuteCallback callback, CyclicBarrier barriar,
			Transaction trasaction) {
		this.threadPool = threadPool;
		this.asyn = asyn;
		this.callback = callback;
		this.barriar = barriar;
		this.trasaction = trasaction;
	}

	@Override
	public Object execute(final Statement statement, final String sql)
			throws SQLException {
		if (!asyn) {
			return doExecute(statement, sql, callback);

		}

		future = threadPool.submit(new Callable() {
			@Override
			public Object call() throws Exception {
				try{
					Object result = doExecute(statement, sql, callback);
					return result;
				}catch(Exception e){
					e.printStackTrace();
					throw e;
				}finally{
					barriar.await(threadPool.getKeepAliveTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
				}
			}
		});

		return null;
	}

	protected final Object doExecute(Statement statement, String sql,
			ExecuteCallback<Integer> callback) throws SQLException {
		Object result = callback.execute(statement, sql);
		return result;
	}
}
