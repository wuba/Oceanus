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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.jdbc.StatementCallback;
import com.bj58.oceanus.core.jdbc.result.SimpleMergedResultSet;
import com.bj58.oceanus.core.lifecycle.LifeCycle;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.exchange.builder.StatementContextBuilder;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.Executor;
import com.bj58.oceanus.exchange.router.Router;
import com.bj58.oceanus.exchange.router.RouterFactory;

/**
 * Statement包装
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class StatementWrapper extends AbstractStatement implements Statement ,LifeCycle{

	StatementContextBuilder builder;
	ResultSet resultSet;
	int updateCount;
	StatementCallback callback;
	ConnectionWrapper connection;
	boolean close;
	protected final Set<Statement> statements = new LinkedHashSet<Statement>();
	boolean poolable;
	int fetchSize;
	int fetchDerection;
	String cursorName;
	int queryTimeout;
	boolean escapeProcessing;
	int maxRows;
	int maxFieldSize;

	public StatementWrapper(ConnectionWrapper connection,
			StatementContextBuilder builder) {
		this.builder = builder;
		this.connection = connection;
		StatementContext context = new StatementContext();
		context.setStatementWrapper(this);
		StatementContext.setContext(context);
		close = false;
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		Router router = RouterFactory.createRouter(context);
		@SuppressWarnings("unchecked")
		Executor<ResultSet> executor = (Executor<ResultSet>) router
				.route(context);
		try {
			return executor.execute(context, new ExecuteCallback<ResultSet>() {
				@Override
				public ResultSet execute(Statement statement, String sql)
						throws SQLException {
					statements.add(statement);
					return statement.executeQuery(sql);
				}
			});
		} finally {
			StatementContext.setContext(null);
		}
	}

	protected static int getIntValue(Object result) {
		if (result != null) {
			if (result instanceof Integer[]) {
				Integer[] results = (Integer[]) (result);
				Integer res = 0;
				for (Integer item : results) {
					res += item;
				}
				return res;
			}
			if (result instanceof int[]) {
				int[] results = (int[]) (result);
				int res = 0;
				for (int item : results) {
					res += item;
				}
				return res;
			}
			return (Integer) result;
		}

		return 0;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		Router router = RouterFactory.createRouter(context);
		@SuppressWarnings("unchecked")
		Executor<Integer> executor = (Executor<Integer>) router.route(context);

		try {
			Object result = executor.execute(context,
					new ExecuteCallback<Integer>() {
						@Override
						public Integer execute(Statement statement, String sql)
								throws SQLException {
							statements.add(statement);
							return statement.executeUpdate(sql);
						}
					});
			return getIntValue(result);

		} finally {
			StatementContext.setContext(null);
		}
	}

	@Override
	public void close() throws SQLException {
		close = true;
		for (Statement stmt : statements) {
			stmt.close();
		}
	}

	

	@Override
	public int getMaxFieldSize() throws SQLException {
		return maxFieldSize;
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		maxFieldSize = max;

	}

	@Override
	public int getMaxRows() throws SQLException {
		return maxRows;
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		maxRows = max;
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return queryTimeout;
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		queryTimeout = seconds;
	}

	@Override
	public void cancel() throws SQLException {
		for (Statement stmt : statements) {
			stmt.cancel();
		}

	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		if (statements.size() > 0) {
			return statements.iterator().next().getWarnings();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		for (Statement stmt : statements) {
			stmt.clearWarnings();
		}

	}

	@Override
	public void setCursorName(String name) throws SQLException {
		cursorName = name;

	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		this.updateCount = 0;
		this.resultSet = null;
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		AnalyzeResult analyzeResult = context.getCurrentBatch()
				.getAnalyzeResult();
		switch (analyzeResult.getStatementType()) {
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
	public ResultSet getResultSet() throws SQLException {
		return resultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return updateCount;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		for (Statement stmt : statements) {
			if (stmt.getMoreResults()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		fetchDerection = direction;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return fetchDerection;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		this.fetchSize = rows;

	}

	@Override
	public int getFetchSize() throws SQLException {
		return fetchSize;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		if (statements.size() > 0) {
			return statements.iterator().next().getResultSetConcurrency();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		if (statements.size() > 0) {
			return statements.iterator().next().getResultSetType();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		StatementContext context = StatementContext.getContext();
		context.addBatch(sql);

	}

	@Override
	public void clearBatch() throws SQLException {
		StatementContext context = new StatementContext();
		StatementContext.setContext(context);
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this); 
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int[] executeBatch() throws SQLException {
		StatementContext context = builder.build(StatementContext.getContext()
				.getCurrentBatch().getSql(), StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this); 
		Router router = RouterFactory.createRouter(context);
		// TODO event && parameters
		Executor executor = router.route(context);
		try {
			return (int[]) executor.execute(context, new ExecuteCallback() {
				@Override
				public Object execute(Statement statement, String sql)
						throws SQLException {
					statements.add(statement);
					if (statement instanceof PreparedStatement) {
						return ((PreparedStatement) statement).executeUpdate();
					}
					return statement.executeUpdate(sql);
				}
			});
		} finally {
			StatementContext.setContext(null);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		for (Statement stmt : statements) {
			if (stmt.getMoreResults(current)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		List<ResultSet> results = new ArrayList<ResultSet>(statements.size());
		for (Statement stmt : statements) {
			results.add(stmt.getGeneratedKeys());
		}
		return new SimpleMergedResultSet(results);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys)
			throws SQLException {
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		Router router = RouterFactory.createRouter(context);
		// TODO event && parameters
		Executor<Integer> executor = (Executor<Integer>) router.route(context);
		try {
			Object result = executor.execute(context,
					new ExecuteCallback<Integer>() {
						@Override
						public Integer execute(Statement statement, String sql)
								throws SQLException {
							statements.add(statement);
							return statement.executeUpdate(sql,
									autoGeneratedKeys);
						}
					});
			return getIntValue(result);
		} finally {
			StatementContext.setContext(null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes)
			throws SQLException {
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		Router router = RouterFactory.createRouter(context);
		// TODO event && parameters
		Executor<Integer> executor = (Executor<Integer>) router.route(context);
		try {
			Object result = executor.execute(context,
					new ExecuteCallback<Integer>() {
						@Override
						public Integer execute(Statement statement, String sql)
								throws SQLException {
							statements.add(statement);
							return statement.executeUpdate(sql, columnIndexes);
						}
					});
			return getIntValue(result);
		} finally {
			StatementContext.setContext(null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int executeUpdate(final String sql, final String[] columnNames)
			throws SQLException {
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		Router router = RouterFactory.createRouter(context);
		// TODO event && parameters
		Executor<Integer> executor = (Executor<Integer>) router.route(context);
		try {
			Object result = executor.execute(context,
					new ExecuteCallback<Integer>() {
						@Override
						public Integer execute(Statement statement, String sql)
								throws SQLException {
							statements.add(statement);
							return statement.executeUpdate(sql, columnNames);
						}
					});
			return getIntValue(result);
		} finally {
			StatementContext.setContext(null);
		}
	}

	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys)
			throws SQLException {
		this.updateCount = 0;
		this.resultSet = null;
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		AnalyzeResult analyzeResult = context.getCurrentBatch()
				.getAnalyzeResult();
		switch (analyzeResult.getStatementType()) {
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
	public boolean execute(final String sql, final int[] columnIndexes)
			throws SQLException {
		this.updateCount = 0;
		this.resultSet = null;
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		AnalyzeResult analyzeResult = context.getCurrentBatch()
				.getAnalyzeResult();
		switch (analyzeResult.getStatementType()) {
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
	public boolean execute(final String sql, final String[] columnNames)
			throws SQLException {
		this.updateCount = 0;
		this.resultSet = null;
		StatementContext context = builder.build(sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		AnalyzeResult analyzeResult = context.getCurrentBatch()
				.getAnalyzeResult();
		switch (analyzeResult.getStatementType()) {
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
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return close;
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		this.poolable = poolable;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return poolable;
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

	public StatementCallback getCallback() {
		return callback;
	}

	public void setCallback(StatementCallback callback) {
		this.callback = callback;
	}

	@Override
	public void start() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
