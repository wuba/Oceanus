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
package com.bj58.oceanus.exchange.unparse;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.ShardQueryGenerator;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.TableName;

/**
 * DefaultShardQueryGenerator
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DefaultShardQueryGenerator implements ShardQueryGenerator {
	static Logger logger = LoggerFactory
			.getLogger(DefaultShardQueryGenerator.class);
	NodeTreeToSql sqlGenerator = new NodeTreeToSql();

	@Override
	public String generate(NameNodeHolder nameNode, AnalyzeResult analyzeResult) {

		Iterator<TableInfo> iterator = analyzeResult.getTableInfos().iterator();
		while (iterator.hasNext()) {
			TableInfo tableInfo = iterator.next();
			if (tableInfo.getOrgName() != null
					&& tableInfo.getOrgName().trim().length() > 0&&tableInfo.getOrgName().equalsIgnoreCase(nameNode.getOrgTableName())) {
				TableName tableName = (TableName) tableInfo.getTableNode();
				String schema = tableInfo.getSchema();
				if (nameNode.getSchema() != null) {
					schema = nameNode.getSchema();
				}
				tableName.init(schema, nameNode.getTableName());
			}
		}
		try {
			String sql = sqlGenerator.toString((QueryTreeNode) analyzeResult
					.getTreeNode());
			reset(analyzeResult.getTableInfos().iterator());// 重置
			return sql;
		} catch (StandardException e) {
			logger.error("sql StandardException,limit generate!", e);
		}
		// TODO Auto-generated method stub
		return null;
	}

	private void reset(Iterator<TableInfo> iterator) {
		while (iterator.hasNext()) {
			TableInfo tableInfo = iterator.next();
			if (tableInfo.getOrgName() != null
					&& tableInfo.getOrgName().trim().length() > 0) {
				TableName tableName = (TableName) tableInfo.getTableNode();
				tableName.init(tableInfo.getSchema(), tableInfo.getOrgName());
			}
		}
	}

}
