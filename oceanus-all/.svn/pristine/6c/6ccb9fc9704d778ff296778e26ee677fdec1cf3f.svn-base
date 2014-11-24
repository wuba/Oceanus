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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.jdbc.ConnectionCallback;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.jdbc.PreparedStatementCallback;
import com.bj58.oceanus.core.jdbc.StatementCallback;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.shard.SqlExecuteInfo;
import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.TrackerExecutor;
import com.bj58.oceanus.core.tx.SimpleTransaction;
import com.bj58.oceanus.core.tx.Transaction;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.ExecuteHandler;

/**
 * 执行操作基类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class BaseExecuteHandler<T> implements ExecuteHandler<T> {
	protected static Logger logger = LoggerFactory
			.getLogger(BaseExecuteHandler.class);

	protected Integer doExecute(Connection connection, Statement statement,
			String sql, ExecuteCallback<Integer> callback) throws SQLException {

		TrackerExecutor.trackBegin(TrackPoint.EXECUTE_SQL, 
				StatementContext.getContext().getCurrentBatch());
		try {
			Integer result = callback.execute(statement, sql);
	
			return result;
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.EXECUTE_SQL);
		}
	}

	protected void initConnection(Connection connection) throws SQLException {
		this.setConnectionProperties(connection);
		this.setTransactionInfo(connection);
	}

	protected Statement createStatement(Connection connection,
			RouteTarget target, StatementContext context) throws SQLException {
		if(connection == null){
			throw new ShardException("can not match table, sql:"+context.getCurrentBatch().getSql());
		}
		
		StatementCallback callback = context.getStatementCreateCallback();
		if (callback instanceof PreparedStatementCallback) {
			SqlExecuteInfo shardInfo = target.getExecuteInfo();
			return ((PreparedStatementCallback) callback).prepareStatement(
					connection, shardInfo.getExecuteSql());
		}
		return callback.createStatement(connection);
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

	protected void printDebugInfo(RouteTarget target,List<DataNode> dataNodeList) {
		NameNodeHolder nameNode = (NameNodeHolder) target.getNameNode();
		 logger.debug("execute @node id=[" + nameNode.getId() + "] @DataNodes="+dataNodeList+"@table=["
				+ nameNode.getTableName() + "] index=[" + nameNode.getIndex()
				+ "] sql=" + target.getExecuteInfo().getExecuteSql()); 
	  	//System.out.println(target.getExecuteInfo().getExecuteSql());
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
