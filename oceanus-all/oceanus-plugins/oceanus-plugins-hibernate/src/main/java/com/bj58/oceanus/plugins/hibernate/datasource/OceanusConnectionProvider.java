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
package com.bj58.oceanus.plugins.hibernate.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;

import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.exchange.jdbc.datasource.DataSourceWrapper;

/**
 * 由Oceanus实现的链接生产者
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class OceanusConnectionProvider implements ConnectionProvider, Configurable {

	private static final long serialVersionUID = 1L;
	
	private DataSourceWrapper dataSource;
	
	@SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        try {
			return dataSource.isWrapperFor(unwrapType);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		}
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        try {
			return dataSource.unwrap(unwrapType);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		}
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void configure(Map configurationValues) {
    		Properties properties = new Properties();
    		for(Iterator<Entry<String, String>> iterator = 
    				configurationValues.entrySet().iterator(); 
    				iterator.hasNext();){
    			
    			Entry<String, String> pair = iterator.next();
    			String key = pair.getKey();
    			String value = pair.getValue();
    			
    			if(DataSourceConfigKey.convertAble(key))
    				key = DataSourceConfigKey.convertToOceanusKey(key);
    				
    			properties.put(key, value);
    		}
    		
    		this.dataSource = new DataSourceWrapper(properties);
    }

    
	
	

}
