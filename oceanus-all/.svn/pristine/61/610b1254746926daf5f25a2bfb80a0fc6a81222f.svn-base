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
package com.bj58.oceanus.core.timetracker.handler;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.Tracker;
import com.bj58.oceanus.core.timetracker.TrackerHodler;
import com.google.common.collect.Maps;

/**
 * 耗时监控处理器基类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class TrackPointHandler {
	
	private static final Executor cacheExecutor = Executors.newCachedThreadPool();
	
	private static Map<TrackPoint, TrackPointHandler> handlers = Maps.newHashMap();
	
	static {
		handlers.put(TrackPoint.GET_CONNECTION, 
				new GetConnectionHandler());
		handlers.put(TrackPoint.CONNECTION_CONTEXT, 
				new ConnectionContextHandler());
		handlers.put(TrackPoint.EXECUTE_SQL, 
				new ExecuteSqlHandler());
		handlers.put(TrackPoint.PARSE_SQL, 
				new ParseSqlHandler());
	}
	
	public static void handle(TrackPoint trackPoint, TrackerHodler trackerHodler, 
			String tableName, String sql, long costTime){
		
		if(!handlers.containsKey(trackPoint))
			return;
		
		handlers.get(trackPoint).handle(trackerHodler, tableName, sql, costTime);
	}
	
	protected abstract void handle(TrackerHodler trackerHodler, String tableName, 
			String sql, long costTime);

	protected void asyncDoTrack(final Tracker tracker, final TrackResult trackResult){
		cacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				tracker.doTrack(trackResult);
			}
		});
	}
	
}
