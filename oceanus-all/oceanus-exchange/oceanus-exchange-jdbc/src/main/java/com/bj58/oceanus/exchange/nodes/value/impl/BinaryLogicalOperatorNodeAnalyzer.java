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
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.sql.parser.BinaryLogicalOperatorNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.QueryTreeNode;

/**
 * SQL节点解析器:BinaryLogicalOperatorNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BinaryLogicalOperatorNodeAnalyzer extends
		AbstractBinaryOperatorNode<BinaryLogicalOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.AND_NODE, NodeTypes.OR_NODE,
			NodeTypes.IS_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(BinaryLogicalOperatorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		QueryTreeNode nextNode = node.getLeftOperand();
		analyzeAndMergeResult(result, nextNode); 
		nextNode = node.getRightOperand();
		analyzeAndMergeResult(result, nextNode); 
		return result;
	}

}
