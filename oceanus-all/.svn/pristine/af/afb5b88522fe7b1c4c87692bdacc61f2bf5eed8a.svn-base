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

import com.bj58.oceanus.config.AccessMode;
import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.DataNodeConfig;
import com.bj58.oceanus.config.DataNodeReferenceConfig;
import com.bj58.oceanus.config.NameNodeConfig;

/**
 * NameNode配置解析器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class NameNodeConfigParser implements Parser<NameNodeConfig> {
	String DATANODE_TAG = "datanode";
	String DATANODES_TAG = "datanodes";

	@Override
	public NameNodeConfig parse(Element el) {
		NameNodeConfig nameNode = new NameNodeConfig();
		String id = ParseUtils.getAttr(el, ID_ATTR);
		String loadbalance = ParseUtils.getAttr(el, LOADBLANCE_ATTR);
		String ref = ParseUtils.getAttr(el, "ref");
		String tableName = ParseUtils.getAttr(el, "tablename");
		String schema = ParseUtils.getAttr(el, "schema");
		nameNode.setTableName(tableName);
		nameNode.setRef(ref);
		nameNode.setId(id);
		nameNode.setSchema(schema);

		nameNode.setLoadbalance(loadbalance);
		List<Node> dataNodes = ParseUtils.getNodeList(el.getChildNodes(),
				DATANODES_TAG);
		if (dataNodes.size() > 0) {
			List<Node> nodes = ParseUtils.getNodeList(dataNodes.get(0)
					.getChildNodes(), DATANODE_TAG);
			for (Node node : nodes) {
				DataNodeConfig dataNodeConfig = (DataNodeConfig) ParseUtils
						.getParser(node).parse((Element) node);
				DataNodeReferenceConfig reference = createReference(dataNodeConfig);
				nameNode.addReferenceNode(reference);
			}
		}
		Configurations.registerConfig(nameNode);
		return nameNode;
	}

	DataNodeReferenceConfig createReference(DataNodeConfig dataNode) {
		DataNodeReferenceConfig reference = new DataNodeReferenceConfig();
		if (dataNode.getRef() != null) {
			reference.setRef(dataNode.getRef());
		} else {
			reference.setRef(dataNode.getId());
		}
		reference.setAccessMode(AccessMode.parse(dataNode.getAccessMode()));
		reference.setWeight(dataNode.getWeight());
		return reference;
	}

}
