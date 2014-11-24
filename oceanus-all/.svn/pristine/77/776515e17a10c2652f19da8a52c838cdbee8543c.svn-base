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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DataNode配置实体
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DataNodeConfig extends BaseDataNodeConfig implements Config {
	
	private static final long serialVersionUID = 1L;
	
	String id;
	String url;
	String username;
	String password;
	String parent;
	String ref;
	String accessMode;
	Long weight;
	String slaves[];
	Map<String, String> properties = new HashMap<String, String>();
	String alarmClass;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getSlaves() {
		return slaves;
	}

	public void setSlaves(String[] slaves) {
		this.slaves = slaves;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public String getAlarmClass() {
		return alarmClass;
	}

	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}

	public DataNodeConfig clone() {
		DataNodeConfig obj = null;
		try {
			obj = (DataNodeConfig) super.clone();
		} catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		obj.setId(id);
		obj.setUrl(url);
		obj.setUsername(username);
		obj.setUrl(url);
		obj.setParent(parent);
		obj.setPassword(password);
		obj.setRef(ref);
		obj.setAccessMode(accessMode);
		obj.setWeight(weight);
		obj.setSlaves(slaves);
		obj.setProperties(properties);
		obj.setAlarmClass(alarmClass);
		return obj;
	}

	@Override
	public String toString() {
		return "DataNodeConfig [id=" + id + ", url=" + url + ", username="
				+ username + ", password=" + password + ", parent=" + parent
				+ ", ref=" + ref + ", accessMode=" + accessMode + ", weight="
				+ weight + ", slaves=" + Arrays.toString(slaves)
				+ ", properties=" + properties + ", alarmClass=" 
				+ alarmClass + "]";
	}

}
