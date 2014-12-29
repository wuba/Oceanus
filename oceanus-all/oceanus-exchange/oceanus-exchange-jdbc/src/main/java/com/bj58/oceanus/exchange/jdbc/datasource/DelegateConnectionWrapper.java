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
package com.bj58.oceanus.exchange.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import com.bj58.oceanus.core.context.TransactionContext;
import com.bj58.oceanus.core.jdbc.ConnectionManager;
import com.bj58.oceanus.exchange.jdbc.ConnectionWrapper;
import com.bj58.oceanus.exchange.jdbc.ProviderDesc;

/**
 * 自定义封装数据源方式的Connection实现
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DelegateConnectionWrapper extends ConnectionWrapper {
	
	private boolean autoCommit = true;

	public DelegateConnectionWrapper(Connection connection) {
		super(connection);
	}

	public DelegateConnectionWrapper(Connection connection, ProviderDesc desc) {
		super(connection, null, desc);
	}

	public DelegateConnectionWrapper(Connection connection,
			ConnectionManager manager, ProviderDesc desc) {
		super(connection, manager, desc);
	}
	
	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}
	
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		super.setAutoCommit(autoCommit);
		this.autoCommit = autoCommit;
	}
	
	@Override
	public void commit() throws SQLException {
		super.commit();
		TransactionContext.getContext().release();
	}
	
	@Override
	public void rollback() throws SQLException {
		super.rollback();
		TransactionContext.getContext().release();
	}
	
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		super.rollback(savepoint);
		TransactionContext.getContext().release();
	}

}
