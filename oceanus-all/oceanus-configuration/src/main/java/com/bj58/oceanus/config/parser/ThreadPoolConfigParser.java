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
package com.bj58.oceanus.config.parser;

import org.w3c.dom.Element;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.ThreadPoolConfig;

/**
 * 连接池配置解析器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ThreadPoolConfigParser implements Parser<ThreadPoolConfig> {
	static final int CORE_SIZE = 50;

	@Override
	public ThreadPoolConfig parse(Element el) {
		ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
		String id = ParseUtils.getAttr(el, ID_ATTR);
		threadPoolConfig.setId(id);
		String sizeStr = ParseUtils.getAttr(el, "size");
		Integer coreSize = ((sizeStr == null || sizeStr.trim().length() <= 0) ? 
				CORE_SIZE : Integer.valueOf(sizeStr));
		threadPoolConfig.setCoreSize(coreSize); 
		
		String queueSizeStr = ParseUtils.getAttr(el, "qsize");
		if(queueSizeStr!=null){
			threadPoolConfig.setQueueSize(Integer.valueOf(queueSizeStr));
		}
		
		String timeoutStr = ParseUtils.getAttr(el, "timeout");
		if(queueSizeStr!=null){
			threadPoolConfig.setTimeout(Long.valueOf(timeoutStr));
		}
		
		Configurations.registerConfig(threadPoolConfig);
		return threadPoolConfig;
	}

}
