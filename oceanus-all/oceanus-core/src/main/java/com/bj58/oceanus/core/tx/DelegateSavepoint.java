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
package com.bj58.oceanus.core.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Savepoint
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DelegateSavepoint implements Savepoint {
	static final AtomicInteger savepointIdGen=new AtomicInteger(Integer.MIN_VALUE);
	Set<SavepointDesc> savePoints=new LinkedHashSet<SavepointDesc>(); 
	String name;
	int savepointId;
	
	DelegateSavepoint(String name){
		this.name=name;
	}
	@Override
	public int getSavepointId() throws SQLException {
		if(savepointId==0){
			savepointId=savepointIdGen.incrementAndGet();
		}
		return savepointId;
	}

	@Override
	public String getSavepointName() throws SQLException {
		return name;
	}
	public void addSavepoint(SavepointWrapper savepoint){
	SavepointDesc desc=new SavepointDesc();
	savePoints.add(desc);
	desc.connection=savepoint.getConnection();
	desc.savepoint=savepoint;
		
	}
	
	
	class SavepointDesc{
		Connection connection;
		Savepoint savepoint;
		
		public void rollback() throws SQLException{
			connection.rollback(savepoint);
		}
		public void releaseSavepoint() throws SQLException{
			connection.releaseSavepoint(savepoint);
		}
	}

}
