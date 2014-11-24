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
package com.bj58.oceanus.config.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.DataNodeConfig;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.factory.ObjectFactory;

/**
 * 数据源节点工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DataSourceFactory implements ObjectFactory<DataNodeConfig> {
	static Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

	@Override
	public Object create(DataNodeConfig config) {
		if (config.getId() == null) {
			return null;
		}
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setMinEvictableIdleTimeMillis(1000 * 60 * 10);
		dataSource.setNumTestsPerEvictionRun(5);
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeout(10);
		dataSource.setTimeBetweenEvictionRunsMillis(1000 * 60 * 5);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setValidationQuery("SELECT 1");
		
		Map<String, String> properties=getProperties(config);
		if(logger.isDebugEnabled()){
			logger.debug("datanode id=["+config.getId()+"] properties="+properties);
		}
		for (Map.Entry<String, String> entry :properties .entrySet()) {
			try {
				BeanUtilsBean2.getInstance().setProperty(dataSource,
						entry.getKey(), entry.getValue());
			} catch (Exception e) {
				logger.error("create DataSource error!", e);
			}

		}
		
		return dataSource;
	}

	private Map<String, String> getProperties(DataNodeConfig config) {
		Map<String, String> results = new LinkedHashMap<String, String>();

		if (config.getParent() != null
				&& config.getParent().trim().length() > 0) {
			DataNodeConfig parent = Configurations.getInstance()
					.getDataNodeConfig(config.getParent().trim());
			if (parent == null) {
				throw new ConfigurationException(
						"datanode config error!parent is not found!parent="
								+ config.getParent());
			}
			results.putAll(getProperties(parent));
		} else {
			return config.getProperties();
		}
		results.putAll(config.getProperties());
		return results;
	}

}
