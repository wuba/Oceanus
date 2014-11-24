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
package com.bj58.oceanus.exchange.jdbc.rw;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.SqlType;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.jdbc.ConnectionCallback;
import com.bj58.oceanus.core.jdbc.StatementCallback;
import com.bj58.oceanus.core.loadbalance.Dispatcher;
import com.bj58.oceanus.core.loadbalance.DispatcherFactory;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.exchange.jdbc.AbstractStatement;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;

/**
 * 读写分离场景中的RWStatementWrapper包装
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RWStatementWrapper extends AbstractStatement {
	
	protected ResultSet resultSet;
	protected int updateCount;
	StatementCallback callback;
	protected NameNode nameNode;

	Dispatcher dispatcher;
	protected Connection usedConn;
	RWConnectionWrapper wrapperConn;
	Statement stmt;
	
	protected BatchItem batchItem;

	protected RWStatementWrapper() {

	}

	RWStatementWrapper(NameNode nameNode, RWConnectionWrapper conn) {
		this.nameNode = nameNode;
		this.wrapperConn = conn;
		dispatcher = DispatcherFactory.create(nameNode);

	}

	protected void setConnectionProperties(Connection connection)
			throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		context.addConnection(connection);
		if (context.getConnectionCallbacks() != null) {
			Iterator<ConnectionCallback> it = context.getConnectionCallbacks()
					.iterator();
			while (it.hasNext()) {
				ConnectionCallback callback = it.next();
				if (callback.getEvent().name().startsWith("CREATE")) {
					callback.call(connection);
					it.remove();
				} else {
					callback.call(connection);
				}
			}
		}
	}

	protected void setStatement(Statement stmt) {
		this.stmt = stmt;
	}

	/**
	 * @TODO......
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	protected void createBatchItem(String sql) throws SQLException {
		batchItem = new BatchItem();
		DefaultAnalyzeResult analyzeResult = new DefaultAnalyzeResult();
		analyzeResult.setStatementType(analyzeStatementType(sql));
		batchItem.setAnalyzeResult(analyzeResult);
		
		if (stmt == null) {
			this.stmt = createStatement(batchItem);
		}
	}

	private StatementType analyzeStatementType(String sql) {
		SqlType sqlType = SqlType.getSqlType(sql);
		
		switch (sqlType) {
			case SELECT:
				return StatementType.SELECT;
			case UPDATE:
			case SELECT_FOR_UPDATE:
			case REPLACE:
			case EXECUTE:
			case CALL:
				return StatementType.UPDATE;
			case DELETE:
				return StatementType.DELETE;
			case INSERT:
				return StatementType.INSERT;
			default:
				throw new IllegalArgumentException("Sql operation can not support!sql=" + sql);
		}
	}

	protected void createQueryBatchItem(String sql) throws SQLException {
		createBatchItem(sql);
	}

	protected void createUpdateBatchItem(String sql) throws SQLException {
		createBatchItem(sql);
	}

	public StatementCallback getCallback() {
		return callback;
	}

	public void setCallback(StatementCallback callback) {
		this.callback = callback;
	}

	private Statement createStatement(BatchItem batchItem) throws SQLException {
		DataNode dataNode = dispatcher.dispatch(nameNode, batchItem);
		this.usedConn = dataNode.getConnection();
		ConnectionContext context = ConnectionContext.getContext();
		context.setCurrentConnection(usedConn);
		setConnectionProperties(usedConn);// 调用前初始化连接属性，在close的时候还原
		Statement stmt = callback.createStatement(usedConn);
		this.stmt = stmt;
		return stmt;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		createBatchItem(sql);
		this.stmt.addBatch(sql);
	}

	@Override
	public void cancel() throws SQLException {
		stmt.cancel();
	}

	@Override
	public void clearBatch() throws SQLException {
		stmt.clearBatch();

	}

	@Override
	public void clearWarnings() throws SQLException {
		if (stmt != null) {
			stmt.clearWarnings();
		}
	}

	@Override
	public void close() throws SQLException {
		stmt.close();
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		createBatchItem(sql);
		switch (batchItem.getAnalyzeResult().getStatementType()) {
			case INSERT:
			case UPDATE:
			case DELETE:
				this.updateCount = this.executeUpdate(sql);
				break;
			case SELECT:
				this.resultSet = this.executeQuery(sql);
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return true;
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		createBatchItem(sql);
		switch (batchItem.getAnalyzeResult().getStatementType()) {
			case INSERT:
			case UPDATE:
			case DELETE:
				this.updateCount = this.executeUpdate(sql, autoGeneratedKeys);
				break;
			case SELECT:
				this.resultSet = this.executeQuery(sql);
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return true;
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		createBatchItem(sql);
		switch (batchItem.getAnalyzeResult().getStatementType()) {
			case INSERT:
			case UPDATE:
			case DELETE:
				this.updateCount = this.executeUpdate(sql, columnIndexes);
				break;
			case SELECT:
				this.resultSet = this.executeQuery(sql);
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return true;
	}

	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		createBatchItem(sql);
		switch (batchItem.getAnalyzeResult().getStatementType()) {
			case INSERT:
			case UPDATE:
			case DELETE:
				this.updateCount = this.executeUpdate(sql, columnNames);
				break;
			case SELECT:
				this.resultSet = this.executeQuery(sql);
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return true;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		int[] result = null;
		try {
			result = this.stmt.executeBatch();
		} finally {

		}
		return result;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		ResultSet result = null;
		try {
			createBatchItem(sql);
			result = stmt.executeQuery(sql);
		} finally {

		}
		return result;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		int result = 0;
		try {
			createBatchItem(sql);
			result = stmt.executeUpdate(sql);
		} finally {

		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		int result = 0;
		try {
			createBatchItem(sql);
			result = stmt.executeUpdate(sql, autoGeneratedKeys);
		} finally {

		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		int result = 0;
		try {
			createBatchItem(sql);
			result = stmt.executeUpdate(sql, columnIndexes);
		} finally {

		}
		return result;
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		int result = 0;
		try {
			createBatchItem(sql);
			result = stmt.executeUpdate(sql, columnNames);
		} finally {

		}
		return result;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return usedConn;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return this.stmt.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws SQLException {
		return this.stmt.getFetchSize();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return this.stmt.getGeneratedKeys();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return this.stmt.getMaxFieldSize();
	}

	@Override
	public int getMaxRows() throws SQLException {
		return this.stmt.getMaxRows();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return this.stmt.getMoreResults();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return this.stmt.getMoreResults(current);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return this.stmt.getQueryTimeout();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.stmt.getResultSet();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return this.stmt.getResultSetConcurrency();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return this.stmt.getResultSetHoldability();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return this.stmt.getResultSetType();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return this.stmt.getUpdateCount();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.stmt.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.stmt.isClosed();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return this.stmt.isPoolable();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		this.stmt.setCursorName(name);

	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		this.stmt.setEscapeProcessing(enable);

	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		this.stmt.setFetchDirection(direction);

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		this.stmt.setFetchSize(rows);

	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		this.stmt.setMaxFieldSize(max);

	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		this.stmt.setMaxRows(max);

	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		this.stmt.setPoolable(poolable);

	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		this.stmt.setQueryTimeout(seconds);

	}

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

}
