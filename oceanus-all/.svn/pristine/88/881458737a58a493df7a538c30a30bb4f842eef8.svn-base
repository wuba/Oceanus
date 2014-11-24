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

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.util.Calendar;

import com.bj58.oceanus.core.jdbc.aggregate.Aggregate;

/**
 * RowSet 约束
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface RowSet {

	ResultSet getResultSet();

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>String</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	String getString(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>boolean</code> in the Java
	 * programming language.
	 * 
	 * <P>
	 * If the designated column has a datatype of CHAR or VARCHAR and contains a
	 * "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and
	 * contains a 0, a value of <code>false</code> is returned. If the
	 * designated column has a datatype of CHAR or VARCHAR and contains a "1" or
	 * has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains
	 * a 1, a value of <code>true</code> is returned.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>false</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	boolean getBoolean(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	byte getByte(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>short</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	short getShort(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>int</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	int getInt(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>long</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	long getLong(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>float</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	float getFloat(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>double</code> in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	double getDouble(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.BigDecimal</code> in
	 * the Java programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @param scale
	 *            the number of digits to the right of the decimal point
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @deprecated
	 */
	BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte</code> array in the Java
	 * programming language. The bytes represent the raw values returned by the
	 * driver.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	byte[] getBytes(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
	 * the Java programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.sql.Date getDate(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
	 * the Java programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.sql.Time getTime(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
	 * in the Java programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of ASCII characters. The value
	 * can then be read in chunks from the stream. This method is particularly
	 * suitable for retrieving large <code>LONGVARCHAR</code> values. The JDBC
	 * driver will do any necessary conversion from the database format into
	 * ASCII.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called whether
	 * there is data available or not.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of one-byte ASCII characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.io.InputStream getAsciiStream(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as as a stream of two-byte 3 characters.
	 * The first byte is the high byte; the second byte is the low byte.
	 * 
	 * The value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>
	 * values. The JDBC driver will do any necessary conversion from the
	 * database format into Unicode.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called, whether
	 * there is data available or not.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of two-byte Unicode characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * 
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @deprecated use <code>getCharacterStream</code> in place of
	 *             <code>getUnicodeStream</code>
	 */
	java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of uninterpreted bytes. The
	 * value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARBINARY</code>
	 * values.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called whether
	 * there is data available or not.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of uninterpreted bytes; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.io.InputStream getBinaryStream(int columnIndex) throws SQLException;

	// Methods for accessing results by column label

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>String</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	String getString(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>boolean</code> in the Java
	 * programming language.
	 * 
	 * <P>
	 * If the designated column has a datatype of CHAR or VARCHAR and contains a
	 * "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and
	 * contains a 0, a value of <code>false</code> is returned. If the
	 * designated column has a datatype of CHAR or VARCHAR and contains a "1" or
	 * has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains
	 * a 1, a value of <code>true</code> is returned.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>false</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	boolean getBoolean(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	byte getByte(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>short</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	short getShort(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>int</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	int getInt(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>long</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	long getLong(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>float</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	float getFloat(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>double</code> in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	double getDouble(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> in
	 * the Java programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @param scale
	 *            the number of digits to the right of the decimal point
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @deprecated
	 */
	BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte</code> array in the Java
	 * programming language. The bytes represent the raw values returned by the
	 * driver.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	byte[] getBytes(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
	 * the Java programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.sql.Date getDate(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
	 * the Java programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.sql.Time getTime(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
	 * in the Java programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.sql.Timestamp getTimestamp(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of ASCII characters. The value
	 * can then be read in chunks from the stream. This method is particularly
	 * suitable for retrieving large <code>LONGVARCHAR</code> values. The JDBC
	 * driver will do any necessary conversion from the database format into
	 * ASCII.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>available</code> is called whether there is data
	 * available or not.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of one-byte ASCII characters. If the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>.
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.io.InputStream getAsciiStream(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of two-byte Unicode characters.
	 * The first byte is the high byte; the second byte is the low byte.
	 * 
	 * The value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>
	 * values. The JDBC technology-enabled driver will do any necessary
	 * conversion from the database format into Unicode.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called, whether
	 * there is data available or not.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of two-byte Unicode characters. If the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>.
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @deprecated use <code>getCharacterStream</code> instead
	 */
	java.io.InputStream getUnicodeStream(String columnLabel)
			throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of uninterpreted
	 * <code>byte</code>s. The value can then be read in chunks from the stream.
	 * This method is particularly suitable for retrieving large
	 * <code>LONGVARBINARY</code> values.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>available</code> is called whether there is data
	 * available or not.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of uninterpreted bytes; if the value is SQL
	 *         <code>NULL</code>, the result is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	java.io.InputStream getBinaryStream(String columnLabel) throws SQLException;

	// Advanced features:

	/**
	 * <p>
	 * Gets the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Object</code> in the Java
	 * programming language.
	 * 
	 * <p>
	 * This method will return the value of the given column as a Java object.
	 * The type of the Java object will be the default Java object type
	 * corresponding to the column's SQL type, following the mapping for
	 * built-in types specified in the JDBC specification. If the value is an
	 * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
	 * 
	 * <p>
	 * This method may also be used to read database-specific abstract data
	 * types.
	 * 
	 * In the JDBC 2.0 API, the behavior of method <code>getObject</code> is
	 * extended to materialize data of SQL user-defined types.
	 * <p>
	 * If <code>Connection.getTypeMap</code> does not throw a
	 * <code>SQLFeatureNotSupportedException</code>, then when a column contains
	 * a structured or distinct value, the behavior of this method is as if it
	 * were a call to: <code>getObject(columnIndex,
	 * this.getStatement().getConnection().getTypeMap())</code>.
	 * 
	 * If <code>Connection.getTypeMap</code> does throw a
	 * <code>SQLFeatureNotSupportedException</code>, then structured values are
	 * not supported, and distinct values are mapped to the default Java class
	 * as determined by the underlying SQL type of the DISTINCT type.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a <code>java.lang.Object</code> holding the column value
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	Object getObject(int columnIndex) throws SQLException;

	/**
	 * <p>
	 * Gets the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Object</code> in the Java
	 * programming language.
	 * 
	 * <p>
	 * This method will return the value of the given column as a Java object.
	 * The type of the Java object will be the default Java object type
	 * corresponding to the column's SQL type, following the mapping for
	 * built-in types specified in the JDBC specification. If the value is an
	 * SQL <code>NULL</code>, the driver returns a Java <code>null</code>.
	 * <P>
	 * This method may also be used to read database-specific abstract data
	 * types.
	 * <P>
	 * In the JDBC 2.0 API, the behavior of the method <code>getObject</code> is
	 * extended to materialize data of SQL user-defined types. When a column
	 * contains a structured or distinct value, the behavior of this method is
	 * as if it were a call to: <code>getObject(columnIndex,
	 * this.getStatement().getConnection().getTypeMap())</code>.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>java.lang.Object</code> holding the column value
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	Object getObject(String columnLabel) throws SQLException;

	// ----------------------------------------------------------------

	/**
	 * Maps the given <code>ResultSet</code> column label to its
	 * <code>ResultSet</code> column index.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column index of the given column name
	 * @exception SQLException
	 *                if the <code>ResultSet</code> object does not contain a
	 *                column labeled <code>columnLabel</code>, a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 */
	int findColumn(String columnLabel) throws SQLException;

	// --------------------------JDBC 2.0-----------------------------------

	// ---------------------------------------------------------------------
	// Getters and Setters
	// ---------------------------------------------------------------------

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.io.Reader</code> object.
	 * 
	 * @return a <code>java.io.Reader</code> object that contains the column
	 *         value; if the value is SQL <code>NULL</code>, the value returned
	 *         is <code>null</code> in the Java programming language.
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.io.Reader getCharacterStream(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.io.Reader</code> object.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>java.io.Reader</code> object that contains the column
	 *         value; if the value is SQL <code>NULL</code>, the value returned
	 *         is <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.io.Reader getCharacterStream(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> with
	 * full precision.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value (full precision); if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code> in the
	 *         Java programming language.
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	BigDecimal getBigDecimal(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code> with
	 * full precision.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value (full precision); if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code> in the
	 *         Java programming language.
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 * 
	 */
	BigDecimal getBigDecimal(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Object</code> in the Java
	 * programming language. If the value is an SQL <code>NULL</code>, the
	 * driver returns a Java <code>null</code>. This method uses the given
	 * <code>Map</code> object for the custom mapping of the SQL structured or
	 * distinct type that is being retrieved.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @param map
	 *            a <code>java.util.Map</code> object that contains the mapping
	 *            from SQL type names to classes in the Java programming
	 *            language
	 * @return an <code>Object</code> in the Java programming language
	 *         representing the SQL value
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Object getObject(int columnIndex, java.util.Map<String, Class<?>> map)
			throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Blob</code> object in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a <code>Blob</code> object representing the SQL <code>BLOB</code>
	 *         value in the specified column
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Blob getBlob(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Clob</code> object in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a <code>Clob</code> object representing the SQL <code>CLOB</code>
	 *         value in the specified column
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Clob getClob(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Array</code> object in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return an <code>Array</code> object representing the SQL
	 *         <code>ARRAY</code> value in the specified column
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Array getArray(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Object</code> in the Java
	 * programming language. If the value is an SQL <code>NULL</code>, the
	 * driver returns a Java <code>null</code>. This method uses the specified
	 * <code>Map</code> object for custom mapping if appropriate.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @param map
	 *            a <code>java.util.Map</code> object that contains the mapping
	 *            from SQL type names to classes in the Java programming
	 *            language
	 * @return an <code>Object</code> representing the SQL value in the
	 *         specified column
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Object getObject(String columnLabel, java.util.Map<String, Class<?>> map)
			throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Blob</code> object in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>Blob</code> object representing the SQL <code>BLOB</code>
	 *         value in the specified column
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Blob getBlob(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Clob</code> object in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>Clob</code> object representing the SQL <code>CLOB</code>
	 *         value in the specified column
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Clob getClob(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Array</code> object in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return an <code>Array</code> object representing the SQL
	 *         <code>ARRAY</code> value in the specified column
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.2
	 */
	Array getArray(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
	 * the Java programming language. This method uses the given calendar to
	 * construct an appropriate millisecond value for the date if the underlying
	 * database does not store timezone information.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @param cal
	 *            the <code>java.util.Calendar</code> object to use in
	 *            constructing the date
	 * @return the column value as a <code>java.sql.Date</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Date</code> object in
	 * the Java programming language. This method uses the given calendar to
	 * construct an appropriate millisecond value for the date if the underlying
	 * database does not store timezone information.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @param cal
	 *            the <code>java.util.Calendar</code> object to use in
	 *            constructing the date
	 * @return the column value as a <code>java.sql.Date</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.sql.Date getDate(String columnLabel, Calendar cal) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
	 * the Java programming language. This method uses the given calendar to
	 * construct an appropriate millisecond value for the time if the underlying
	 * database does not store timezone information.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @param cal
	 *            the <code>java.util.Calendar</code> object to use in
	 *            constructing the time
	 * @return the column value as a <code>java.sql.Time</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.sql.Time getTime(int columnIndex, Calendar cal) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Time</code> object in
	 * the Java programming language. This method uses the given calendar to
	 * construct an appropriate millisecond value for the time if the underlying
	 * database does not store timezone information.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @param cal
	 *            the <code>java.util.Calendar</code> object to use in
	 *            constructing the time
	 * @return the column value as a <code>java.sql.Time</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.sql.Time getTime(String columnLabel, Calendar cal) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
	 * in the Java programming language. This method uses the given calendar to
	 * construct an appropriate millisecond value for the timestamp if the
	 * underlying database does not store timezone information.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @param cal
	 *            the <code>java.util.Calendar</code> object to use in
	 *            constructing the timestamp
	 * @return the column value as a <code>java.sql.Timestamp</code> object; if
	 *         the value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
	 * in the Java programming language. This method uses the given calendar to
	 * construct an appropriate millisecond value for the timestamp if the
	 * underlying database does not store timezone information.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @param cal
	 *            the <code>java.util.Calendar</code> object to use in
	 *            constructing the date
	 * @return the column value as a <code>java.sql.Timestamp</code> object; if
	 *         the value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnLabel is not valid or if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @since 1.2
	 */
	java.sql.Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.net.URL</code> object in
	 * the Java programming language.
	 * 
	 * @param columnIndex
	 *            the index of the column 1 is the first, 2 is the second,...
	 * @return the column value as a <code>java.net.URL</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs; this method is called on a closed result set
	 *                or if a URL is malformed
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.4
	 */
	java.net.URL getURL(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.net.URL</code> object in
	 * the Java programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value as a <code>java.net.URL</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs; this method is called on a closed result set
	 *                or if a URL is malformed
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.4
	 */
	java.net.URL getURL(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>NClob</code> object in the Java
	 * programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a <code>NClob</code> object representing the SQL
	 *         <code>NCLOB</code> value in the specified column
	 * @exception SQLException
	 *                if the columnIndex is not valid; if the driver does not
	 *                support national character sets; if the driver can detect
	 *                that a data conversion error could occur; this method is
	 *                called on a closed result set or if a database access
	 *                error occurs
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	NClob getNClob(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>NClob</code> object in the Java
	 * programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>NClob</code> object representing the SQL
	 *         <code>NCLOB</code> value in the specified column
	 * @exception SQLException
	 *                if the columnLabel is not valid; if the driver does not
	 *                support national character sets; if the driver can detect
	 *                that a data conversion error could occur; this method is
	 *                called on a closed result set or if a database access
	 *                error occurs
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	NClob getNClob(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> as a <code>java.sql.SQLXML</code> object in the
	 * Java programming language.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return a <code>SQLXML</code> object that maps an <code>SQL XML</code>
	 *         value
	 * @throws SQLException
	 *             if the columnIndex is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	SQLXML getSQLXML(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> as a <code>java.sql.SQLXML</code> object in the
	 * Java programming language.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>SQLXML</code> object that maps an <code>SQL XML</code>
	 *         value
	 * @throws SQLException
	 *             if the columnLabel is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	SQLXML getSQLXML(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>String</code> in the Java
	 * programming language. It is intended for use when accessing
	 * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
	 * columns.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	String getNString(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>String</code> in the Java
	 * programming language. It is intended for use when accessing
	 * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
	 * columns.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	String getNString(String columnLabel) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.io.Reader</code> object. It
	 * is intended for use when accessing <code>NCHAR</code>,
	 * <code>NVARCHAR</code> and <code>LONGNVARCHAR</code> columns.
	 * 
	 * @return a <code>java.io.Reader</code> object that contains the column
	 *         value; if the value is SQL <code>NULL</code>, the value returned
	 *         is <code>null</code> in the Java programming language.
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @exception SQLException
	 *                if the columnIndex is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	java.io.Reader getNCharacterStream(int columnIndex) throws SQLException;

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.io.Reader</code> object. It
	 * is intended for use when accessing <code>NCHAR</code>,
	 * <code>NVARCHAR</code> and <code>LONGNVARCHAR</code> columns.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @return a <code>java.io.Reader</code> object that contains the column
	 *         value; if the value is SQL <code>NULL</code>, the value returned
	 *         is <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if the columnLabel is not valid; if a database access
	 *                error occurs or this method is called on a closed result
	 *                set
	 * @exception SQLFeatureNotSupportedException
	 *                if the JDBC driver does not support this method
	 * @since 1.6
	 */
	java.io.Reader getNCharacterStream(String columnLabel) throws SQLException;

	// ------------------------- JDBC 4.1 -----------------------------------

	/**
	 * <p>
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object and will convert from the SQL type of the
	 * column to the requested Java data type, if the conversion is supported.
	 * If the conversion is not supported or null is specified for the type, a
	 * <code>SQLException</code> is thrown.
	 * <p>
	 * At a minimum, an implementation must support the conversions defined in
	 * Appendix B, Table B-3 and conversion of appropriate user defined SQL
	 * types to a Java type which implements {@code SQLData}, or {@code Struct}.
	 * Additional conversions may be supported and are vendor defined.
	 * 
	 * @param columnIndex
	 *            the first column is 1, the second is 2, ...
	 * @param type
	 *            Class representing the Java data type to convert the
	 *            designated column to.
	 * @return an instance of {@code type} holding the column value
	 * @throws SQLException
	 *             if conversion is not supported, type is null or another error
	 *             occurs. The getCause() method of the exception may provide a
	 *             more detailed exception, for example, if a conversion error
	 *             occurs
	 * @throws SQLFeatureNotSupportedException
	 *             if the JDBC driver does not support this method
	 * @since 1.7
	 */
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException;

	/**
	 * <p>
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object and will convert from the SQL type of the
	 * column to the requested Java data type, if the conversion is supported.
	 * If the conversion is not supported or null is specified for the type, a
	 * <code>SQLException</code> is thrown.
	 * <p>
	 * At a minimum, an implementation must support the conversions defined in
	 * Appendix B, Table B-3 and conversion of appropriate user defined SQL
	 * types to a Java type which implements {@code SQLData}, or {@code Struct}.
	 * Additional conversions may be supported and are vendor defined.
	 * 
	 * @param columnLabel
	 *            the label for the column specified with the SQL AS clause. If
	 *            the SQL AS clause was not specified, then the label is the
	 *            name of the column
	 * @param type
	 *            Class representing the Java data type to convert the
	 *            designated column to.
	 * @return an instance of {@code type} holding the column value
	 * @throws SQLException
	 *             if conversion is not supported, type is null or another error
	 *             occurs. The getCause() method of the exception may provide a
	 *             more detailed exception, for example, if a conversion error
	 *             occurs
	 * @throws SQLFeatureNotSupportedException
	 *             if the JDBC driver does not support this method
	 * @since 1.7
	 */
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException;

	public void setAggregate(int columnIndex, Aggregate<?> obj);
}
