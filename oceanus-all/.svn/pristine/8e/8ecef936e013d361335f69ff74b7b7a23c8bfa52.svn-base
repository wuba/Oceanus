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
package com.bj58.oceanus.exchange.nodes.value.impl;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.exchange.nodes.AbstractNodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.sql.parser.BetweenOperatorNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.SubqueryNode;

/**
 * SQL节点解析器:BetweenOperatorNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BetweenOperatorNodeAnalyzer extends
		AbstractNodeAnalyzer<BetweenOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.BETWEEN_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(BetweenOperatorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		QueryTreeNode nextNode = node.getLeftOperand();
		if(nextNode instanceof SubqueryNode){
			analyzeAndMergeResult(result, nextNode);
		}
		nextNode = node.getRightOperandList();
		if(nextNode!=null){
			analyzeAndMergeResult(result, nextNode);
		}
		return result;
	}

}
