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
package com.bj58.oceanus.exchange.nodes.dml.impl;

import java.sql.SQLException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.exchange.nodes.AbstractNodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.oceanus.exchange.nodes.NodeHelper;
import com.bj58.sql.parser.InsertNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.ResultColumnList;
import com.bj58.sql.parser.TableName;

/**
 * SQL节点解析器:InsertNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class InsertNodeAnalyzer extends
		AbstractNodeAnalyzer<InsertNode, AnalyzeResult> {
	static Logger logger = LoggerFactory.getLogger(InsertNodeAnalyzer.class);
	int[] nodeTypes = { NodeTypes.INSERT_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	TableInfo createTableInfo(TableName tableName) {
		TableInfo info = new TableInfo();
		try {
			info.setSchema(tableName.getSchemaName());
			info.setOrgName(tableName.getTableName());
			info.setName(tableName.getTableName());
			info.setTableNode(tableName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	@Override
	public AnalyzeResult doAnalyze(InsertNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(node);
		result.setStatementType(StatementType.INSERT);
		TableInfo info = createTableInfo(node.getTargetTableName());
		result.getTableInfos().add(info);
		this.analyzeAndMergeResult(result, node.getResultSetNode());
		ResultColumnList resultColumnList = node.getResultSetNode()
				.getResultColumns();
		if (resultColumnList != null) {
			AnalyzeResult resultCols = Analyzers.get(
					resultColumnList.getNodeType()).analyze(resultColumnList);
			result.addResultColumns(resultCols.getResultColumns());
		}
		ResultColumnList targetColumnList = node.getTargetColumnList();
		if (targetColumnList != null && !targetColumnList.isEmpty()) {
			Iterator<TableColumn> iteratorColumn = result.getResultColumns()
					.iterator();
			while (iteratorColumn.hasNext()) {
				TableColumn tableColumn = iteratorColumn.next();
				tableColumn.setTable(info.getName());
				tableColumn.setName(targetColumnList.get(
						tableColumn.getResultIndex() - 1).getName());
			}
		} else {
			Iterator<TableColumn> iteratorColumn = result.getResultColumns()
					.iterator();
			while (iteratorColumn.hasNext()) {
				TableColumn tableColumn = iteratorColumn.next();
				tableColumn.setTable(info.getName());
				int index = (tableColumn.getResultIndex() - 1);
				String columnName;
				try {
					columnName = NodeHelper.getColumnName(info, index);
					tableColumn.setName(columnName);
				} catch (SQLException e) {
					logger.error("get column name from metadata error!table info="+info, e);
				}

			}
		}
		return result;
	}

}
