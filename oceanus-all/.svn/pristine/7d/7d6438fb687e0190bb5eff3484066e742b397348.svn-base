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
package com.bj58.oceanus.exchange.executors.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.TransactionContext;
import com.bj58.oceanus.core.jdbc.ConnectionCallback;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.jdbc.PreparedStatementCallback;
import com.bj58.oceanus.core.jdbc.StatementCallback;
import com.bj58.oceanus.core.loadbalance.Dispatcher;
import com.bj58.oceanus.core.loadbalance.DispatcherFactory;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.shard.SqlExecuteInfo;
import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.TrackerExecutor;
import com.bj58.oceanus.core.tx.SimpleTransaction;
import com.bj58.oceanus.core.tx.Transaction;
import com.bj58.oceanus.core.utils.Transporter;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.ExecuteHandler;

/**
 * 可写操作执行基类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class WritableExecuteHandler implements ExecuteHandler<Integer> {
	protected static Logger logger = LoggerFactory
			.getLogger(WritableExecuteHandler.class);

	@Override
	public Integer handle(RouteTarget target, StatementContext context,
			ExecuteCallback<Integer> callback) throws SQLException {

		NameNode nameNode = target.getNameNode();
		Dispatcher dispatcher = DispatcherFactory.create(nameNode);
		DataNode dataNode = dispatcher
				.dispatch(nameNode, target.getBatchItem());
		if (logger.isDebugEnabled()) {
			printDebugInfo(target, Arrays.asList(new DataNode[] { dataNode }));
		}
		
		Statement statement = null;
		
		if(ConnectionContext.getContext().isTransaction()) {
			TransactionContext transactionContext = TransactionContext.getContext();
			Transporter<Boolean> isNewConn = new Transporter<Boolean>(false);
			
			Connection connection = transactionContext.getTransactionConnection(dataNode, isNewConn);
			if(isNewConn.getValue())
				initConnection(connection);
				
			statement = transactionContext.getTransactionStatement(connection, target);
			
			if(statement == null){
				statement = this.createStatement(connection, target, context);
				
				TransactionContext.getContext().setTransactionStatement(connection, target, statement);
			}
		} else {
			Connection connection = dataNode.getConnection();
			initConnection(connection);
			statement = this.createStatement(connection, target, context);
		}

		if (statement instanceof PreparedStatement) {
			this.parameterizedStatement((PreparedStatement) statement, target);
		}
		String sql = target.getExecuteInfo().getExecuteSql();
		return this.doExecute(statement, sql, callback);
	}

	protected Integer doExecute(Statement statement, String sql,
			ExecuteCallback<Integer> callback) throws SQLException {
		TrackerExecutor.trackBegin(TrackPoint.EXECUTE_SQL, 
				StatementContext.getContext().getCurrentBatch());
		try {
			Integer result = callback.execute(statement, sql);
			return result;
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.EXECUTE_SQL);
		}
	}

	protected Statement createStatement(Connection connection,
			RouteTarget target, StatementContext context) throws SQLException {
		StatementCallback callback = context.getStatementCreateCallback();
		if (callback instanceof PreparedStatementCallback) {
			SqlExecuteInfo shardInfo = target.getExecuteInfo();
			return ((PreparedStatementCallback) callback).prepareStatement(
					connection, shardInfo.getExecuteSql());
		}
		return callback.createStatement(connection);
	}

	protected void initConnection(Connection connection) throws SQLException {
		this.setConnectionProperties(connection);
		this.setTransactionInfo(connection);
	}

	protected void setConnectionProperties(Connection connection)
			throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		context.addConnection(connection);
		if (context.getConnectionCallbacks() != null) {
			Iterator<ConnectionCallback> it = context.getConnectionCallbacks()
					.iterator();
			while (it.hasNext()) {
				ConnectionCallback callback = it.next();
				if (callback.getEvent().name().startsWith("CREATE")) {
					callback.call(connection);
					it.remove();
				} else {
					callback.call(connection);
				}
			}
		}
	}

	protected void setTransactionInfo(Connection connection) {
		ConnectionContext context = ConnectionContext.getContext();
		if (context.getTransaction() != null) {
			Transaction transaction = new SimpleTransaction(connection);
			context.addTransaction(transaction);
		}

	}

	protected void parameterizedStatement(PreparedStatement statetement,
			RouteTarget target) throws SQLException {
		Collection<ParameterCallback<?>> callbacks = target.getBatchItem()
				.getCallbacks();
		for (ParameterCallback<?> callback : callbacks) {
			callback.call(statetement);
		}
	}

	protected void printDebugInfo(RouteTarget target,
			List<DataNode> dataNodeList) {
		NameNodeHolder nameNode = (NameNodeHolder) target.getNameNode();

		logger.debug("execute @node id=[" + nameNode.getId() + "] @DataNodes="
				+ dataNodeList + "@table=[" + nameNode.getTableName()
				+ "] index=[" + nameNode.getIndex() + "] sql="
				+ target.getExecuteInfo().getExecuteSql());

		// System.out.println(target.getExecuteInfo().getExecuteSql());
		if (target.getBatchItem().getCallbacks().size() > 0) {
			logger.debug(getParametersLog(target));
		}
	}

	protected String getParametersLog(RouteTarget target) {
		StringBuilder sb = new StringBuilder();
		sb.append("parameters:");
		for (ParameterCallback<?> callback : target.getBatchItem()
				.getCallbacks()) {
			sb.append("\nindex=").append(callback.parameterIndex())
					.append(" value=").append(callback.getParameter());
		}
		return sb.toString();
	}
}
