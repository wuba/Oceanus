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

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bj58.oceanus.config.IncludeConfig;

/**
 * 配置装载入口
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ConfigurationLoader {
	
	static Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);
	
	static final String BEAN_TAG = "bean";
	static final String DATA_NODE_TAG = "datanode";
	static final String NAME_NODE_TAG = "namenode";
	static final String TABLE_TAG = "table";
	static final String THREAD_POOL_TAG = "threadpool";
	static final String INCLUDE_TAG = "include";
	static final String TRACKER_TAG = "tracker";
	static final String SEPERATOR = System.getProperty("file.separator");
 
	public void load(String path) throws Exception {
		logger.info("start load configuration files ...");
		long begin = System.currentTimeMillis();
		
		loadConfigFile(path);
		
		logger.info("configuration files loaded ...   cost "+(System.currentTimeMillis()-begin)+"ms");
	}
	
	void loadConfigFile(String path) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		File configFile = new File(path);
		if(configFile==null || !configFile.exists())
			throw new Exception("oceanus config file:"+path+" not found !");
		
		Document document = docBuilder.parse(configFile);
		NodeList rootNodeList = ParseUtils
				.getNodeList(document.getChildNodes(), "configurations").get(0)
				.getChildNodes();
		
		this.parseBeanList(rootNodeList);
		this.parseDataNodeList(rootNodeList);
		this.parseNameNodeList(rootNodeList);
		this.parseTableList(rootNodeList);
		this.parseTrackerList(rootNodeList);
		
		List<Node> includeNodes = ParseUtils.getNodeList(rootNodeList, INCLUDE_TAG);
		for(Node includeNode : includeNodes) {
			IncludeConfig includeConfig = (IncludeConfig) ParseUtils.getParser(includeNode).parse((Element)includeNode);
			
			loadConfigFile(configFile.getParent() + SEPERATOR + includeConfig.getFile());
		}
	}
	

	void parseBeanList(NodeList rootNodeList) {
		List<Node> nodeList = ParseUtils.getNodeList(rootNodeList, 
				BEAN_TAG);
		parseNodeList(nodeList);
	}

	void parseDataNodeList(NodeList rootNodeList) {
		List<Node> nodeList = ParseUtils.getNodeList(rootNodeList,
				DATA_NODE_TAG);
		parseNodeList(nodeList);
	}

	void parseNameNodeList(NodeList rootNodeList) {
		List<Node> nodeList = ParseUtils.getNodeList(rootNodeList,
				NAME_NODE_TAG);
		parseNodeList(nodeList);
	}

	void parseTableList(NodeList rootNodeList) {
		List<Node> nodeList = ParseUtils.getNodeList(rootNodeList, 
				TABLE_TAG);
		parseNodeList(nodeList);
	}
	
	void parseTrackerList(NodeList rootNodeList) {
		List<Node> nodeList = ParseUtils.getNodeList(rootNodeList, 
				TRACKER_TAG);
		parseNodeList(nodeList);
	}

	private void parseNodeList(List<Node> nodeList) {
		for (Node node : nodeList) {
			Parser<?> parser = ParseUtils.getParser(node);
			 parser.parse((Element) node); 
		}
	}

}
