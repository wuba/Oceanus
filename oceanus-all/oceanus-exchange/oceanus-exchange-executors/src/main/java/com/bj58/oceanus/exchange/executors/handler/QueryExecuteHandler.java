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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.core.context.TransactionContext;
import com.bj58.oceanus.core.loadbalance.Dispatcher;
import com.bj58.oceanus.core.loadbalance.DispatcherFactory;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.TrackerExecutor;
import com.bj58.oceanus.core.utils.Transporter;
import com.bj58.oceanus.exchange.executors.ExecuteCallback;
import com.bj58.oceanus.exchange.executors.ExecuteHandler;

/**
 * 查询操作执行器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class QueryExecuteHandler extends BaseExecuteHandler<ResultSet>
		implements ExecuteHandler<ResultSet> {

	@Override
	public ResultSet handle(RouteTarget target, StatementContext context,
			ExecuteCallback<ResultSet> callback) throws SQLException {
		ResultSet result = null;
		String sql = target.getExecuteInfo().getExecuteSql();

		NameNode nameNode = target.getNameNode();
		Dispatcher dispatcher = DispatcherFactory.create(nameNode);
		DataNode dataNode = dispatcher
				.dispatch(nameNode, target.getBatchItem());
        if (logger.isDebugEnabled()) {
            printDebugInfo(target, Arrays.asList(new DataNode[]{dataNode}));
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
		
		TrackerExecutor.trackBegin(TrackPoint.EXECUTE_SQL, 
				StatementContext.getContext().getCurrentBatch());
		try {
			result = callback.execute(statement, sql);
	
			return result;
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.EXECUTE_SQL);
		}
	}

}
