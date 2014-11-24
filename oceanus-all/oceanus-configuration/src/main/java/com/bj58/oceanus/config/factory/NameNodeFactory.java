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

import java.util.Iterator;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.DataNodeReferenceConfig;
import com.bj58.oceanus.config.NameNodeConfig;
import com.bj58.oceanus.core.factory.ObjectFactory;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.DataNodeHolder;
import com.bj58.oceanus.core.resource.DefaultNameNode;
import com.bj58.oceanus.core.resource.LoadBanlance;

/**
 * NameNode配置工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class NameNodeFactory implements ObjectFactory<NameNodeConfig> {

	@Override
	public Object create(NameNodeConfig config) {
		DefaultNameNode nameNode = new DefaultNameNode();
		Iterator<DataNodeReferenceConfig> iterator = config.getReferenceNodes()
				.iterator();
		while (iterator.hasNext()) {
			DataNodeReferenceConfig dataNodeConfig = iterator.next();
			DataNodeHolder holder = this.createDataNode(dataNodeConfig);
			nameNode.addDataNode(holder);

		}
		nameNode.setId(config.getId());
		nameNode.setLoadBanlance(LoadBanlance.parse(config.getLoadbalance()));
		return nameNode;
	}

	private DataNodeHolder createDataNode(DataNodeReferenceConfig config) {
		DataNode dataNode = null;
		DataNodeReferenceConfig nodeConfig = config;
		if (nodeConfig.getRef() != null) {
			dataNode = Configurations.getInstance().getDataNode(
					nodeConfig.getRef());
		}
		if (dataNode == null) {
			throw new IllegalArgumentException("datanode must not null!id="
					+ nodeConfig.getRef());
		}
		DataNodeHolder holder = new DataNodeHolder(dataNode);

		holder.setCanRead(nodeConfig.getAccessMode().isCanRead());
		holder.setCanWrite(nodeConfig.getAccessMode().isCanWrite());
		holder.setWeight(nodeConfig.getWeight());
		holder.setId(nodeConfig.getRef());
		return holder;
	}

}
