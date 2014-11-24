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
package com.bj58.oceanus.core.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MockClob implements NClob, OutputStreamWatcher, WriterWatcher {
	Clob clob;

	private String charData;

	/**
	 * @see java.sql.Clob#getAsciiStream()
	 */
	public InputStream getAsciiStream() throws SQLException {
		if (this.charData != null) {
			return new ByteArrayInputStream(this.charData.getBytes());
		}

		return null;
	}

	/**
	 * @see java.sql.Clob#getCharacterStream()
	 */
	public Reader getCharacterStream() throws SQLException {
		if (this.charData != null) {
			return new StringReader(this.charData);
		}

		return null;
	}

	/**
	 * @see java.sql.Clob#getSubString(long, int)
	 */
	public String getSubString(long startPos, int length) throws SQLException {
		if (startPos < 1) {
			throw new SQLException("startPos < 1");
		}

		int adjustedStartPos = (int) startPos - 1;
		int adjustedEndIndex = adjustedStartPos + length;

		if (this.charData != null) {
			if (adjustedEndIndex > this.charData.length()) {
				throw new SQLException(
						"adjustedEndIndex > this.charData.length()");
			}

			return this.charData.substring(adjustedStartPos, adjustedEndIndex);
		}

		return null;
	}

	/**
	 * @see java.sql.Clob#length()
	 */
	public long length() throws SQLException {
		if (this.charData != null) {
			return this.charData.length();
		}

		return 0;
	}

	/**
	 * @see java.sql.Clob#position(Clob, long)
	 */
	public long position(java.sql.Clob arg0, long arg1) throws SQLException {
		return position(arg0.getSubString(0L, (int) arg0.length()), arg1);
	}

	/**
	 * @see java.sql.Clob#position(String, long)
	 */
	public long position(String stringToFind, long startPos)
			throws SQLException {
		if (startPos < 1) {
			throw new SQLException("startPos < 1");
		}

		if (this.charData != null) {
			if ((startPos - 1) > this.charData.length()) {
				throw new SQLException(
						"(startPos - 1) > this.charData.length()");
			}

			int pos = this.charData.indexOf(stringToFind, (int) (startPos - 1));

			return (pos == -1) ? (-1) : (pos + 1);
		}

		return -1;
	}

	/**
	 * @see java.sql.Clob#setAsciiStream(long)
	 */
	public OutputStream setAsciiStream(long indexToWriteAt) throws SQLException {
		if (indexToWriteAt < 1) {
			throw new SQLException("indexToWriteAt < 1");
		}

		WatchableOutputStream bytesOut = new WatchableOutputStream();
		bytesOut.setWatcher(this);

		if (indexToWriteAt > 0) {
			bytesOut.write(this.charData.getBytes(), 0,
					(int) (indexToWriteAt - 1));
		}

		return bytesOut;
	}

	/**
	 * @see java.sql.Clob#setCharacterStream(long)
	 */
	public Writer setCharacterStream(long indexToWriteAt) throws SQLException {
		if (indexToWriteAt < 1) {
			throw new SQLException("indexToWriteAt < 1");
		}

		WatchableWriter writer = new WatchableWriter();
		writer.setWatcher(this);

		//
		// Don't call write() if nothing to write...
		//
		if (indexToWriteAt > 1) {
			writer.write(this.charData, 0, (int) (indexToWriteAt - 1));
		}

		return writer;
	}

	/**
	 * @see java.sql.Clob#setString(long, String)
	 */
	public int setString(long pos, String str) throws SQLException {
		if (pos < 1) {
			throw new SQLException("pos < 1");
		}

		if (str == null) {
			throw new SQLException("str == null");
		}

		StringBuffer charBuf = new StringBuffer(this.charData);

		pos--;

		int strLength = str.length();

		charBuf.replace((int) pos, (int) (pos + strLength), str);

		this.charData = charBuf.toString();

		return strLength;
	}

	/**
	 * @see java.sql.Clob#setString(long, String, int, int)
	 */
	public int setString(long pos, String str, int offset, int len)
			throws SQLException {
		if (pos < 1) {
			throw new SQLException("pos < 1");
		}

		if (str == null) {
			throw new SQLException("str == null");
		}

		StringBuffer charBuf = new StringBuffer(this.charData);

		pos--;

		String replaceString = str.substring(offset, len);

		charBuf.replace((int) pos, (int) (pos + replaceString.length()),
				replaceString);

		this.charData = charBuf.toString();

		return len;
	}

	/**
	 * @see com.mysql.jdbc.OutputStreamWatcher#streamClosed(byte[])
	 */
	public void streamClosed(WatchableOutputStream out) {
		int streamSize = out.size();

		if (streamSize < this.charData.length()) {
			out.write(this.charData.getBytes(), streamSize,
					this.charData.length() - streamSize);

		}

		this.charData = toAsciiString(out.toByteArray());
	}

	/**
	 * Returns the bytes as an ASCII String.
	 * 
	 * @param buffer
	 *            the bytes representing the string
	 * 
	 * @return The ASCII String.
	 */
	public static final String toAsciiString(byte[] buffer) {
		return toAsciiString(buffer, 0, buffer.length);
	}

	/**
	 * Returns the bytes as an ASCII String.
	 * 
	 * @param buffer
	 *            the bytes to convert
	 * @param startPos
	 *            the position to start converting
	 * @param length
	 *            the length of the string to convert
	 * 
	 * @return the ASCII string
	 */
	public static final String toAsciiString(byte[] buffer, int startPos,
			int length) {
		char[] charArray = new char[length];
		int readpoint = startPos;

		for (int i = 0; i < length; i++) {
			charArray[i] = (char) buffer[readpoint];
			readpoint++;
		}

		return new String(charArray);
	}

	/**
	 * @see java.sql.Clob#truncate(long)
	 */
	public void truncate(long length) throws SQLException {
		if (length > this.charData.length()) {
			throw new SQLException("length > this.charData.length()");
		}

		this.charData = this.charData.substring(0, (int) length);
	}

	/**
	 * @see com.mysql.jdbc.WriterWatcher#writerClosed(char[])
	 */
	public void writerClosed(char[] charDataBeingWritten) {
		this.charData = new String(charDataBeingWritten);
	}

	/**
	 * @see com.mysql.jdbc.WriterWatcher#writerClosed(char[])
	 */
	public void writerClosed(WatchableWriter out) {
		int dataLength = out.size();

		if (dataLength < this.charData.length()) {
			out.write(this.charData, dataLength, this.charData.length()
					- dataLength);
		}

		this.charData = out.toString();
	}

	public void free() throws SQLException {
		this.charData = null;
	}

	public Reader getCharacterStream(long pos, long length) throws SQLException {
		return new StringReader(getSubString(pos, (int) length));
	}

	public synchronized void wrapAndCopy(Clob clob) {

	}
}
