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
package com.bj58.oceanus.core.timetracker;

import java.util.HashMap;
import java.util.Map;

import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.timetracker.handler.TrackResult;
import com.google.common.collect.Maps;

/**
 * 耗时监控点以及处理器的持有者，也是监控器的基类
 * 子类实现 {@link #doTrack()} 方法并加入配置，即可实现自定义监控
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class Tracker {
	
	private static final Map<TrackPoint, TrackerHodler> generalTrackerMap = Maps.newHashMap();
	private static final Map<String, Map<TrackPoint, TrackerHodler>> tableTrackerMap = Maps.newHashMap();
	
	/**
	 * 添加全局Tracker
	 * @param trackerHodler
	 */
	static synchronized void addTracker(TrackerHodler trackerHodler) {
		if(generalTrackerMap.containsKey(trackerHodler.getTrackPoint()))
			throw new ConfigurationException("TrackPoint ["+ trackerHodler.getTrackPoint() + 
					"] already exsit!");
		
		generalTrackerMap.put(trackerHodler.getTrackPoint(), trackerHodler);
	}
	
	/**
	 * 为指定table添加Tracker
	 * @param tableName
	 * @param trackerHodler
	 */
	static synchronized void addTracker(String tableName, TrackerHodler trackerHodler) {
		if(!tableTrackerMap.containsKey(tableName))
			tableTrackerMap.put(tableName, new HashMap<TrackPoint, TrackerHodler>());
		
		if(tableTrackerMap.get(tableName).containsKey(trackerHodler.getTrackPoint()))
			throw new ConfigurationException("TrackPoint [" + trackerHodler.getTrackPoint() + 
					"] for table [" + tableName + "] already exsit!");
		
		tableTrackerMap.get(tableName).put(trackerHodler.getTrackPoint(), trackerHodler);
	}

	/**
	 * 获取全局指定类型的 tracker
	 * @param trackPoint
	 * @return
	 */
	static TrackerHodler getTracker(TrackPoint trackPoint) {
		return generalTrackerMap.get(trackPoint);
	}
	
	/**
	 * 获取指定表、指定类型的 tracker
	 * @param tableName
	 * @param trackPoint
	 * @return
	 */
	static TrackerHodler getTableTracker(String tableName, TrackPoint trackPoint) {
		if(!tableTrackerMap.containsKey(tableName))
			return null;
		
		return tableTrackerMap.get(tableName).get(trackPoint);
	}
	
	/**
	 * 当对应的 TrackPoint 执行时间大于 getThreshold() 时的回调方法
	 * @param TrackResult 
	 * 	- tableName 对应的表
	 * 	- sql 对应的sql
	 * 	- costTime 执行耗时
	 */
	public abstract void doTrack(TrackResult trackResult);
	
}
