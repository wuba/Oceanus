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

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.DataNodeConfig;

/**
 * DataNode配置解析器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DataNodeConfigParser implements Parser<DataNodeConfig> {

	@Override
	public DataNodeConfig parse(Element el) {
		DataNodeConfig dataNode = new DataNodeConfig();
		String ref = ParseUtils.getAttr(el, "ref");
		dataNode.setRef(ref);

		String id = ParseUtils.getAttr(el, ID_ATTR);
		dataNode.setId(id);
		String slaves = ParseUtils.getAttr(el, "slaves");
		if (slaves != null) {
			dataNode.setSlaves(slaves.split("[,]"));
		}
		String parent = ParseUtils.getAttr(el, "parent");
		dataNode.setParent(parent);

		String accessMode = ParseUtils.getAttr(el, "access-mode");
		dataNode.setAccessMode(accessMode);
		String weight = ParseUtils.getAttr(el, "weight");
		if (weight != null) {
			dataNode.setWeight(Long.valueOf(weight));
		}

		List<Node> nodeList = ParseUtils.getNodeList(el.getChildNodes());
		for (Node node : nodeList) {
			dataNode.getProperties().put(
					node.getNodeName(),
					(node.getTextContent() == null ? null : node
							.getTextContent().trim()));
		}
		
		String alarmClass = ParseUtils.getAttr(el, "alarm");
		if (alarmClass != null) {
			dataNode.setAlarmClass(alarmClass);
		} else if(parent != null){
			dataNode.setAlarmClass(Configurations.getInstance()
					.getDataNodeConfig(parent).getAlarmClass());
		}

		Configurations.registerConfig(dataNode);
		return dataNode;
	}

}
