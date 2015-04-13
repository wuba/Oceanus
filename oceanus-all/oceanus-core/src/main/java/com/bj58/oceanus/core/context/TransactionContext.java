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
package com.bj58.oceanus.core.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.utils.JdbcUtil;
import com.bj58.oceanus.core.utils.Transporter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 一个事务处理的上下文
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TransactionContext {

    static final ThreadLocal<TransactionContext> threadLocal = new ThreadLocal<TransactionContext>();

    /**
     * 在一次事务中，一个数据源对应一个connection
     */
    final Map<String, Connection>                connsInTransaction;

    /**
     * 在一次事务中,一个connection中的相同table对应同一个statement
     */
    final Map<String, Statement>                 stmtsInTransaction;

    /**
     * 在一次事务中的sql
     */
    final List<String>                           sqlsInTransaction;

    public TransactionContext(){
        connsInTransaction = Maps.newLinkedHashMap();
        stmtsInTransaction = Maps.newLinkedHashMap();
        sqlsInTransaction = Lists.newLinkedList();
    }

    public static TransactionContext getContext() {
        if (threadLocal.get() == null) threadLocal.set(new TransactionContext());

        return threadLocal.get();
    }

    public void setContext(TransactionContext context) {
        threadLocal.set(context);
    }

    public Connection getTransactionConnection(DataNode dataNode, Transporter<Boolean> isNewConn) throws SQLException {

        Connection connection = connsInTransaction.get(dataNode.getId());
        if (connection == null) {
            connection = dataNode.getConnection();
            connsInTransaction.put(dataNode.getId(), connection);
            isNewConn.setValue(true);
        }

        return connection;
    }

    public Statement getTransactionStatement(Connection connection, RouteTarget target) {

        return stmtsInTransaction.get(connection.toString() + target.getNameNode().toString()
                                      + target.getBatchItem().sql);
    }

    public void setTransactionStatement(Connection connection, RouteTarget target, Statement statement) {

        stmtsInTransaction.put(connection.toString() + target.getNameNode().toString() + target.getBatchItem().sql,
            statement);
        sqlsInTransaction.add(target.getExecuteInfo().getExecuteSql());
    }

    public void release() {
        for (Statement statement : stmtsInTransaction.values()) {
            JdbcUtil.closeStatement(statement);
        }
        setContext(null);
        sqlsInTransaction.clear();
    }

}
