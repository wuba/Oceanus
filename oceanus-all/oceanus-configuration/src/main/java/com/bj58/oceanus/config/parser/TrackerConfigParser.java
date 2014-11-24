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
package com.bj58.oceanus.config.parser;

import org.w3c.dom.Element;

import com.bj58.oceanus.config.TrackerConfig;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.timetracker.TrackPoint;
import com.bj58.oceanus.core.timetracker.Tracker;
import com.bj58.oceanus.core.timetracker.TrackerHodler;

/**
 * Tracker配置解析器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TrackerConfigParser implements Parser<TrackerConfig> {

	@Override
	public TrackerConfig parse(Element el) {
		TrackerConfig trackerConfig = new TrackerConfig();
		TrackerHodler trackerHodler = new TrackerHodler();
		
		String type = ParseUtils.getAttr(el, "type");
		trackerConfig.setType(type);
		trackerHodler.setTrackPoint(TrackPoint.parse(type));
		
		String threshold = ParseUtils.getAttr(el, "threshold");
		trackerConfig.setThreshold(Long.valueOf(threshold));
		trackerHodler.setThreshold(Long.valueOf(threshold));
		
		try {
			String className = ParseUtils.getAttr(el, "class");
			trackerConfig.setClassName(className);
			Tracker tracker = (Tracker) Class.forName(className).newInstance();
			trackerHodler.setTracker(tracker);
		} catch (Exception e){
			throw new ConfigurationException(e);
		}
		
		trackerHodler.regist();
		return trackerConfig;
	}

}
