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
package com.bj58.oceanus.exchange.parse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.nodes.NodeHelper;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.AndNode;
import com.bj58.sql.parser.BinaryOperatorNode;
import com.bj58.sql.parser.BinaryRelationalOperatorNode;
import com.bj58.sql.parser.ColumnReference;
import com.bj58.sql.parser.ConstantNode;
import com.bj58.sql.parser.DeleteNode;
import com.bj58.sql.parser.InListOperatorNode;
import com.bj58.sql.parser.IsNode;
import com.bj58.sql.parser.IsNullNode;
import com.bj58.sql.parser.JoinNode;
import com.bj58.sql.parser.OrNode;
import com.bj58.sql.parser.ParameterNode;
import com.bj58.sql.parser.SQLParser;
import com.bj58.sql.parser.SelectNode;
import com.bj58.sql.parser.ValueNode;

/**
 * ValueNodeParser
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ValueNodeParser {
	
	public static List<TableColumn> parse(ValueNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		if (valueNode instanceof AndNode) {
			AndNode node = (AndNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		

		if (valueNode instanceof OrNode) {
			OrNode node = (OrNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof BinaryOperatorNode) {
			BinaryOperatorNode node = (BinaryOperatorNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		

		if (valueNode instanceof IsNullNode) {
			IsNullNode node = (IsNullNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof IsNode) {
			IsNode node = (IsNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof BinaryRelationalOperatorNode) {
			BinaryRelationalOperatorNode node = (BinaryRelationalOperatorNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof InListOperatorNode) {
			InListOperatorNode node = (InListOperatorNode) valueNode;
			results.addAll(parse(node));
			return results;
		}

		return results;

	}
	
	public static List<TableColumn> parse(JoinNode joinNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		ValueNode joinClause=joinNode.getJoinClause();
		results.addAll(parse(joinClause));
		if(joinNode.getLeftResultSet() instanceof JoinNode){
			results.addAll(parse((JoinNode)joinNode.getLeftResultSet()));
		}
		return results;

	}
	public static List<TableColumn> parse(AndNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		results.addAll(parse(valueNode.getLeftOperand()));
		results.addAll(parse(valueNode.getRightOperand()));
		return results;

	}
	public static List<TableColumn> parse(BinaryOperatorNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		results.addAll(parse(valueNode.getLeftOperand()));
		results.addAll(parse(valueNode.getRightOperand()));
		return results;
	}
	
	public static List<TableColumn> parse(OrNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		results.addAll(parse(valueNode.getLeftOperand()));
		results.addAll(parse(valueNode.getRightOperand()));
		return results;
	}

	public static List<TableColumn> parse(BinaryRelationalOperatorNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();

		ValueNode leftNode = valueNode.getLeftOperand();
		ValueNode rightNode = valueNode.getRightOperand();
		if (leftNode instanceof ConstantNode
				&& rightNode instanceof ConstantNode) {
			return results;
		}
		TableColumn column = new TableColumn();
		if(leftNode instanceof ColumnReference){
			if(rightNode instanceof ConstantNode || rightNode instanceof ParameterNode){
				initColumnValue(column, leftNode);
				column.setValue(NodeHelper.getValue(rightNode));
				results.add(column);
				if(rightNode instanceof ParameterNode){
					ParameterNode parNode=(ParameterNode)rightNode;
					column.setPreparedIndex(parNode.getParameterNumber());
				}
				return results;
			}else{
				results.addAll(parse(rightNode));
			}
		}else if(rightNode instanceof ColumnReference){
			if(leftNode instanceof ConstantNode || leftNode instanceof ParameterNode){
				initColumnValue(column, rightNode);
				column.setValue(NodeHelper.getValue(leftNode));
				results.add(column);
				if(leftNode instanceof ParameterNode){
					ParameterNode parNode=(ParameterNode)leftNode;
					column.setPreparedIndex(parNode.getParameterNumber());
				}
				return results;
			}else{
				results.addAll(parse(leftNode));
			}
		} else {
			results.addAll(parse(leftNode));
			results.addAll(parse(rightNode));
		}
		return results;

	}

	public static List<TableColumn> parse(InListOperatorNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		
		;
		Iterator<ValueNode> valuesIterator=valueNode.getRightOperandList().getNodeList().iterator();
		while(valuesIterator.hasNext()){
			ValueNode item=valuesIterator.next();
			TableColumn column=new TableColumn();
			results.add(column);
			initColumnValue(column, valueNode.getLeftOperand().getNodeList().get(0));
			column.setValue(NodeHelper.getValue(item));
			if(item instanceof ParameterNode){
				ParameterNode parNode=(ParameterNode)item;
				column.setPreparedIndex(parNode.getParameterNumber());
			}
		}
		return results;

	}

	public static List<TableColumn> parse(IsNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		TableColumn column = new TableColumn();
		initColumnValue(column, valueNode.getLeftOperand());
		results.add(column);
		column.setValue(NodeHelper.getValue((ConstantNode) valueNode
				.getRightOperand()));
		return results;
	}

	public static List<TableColumn> parse(IsNullNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		TableColumn column = new TableColumn();
		initColumnValue(column, valueNode.getOperand());
		results.add(column);
		return results;
	}

	public static void initColumnValue(TableColumn column, ValueNode valueNode) {
		column.setName(valueNode.getColumnName());
		column.setTable(valueNode.getTableName());
		try {
			column.setSchema(valueNode.getSchemaName());
		} catch (StandardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		SQLParser parser = new SQLParser();
		DeleteNode node = (DeleteNode) parser
				.parseStatement("delete from orders t where t.id in(1,2)");
		List<TableColumn> results = parse(((SelectNode) node.getResultSetNode())
				.getWhereClause());
		System.out.println(results);
		node = (DeleteNode) parser
				.parseStatement("delete from orders where id is true");
		results = parse(((SelectNode) node.getResultSetNode()).getWhereClause());
		System.out.println(results);
		node = (DeleteNode) parser
				.parseStatement("delete from orders where id = 1 and 1=1 and 2=name");

		results = parse(((SelectNode) node.getResultSetNode()).getWhereClause());
		System.out.println(results);
		
	}

}
