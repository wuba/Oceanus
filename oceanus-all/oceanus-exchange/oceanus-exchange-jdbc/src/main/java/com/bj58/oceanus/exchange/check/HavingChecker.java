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
package com.bj58.oceanus.exchange.check;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;

/**
 * Having语句检查
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HavingChecker implements Checker {

	@Override
	public boolean check(AnalyzeResult result) throws ShardException {
		if (result.getHavingInfo() == null) {
			return true;
		}
		Collection<TableColumn> resultColumns = result.getResultColumns();
		Collection<TableColumn> havingColumns = result.getHavingInfo()
				.getAggregateColumns();
		Map<String, TableColumn> columnsMap = new HashMap<String, TableColumn>();
		for (TableColumn column : resultColumns) {
			String key = column.getAggregate() + column.getName();
			columnsMap.put(key.toUpperCase(), column);
			if (column.getAliasName() != null
					&& column.getAliasName().trim().length() > 0) {
				key = column.getAliasName();
				columnsMap.put(key.toUpperCase(), column);
			}
		}
		for (TableColumn column : havingColumns) {
			String key = null;
			if (column.getAliasName() != null
					&& column.getAliasName().trim().length() > 0) {
				key = column.getAliasName().toUpperCase();
				if (!columnsMap.containsKey(key)) {
					throw new ShardException(
							"having error!result not include column=" + column);
				}
				continue;
			}
			key = column.getAggregate() + column.getName();
			if (!columnsMap.containsKey(key)) {
				throw new ShardException(
						"having error!result not include column=" + column);
			}
		}
		return true;
	}

}
