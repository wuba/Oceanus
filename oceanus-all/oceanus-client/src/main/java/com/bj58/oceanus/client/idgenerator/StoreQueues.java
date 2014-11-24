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
package com.bj58.oceanus.client.idgenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * 
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings("unchecked")
public class StoreQueues {
	
	@SuppressWarnings("rawtypes")
	static final Map<String, BlockingQueue> blockingQueues = new HashMap<String, BlockingQueue>();
	static Executor executor = Executors
			.newCachedThreadPool(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName("id generator thread");
					t.setDaemon(true);
					return t;
				}
			});

	public static <T> BlockingQueue<T> getQueue(String name) {
		return blockingQueues.get(name);
	}

	public static synchronized void createQueue(Config config) {

		switch (config.type) {
		case SEQUENCE:
			BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue<Long>(
					config.concurrentSize);
			blockingQueues.put(config.name, blockingQueue);
			ValueProducer<Long> producer = new SequenceValueProducer(
					config.initValue, config.step);
			executor.execute(new SequenceGeneratorTask(blockingQueue, producer));
			break;
		default:
			break;
		}
	}

}
