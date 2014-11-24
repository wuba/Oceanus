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
package com.bj58.oceanus.core.resource;

import java.util.List;

/**
 * NameNode 实例持有者
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class NameNodeHolder implements NameNode {
	
	final NameNode nameNode;
	String orgTableName;
	String tableName;
	String schema;
	int index;

	public NameNodeHolder(NameNode node) {
		this.nameNode = node;
	}
 
	@Override
	public List<DataNodeHolder> getDataNodes() {
		return nameNode.getDataNodes();
	}

	@Override
	public List<DataNodeHolder> getWriteNodes() {
		return nameNode.getWriteNodes();
	}

	@Override
	public List<DataNodeHolder> getReadNodes() {
		return nameNode.getReadNodes();
	}

	@Override
	public DataNodeHolder remove(DataNodeHolder dataNode) {
		return nameNode.remove(dataNode);
	}

	@Override
	public String getId() {
		return nameNode.getId();
	}
	
	@Override
	public LoadBanlance getLoadBanlance() {
		return nameNode.getLoadBanlance();
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
