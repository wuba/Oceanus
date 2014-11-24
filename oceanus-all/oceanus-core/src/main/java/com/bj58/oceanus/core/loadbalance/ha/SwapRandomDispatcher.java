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
package com.bj58.oceanus.core.loadbalance.ha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.loadbalance.Dispatcher;
import com.bj58.oceanus.core.loadbalance.RandomDispatcher;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.NameNode;

/**
 * 分发器：支持HA、随机负载
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SwapRandomDispatcher extends RandomDispatcher implements Dispatcher, Swapable{
	
	static Logger logger = LoggerFactory.getLogger(SwapRandomDispatcher.class);
	
	@Override
	public DataNode dispatch(NameNode nameNode, BatchItem batchItem) {
		DataNode dataNode = super.dispatch(nameNode, batchItem);
		
		if(!dataNode.isAlive()){
			DataNode slave = swapAvailable(dataNode, batchItem);
			
			if(slave!=null)
				return slave;
		}
		
		return dataNode;
	}

	@Override
	public DataNode swapAvailable(DataNode dataNode, BatchItem batchItem) {
		DataNode[] slaves = dataNode.getSlaves();
		DataNode slave = null;
		
		if(slaves != null && slaves.length>0){
			for(DataNode tmp : slaves){
				if(!tmp.isAlive())
					continue;
				
				slave = tmp;
				break;
			}
		}
		
		if(slave == null){
			logger.error("DataNode <"+dataNode.getId()+"> has no avaliable slaves");
		}
		return slave;
	}

}
