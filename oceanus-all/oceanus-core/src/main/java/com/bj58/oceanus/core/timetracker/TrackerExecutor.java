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

import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.timetracker.handler.TrackPointHandler;

/**
 * 耗时监控执行器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class TrackerExecutor {
	
	private static final ThreadLocal<String> tableNameLocal = new ThreadLocal<String>();
	private static final ThreadLocal<String> sqlLocal = new ThreadLocal<String>();
	
	public static final void trackBegin(final TrackPoint trackPoint) {
		trackBegin(trackPoint, null, null);
	}
	
	public static final void trackBegin(final TrackPoint trackPoint, 
			final String sql) {
		trackBegin(trackPoint, null, sql);
	}
	
	public static final void trackBegin(final TrackPoint trackPoint, 
			final BatchItem batchItem) {
		trackBegin(trackPoint, batchItem, null);
	}
	
	public static final void trackBegin(final TrackPoint trackPoint, 
			final BatchItem batchItem, final String sql) {
		
		TimeMeter.getContext().begin(trackPoint);
		
		if(sql!=null)
			sqlLocal.set(sql);
			
		
		if(batchItem==null)
			return;
		else if(batchItem.getAnalyzeResult()==null)
			return;
		else if(batchItem.getAnalyzeResult().getTableInfos()==null)
			return;
		
		tableNameLocal.set(batchItem.getAnalyzeResult()
				.getTableInfos().iterator().next().getName());
	} 
	
	public static final void trackEnd(final TrackPoint trackPoint) {
		long costTime = TimeMeter.getContext().getTimeSpan(trackPoint);
		
		String tableName = tableNameLocal.get();
		
		String sql = sqlLocal.get();
		
		TrackerHodler trackerHodler = getTrackerHodler(trackPoint, tableName);
		
		TrackPointHandler.handle(trackPoint, trackerHodler, tableName, sql, costTime);
		
	}
	
	private static TrackerHodler getTrackerHodler(final TrackPoint trackPoint, final String tableName) {
		TrackerHodler trackerHodler = null;
		
		trackerHodler = Tracker.getTableTracker(tableName, trackPoint);
		
		// 如果没有table对应的 Tracker 就返回全局的 Tracker
		if(trackerHodler == null)
			trackerHodler = Tracker.getTracker(trackPoint);
		
		return trackerHodler;
	}
	
	public static final void trackRelease() {
		TimeMeter.release();
		tableNameLocal.set(null);
		sqlLocal.set(null);
	}
	
}
