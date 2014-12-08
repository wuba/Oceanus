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
package com.bj58.oceanus.plugins.mybatis.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import com.bj58.oceanus.client.Oceanus;

/**
 * 由Oceanus实现的事务管理器工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class OceanusTransactionFactory implements TransactionFactory{

	@Override
	public void setProperties(Properties props) {
		
	}

	@Override
	public Transaction newTransaction(Connection conn) {
		try {
			Oceanus.beginTransaction(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new OceanusTransaction(conn, true);
	}

	@Override
	public Transaction newTransaction(DataSource dataSource,
			TransactionIsolationLevel level, boolean autoCommit) {
		return new OceanusTransaction(dataSource, level, true);
	}

}
