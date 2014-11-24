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
package com.bj58.oceanus.core.interceptor;

import java.io.Serializable;

import com.bj58.oceanus.core.cache.Cache;
import com.bj58.oceanus.core.cache.KeyValueOperator;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;

/**
 * 缓存拦截器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CacheInterceptor implements Interceptor {
	
	Cache<String, Serializable> cache = null;
	KeyValueOperator operator;

	@Override
	public void intercept(TableInfo tableInfo, TableColumn[] updatedColumns,
			TableColumn[] conditionColumns) {
		String key = operator.generateKey(tableInfo, updatedColumns,
				conditionColumns);
		Serializable value = operator.resolveValue(tableInfo, updatedColumns,
				conditionColumns);
		cache.put(key, value);

	}

}
