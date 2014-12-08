package com.bj58.oceanus.plugins.hibernate.trackers;

import com.bj58.oceanus.core.timetracker.Tracker;
import com.bj58.oceanus.core.timetracker.handler.TrackResult;

public class ParseSqlTracker extends Tracker{

	@Override
	public void doTrack(TrackResult trackResult) {
		System.err.println("in custom ParseSqlTracker:" + trackResult.toString());
	}

}
