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
package com.bj58.oceanus.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JdbcUtil
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class JdbcUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

	/**
	 * close a connection
	 * @param con
	 */
	public static void closeConnection(Connection conn) {
		try {
			if (conn == null || conn.isClosed())
				return;
			
			conn.close();
		} catch (SQLException ex) {
			logger.debug("Could not close JDBC Connection", ex);
		} catch (Throwable ex) {
			logger.debug("Unexpected exception on closing JDBC Connection", ex);
		}
	}

	/**
	 * close a statement
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt) {
		if (stmt == null) {
			return;
		}
		try {
			stmt.close();
		} catch (SQLException ex) {
			logger.trace("Could not close JDBC Statement", ex);
		} catch (Throwable ex) {
			logger.trace("Unexpected exception on closing JDBC Statement", ex);
		}
	}

	/**
	 * close a ResultSet
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException ex) {
			logger.trace("Could not close JDBC ResultSet", ex);
		} catch (Throwable ex) {
			logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
		}
	}
	
	/**
	 * close all
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(conn);
	}
}
