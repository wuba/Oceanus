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
package com.bj58.oceanus.exchange.nodes.resultset.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.exchange.nodes.AbstractNodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.ConstantNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.RowResultSetNode;

/**
 * SQL节点解析器:RowResultSetNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RowResultSetNodeAnalyzer extends
		AbstractNodeAnalyzer<RowResultSetNode, AnalyzeResult> {
	static Logger logger = LoggerFactory
			.getLogger(RowResultSetNodeAnalyzer.class);
	int[] nodeTypes = { NodeTypes.ROW_RESULT_SET_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(RowResultSetNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		this.analyzeAndMergeResult(result, node.getResultColumns());
		return result;
	}

	protected void analyzeAndMergeResult(DefaultAnalyzeResult result,
			QueryTreeNode node) {
		if (node != null) {
			if (node instanceof ConstantNode
					|| node.getNodeType() == NodeTypes.PARAMETER_NODE
					|| node.getNodeType() == NodeTypes.COLUMN_REFERENCE) {
				return;
			}
			NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
					.get(node.getNodeType());
			if (analyzer == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("analyzer is null,node=" + node);
				}
				return;
			}
			AnalyzeResult fromListResult = analyzer.analyze(node);
			if (fromListResult == null) {
				return;
			}
			result.addTables(fromListResult.getTableInfos());
			result.addConditionColumns(fromListResult.getConditionColumns());
			result.addResultColumns(fromListResult.getResultColumns()); 
		}
	}
}
