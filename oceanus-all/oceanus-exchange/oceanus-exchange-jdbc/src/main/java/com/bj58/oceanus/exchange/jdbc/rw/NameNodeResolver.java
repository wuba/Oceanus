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
package com.bj58.oceanus.exchange.jdbc.rw;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.exchange.jdbc.ProviderDesc;

/**
 * 读写分离场景中必须指定使用哪一个NameNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class NameNodeResolver {

	public static NameNode resolve(ProviderDesc desc) throws ConfigurationException {
		NameNode nameNode = Configurations.getInstance().getNameNodeById(desc.getNameNodeId());
		
		if(nameNode == null)
			throw new ConfigurationException("NameNode id ["+desc.getNameNodeId()+"] does not exsit!");
		
		return nameNode;
	}

}
