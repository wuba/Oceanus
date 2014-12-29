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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.ShardQueryGenerator;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.TrackerExecutor;
import com.bj58.oceanus.exchange.handler.HandlerFactory;
import com.bj58.oceanus.exchange.handler.StatementContextHandler;
import com.bj58.oceanus.exchange.unparse.DefaultShardQueryGenerator;
import com.bj58.oceanus.exchange.unparse.LimitAggregateQueryGenerator;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.DMLStatementNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.SQLParser;

/**
 * 默认Statement上下文构建器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DefaultStatementContextBuilder implements StatementContextBuilder {
	static Logger logger = LoggerFactory
			.getLogger(DefaultStatementContextBuilder.class);
	static final ShardQueryGenerator generator = new DefaultShardQueryGenerator();
	static final ShardQueryGenerator limitAvgGenerator = new LimitAggregateQueryGenerator();

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public StatementContext build(String sql, StatementContext context)
			throws SQLException {
		if (context == null) {
			context = new StatementContext();
			StatementContext.setContext(context);
			if (logger.isDebugEnabled()) {
				logger.debug("create context!sql=" + sql);
			}
		}
		if (context.getCurrentBatch().getSql() == null) {
			context.getCurrentBatch().setSql(sql);
		}

		StatementContextHandler handler = null;
		if (context.isBatch()) {
			handler = HandlerFactory.create(StatementType.BATCH);
			StatementContext resultContext = handler.handle(sql, context);
			processPreparedValues(resultContext);
			return resultContext;
		}
		
		TrackerExecutor.trackBegin(TrackPoint.PARSE_SQL, sql);
		SQLParser parser = StatementHelper.createSQLParser();
		try {
			DMLStatementNode statementNode = (DMLStatementNode) parser
					.parseStatement(sql);
			switch (statementNode.getNodeType()) {
			case NodeTypes.CURSOR_NODE:
				handler = HandlerFactory.create(StatementType.SELECT);
				break;
			case NodeTypes.DELETE_NODE:
				handler = HandlerFactory.create(StatementType.DELETE);
				break;
			case NodeTypes.UPDATE_NODE:
				handler = HandlerFactory.create(StatementType.UPDATE);
				break;
			case NodeTypes.INSERT_NODE:
				handler = HandlerFactory.create(StatementType.INSERT);
				break;
			case NodeTypes.CALL_STATEMENT_NODE:
				handler = HandlerFactory.create(StatementType.CALLABLE);
				break;
			}
			
			StatementContext resultContext = handler.handle(statementNode,
					context);
			
			TrackerExecutor.trackEnd(TrackPoint.PARSE_SQL);
			
			processPreparedValues(resultContext);
			return resultContext;

		} catch (StandardException se) {
			System.out.println("sql parse error, sql:"+sql);
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		processPreparedValues(context);
		return context;
	}

	private void processPreparedValues(StatementContext context) {
		Statement statement = context.getStatementWrapper();
		if (statement instanceof PreparedStatement) {
			for (BatchItem batchItem : context.getBaches()) {
				this.setResolveColumnValues(batchItem,
						batchItem.getAnalyzeResult());
			}

		}
	}

	/**
	 * 解析prepared的值
	 * 
	 * @param iterator
	 * @param batchItem
	 */
	void setResolveColumnValues(BatchItem batchItem, AnalyzeResult analyzeResult) {
		Collection<TableColumn> columns = analyzeResult.getConditionColumns();
		for (TableColumn column : columns) {
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
				logger.debug("resolve value,column=" + column.getName()
						+ ",value=" + column.getValue());
			}
		}

	}

	public static void main(String args[]) throws Exception {
		DefaultStatementContextBuilder builder = new DefaultStatementContextBuilder();
		String sql = "select * from tablename as n,tablename2 n2 where n.col=1 and (n.c=n2.b or a=a) or 1=1";
		builder.build(sql, null);
		sql = "select * from tablename as n,tablename2 n2 where n.col=? and (n.c=n2.b or a=a) or 1=1";
		builder.build(sql, null);
		sql = "delete from tablename a";
		builder.build(sql, null);
		sql = "update tablename set n='a'";
		builder.build(sql, null);
		sql = "insert into tablename (n) values ('a')";
		builder.build(sql, null);
	}

}
