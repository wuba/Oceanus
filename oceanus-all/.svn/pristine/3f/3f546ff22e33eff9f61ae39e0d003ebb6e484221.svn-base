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
package com.bj58.oceanus.exchange.router;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;

/**
 * RouteHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class RouteHelper {
	public static Set<TableInfo> getOrgTables(Collection<TableInfo> tableInfos) {
		Iterator<TableInfo> iterator = tableInfos.iterator();
		Set<TableInfo> results = new LinkedHashSet<TableInfo>();
		while (iterator.hasNext()) {
			TableInfo info = iterator.next();
			if (info.getOrgName() != null) {
				results.add(info);
			}
		}
		return results;
	}

	private static List<Map.Entry<String, List<TableColumn>>> cloneIterator(
			Iterator<Map.Entry<String, List<TableColumn>>> iterator) {
		List<Map.Entry<String, List<TableColumn>>> results = new LinkedList<Map.Entry<String, List<TableColumn>>>();
		while (iterator.hasNext()) {
			results.add(iterator.next());
		}
		return results;
	}

	/**
	 * 获取一个语句中出现的数据的值的组合： 譬如： select * from users where username ='name1' or
	 * username='name2'
	 * 
	 * @param parameters
	 * @return
	 */
	public static List<Map<String, Object>> getParameterValues(
			Map<String, List<TableColumn>> resolveColumnsMap) {
		Set<Map.Entry<String, List<TableColumn>>> entrySet = resolveColumnsMap
				.entrySet();
		Iterator<Map.Entry<String, List<TableColumn>>> iterator = entrySet
				.iterator();
		List<KeyValue> leaves = new LinkedList<KeyValue>();
		getLeaf(null, iterator, leaves);
		List<Map<String, Object>> datas = new LinkedList<Map<String, Object>>();
		for (KeyValue kv : leaves) {
			Map<String, Object> values = new LinkedHashMap<String, Object>();
			toMap(values, kv);
			datas.add(values);
		}
		return datas;
	}

	private static void toMap(Map<String, Object> values, KeyValue kv) {
		values.put(kv.key, kv.value);
		while (kv.parent != null) {
			kv = kv.parent;
			values.put(kv.key, kv.value);
		}

	}

	/**
	 * 获取所有的叶子节点，进行路由规划
	 * 
	 * @param parent
	 * @param iterator
	 * @param leaves
	 */
	private static void getLeaf(KeyValue parent,
			Iterator<Map.Entry<String, List<TableColumn>>> iterator,
			List<KeyValue> leaves) {
		if (!iterator.hasNext()) {
			return;
		}
		Map.Entry<String, List<TableColumn>> entry = iterator.next();
		if (!iterator.hasNext()) {
			String key = entry.getKey();
			List<TableColumn> columns = entry.getValue();
			for (TableColumn column : columns) {
				KeyValue kv = new KeyValue();
				kv.parent = parent;
				kv.key = key;
				kv.value = column.getValue();
				leaves.add(kv);
			}
			return;
		} else {
			String key = entry.getKey();
			List<TableColumn> columns = entry.getValue();
			List<Map.Entry<String, List<TableColumn>>> list = cloneIterator(iterator);
			for (int i = 0; i < columns.size(); i++) {
				TableColumn column = columns.get(i);
				KeyValue kv = new KeyValue();
				kv.parent = parent;
				kv.key = key;
				kv.value = column.getValue();
				getLeaf(kv, list.iterator(), leaves);
			}
		}

	}

	/**
	 * 获取分库分表的配置的字段
	 * 
	 * @param tableName
	 * @return
	 */
	public static String[] getConfigColumns(String tableName) { 
		return Configurations.getInstance().getTableConfig(tableName).getColumns();
	}

	/**
	 * 获取配置的列的名称
	 * 
	 * @param tableName
	 * @return
	 */
	private static Map<String, List<TableColumn>> getConfigColumnsMap(
			String tableName) {
		Map<String, List<TableColumn>> results = new HashMap<String, List<TableColumn>>();
		String configColumns[] = getConfigColumns(tableName);
		for (String item : configColumns) {
			results.put(item.toUpperCase(), new ArrayList<TableColumn>());
		}
		return results;
	}

	private static Map<String, TableInfo> tableInfoMap(
			Collection<TableInfo> tableInfos) {
		Iterator<TableInfo> iterator = tableInfos.iterator();
		Map<String, TableInfo> results = new HashMap<String, TableInfo>(
				tableInfos.size());
		while (iterator.hasNext()) {
			TableInfo itemInfo = iterator.next();
			results.put(itemInfo.getName().toUpperCase(), itemInfo);
		}
		return results;
	}

	/**
	 * 从解析中的语句和Table的配置分裤分表字段里获取字段信息
	 * 
	 * @param tableName
	 * @param result
	 * @return
	 */
	public static Map<String, List<TableColumn>> getResolveColumns(
			String tableName, AnalyzeResult result) {
		Collection<TableColumn> columns = result.getConditionColumns();
		Iterator<TableColumn> it = columns.iterator();
		Map<String, List<TableColumn>> resultsMap = getConfigColumnsMap(tableName);
		Map<String, TableInfo> tableInfosMap = tableInfoMap(result
				.getTableInfos());
		while (it.hasNext()) {
			TableColumn column = it.next();

			if (column.getTable() != null) {
				TableInfo tableInfo = tableInfosMap.get(column.getTable()
						.toUpperCase());
				if (tableName.equalsIgnoreCase(tableInfo.getOrgName())) {
					List<TableColumn> results = resultsMap.get(column.getName()
							.toUpperCase());
					if (results != null) {
						results.add(column);
					}
				}
			} else {
				List<TableColumn> results = resultsMap.get(column.getName()
						.toUpperCase());
				if (results != null) {
					results.add(column);
				}
			}
		}

		return resultsMap;
	}
}
