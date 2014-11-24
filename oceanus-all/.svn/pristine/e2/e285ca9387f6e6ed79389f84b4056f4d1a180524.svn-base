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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 配置解析内部工具
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ParseUtils {

	static final Map<String, Parser<?>> parsersMap = new ConcurrentHashMap<String, Parser<?>>();
	static {
		parsersMap.put(ConfigurationLoader.BEAN_TAG, 
				new BeanConfigParser());
		parsersMap.put(ConfigurationLoader.DATA_NODE_TAG,
				new DataNodeConfigParser());
		parsersMap.put(ConfigurationLoader.NAME_NODE_TAG,
				new NameNodeConfigParser());
		parsersMap.put(ConfigurationLoader.TABLE_TAG, 
				new TableConfigParser());
		parsersMap.put(ConfigurationLoader.THREAD_POOL_TAG,
				new ThreadPoolConfigParser());
		parsersMap.put(ConfigurationLoader.INCLUDE_TAG, 
				new IncludeConfigParser());
		parsersMap.put(ConfigurationLoader.TRACKER_TAG, 
				new TrackerConfigParser());
	}

	public static String getAttr(Element e, String name) {
		String value = e.getAttribute(name);
		return ((value == null || value.trim().length() <= 0) ? null : value
				.trim());
	}

	public static List<Node> getNodeList(NodeList nodeList, String tagName) {
		List<Node> results = new ArrayList<Node>();
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element
					&& node.getNodeName().equalsIgnoreCase(tagName)) {
				results.add(node);
			} else {
				continue;
			}

		}
		return results;
	}

	public static List<Node> getNodeList(NodeList nodeList) {
		List<Node> results = new ArrayList<Node>();
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				results.add(node);
			} else {
				continue;
			}

		}
		return results;
	}

	public static Parser<?> getParser(Node node) {
		return parsersMap.get(node.getNodeName());
	}
}
