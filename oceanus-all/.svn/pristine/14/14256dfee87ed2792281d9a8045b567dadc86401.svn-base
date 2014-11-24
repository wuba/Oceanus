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

import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列化ID生产者
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SequenceValueProducer implements ValueProducer<Long> {
	private final AtomicLong sequence;
	private int step;

	public SequenceValueProducer(long initValue, int step) {
		sequence = new AtomicLong(initValue);
		this.step = step;
	}

	@Override
	public Long produce() {
		Long value = sequence.addAndGet(step);
		return value;
	}
	
	public static void main(String args[]){
		
		for(int i=0;i<10000000;i++){
			System.out.println("i="+i+" pos="+(i&63));
		}
	}

}
