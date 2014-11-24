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

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.script.CompiledJavaScriptExecutor;
import com.bj58.oceanus.core.script.ScriptExecutor;

/**
 * 根据script配置来获取下标
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ScriptFunction implements Function {
	final ScriptExecutor<Double> scriptExecutor;

	public ScriptFunction(Script script) {
		try {
			String function = null;

			if (script.getExecute() != null
					&& script.getExecute().trim().length() > 0) {
				function = script.getExecute();
			} else {
				function = " function result(){" + script.getContent()
						+ "};result();";
			}
			scriptExecutor = new CompiledJavaScriptExecutor<Double>(function,
					script.getLanguage());
		} catch (Exception e) {
			throw new ConfigurationException("script config error!script="
					+ script, e);
		}
	}

	@Override
	public int execute(int size, Map<String, Object> parameters) {

		try {
			Map<String, Object> context = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				context.put("$" + entry.getKey(), entry.getValue());
				context.put("$" + entry.getKey().toUpperCase(),
						entry.getValue());
				context.put("$" + entry.getKey().toLowerCase(),
						entry.getValue());
			}
			context.put("$NODE_SIZE", size);
			Double value = scriptExecutor.execute(null,context);
			if (value != null) {
				return value.intValue();
			} else {
				throw new ShardException("function error,could not find namenodes index="+value+"!parameters=" + parameters);
			}
		} catch (ScriptException e) {
			throw new ShardException("script error!parameters=" + parameters, e);
		}

	}
}
