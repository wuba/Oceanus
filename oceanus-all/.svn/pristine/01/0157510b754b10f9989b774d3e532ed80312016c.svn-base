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
package com.bj58.oceanus.core.timetracker;

import com.bj58.oceanus.core.exception.ConfigurationException;

/**
 * 耗时监控点
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum TrackPoint {
	
	/**
	 * 从连接池中获取连接
	 */
	GET_CONNECTION,
	
	/**
	 * 一个connection context的生命周期
	 */
	CONNECTION_CONTEXT,
	
	/**
	 * 执行sql
	 */
	EXECUTE_SQL,
	
	/**
	 * 解析sql
	 */
	PARSE_SQL,
	
	/**
	 * 未知类型
	 */
	UNKNOW;
	
	public static TrackPoint parse(String name) {
		if(name!=null){
			for(TrackPoint tmp : values()){
				if(name.toUpperCase().equals(tmp.name()))
					return tmp;
			}
		}
		throw new ConfigurationException("No such TrackPoint type : "+name);
	}

}
