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
package com.bj58.oceanus.exchange.builder;

import java.util.ArrayList;
import java.util.List;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.SQLParser;

/**
 * StatementHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class StatementHelper {

	static final ThreadLocal<SQLParser> threadLocal = new ThreadLocal<SQLParser>();

	public static SQLParser createSQLParser() {
		SQLParser parser = threadLocal.get();
		if (parser == null) {
			parser = new SQLParser();
			
			threadLocal.set(parser);
		}
		return parser;
	}

	public static List<TableInfo> getTableNames(QueryTreeNode queryNode) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(queryNode);
		AnalyzeResult result = analyzer.analyze(queryNode);
		return new ArrayList<TableInfo>(result.getTableInfos());
	}

}
