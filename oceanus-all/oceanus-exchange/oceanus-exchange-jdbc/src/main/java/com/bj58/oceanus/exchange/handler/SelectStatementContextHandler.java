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
package com.bj58.oceanus.exchange.handler;

import java.util.ArrayList;
import java.util.List;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.exchange.check.Checker;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.CursorNode;
import com.bj58.sql.parser.QueryTreeNode;

/**
 * SelectStatementContextHandler
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SelectStatementContextHandler implements
		StatementContextHandler<CursorNode> {
	static final List<Checker> checkers = new ArrayList<Checker>();

	@Override
	public StatementContext handle(CursorNode statementNode,
			StatementContext context) {

		BatchItem batchItem = context.getCurrentBatch();
		batchItem.setStatementTreeNode(statementNode);
		/**
		 * 解析表信息
		 */
		this.parseTableInfo(batchItem, statementNode);
		return context;
	}

	void parseTableInfo(BatchItem batchItem, CursorNode statementNode) {

		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode.getNodeType());
		AnalyzeResult result = analyzer.analyze(statementNode);
		batchItem.setAnalyzeResult(result);
 
		if (!checkers.isEmpty()) {
			for (Checker checker : checkers) {
				checker.check(result);
			}
		}
	}

	 

}
