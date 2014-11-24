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

import java.util.Set;

import com.bj58.oceanus.core.jdbc.ParameterCallback;

/**
 * sql执行信息
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SqlExecuteInfo {
	String executeSql;
	Set<ParameterCallback<?>> callbacks;

	public String getExecuteSql() {
		return executeSql;
	}

	public Set<ParameterCallback<?>> parameterCallbacks() {
		return callbacks;
	}

	public Set<ParameterCallback<?>> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(Set<ParameterCallback<?>> callbacks) {
		this.callbacks = callbacks;
	}

	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}
}
