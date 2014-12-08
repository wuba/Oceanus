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
package com.bj58.oceanus.core.utils;

import java.util.Random;
import java.util.UUID;

/**
 * RandomUtil
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RandomUtil {

	public static final Random random = new Random();
	
	public static int nextInt() {
		return random.nextInt() & 2147483647 | 16777472;
	}

	public static int nextInt(int max) {
		return random.nextInt(max);
	}

	public static int nextInt(int min, int max) {
		if(min == max){
			return min;
		}
		return random.nextInt((max - min) + 1) + min;
	}
	
	public static long nextLong(long min, long max) {
		return (long) nextDouble(min, max);
	}
	
	public static double nextDouble(double min, double max) {
        if (min == max) {
            return min;
        }
        return ((max - min) * random.nextDouble()) + min;
    }
	
	public static String uuid(){
		return UUID.randomUUID().toString();
	}
	
}
