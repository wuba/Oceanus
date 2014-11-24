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
package com.bj58.oceanus.config;

/**
 * 数据源的访问模式
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum AccessMode {
	
	READONLY("READONLY", true, false),
	
	WRITEONLY("WRITEONLY", false, true),
	
	READ_WRITE("READ-WRITE", true, true);

	private String input;
	
	private boolean canRead;
	
	private boolean canWrite;
	
	private AccessMode(String input, boolean canRead, boolean canWrite) {
		this.input = input;
		this.canRead = canRead;
		this.canWrite = canWrite;
	}

	public String getInput() {
		return input;
	}

	public boolean isCanRead() {
		return canRead;
	}

	public boolean isCanWrite() {
		return canWrite;
	}
	
	public static AccessMode parse(String input){
		for(AccessMode mode : values()){
			if(mode.getInput().equals(input))
				return mode;
		}
		throw new IllegalArgumentException("Unsupport AccessMode, input string : " + input);
	}
	
}
