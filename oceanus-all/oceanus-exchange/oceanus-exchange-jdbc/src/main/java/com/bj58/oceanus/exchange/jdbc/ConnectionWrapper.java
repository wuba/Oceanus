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
package com.bj58.oceanus.exchange.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.jdbc.ConnectionCallback;
import com.bj58.oceanus.core.jdbc.ConnectionEvent;
import com.bj58.oceanus.core.jdbc.ConnectionManager;
import com.bj58.oceanus.core.jdbc.MockBlob;
import com.bj58.oceanus.core.jdbc.MockClob;
import com.bj58.oceanus.core.jdbc.MockSQLXML;
import com.bj58.oceanus.core.jdbc.PreparedStatementCallback;
import com.bj58.oceanus.core.jdbc.StatementCallback;
import com.bj58.oceanus.core.lifecycle.LifeCycle;
import com.bj58.oceanus.core.tx.Transaction;
import com.bj58.oceanus.exchange.builder.DefaultStatementContextBuilder;
import com.bj58.oceanus.exchange.builder.StatementContextBuilder;

/**
 * Connection包装
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ConnectionWrapper extends AbstractConnection implements
		Connection, LifeCycle {
	StatementContextBuilder builder = new DefaultStatementContextBuilder();
	volatile int closedCount = 0;
	final Connection orgConnection;
	ConnectionManager connectionManager;

	public ConnectionWrapper(Connection connection) {
		this(connection, null, null);
	}

	public ConnectionWrapper(Connection connection, ProviderDesc desc) {
		this(connection, null, desc);
	}

	public ConnectionWrapper(Connection connection, ConnectionManager manager,
			ProviderDesc desc) {
		this.orgConnection = connection;
		this.connectionManager = manager;
		ConnectionContext context = new ConnectionContext(orgConnection, this);
		context.setOrgConnectionManager(connectionManager);
		ConnectionContext.setContext(context);
	}

	public void init() {
		closedCount = 0;
	}

	public Connection getOrgConnection() {
		return orgConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.getClass().isAssignableFrom(iface);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return (T) this;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {

		StatementWrapper statement = new StatementWrapper(this, builder);

		StatementCallback callback = new StatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatementWrapper statement = new PreparedStatementWrapper(this,
				builder, sql);
		PreparedStatementCallback callback = new PreparedStatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}

			@Override
			public PreparedStatement prepareStatement(Connection conn,
					String sql) throws SQLException {
				return conn.prepareStatement(sql);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return this.orgConnection.prepareCall(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	@Override
	public String nativeSQL(String sql) throws SQLException {
		return this.orgConnection.nativeSQL(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {

		ConnectionContext context = ConnectionContext.getContext();
		if (!autoCommit) {
			context.beginTransaction();
		}
		ConnectionCallback callback = new ConnectionCallback() {
			boolean preAutoCommit;

			@Override
			public void call(Connection connection) throws SQLException {
				preAutoCommit = connection.getAutoCommit();
				if (preAutoCommit != autoCommit) {// 只有不一致才赋值
					connection.setAutoCommit(autoCommit);
				}
			}

			@Override
			public void reset(Connection connection) throws SQLException {
				connection.setAutoCommit(preAutoCommit);
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.SET_AUTO_COMMIT;
			}

		};
		context.addCallback(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getAutoCommit()
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		return orgConnection.getAutoCommit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#commit()
	 */
	@Override
	public void commit() throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		if (context == null) {
			return;
		}
		try {
			Transaction transaction = context.getTransaction();
			if (transaction != null) {
				transaction.commit();
			}

		} finally {
			context.rollbackOrCommited();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		if (context == null) {
			return;
		}
		try {
			Transaction transaction = context.getTransaction();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			context.rollbackOrCommited();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		if (context == null) {
			return;
		}
		context.releaseAfterClosed();
		if (connectionManager != null) {
			connectionManager.release(orgConnection);
		}
		closedCount++;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		return closedCount > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getMetaData()
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return orgConnection.getMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {
			boolean preReadOnly;

			@Override
			public void call(Connection connection) throws SQLException {
				preReadOnly = connection.isReadOnly();
				if (preReadOnly != readOnly) {// 只有不一致才赋值
					connection.setReadOnly(readOnly);
				}
			}

			@Override
			public void reset(Connection connection) throws SQLException {
				connection.setReadOnly(preReadOnly);
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.SET_READ_ONLY;
			}

		};
		context.addCallback(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		return orgConnection.isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	@Override
	public void setCatalog(final String catalog) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {
			String preCatalog;

			@Override
			public void call(Connection connection) throws SQLException {
				preCatalog = connection.getCatalog();
				if (preCatalog != catalog) {
					connection.setCatalog(catalog);
				}
			}

			@Override
			public void reset(Connection connection) throws SQLException {
				connection.setCatalog(preCatalog);
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.SET_CATALOG;
			}

		};
		context.addCallback(callback);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getCatalog()
	 */
	@Override
	public String getCatalog() throws SQLException {
		return orgConnection.getCatalog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {
			int preLevel;

			@Override
			public void call(Connection connection) throws SQLException {
				preLevel = connection.getTransactionIsolation();
				if (preLevel != level) {// 只有不一致才赋值
					connection.setTransactionIsolation(level);
				}
			}

			@Override
			public void reset(Connection connection) throws SQLException {
				connection.setTransactionIsolation(preLevel);
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.SET_ISOLATE_LEVEL;
			}

		};
		context.addCallback(callback);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		return orgConnection.getTransactionIsolation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return orgConnection.getWarnings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		orgConnection.clearWarnings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	@Override
	public Statement createStatement(final int resultSetType,
			final int resultSetConcurrency) throws SQLException {
		StatementWrapper statement = new StatementWrapper(this, builder);
		StatementCallback callback = new StatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn
						.createStatement(resultSetType, resultSetConcurrency);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql,
			final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		PreparedStatementWrapper statement = new PreparedStatementWrapper(this,
				builder, sql);
		PreparedStatementCallback callback = new PreparedStatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}

			@Override
			public PreparedStatement prepareStatement(Connection conn,
					String sql) throws SQLException {
				return conn.prepareStatement(sql, resultSetType,
						resultSetConcurrency);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return this.orgConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getTypeMap()
	 */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return orgConnection.getTypeMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {
			Map<String, Class<?>> preMap;

			@Override
			public void call(Connection connection) throws SQLException {
				preMap = connection.getTypeMap();
				connection.setTypeMap(map);
			}

			@Override
			public void reset(Connection connection) throws SQLException {
				connection.setTypeMap(preMap);
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.SET_TYPE_MAP;
			}

		};
		context.addCallback(callback);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setHoldability(int)
	 */
	@Override
	public void setHoldability(final int holdability) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {
			int preHoldability;

			@Override
			public void call(Connection connection) throws SQLException {
				preHoldability = connection.getHoldability();
				if (preHoldability != holdability) {// 只有不一致才赋值
					connection.setHoldability(holdability);
				}
			}

			@Override
			public void reset(Connection connection) throws SQLException {
				connection.setHoldability(preHoldability);
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.SET_HOLDABILITY;
			}

		};
		context.addCallback(callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		return orgConnection.getHoldability();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setSavepoint()
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		return setSavepoint(Transaction.BLANK_SAVE_POINT_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		if (context == null) {
			return null;
		}
		Transaction transaction = context.getTransaction();
		return transaction.setSavepoint(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {

		ConnectionContext context = ConnectionContext.getContext();
		if (context == null) {
			return;
		}
		Transaction transaction = context.getTransaction();
		if (transaction != null) {
			transaction.rollback(savepoint);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		if (context == null) {
			return;
		}
		Transaction transaction = context.getTransaction();
		if (transaction != null) {
			transaction.releaseSavepoint(savepoint);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	@Override
	public Statement createStatement(final int resultSetType,
			final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		StatementWrapper statement = new StatementWrapper(this, builder);
		StatementCallback callback = new StatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement(resultSetType,
						resultSetConcurrency, resultSetHoldability);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int,
	 * int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql,
			final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		PreparedStatementWrapper statement = new PreparedStatementWrapper(this,
				builder, sql);
		PreparedStatementCallback callback = new PreparedStatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}

			@Override
			public PreparedStatement prepareStatement(Connection conn,
					String sql) throws SQLException {
				return conn.prepareStatement(sql, resultSetType,
						resultSetConcurrency, resultSetHoldability);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.orgConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql,
			final int autoGeneratedKeys) throws SQLException {
		PreparedStatementWrapper statement = new PreparedStatementWrapper(this,
				builder, sql);
		PreparedStatementCallback callback = new PreparedStatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}

			@Override
			public PreparedStatement prepareStatement(Connection conn,
					String sql) throws SQLException {
				return conn.prepareStatement(sql, autoGeneratedKeys);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	@Override
	public PreparedStatement prepareStatement(String sql,
			final int[] columnIndexes) throws SQLException {
		PreparedStatementWrapper statement = new PreparedStatementWrapper(this,
				builder, sql);
		PreparedStatementCallback callback = new PreparedStatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}

			@Override
			public PreparedStatement prepareStatement(Connection conn,
					String sql) throws SQLException {
				return conn.prepareStatement(sql, columnIndexes);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public PreparedStatement prepareStatement(String sql,
			final String[] columnNames) throws SQLException {
		PreparedStatementWrapper statement = new PreparedStatementWrapper(this,
				builder, sql);
		PreparedStatementCallback callback = new PreparedStatementCallback() {

			@Override
			public Statement createStatement(Connection conn)
					throws SQLException {
				return conn.createStatement();
			}

			@Override
			public PreparedStatement prepareStatement(Connection conn,
					String sql) throws SQLException {
				return conn.prepareStatement(sql, columnNames);
			}
		};
		statement.setCallback(callback);
		return statement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createClob()
	 */
	@Override
	public Clob createClob() throws SQLException {
		final MockClob resultClob = new MockClob();
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {

			@Override
			public void call(Connection connection) throws SQLException {
				Clob clob = connection.createClob();
				resultClob.wrapAndCopy(clob);
			}

			@Override
			public void reset(Connection connection) throws SQLException {
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.CREATE_CLOB;
			}

		};
		context.addCallback(callback);
		return resultClob;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createBlob()
	 */
	@Override
	public Blob createBlob() throws SQLException {
		final MockBlob resultBlob = new MockBlob();
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {

			@Override
			public void call(Connection connection) throws SQLException {
				Blob blob = connection.createBlob();
				resultBlob.wrapAndCopy(blob);
			}

			@Override
			public void reset(Connection connection) throws SQLException {
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.CREATE_BLOB;
			}

		};
		context.addCallback(callback);
		return resultBlob;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createNClob()
	 */
	@Override
	public NClob createNClob() throws SQLException {
		final MockClob resultClob = new MockClob();
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {

			@Override
			public void call(Connection connection) throws SQLException {
				Clob clob = connection.createNClob();
				resultClob.wrapAndCopy(clob);
			}

			@Override
			public void reset(Connection connection) throws SQLException {
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.CREATE_NCLOB;
			}

		};
		context.addCallback(callback);
		return resultClob;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createSQLXML()
	 */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		final MockSQLXML sqlXml = new MockSQLXML();
		ConnectionContext context = ConnectionContext.getContext();
		ConnectionCallback callback = new ConnectionCallback() {

			@Override
			public void call(Connection connection) throws SQLException {
				SQLXML sql = connection.createSQLXML();
				sqlXml.wrapAndCopy(sql);
			}

			@Override
			public void reset(Connection connection) throws SQLException {
			}

			@Override
			public ConnectionEvent getEvent() {
				return ConnectionEvent.CREATE_SQL_XML;
			}

		};
		context.addCallback(callback);
		return sqlXml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isValid(int)
	 */
	@Override
	public boolean isValid(int timeout) throws SQLException {
		return orgConnection.isValid(timeout);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setClientInfo(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		orgConnection.setClientInfo(name, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		orgConnection.setClientInfo(properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	@Override
	public String getClientInfo(String name) throws SQLException {
		return orgConnection.getClientInfo(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getClientInfo()
	 */
	@Override
	public Properties getClientInfo() throws SQLException {
		return orgConnection.getClientInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createArrayOf(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return orgConnection.createArrayOf(typeName, elements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStruct(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return orgConnection.createStruct(typeName, attributes);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
