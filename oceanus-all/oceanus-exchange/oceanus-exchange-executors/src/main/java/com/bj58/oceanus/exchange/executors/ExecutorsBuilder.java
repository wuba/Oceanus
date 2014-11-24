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

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.exchange.executors.jdbc.BatchExecutor;
import com.bj58.oceanus.exchange.executors.jdbc.SimpleExecutor;

/**
 * 执行器构建
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings("rawtypes")
public class ExecutorsBuilder {

	static final Executor DEFAULT_EXECUTOR = new SimpleExecutor();
	static final Executor BATCH_EXECUTOR = new BatchExecutor();

	public static Executor<?> build(StatementContext context) {
		if (context.isBatch()) {
			return BATCH_EXECUTOR;
		}
		return DEFAULT_EXECUTOR;
	}

}
