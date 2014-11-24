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

import java.util.LinkedList;
import java.util.List;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.nodes.AbstractNodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.sql.parser.LikeEscapeOperatorNode;
import com.bj58.sql.parser.NodeTypes;

/**
 * SQL节点解析器:LikeOperatorNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class LikeOperatorNodeAnalyzer extends
		AbstractNodeAnalyzer<LikeEscapeOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.LIKE_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(LikeEscapeOperatorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(); 
		List<TableColumn> columns = new LinkedList<TableColumn>();
		TableColumn column = new TableColumn();
		column.setName(node.getReceiver().getColumnName());
		column.setTable(node.getReceiver().getTableName());
		column.setValue(node.getLeftOperand());
		
		columns.add(column);
		result.addConditionColumns(columns);
		return result;
	}
	
}
