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
package com.bj58.oceanus.core.alarm;

/**
 * 警报接口约束，提供相应警报的对接功能
 * 
 * <p>
 * 接入方通过配置指定警报类型对应的实现类
 * 当触发一个警报时，由警报执行器找到对应的警报实现类进行接口调用
 * </p>
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface Alarm {
	
	void excute(AlarmType type, String dataNodeId);

}

