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
package com.bj58.oceanus.core.loadbalance.ha;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.alarm.AlarmExcutor;
import com.bj58.oceanus.core.alarm.AlarmType;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.utils.JdbcUtil;
import com.google.common.collect.Maps;

/**
 * 用户检测配置中的DataNode是否可用
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class DataNodeChecker {
	
	private DataNodeChecker() {}
	
	private static Logger logger = LoggerFactory.getLogger(DataNodeChecker.class);
	
	// 存储DataNode可用状态
	private static final Map<String, AtomicBoolean> dataNodeAliveMap = Maps.newHashMap();
	
	// 定时调度线程池，用于循环监测DataNode可用性
	private static final ScheduledExecutorService schedulerService = Executors.newSingleThreadScheduledExecutor();
	
	// 该缓存线程池用于执行确认DataNode可用性的线程
	private static final ExecutorService executorService = Executors.newCachedThreadPool();
	
	// 只启动一次的判断条件，防止重复开启线程
	private static final AtomicBoolean onceCondition = new AtomicBoolean(false);
	
	// 根据这个sql监测DataNode是否可用，一般可用 SELECT 1
	protected static String checkSql;
	
	protected static Map<String, DataNode> dataNodeMap;
	
	public static void startChecker(String checkSql, Map<String, DataNode> dataNodeMap){
		if(!onceCondition.compareAndSet(false, true)){
			throw new ConfigurationException("DataNode Checker can only start once");
		}

		DataNodeChecker.checkSql = checkSql;
		DataNodeChecker.dataNodeMap = dataNodeMap;
		
		for(Iterator<String> it = DataNodeChecker.dataNodeMap.keySet().iterator(); it.hasNext();)
			dataNodeAliveMap.put(it.next(), new AtomicBoolean(true));
		
		// 目前两次检测是串行执行，数据源太多的时候不能保证及时更新状态，后期可以根据数据源数量改为自动适配，多线程检测
		schedulerService.scheduleAtFixedRate(new FirstChecker(), 1, 30, TimeUnit.SECONDS);
	}
	
	public static void stopChecker(){
		schedulerService.shutdownNow();
		onceCondition.compareAndSet(true, false);
	}
	
	/**
	 * 从状态映射map中查看DataNode是否可用
	 * @param dataNode
	 * @return
	 */
	public static boolean dataNodeAlive(DataNode dataNode){
		if(dataNode==null || dataNode.getId()==null)
			return true;
		
		if(!dataNodeAliveMap.containsKey(dataNode.getId())){
			logger.error("DataNode ["+dataNode.getId()+"] is not in the check list !!");
			return true;
		}
		
		return dataNodeAliveMap.get(dataNode.getId()).get();
	}
	
	/**
	 * 使用check sql来尝试DataNode是否可用，该方法只提供内部线程类使用
	 * @param dataNode
	 * @return
	 */
	private static boolean checkDataNodeAlive(DataNode dataNode) {
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = dataNode.getConnection();
			statement = connection.createStatement();
			return statement.execute(checkSql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.closeStatement(statement);
			JdbcUtil.closeConnection(connection);
		}
		return false;
	}
	
	/**
	 * 该线程用于轮询检测全部DataSource状态，初步判断数据源是否可用，如果不可用，交给ConfirmChecker(确认线程)延时处理
	 */
	private static class FirstChecker implements Runnable{
		
		@Override
		public void run() {
			logger.debug("dataNodeMap:"+DataNodeChecker.dataNodeMap);
			if (DataNodeChecker.dataNodeMap.size()<1) {
				logger.error("no datanode need to check");
				return;
			}
			
			for (Iterator<DataNode> it = DataNodeChecker.dataNodeMap.values().iterator(); it.hasNext();) {
				DataNode dataNode = it.next();
				if (checkDataNodeAlive(dataNode)) {
					if (dataNodeAliveMap.get(dataNode.getId()).compareAndSet(false, true)) {
						logger.error("DataNode ["+dataNode.getId()+"] has been restored !!");
						
						// 调用配置中约定的警报实现类，通知DB已恢复
						AlarmExcutor.doAlarm(AlarmType.DB_AVALIABLE, dataNode);
					}
				} else {
					logger.info("DataNode ["+dataNode.getId()+"] start confirm checker !!");
					executorService.execute(new ConfirmChecker(dataNode));
				}
			}
			
		}
	}
	
	/**
	 * 该线程用于确认数据源是否真的已经不可用,尝试5次,间隔1秒
	 */
	private static class ConfirmChecker implements Runnable{
		
		private DataNode dataNode;
		
		ConfirmChecker(DataNode dataNode) {
			this.dataNode = dataNode;
		}

		@Override
		public void run() {
			int countDown = 5;
			try {
				while(countDown-- > 0){
					TimeUnit.SECONDS.sleep(1);
					if(checkDataNodeAlive(dataNode))
						return;
				}
				
				dataNodeAliveMap.get(dataNode.getId()).set(false);
				logger.error("DataNode ["+dataNode.getId()+"] is died !!");
				
				// 调用配置中约定的警报实现类，通知DB已不可用
				AlarmExcutor.doAlarm(AlarmType.DB_NOTAVALIABLE, dataNode);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
