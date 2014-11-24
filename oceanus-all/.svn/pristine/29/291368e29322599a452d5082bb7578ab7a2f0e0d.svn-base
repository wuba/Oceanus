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
package com.bj58.oceanus.core.jdbc.aggregate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bj58.oceanus.core.jdbc.result.RowSet;
 

/**
 * 最大值
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings("rawtypes")
public class Max implements Aggregate<Comparable> {
	private String name;
	private String key;
	private Comparable value;
	private final int resultIndex;

	public Max(String name, String key, int resultIndex) {
		this.name = name;
		this.key = key;
		this.resultIndex=resultIndex;
	}

	@Override
	public String key() {
		return key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addRow(ResultSet resultSet,RowSet row) throws SQLException {
		Comparable item=(Comparable) row.getObject(resultIndex);
		if(item==null){
			return;
		}
		if (value == null) {
			value = item;
		}
		value = (value.compareTo(item) > 0 ? value : item);
	}

	@Override
	public Comparable value() {
		return value;
	}

	@Override
	public String getColumnName() {
		return name;
	}

	@Override
	public int resultIndex() { 
		return resultIndex;
	}
}
