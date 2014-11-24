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
package com.bj58.oceanus.exchange.handler;

import com.bj58.oceanus.core.context.StatementType;

/**
 * HandlerFactory
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HandlerFactory {
	static StatementContextHandler<?>[] handlersArr = new StatementContextHandler[StatementType
			.values().length];
	static {
		handlersArr[StatementType.DELETE.ordinal()] = new DeleteStatementContextHandler();
		handlersArr[StatementType.SELECT.ordinal()] = new SelectStatementContextHandler();
		handlersArr[StatementType.INSERT.ordinal()] = new InsertStatementContextHandler();
		handlersArr[StatementType.UPDATE.ordinal()] = new UpdateStatementContextHandler();
		handlersArr[StatementType.CALLABLE.ordinal()] = new CallableStatementContextHandler();
		handlersArr[StatementType.BATCH.ordinal()] = new BatchStatementContextHandler();
	}

	public static StatementContextHandler<?> create(StatementType type) {
		return handlersArr[type.ordinal()];
	}

	public static void main(String args[]) {
		for (StatementType type : StatementType.values()) {
			StatementContextHandler<?> handler = HandlerFactory.create(type);
			System.out.println(type + "=" + handler.getClass());
		}
	}
}
