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
package com.bj58.oceanus.result.merger;

/**
 * MergeUtils
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MergeUtils {

	public static String getScriptExp(String exp) {
		exp = exp.replaceAll("AND", "&&");
		exp = exp.replaceAll("OR", "||");
		StringBuilder sb = new StringBuilder();
		char[] chars = exp.toCharArray();
		for (int i = 0; i < chars.length; i++) {

			char c = chars[i];
			switch (c) {
			case '<':
				char pre = chars[i - 1],
				after = chars[i + 1];
				int j = i + 1;
				while (after == ' ') {// 排除空格
					after = chars[++j];
				}
				if (after == '>') {
					i = j;
					sb.append("!=");
					continue;
				}
				sb.append(c);
				break;
			case '=':
				pre = chars[i - 1];
				after = chars[i + 1];
				j = i - 1;
				while (pre == ' ') {
					pre = chars[--j];
				}
				if (pre == '>' || pre == '<') {// 排除空格,< =
					sb.append(c);
					continue;
				}
				sb.append(c);
				sb.append(c);// append 变成＝＝
				break;
			default:
				sb.append(c);

			}

		}
		return sb.toString();
	}

}
