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
package com.bj58.oceanus.core.jdbc.result;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.jdbc.aggregate.Aggregate;

/**
 * RowSet实现
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SimpleRowSet implements RowSet {
	protected ColumnFinder columnFinder;
	protected Object[] values;
	protected ResultSet resultSet;

	public SimpleRowSet(Object[] values, ColumnFinder columnFinder) {
		this.columnFinder = columnFinder;
		this.values = values;
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), String.class);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Boolean.TYPE);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Byte.TYPE);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Short.TYPE);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Integer.TYPE);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Long.TYPE);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Float.TYPE);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Double.TYPE);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex),
				BigDecimal.class);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), byte[].class);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Date.class);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), Time.class);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return TypeConvertUtils
				.convert(getObject(columnIndex), Timestamp.class);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getString(columnIndex);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getBoolean(columnIndex);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getByte(columnIndex);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getShort(columnIndex);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getInt(columnIndex);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getLong(columnIndex);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getFloat(columnIndex);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getDouble(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getBigDecimal(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getBytes(columnIndex);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getDate(columnIndex);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getTimestamp(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		Object value = values[columnIndex - 1];
		if (value instanceof Aggregate) {
			try {
				value = ((Aggregate) value).value();
			} catch (SQLException e) {
				throw new ShardException("shard error", e);
			}
		}
		return value;
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getObject(columnIndex);
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		return columnFinder.findIndex(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getBigDecimal(columnIndex);
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getDate(columnIndex, cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getTime(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getTimestamp(columnIndex, cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		return TypeConvertUtils.convert(getObject(columnIndex), type);
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		int columnIndex = columnFinder.findIndex(columnLabel);
		return getObject(columnIndex, type);
	}

	@Override
	public void setAggregate(int columnIndex, Aggregate<?> obj) {
		this.values[columnIndex - 1] = obj;

	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

}
