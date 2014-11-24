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

/**
 * 负载类型
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum LoadBanlance {
	
	POLL("POLL"),
	
	POLL_WEIGHT("POLL-WEIGHT"),
	
	RANDOM("RANDOM"),
	
	RANDOM_WEIGHT("RANDOM-WEIGHT"),
	
	HA_RANDOM("HA-RANDOM"),
	
	HA_RANDOM_WEIGHT("HA-RANDOM-WEIGHT");
	
	private String input;
	
	private LoadBanlance(String input) {
		this.input = input;
	}
	
	public static LoadBanlance parse(String input) {
		if(input==null || input.length()<1)
			return HA_RANDOM_WEIGHT;
		
		for(LoadBanlance loadBanlance : values()){
			if(loadBanlance.input.equals(input))
				return loadBanlance;
		}
		throw new IllegalArgumentException("Unsupport LoadBanlance, input string : " + input);
	}

}
