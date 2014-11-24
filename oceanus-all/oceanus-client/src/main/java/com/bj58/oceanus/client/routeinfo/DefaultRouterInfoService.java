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
package com.bj58.oceanus.client.routeinfo;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.exchange.builder.DefaultStatementContextBuilder;
import com.bj58.oceanus.exchange.builder.StatementContextBuilder;
import com.bj58.oceanus.exchange.router.Router;
import com.bj58.oceanus.exchange.router.RouterFactory;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DefaultRouterInfoService implements RouterInfoService {
	static final StatementContextBuilder builder = new DefaultStatementContextBuilder();

	@Override
	public Map<BatchItem, Set<RouteTarget>> route(String sql)
			throws SQLException {
		try {
			StatementContext context = new StatementContext();
			StatementContext.setContext(context);
			builder.build(sql, context);
			Router router = RouterFactory.createRouter(context);
			router.route(context);

			Map<BatchItem, Set<RouteTarget>> targets = context
					.getExecuteInfosMap();
			return targets;
		} finally {
			StatementContext.setContext(null);
		}
	}
	 

}
