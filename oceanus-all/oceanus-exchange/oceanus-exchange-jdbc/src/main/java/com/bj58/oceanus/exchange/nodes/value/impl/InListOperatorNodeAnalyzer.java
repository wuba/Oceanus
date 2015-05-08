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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.nodes.AbstractNodeAnalyzer;
import com.bj58.oceanus.exchange.nodes.DefaultAnalyzeResult;
import com.bj58.oceanus.exchange.nodes.NodeHelper;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.InListOperatorNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.ParameterNode;
import com.bj58.sql.parser.ValueNode;

/**
 * SQL节点解析器:InListOperatorNodeAnalyzer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class InListOperatorNodeAnalyzer extends
		AbstractNodeAnalyzer<InListOperatorNode, AnalyzeResult> {
	static Logger logger=LoggerFactory.getLogger(InListOperatorNodeAnalyzer.class);
	int[] nodeTypes = { NodeTypes.IN_LIST_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	/**
	 * 将in查询解析分解成N个，根据这n个值进行路由，假如存在子查询，则继续解析
	 */
	@Override
	public AnalyzeResult doAnalyze(InListOperatorNode valueNode) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		List<TableColumn> columns = new LinkedList<TableColumn>();
		Iterator<ValueNode> valuesIterator = valueNode.getRightOperandList()
				.getNodeList().iterator();
		while (valuesIterator.hasNext()) {
			ValueNode item = valuesIterator.next();
			if (!NodeHelper.isColumnValueNode(item)) {
				this.analyzeAndMergeResult(result, item);
				continue;
			}
			TableColumn column = new TableColumn();
			columns.add(column);
			initColumnValue(column, valueNode.getLeftOperand().getNodeList()
					.get(0));
			column.setValue(NodeHelper.getValue(item));
			if (item instanceof ParameterNode) {
				ParameterNode parNode = (ParameterNode) item;
				column.setPreparedIndex(parNode.getParameterNumber()+1);
			}
		}
		result.addConditionColumns(columns);
		return result;
	}

	public static void initColumnValue(TableColumn column, ValueNode valueNode) {
		column.setName(valueNode.getColumnName());
		column.setTable(valueNode.getTableName());
		try {
			column.setSchema(valueNode.getSchemaName());
		} catch (StandardException e) {
			logger.error("sql StandardException!",e);
		}
	}
}
