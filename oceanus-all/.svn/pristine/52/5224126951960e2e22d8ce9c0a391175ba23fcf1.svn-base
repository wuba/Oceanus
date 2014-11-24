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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bj58.oceanus.core.jdbc.result.RowSet;

/**
 * Count
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Count implements Aggregate<Number> {
	private String name;
	private String key;
	private Number value;
	private Map<ResultSet, Number> values = new HashMap<ResultSet, Number>();
	private final int resultIndex;

	public Count(String name, String key, int resultIndex) {
		this.name = name;
		this.key = key;
		this.resultIndex = resultIndex;
	}

	@Override
	public String key() {
		return key;
	}

	int temp = 0;

	@Override
	public void addRow(ResultSet result, RowSet item) throws SQLException {
		if (item == null) {
			return;
		} 
		values.put(result, (Number) item.getObject(resultIndex));
		temp += ((Number) item.getObject(resultIndex)).intValue(); 
	}

	private void doCount(Number item) {

		if (value == null) {
			value = item;
			return;
		}
		if (item instanceof Integer || item.getClass() == Integer.TYPE) {
			value = (Integer) value + (Integer) item;
			return;
		}
		if (item instanceof Long || item.getClass() == Long.TYPE) {
			value = (Long) value + (Long) item;
			return;
		}
		if (item instanceof Byte || item.getClass() == Byte.TYPE) {
			value = (Byte) value + (Byte) item;
			return;
		}
		if (item instanceof Double || item.getClass() == Double.TYPE) {
			value = (Double) value + (Double) item;
			return;
		}
		if (item instanceof Double || item.getClass() == Double.TYPE) {
			value = (Double) value + (Double) item;
			return;
		}
		if (item instanceof Double || item.getClass() == Double.TYPE) {
			value = (Double) value + (Double) item;
			return;
		}
		if (item instanceof Float || item.getClass() == Float.TYPE) {
			value = (Float) value + (Float) item;
			return;
		}
		if (item instanceof Short || item.getClass() == Short.TYPE) {
			value = (Short) value + (Short) item;
			return;
		}
		if (item instanceof BigDecimal) {
			value =((BigDecimal) value).add((BigDecimal) item);
			return;
		}
		if (item instanceof BigInteger) {
			value =((BigInteger) value).add((BigInteger) item);
			return;
		}
		// if AtomicLong 等等
		value = value.floatValue() + item.floatValue();
	}

	@Override
	public Number value() throws SQLException {
		if (value == null && !values.isEmpty()) {
			Collection<Number> vals=values.values();
			for (Number value :vals ) {
				this.doCount((Number) value);
			}
		}
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
