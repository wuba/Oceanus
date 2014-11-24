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
package com.bj58.oceanus.core.context;

import java.util.regex.Pattern;

/**
 * SQL类型
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum SqlType {

	INSERT, 
	
	DELETE, 
	
	UPDATE, 
	
	SELECT, 
	
	SELECT_FOR_UPDATE, 
	
	REPLACE, 
	
	TRUNCATE,
	
	EXECUTE,
	
	CALL,
	
	CREATE, 
	
	DROP, 
	
	LOAD, 
	
	MERGE, 
	
	SHOW, 
	
	DEFAULT;
	
	private static final Pattern SELECT_FOR_UPDATE_PATTERN = Pattern.compile("^select\\s+.*\\s+for\\s+update.*$", Pattern.CASE_INSENSITIVE);
	
	public static SqlType getSqlType(String sql) {
		if(sql==null || sql.trim().length()==0)
			throw new IllegalArgumentException("Can not parse sql type, sql is empty!");
		
		String upperSql = sql.trim().toUpperCase();
		
		if (upperSql.startsWith(SELECT.name())) {
			if (SELECT_FOR_UPDATE_PATTERN.matcher(upperSql).matches()) {
				return SELECT_FOR_UPDATE;
			} else {
				return SELECT;
			}
		} else if (upperSql.startsWith(SHOW.name())) {
			return SHOW;
		} else if (upperSql.startsWith(INSERT.name())) {
			return INSERT;
		} else if (upperSql.startsWith(UPDATE.name())) {
			return UPDATE;
		} else if (upperSql.startsWith(DELETE.name())) {
			return DELETE;
		} else if (upperSql.startsWith(REPLACE.name())) {
			return REPLACE;
		} else if (upperSql.startsWith(TRUNCATE.name())){
			return TRUNCATE;
		} else if (upperSql.startsWith(EXECUTE.name())){
			return EXECUTE;
		} else if (upperSql.startsWith(CALL.name())){
			return CALL;
		} else if (upperSql.startsWith(CREATE.name())) {
			return CREATE;
		} else if (upperSql.startsWith(DROP.name())) {
			return DROP;
		} else if (upperSql.startsWith(LOAD.name())) {
			return LOAD;
		} else if (upperSql.startsWith(MERGE.name())){
			return MERGE;
		} else {
			throw new IllegalArgumentException("Sql is not supported : <"+sql+">");
		}
	}
	
}
