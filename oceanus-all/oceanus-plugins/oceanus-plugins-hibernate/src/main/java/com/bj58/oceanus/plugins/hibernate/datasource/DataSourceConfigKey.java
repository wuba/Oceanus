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
package com.bj58.oceanus.plugins.hibernate.datasource;

import java.util.HashMap;
import java.util.Map;

/**
 * 从hibernate配置项转换到Oceanus配置，这个枚举提供key的映射关系
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum DataSourceConfigKey {
	
	DRIVER_CLASS("hibernate.connection.driver_class", "driver"),
	
	URL("hibernate.connection.url","url"),
	
	USERNAME("hibernate.connection.username","username"),
	
	PASSWORD("hibernate.connection.password","password");
	
	private String hibernateKey;
	private String oceanusKey;
	
	private DataSourceConfigKey(String hibernateKey, String oceanusKey) {
		this.hibernateKey = hibernateKey;
		this.oceanusKey = oceanusKey;
	}
	
	private static Map<String, DataSourceConfigKey> MAPPING = new HashMap<String, DataSourceConfigKey>();
	static {
		for(DataSourceConfigKey key : values()){
			MAPPING.put(key.hibernateKey, key);
		}
	}
	
	static boolean convertAble(String key) {
		return MAPPING.containsKey(key);
	}
	
	static String convertToOceanusKey(String key){
		String result = null;
		if(MAPPING.containsKey(key)){
			result = MAPPING.get(key).oceanusKey;
		}
		return result;
	}

}
