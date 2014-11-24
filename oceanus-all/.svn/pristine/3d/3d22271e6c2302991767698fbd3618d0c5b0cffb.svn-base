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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.tx.Transaction;

/**
 * ConnectionUtils
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ConnectionUtils {
	static final Map<String, TableColumn[]> tableInfos = new HashMap<String, TableColumn[]>();

	public static TableColumn[] getTableConlumns(Connection connection,
			String schema, String tableName) throws SQLException {
		try {
			TableColumn[] results = tableInfos.get(tableName.toUpperCase());
			if (results != null) {
				return results;
			}
			DatabaseMetaData dbMeata = connection.getMetaData();

			if (schema == null) {
				schema = "%";
			}
			ResultSet resultSet = dbMeata.getColumns(null, schema, tableName,
					"%");
			List<TableColumn> columns = new LinkedList<TableColumn>();
			int i = 0;
			while (resultSet.next()) {
				TableColumn column = new TableColumn();
				column.setName(resultSet.getString("COLUMN_NAME"));
				column.setResultIndex(i++);
				columns.add(column);
			}

			results = columns.toArray(new TableColumn[] {});
			tableInfos.put(tableName.toUpperCase(), results);
			resultSet.close();
			return results;
		} finally {
			connection.close();
		}
	}

	public static TableColumn[] getTableConlumns(Connection connection,
			String tableName) throws SQLException {
		return getTableConlumns(connection, "%", tableName);
	}

	public static TableColumn[] getTableConlumns(DataNode node, String tableName)
			throws SQLException {
		TableColumn[] results = tableInfos.get(tableName.toUpperCase());
		if (results != null) {
			return results;
		}
		return getTableConlumns(node.getConnection(), "%", tableName);
	}

	public static Transaction getTransaction() {
		return ConnectionContext.getContext().getTransaction();
	}
}
