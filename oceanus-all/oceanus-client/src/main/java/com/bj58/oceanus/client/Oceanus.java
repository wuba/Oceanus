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
package com.bj58.oceanus.client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.exchange.DatabaseUtils;
import com.bj58.oceanus.exchange.jdbc.ProviderDesc;
import com.bj58.oceanus.exchange.jdbc.datasource.DataSourceWrapper;
import com.bj58.oceanus.exchange.jdbc.rw.RWConnectionWrapper;

/**
 * Oceanus 的使用入口
 * <p>
 * 提供常规JDBC操作的封装
 * </p>
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class Oceanus {
	
	private static final AtomicBoolean inited = new AtomicBoolean(false);
	
	private Oceanus() {}
	
	public static synchronized void init(String configuration) {
		if(!inited.compareAndSet(false, true))
			throw new ConfigurationException("oceanus has been inited !");
		
		Configurations.getInstance().init(configuration);
	}
	
	public static void checkInit() {
		if(!inited.get())
			throw new ConfigurationException("oceanus need init !");
	}
	
	public static Connection wrapConnection(Connection connection, ProviderDesc providerDesc) {
		checkInit();
		return DatabaseUtils.wrapConnection(connection, null, providerDesc);
	}
	
	public static DataSource wrapDataSource(DataSource dataSource) {
		checkInit();
		return new DataSourceWrapper(dataSource);
	}
	
	public static Connection getConnection() {
		checkInit();
		return DatabaseUtils.wrapConnection(null, null, null);
	}
	
	public static Connection getConnection(ProviderDesc providerDesc) {
		checkInit();
		return new RWConnectionWrapper(null, providerDesc);
	}
	
	public static void beginTransaction(Connection connection) throws SQLException {
		checkInit();
		DatabaseUtils.beginTransaction(connection);
	}

	public static void endTransaction(Connection connection) throws SQLException {
		checkInit();
		DatabaseUtils.endTransaction(connection);
	}
	
	public static void releaseTransaction() {
		checkInit();
		DatabaseUtils.releaseTransaction();
	}
	
	public static void closeConnection(Connection connection) {
		checkInit();
		DatabaseUtils.closeConnection(connection);
	}

	public static void closeStatement(Statement statement) {
		checkInit();
		DatabaseUtils.closeStatement(statement);
	}

	public static void closeResultSet(ResultSet resultSet) {
		checkInit();
		DatabaseUtils.closeResultSet(resultSet);
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		checkInit();
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(conn);
	}
	
}
