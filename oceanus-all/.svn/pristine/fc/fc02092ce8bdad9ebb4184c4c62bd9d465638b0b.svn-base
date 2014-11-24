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
package com.bj58.oceanus.config.factory;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.DataNodeConfig;
import com.bj58.oceanus.core.alarm.Alarm;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.factory.ObjectFactory;
import com.bj58.oceanus.core.jdbc.DataSourceConnectionProvider;
import com.bj58.oceanus.core.resource.DefaultDataNode;

/**
 * DataNode配置工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DataNodeFactory implements ObjectFactory<DataNodeConfig> {

	@Override
	public Object create(DataNodeConfig config) {
		DefaultDataNode dataNode = new DefaultDataNode();
		DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider();
		connectionProvider.setDatasource(Configurations.getInstance()
				.getDataSource(config.getId()));
		// TODO DATANODE init
		dataNode.setId(config.getId());
		dataNode.setConnectionProvider(connectionProvider);
		
		String alarmClass = config.getAlarmClass();
		if(alarmClass!=null){
			try {
				Alarm alarm = (Alarm) Class.forName(alarmClass).newInstance();
				dataNode.setAlarm(alarm);
			}  catch (Exception e) {
				throw new ConfigurationException(
						"datanode alarm init faile! class="
								+ alarmClass);
			}
		}
		return dataNode;
	}

}
