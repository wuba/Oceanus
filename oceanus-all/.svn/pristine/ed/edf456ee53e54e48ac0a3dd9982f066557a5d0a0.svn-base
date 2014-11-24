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
package com.bj58.oceanus.core.loadbalance;

import com.bj58.oceanus.core.loadbalance.ha.SwapRandomDispatcher;
import com.bj58.oceanus.core.loadbalance.ha.SwapRandomWeightDispatcher;
import com.bj58.oceanus.core.resource.NameNode;

/**
 * 分发器工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DispatcherFactory {
	
	static final Dispatcher random = new RandomDispatcher();
	static final Dispatcher swapRandom = new SwapRandomDispatcher();
	static final Dispatcher swapRandomWeight = new SwapRandomWeightDispatcher();
	static final Dispatcher randomWeight = new RandomWeightDispatcher();

	public static Dispatcher create(NameNode nameNode) {
		switch (nameNode.getLoadBanlance()) {
			case HA_RANDOM_WEIGHT:
				return swapRandomWeight;
				
			case HA_RANDOM:
				return swapRandom;
				
			case RANDOM:
				return random;
				
			case RANDOM_WEIGHT:
				return randomWeight;
				
			default:
				return swapRandomWeight;
		}
	}
}
