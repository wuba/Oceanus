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

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * javascript 编译器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class JavaScriptCompiler {
	static final ScriptEngineManager manager = new ScriptEngineManager();

	public static CompiledScript compile(String script, String lang)
			throws ScriptException {
		final ScriptEngine engine = manager.getEngineByName(lang);
		Compilable compilingEngine = (Compilable) engine;
		CompiledScript compiledScript = compilingEngine.compile(script);

		return compiledScript;
	}

}
