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
package com.bj58.oceanus.core.jdbc;

/**
 * Connection 生命周期中的事件类型
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum ConnectionEvent {
	
	SET_READ_ONLY, SET_ISOLATE_LEVEL, SET_AUTO_COMMIT, SET_CATALOG, SET_TYPE_MAP, SET_HOLDABILITY,
	/**
	  * 以下是一个statement执行完毕后需要清除的，不是属性的设置，而且该场境，仅支持单库单表路由的情况
	  */
	CREATE_CLOB, CREATE_NCLOB, CREATE_BLOB, CREATE_SQL_XML;

}
