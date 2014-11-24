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
package com.bj58.oceanus.config;

import java.util.ArrayList;
import java.util.List;

/**
 * NameNode配置实体
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class NameNodeConfig extends BaseNameNodeConfig implements Config,
		Cloneable {

	private static final long serialVersionUID = 1L;
	
	String id;
	String schema; 
	String loadbalance;
	String ref;
	List<DataNodeReferenceConfig> referenceNodes = new ArrayList<DataNodeReferenceConfig>();
	String tableName;
	String orgTableName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public List<DataNodeReferenceConfig> getReferenceNodes() {
		return referenceNodes;
	}

	public void setReferenceNodes(List<DataNodeReferenceConfig> referenceNodes) {
		this.referenceNodes = referenceNodes;
	}

	public void addReferenceNode(DataNodeReferenceConfig node) {
		referenceNodes.add(node);
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	 

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOrgTableName() {
		return orgTableName;
	}

	public void setOrgTableName(String orgTableName) {
		this.orgTableName = orgTableName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	@Override
	public String toString() {
		return "NameNodeConfig [id=" + id + ", loadbalance=" + loadbalance + ", dataNodes="
				+ referenceNodes + "]";
	}

}
