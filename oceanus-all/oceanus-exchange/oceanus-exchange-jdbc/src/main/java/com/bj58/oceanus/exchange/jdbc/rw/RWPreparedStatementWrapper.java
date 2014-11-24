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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.jdbc.PreparedStatementCallback;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.NameNode;

/**
 * 读写分离场景中的RWPreparedStatementWrapper包装
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RWPreparedStatementWrapper extends RWStatementWrapper implements
		PreparedStatement {
	PreparedStatementCallback callback;
	String sql;
	PreparedStatement stmt;
	DataNode dataNode;
 
	RWPreparedStatementWrapper(NameNode nameNode,RWConnectionWrapper conn, String sql)
			throws SQLException {
		super(nameNode, conn);
		this.sql = sql;
	}

	
	public PreparedStatementCallback getCallback() {
		return callback;
	}

	public void setCallback(PreparedStatementCallback callback) throws SQLException {
		super.setCallback(callback);
		this.callback = callback;
		createBatchItem(sql);
		stmt = this.callback.prepareStatement(this.usedConn, sql);
		this.setStatement(stmt);
		
		dataNode = dispatcher.dispatch(nameNode, batchItem);
		this.usedConn = dataNode.getConnection();
		ConnectionContext context = ConnectionContext.getContext();
		context.setCurrentConnection(usedConn);	
		setConnectionProperties(usedConn);//调用前初始化连接属性，在close的时候还原
	}

	@Override
	public void addBatch() throws SQLException {
		stmt.addBatch();
	}

	@Override
	public void clearParameters() throws SQLException {
		stmt.clearParameters();
	}

	@Override
	public boolean execute() throws SQLException {
		switch (batchItem.getAnalyzeResult().getStatementType()) {
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
	public ResultSet executeQuery() throws SQLException {
		return stmt.executeQuery();
	}

	@Override
	public int executeUpdate() throws SQLException {
		return stmt.executeUpdate();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return stmt.getMetaData();
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return stmt.getParameterMetaData();
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		stmt.setArray(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream arg1)
			throws SQLException {
		stmt.setAsciiStream(parameterIndex, arg1);

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream arg1, int arg2)
			throws SQLException {
		stmt.setAsciiStream(parameterIndex, arg1, arg2);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream arg1, long arg2)
			throws SQLException {
		stmt.setAsciiStream(parameterIndex, arg1, arg2);

	}

	@Override
	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		stmt.setBigDecimal(arg0, arg1);
	}

	@Override
	public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
		stmt.setBinaryStream(arg0, arg1);

	}

	@Override
	public void setBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		stmt.setBinaryStream(arg0, arg1, arg2);
	}

	@Override
	public void setBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		stmt.setBinaryStream(arg0, arg1, arg2);
	}

	@Override
	public void setBlob(int arg0, Blob arg1) throws SQLException {
		stmt.setBlob(arg0, arg1);
	}

	@Override
	public void setBlob(int arg0, InputStream arg1) throws SQLException {
		stmt.setBlob(arg0, arg1);

	}

	@Override
	public void setBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		stmt.setBlob(arg0, arg1, arg2);

	}

	@Override
	public void setBoolean(int arg0, boolean arg1) throws SQLException {
		stmt.setBoolean(arg0, arg1);

	}

	@Override
	public void setByte(int arg0, byte arg1) throws SQLException {
		stmt.setByte(arg0, arg1);

	}

	@Override
	public void setBytes(int arg0, byte[] arg1) throws SQLException {
		stmt.setBytes(arg0, arg1);

	}

	@Override
	public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
		stmt.setCharacterStream(arg0, arg1);

	}

	@Override
	public void setCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException {
		stmt.setCharacterStream(arg0, arg1, arg2);

	}

	@Override
	public void setCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		stmt.setCharacterStream(arg0, arg1, arg2);
	}

	@Override
	public void setClob(int arg0, Clob arg1) throws SQLException {
		stmt.setClob(arg0, arg1);

	}

	@Override
	public void setClob(int arg0, Reader arg1) throws SQLException {
		stmt.setClob(arg0, arg1);

	}

	@Override
	public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
		stmt.setClob(arg0, arg1, arg2);

	}

	@Override
	public void setDate(int arg0, Date arg1) throws SQLException {
		stmt.setDate(arg0, arg1);
	}

	@Override
	public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException {
		stmt.setDate(arg0, arg1, arg2);
	}

	@Override
	public void setDouble(int arg0, double arg1) throws SQLException {
		stmt.setDouble(arg0, arg1);
	}

	@Override
	public void setFloat(int arg0, float arg1) throws SQLException {
		stmt.setFloat(arg0, arg1);
	}

	@Override
	public void setInt(int arg0, int arg1) throws SQLException {
		stmt.setInt(arg0, arg1);
	}

	@Override
	public void setLong(int arg0, long arg1) throws SQLException {
		stmt.setLong(arg0, arg1);
	}

	@Override
	public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
		stmt.setNCharacterStream(arg0, arg1);
	}

	@Override
	public void setNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		stmt.setNCharacterStream(arg0, arg1, arg2);

	}

	@Override
	public void setNClob(int arg0, NClob arg1) throws SQLException {
		stmt.setNClob(arg0, arg1);

	}

	@Override
	public void setNClob(int arg0, Reader arg1) throws SQLException {
		stmt.setNClob(arg0, arg1);

	}

	@Override
	public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		stmt.setNClob(arg0, arg1, arg2);

	}

	@Override
	public void setNString(int arg0, String arg1) throws SQLException {
		stmt.setNString(arg0, arg1);
	}

	@Override
	public void setNull(int arg0, int arg1) throws SQLException {
		stmt.setNull(arg0, arg1);

	}

	@Override
	public void setNull(int arg0, int arg1, String arg2) throws SQLException {
		stmt.setNull(arg0, arg1, arg2);
	}

	@Override
	public void setObject(int arg0, Object arg1) throws SQLException {
		stmt.setObject(arg0, arg1);

	}

	@Override
	public void setObject(int arg0, Object arg1, int arg2) throws SQLException {
		stmt.setObject(arg0, arg1, arg2);
	}

	@Override
	public void setObject(int arg0, Object arg1, int arg2, int arg3)
			throws SQLException {
		stmt.setObject(arg0, arg1, arg2);

	}

	@Override
	public void setRef(int arg0, Ref arg1) throws SQLException {
		stmt.setRef(arg0, arg1);

	}

	@Override
	public void setRowId(int arg0, RowId arg1) throws SQLException {
		stmt.setRowId(arg0, arg1);

	}

	@Override
	public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
		stmt.setSQLXML(arg0, arg1);

	}

	@Override
	public void setShort(int arg0, short arg1) throws SQLException {
		stmt.setShort(arg0, arg1);

	}

	@Override
	public void setString(int arg0, String arg1) throws SQLException {
		stmt.setString(arg0, arg1);

	}

	@Override
	public void setTime(int arg0, Time arg1) throws SQLException {
		stmt.setTime(arg0, arg1);

	}

	@Override
	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException {
		stmt.setTime(arg0, arg1, arg2);

	}

	@Override
	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException {
		stmt.setTimestamp(arg0, arg1);

	}

	@Override
	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2)
			throws SQLException {
		stmt.setTimestamp(arg0, arg1, arg2);

	}

	@Override
	public void setURL(int arg0, URL arg1) throws SQLException {
		stmt.setURL(arg0, arg1);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void setUnicodeStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		stmt.setUnicodeStream(arg0, arg1, arg2);

	}

}
