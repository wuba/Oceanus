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
package com.bj58.oceanus.client.orm.bean;

import java.io.Serializable;

import com.bj58.oceanus.client.orm.annotation.Column;
import com.bj58.oceanus.client.orm.annotation.NotColumn;

public class UserDynamic implements Serializable{
	
	@NotColumn
	private static final long serialVersionUID = 1L;

	private long uid;
	
	@Column(getFuncName="getPwds", setFuncName="setPwds")
	private String PWDS;
	
	public String getTestfield() {
		return TESTfield;
	}

	public void setTestfield(String testfield) {
		this.TESTfield = testfield;
	}

	@Column(name="ver")
	private String version;
	
	private String TESTfield;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getPwds() {
		return PWDS;
	}

	public void setPwds(String pwds) {
		this.PWDS = pwds;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "UserDynamic [uid=" + uid + ", PWDS=" + PWDS + ", version="
				+ version + ", testfield=" + TESTfield + "]";
	}


}
