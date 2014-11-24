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
import java.util.List;

import com.bj58.oceanus.core.alarm.Alarm;
import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.TrackerExecutor;

/**
 * DataNode 实例持有者
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DataNodeHolder implements DataNode {

    String id;

	boolean canRead;
	
	boolean canWrite;
	
	Long weight;

    final DataNode dataNode;
    
    DataNode[] slaves = new DataNode[0];

	public DataNodeHolder(DataNode dataNode) {
		
		this.dataNode = dataNode;
	}

	@Override
	public boolean isMarster() {
		return dataNode.isMarster();
	}

	@Override
	public boolean isSalve() {
		return dataNode.isSalve();
	}

	@Override
	public DataNode[] getMarsters() {
		return dataNode.getMarsters();
	}

	@Override
	public DataNode[] getSlaves() {
		return slaves;
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			TrackerExecutor.trackBegin(TrackPoint.GET_CONNECTION);
			
			return dataNode.getConnection();
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.GET_CONNECTION);
		}
	}
	
	@Override
	public boolean isAlive() {
		return dataNode.isAlive();
	}

	public boolean canRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	public boolean canWrite() {
		return canWrite;
	}

	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	public DataNode getDataNode() {
		return dataNode;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setSlaves(List<DataNode> slaves) {
		this.slaves = slaves.toArray(new DataNode[slaves.size()]);
	}

    @Override
    public String toString() {
        return "DataNodeHolder{" +
                "weight=" + weight +
                ", id='" + id + '\'' +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                '}';
    }

	@Override
	public Alarm getAlarm() {
		return dataNode.getAlarm();
	}


}
