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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.jdbc.PreparedStatementCallback;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.exchange.builder.StatementContextBuilder;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.Executor;
import com.bj58.oceanus.exchange.router.Router;
import com.bj58.oceanus.exchange.router.RouterFactory;

/**
 * PreparedStatement包装
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PreparedStatementWrapper extends StatementWrapper implements
		PreparedStatement {
	PreparedStatementCallback callback;
	String sql;

	public PreparedStatementWrapper(ConnectionWrapper connection,
			StatementContextBuilder builder) {
		super(connection, builder);
	}

	public PreparedStatementWrapper(ConnectionWrapper connection,
			StatementContextBuilder builder, String sql) {
		super(connection, builder);
		this.sql = sql;
		StatementContext.getContext().getCurrentBatch().setSql(sql);
	}

	public PreparedStatementCallback getCallback() {
		return callback;
	}

	public void setCallback(PreparedStatementCallback callback) {
		super.setCallback(callback);
		this.callback = callback;
	}

	private void addParameterCallback(ParameterCallback callback) {
		StatementContext.getContext().getCurrentBatch().add(callback);
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		StatementContext context = builder.build(this.sql,
				StatementContext.getContext());
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		try {
			Router router = RouterFactory.createRouter(context);
			// TODO event && parameters
			Executor<ResultSet> executor = (Executor<ResultSet>) router
					.route(context);
			return executor.execute(context, new ExecuteCallback<ResultSet>() {
				@Override
				public ResultSet execute(Statement statement, String sql)
						throws SQLException {
					statements.add(statement);
					PreparedStatement preparedStatement = (PreparedStatement) statement;
					return preparedStatement.executeQuery();
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			StatementContext.setContext(null);
		}
	}

	@Override
	public int executeUpdate() throws SQLException { 
		StatementContext context = builder.build(this.sql,
				StatementContext.getContext()); 
		context.setStatementCreateCallback(callback);
		context.setStatementWrapper(this);
		try {
			Router router = RouterFactory.createRouter(context);

			Executor executor = router.route(context);  
			Object result = executor.execute(context,
					new ExecuteCallback<Integer>() {
						@Override
						public Integer execute(Statement statement, String sql)
								throws SQLException {
							statements.add(statement);
							PreparedStatement preparedStatement = (PreparedStatement) statement;
							Integer result = preparedStatement.executeUpdate();
							return result;
						}
					});  
			return getIntValue(result);
		} finally {
			StatementContext.setContext(null);
		}
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, null) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNull(parameterIndex(), sqlType);
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBoolean(parameterIndex(), x);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setByte(final int parameterIndex, final byte x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setByte(parameterIndex(), (Byte)getParameter());
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setShort(final int parameterIndex, final short x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setShort(parameterIndex(), (Short)getParameter());
			}

		};
		addParameterCallback(callback);

	}

	@Override
	public void setInt(final int parameterIndex, final int x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setInt(parameterIndex(), (Integer)getParameter());
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setLong(final int parameterIndex, final long x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setLong(parameterIndex(), (Long)getParameter());
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setFloat(final int parameterIndex, final float x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setFloat(parameterIndex(), (Float)getParameter());
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setDouble(final int parameterIndex, final double x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setDouble(parameterIndex(), (Double)getParameter());
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBigDecimal(parameterIndex(), (BigDecimal)getParameter());
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setString(final int parameterIndex, final String x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setString(parameterIndex(), (String)getParameter());
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setBytes(final int parameterIndex, final byte[] x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBytes(parameterIndex(), (byte[])getParameter());
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setDate(final int parameterIndex, final Date x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setDate(parameterIndex(), (Date)getParameter());
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setTime(parameterIndex(), (Time)getParameter());
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setTimestamp(parameterIndex(), (Timestamp)getParameter());
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x,
			final int length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setAsciiStream(parameterIndex(), (InputStream)getParameter(), length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setUnicodeStream(final int parameterIndex, final InputStream x,
			final int length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@SuppressWarnings("deprecation")
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setUnicodeStream(parameterIndex(), (InputStream)getParameter(), length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x,
			final int length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBinaryStream(parameterIndex(), (InputStream)getParameter(), length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void clearParameters() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x,
			final int targetSqlType) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setObject(parameterIndex(),getParameter(), targetSqlType);
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setObject(parameterIndex(), getParameter());
			}
		};

		addParameterCallback(callback);
	}

	@Override
	public boolean execute() throws SQLException {
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
			this.updateCount = this.executeUpdate();
			break;
		case SELECT:
			this.resultSet = this.executeQuery();
			break;
		default:
			throw new UnsupportedOperationException();
		}

		return updateCount>=0||resultSet!=null;
	}

	@Override
	public void addBatch() throws SQLException {
		StatementContext context = StatementContext.getContext();
		context.addBatch();

	}

	@Override
	public void setCharacterStream(final int parameterIndex,
			final Reader reader, final int length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setCharacterStream(parameterIndex(), reader,
						length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setRef(final int parameterIndex, final Ref x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setRef(parameterIndex(), x);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setBlob(final int parameterIndex, final Blob x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBlob(parameterIndex(), x);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setClob(final int parameterIndex, final Clob x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setClob(parameterIndex(), x);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setArray(final int parameterIndex, final Array x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setArray(parameterIndex(), x);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x,
			final Calendar cal) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setDate(parameterIndex(), x, cal);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x,
			final Calendar cal) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setTime(parameterIndex(), x, cal);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x,
			final Calendar cal) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setTimestamp(parameterIndex(), x, cal);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType,
			final String typeName) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, null) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNull(parameterIndex(), sqlType, typeName);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setURL(final int parameterIndex, final URL x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setURL(parameterIndex(), x);
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRowId(final int parameterIndex, final RowId x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setRowId(parameterIndex(), x);
				;
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setNString(final int parameterIndex, final String value)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, value) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNString(parameterIndex(), (String) value);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setNCharacterStream(final int parameterIndex,
			final Reader value, final long length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, value) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNCharacterStream(parameterIndex(),
						(Reader) value, length);
				;
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setNClob(final int parameterIndex, final NClob value)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, value) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNClob(parameterIndex(), (NClob) value);
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader,
			final long length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setClob(parameterIndex(), reader, length);
			}

		};
		addParameterCallback(callback);

	}

	@Override
	public void setBlob(final int parameterIndex,
			final InputStream inputStream, final long length)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, inputStream) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement
						.setBlob(parameterIndex(), inputStream, length);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader,
			final long length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNClob(parameterIndex(), reader, length);
			}

		};
		addParameterCallback(callback);

	}

	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, xmlObject) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setSQLXML(parameterIndex(), xmlObject);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setObject(final int parameterIndex, final Object x,
			final int targetSqlType, final int scaleOrLength)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setObject(parameterIndex(), x, targetSqlType,
						scaleOrLength);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x,
			final long length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setAsciiStream(parameterIndex(), x, length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x,
			final long length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBinaryStream(parameterIndex(), x, length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setCharacterStream(final int parameterIndex,
			final Reader reader, final long length) throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setCharacterStream(parameterIndex(), reader,
						length);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setAsciiStream(parameterIndex(), x);
			}
		};
		addParameterCallback(callback);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, x) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBinaryStream(parameterIndex(), x);
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setCharacterStream(parameterIndex(), reader);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, value) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNCharacterStream(parameterIndex(),
						(Reader) value);
			}

		};
		addParameterCallback(callback);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setClob(parameterIndex(), reader);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, inputStream) {

			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setBlob(parameterIndex(), inputStream);
			}
		};
		addParameterCallback(callback);

	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader)
			throws SQLException {
		ParameterCallback callback = new ParameterCallbackAction(
				parameterIndex, reader) {
			@Override
			public void call(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setNClob(parameterIndex(), reader);
			}
		};
		addParameterCallback(callback);

	}

	abstract class ParameterCallbackAction implements ParameterCallback {
		int index;
		Object value;

		ParameterCallbackAction(int parameterIndex) {
			index = parameterIndex;
		}

		ParameterCallbackAction(int parameterIndex, Object value) {
			index = parameterIndex;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return index;
		}

		@Override
		public int parameterIndex() {

			return index;
		}

		public void setParameterIndex(int index) {
			this.index = index;
		}

		@Override
		public Object getParameter() {
			return value;
		}
		public void setParameter(Object object){
			this.value=object;
		}
	}
}
