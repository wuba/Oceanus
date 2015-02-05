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
package com.bj58.oceanus.core.script;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 脚本执行器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class InterpretedScriptExecutor<T> implements ScriptExecutor<T> {
	static final ScriptEngineManager manager = new ScriptEngineManager();

	@SuppressWarnings("unchecked")
	@Override
	public T execute(String script, Map<String, Object> parameters)
			throws ScriptException {
		ScriptEngine scriptEngine = manager.getEngineByName("js");
		Bindings binding = scriptEngine.createBindings();
		if (parameters != null) {
			binding.putAll(parameters);
		}
		Object val = scriptEngine.eval(script, binding);
		return ((T) val);
	}

	public static void main(String args[]) throws Exception {
		ScriptExecutor<Boolean> executor = new InterpretedScriptExecutor<Boolean>();
		String exp = "var result=(1 >0 && 2>99||1==1);result";
		Object value = executor.execute(exp, null);
		System.out.println("value=" + value);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("a", "5");
		boolean result = executor.execute("a < 1", parameters);
		System.out.println(result);
	}
}
