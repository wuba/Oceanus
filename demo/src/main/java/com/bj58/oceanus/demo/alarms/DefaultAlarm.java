package com.bj58.oceanus.demo.alarms;

import java.util.concurrent.TimeUnit;

import com.bj58.oceanus.core.alarm.AlarmType;
import com.bj58.oceanus.core.alarm.PeriodicAlarm;

public class DefaultAlarm extends PeriodicAlarm{
	
	public DefaultAlarm() {
		System.out.println("==============================");
		System.out.println("=====   Alarm Inited !   =====");
		System.out.println("==============================");
	}

	@Override
	public void excuteAlarm(AlarmType type, String dataNodeId) {
		
		switch (type) {
			case DB_AVALIABLE:
				System.out.println(dataNodeId + " 已恢复......");
				break;
			case DB_NOTAVALIABLE:
				System.out.println(dataNodeId + " 不可用了!!!!!");
				break;
		}
	}

	@Override
	protected long getAlarmCycle() {
		return 5;
	}

	@Override
	protected TimeUnit getAlarmUnit() {
		return TimeUnit.MINUTES;
	}

}

