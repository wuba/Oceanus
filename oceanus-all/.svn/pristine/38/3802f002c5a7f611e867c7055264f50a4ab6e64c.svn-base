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
package com.bj58.oceanus.core.exception.sqlexception;

import java.sql.SQLException;

/**
 * mysql 异常
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class MySqlException {

	private MySqlException() {
	}

	public static boolean isMysqlException(SQLException exception) {
		String sqlState = exception.getSQLState();
		if (sqlState != null && sqlState.startsWith("08")) { // per Mark Matthews at MySQL
			return true;
		}

		MySqlExceptionType type = MySqlExceptionType
				.getMySqlExceptionType(exception.getErrorCode());

		return type != null;
	}

}
