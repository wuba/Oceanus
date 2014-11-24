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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MockSQLXML implements SQLXML {

	@Override
	public void free() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream setBinaryStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer setCharacterStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setString(String value) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends Source> T getSource(Class<T> sourceClass)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Result> T setResult(Class<T> resultClass)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public synchronized void wrapAndCopy(SQLXML sqlXML) {

	}
}
