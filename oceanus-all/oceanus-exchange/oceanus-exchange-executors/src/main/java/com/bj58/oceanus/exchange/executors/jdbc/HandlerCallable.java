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
import java.util.concurrent.Callable;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.ExecuteHandler;

/**
 * @author luolishu
 * 
 */
@SuppressWarnings("rawtypes")
public class HandlerCallable implements Callable<HandlerResult>, ExecuteHandler {

	ExecuteHandler handler;
	RouteTarget target;
	StatementContext context;

	ExecuteCallback callback;

	public HandlerCallable(ExecuteHandler handler, RouteTarget target,
			StatementContext context, ExecuteCallback callback) {
		this.handler = handler;
		this.target = target;
		this.context = context;
		this.callback = callback;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HandlerResult call() throws Exception {
		try {
			HandlerResult result = new HandlerResult();
			Object v = handler.handle(target, context, callback);
			result.setResult(v);
			return result;
		} finally {

		}
	}

	@Override
	public Object handle(RouteTarget target, StatementContext context,
			ExecuteCallback callback) throws SQLException {
		Object v = handler.handle(target, context, callback);
		return v;
	}

}
