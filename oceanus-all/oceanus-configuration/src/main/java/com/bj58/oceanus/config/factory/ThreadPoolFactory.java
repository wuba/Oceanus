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
package com.bj58.oceanus.config.factory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bj58.oceanus.config.ThreadPoolConfig;
import com.bj58.oceanus.core.factory.ObjectFactory;

/**
 * 连接池配置工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ThreadPoolFactory implements ObjectFactory<ThreadPoolConfig> {

	@Override
	public Object create(final ThreadPoolConfig config) {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(config.getCoreSize(), 
				config.getCoreSize(), config.getTimeout(), TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>(config.getQueueSize()),
				
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setName("oceanus-thread-"+config.getId());
						return t;
					}
				},
				
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						throw new RejectedExecutionException("oceanus-thread-" + config.getId() + 
                                r.toString() + " rejected from " + executor.toString());
					}
				});
		
		if(config.getTimeout() > 1L)
			threadPool.allowCoreThreadTimeOut(true);
		
		return threadPool;
	}

}
