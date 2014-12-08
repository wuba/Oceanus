package com.bj58.oceanus.plugins.hibernate.trackers;

import com.bj58.oceanus.core.timetracker.Tracker;
import com.bj58.oceanus.core.timetracker.handler.TrackResult;

public class ExecuteSqlTracker extends Tracker{

	@Override
	public void doTrack(TrackResult trackResult) {
		// 耗时
		trackResult.getCostTime();
		
		// 当前埋点执行的sql
		trackResult.getSql();
		
		// 当前埋点sql对应的table名字
		trackResult.getTableName();
		System.err.println("in custom ExecuteSqlTracker:" + trackResult.toString());
	}

}
