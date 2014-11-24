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
package com.bj58.oceanus.core.resource;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.alarm.Alarm;
import com.bj58.oceanus.core.jdbc.ConnectionProvider;
import com.bj58.oceanus.core.loadbalance.ha.DataNodeChecker;

/**
 * 默认 DataNode 实现
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DefaultDataNode implements DataNode {
	
	static final Logger logger=LoggerFactory.getLogger(DefaultDataNode.class);
	
	String id;
	
	ConnectionProvider connectionProvider;
	
	Alarm alarm;

	@Override
	public boolean isMarster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalve() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataNode[] getMarsters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataNode[] getSlaves() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isAlive() {
		return DataNodeChecker.dataNodeAlive(this);
	}

	@Override
	public Connection getConnection() throws SQLException {
		try{
		return connectionProvider.getConnection();
		}catch(SQLException e){
			logger.error("fetch connection error!datanode id="+id,e);
			throw e;
		}
	}
	
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

}
