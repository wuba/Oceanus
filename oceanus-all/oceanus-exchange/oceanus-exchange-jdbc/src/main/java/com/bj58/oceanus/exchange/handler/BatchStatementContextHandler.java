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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.builder.StatementHelper;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.QueryTreeNode;

/**
 * BatchStatementContextHandler
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class BatchStatementContextHandler implements StatementContextHandler<String> {
	static Logger logger=LoggerFactory.getLogger(BatchStatementContextHandler.class);

	@Override
	public StatementContext handle(String sql, StatementContext context) {
		List<BatchItem> batchItems = context.getBaches();
		Iterator<BatchItem> iterator = batchItems.iterator();

		while (iterator.hasNext()) {
			BatchItem batchItem = iterator.next();
			QueryTreeNode statementNode = null;
			try {
				statementNode = StatementHelper.createSQLParser()
						.parseStatement(batchItem.getSql());
				batchItem.setStatementTreeNode(statementNode);
			} catch (StandardException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 解析表信息
			 */
			this.parseTableInfo(batchItem, statementNode);

		}

		return context;
	}

	void parseTableInfo(BatchItem batchItem, QueryTreeNode statementNode) {

		/**
		 * 获取当前sql的数据库表信息
		 */
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode);
		AnalyzeResult result = analyzer.analyze(statementNode);
		batchItem.setAnalyzeResult(result);
		if (StatementType.INSERT.equals(result.getStatementType())) {
			result.getConditionColumns().addAll(result.getResultColumns());
		}

		this.setResolveColumnValues(batchItem);
	}


	/**
	 * 解析prepared的值
	 * 
	 * @param iterator
	 * @param batchItem
	 */
	void setResolveColumnValues(BatchItem batchItem) {
		Collection<TableColumn> columns = batchItem.getAnalyzeResult()
				.getConditionColumns();
		for (TableColumn column: columns){
			if (column.getPreparedIndex() != null) {
				ParameterCallback<?> callback = batchItem.getCallback(column
						.getPreparedIndex());
				if (callback == null) {
					logger.error("callback is null!column=" + column);

				}
				Object value = callback.getParameter();
				column.setValue(value);

			}
			if (logger.isDebugEnabled()) {
				logger.debug("resolve value,column="+column.getName()+",value="+column.getValue());
			}
		}

	}
}
