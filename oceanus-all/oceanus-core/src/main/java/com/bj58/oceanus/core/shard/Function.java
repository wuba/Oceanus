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
package com.bj58.oceanus.core.shard;

import java.util.Map;

/**
 * 分库分表规则约束
 * 通过对sql中的字段的值来判断放在哪个数据库或者表中 
 * 常用的方式有： 
 * 1.基于ID段的 
 * 2.基于hash的 
 * 3.按日期等
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface Function {
	
	/**
	 *  一个无分库分表规则的funcion
	 */
	public static final Function NO_SHARD_FUNCTION = new NoShardFunction();
	
	/**
	 * 执行方法返回下标
	 * 
	 * @param parameters
	 *            key为字段名，大写开头，value为字段在sql中的值
	 * @return namenode的下标
	 */
	int execute(int size, Map<String, Object> parameters);
	
	/**
	 * 一个无分库分表规则的funcion
	 */
	static class NoShardFunction implements Function {

		@Override
		public int execute(int size, Map<String, Object> parameters) {
			return 0;
		}
		
	}

}
