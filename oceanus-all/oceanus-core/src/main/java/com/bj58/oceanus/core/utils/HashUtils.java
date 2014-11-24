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

import java.util.Date;

/**
 * HashUtils
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HashUtils {

	/**
	 * 计算String类型的hashcode
	 * */
	private static int GetHashCode(String value) {
		int hash1 = 5381;
		int hash2 = hash1;
		int len = value.length();
		for (int i = 0; i < len; i++) {
			int c = value.charAt(i);
			hash1 = ((hash1 << 5) + hash1) ^ c;
			if (++i >= len) {
				break;
			}
			c = value.charAt(i);
			hash2 = ((hash2 << 5) + hash2) ^ c;
		}
		return hash1 + (hash2 * 1566083941);
	}

	private static int GetHashCode(Long value) {
		value = (~value) + (value << 18); // key = (key << 18) - key - 1;
		value = value ^ (value >> 31);
		value = value * 21; // key = (key + (key << 2)) + (key << 4);
		value = value ^ (value >> 11);
		value = value + (value << 6);
		value = value ^ (value >> 22);
		return value.intValue();
	}

	private static int GetHashCode(Date value) {

		return GetHashCode(value.getTime());
	}

	private static int GetHashCode(Double value) {
		return GetHashCode(Double.doubleToLongBits(value));
	}

	/**
	 * Bob Jenkins' 32 bit integer hash function 这六个数是随机数，
	 * 通过设置合理的6个数，你可以找到对应的perfect hash.
	 * */
	private static int GetHashCode(Integer value) {
		value = (value + 0x7ed55d16) + (value << 12);
		value = (value ^ 0xc761c23c) ^ (value >> 19);
		value = (value + 0x165667b1) + (value << 5);
		value = (value + 0xd3a2646c) ^ (value << 9);
		value = (value + 0xfd7046c5) + (value << 3); // <<和 +的组合是可逆的
		value = (value ^ 0xb55a4f09) ^ (value >> 16);
		return value;
	}

	private static int GetHashCode(Float value) {
		return GetHashCode(Float.floatToIntBits(value));
	}

	private static int GetHashCode(Boolean value) {
		return value ? 1231 : 1237;
	}

	private static int GetHashCode(Byte value) {
		return GetHashCode((int) value);
	}

	private static int GetHashCode(Short value) {
		return GetHashCode((int) value);
	}

	public static <T> int GetHashCode(T value) {
		int result = 0;
		if (value instanceof Integer) {
			result = GetHashCode((Integer) value);
		} else if (value instanceof Short) {
			result = GetHashCode((Short) value);
		} else if (value instanceof Long) {
			result = GetHashCode((Long) value);
		} else if (value instanceof Long) {
			result = GetHashCode((Long) value);
		} else if (value instanceof Double) {
			result = GetHashCode((Double) value);
		} else if (value instanceof Float) {
			result = GetHashCode((Float) value);
		} else if (value instanceof Byte) {
			result = GetHashCode((Byte) value);
		} else if (value instanceof Date) {
			result = GetHashCode((Date) value);
		} else if (value instanceof Boolean) {
			result = GetHashCode((Boolean) value);
		} else if (value instanceof String) {
			result = GetHashCode((String) value);
		} else {
			result = value.hashCode();
		}
		return result;
	}
}
