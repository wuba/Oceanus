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
package com.bj58.oceanus.exchange.executors;

import java.sql.SQLException;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.shard.RouteTarget;

/**
 * 执行操作约束
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface ExecuteHandler<T> {

	public T handle(RouteTarget target, StatementContext context,
			ExecuteCallback<T> callback) throws SQLException;

}
