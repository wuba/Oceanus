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
package com.bj58.oceanus.client.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.bj58.oceanus.client.Oceanus;

/**
 * 使用ORM功能的DAO基类
 * <p>
 * 提供标准的excute方法，由子类继承后，进行业务封装扩展
 * </p>
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BaseDao {
	
	public boolean excute(String sql, Object... objects) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Oceanus.getConnection();
			ps = connection.prepareStatement(sql);
			if(objects!=null && objects.length>0){
				for(int i=0; i<objects.length; i++){
					ps.setObject(i+1, objects[i]);
				}
			}
			return ps.execute();
		} finally {
			Oceanus.closeStatement(ps);
			Oceanus.closeConnection(connection);
		}
	}
	
	public int excuteUpdate(String sql, Object... objects) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Oceanus.getConnection();
			ps = connection.prepareStatement(sql);
			if(objects!=null && objects.length>0){
				for(int i=0; i<objects.length; i++){
					ps.setObject(i+1, objects[i]);
				}
			}
			return ps.executeUpdate();
		} finally {
			Oceanus.closeStatement(ps);
			Oceanus.closeConnection(connection);
		}
	}
	
	public <T> List<T> excuteQuery(Class<T> clazz, String sql, Object... objects) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = Oceanus.getConnection();
			ps = connection.prepareStatement(sql);
			if(objects!=null && objects.length>0){
				for(int i=0; i<objects.length; i++){
					ps.setObject(i+1, objects[i]);
				}
			}
			rs = ps.executeQuery();
			return populateData(rs, clazz);
		} finally {
			Oceanus.close(rs, ps, connection);
		}
	}
	
	private <T> List<T> populateData(ResultSet resultSet, Class<T> clazz) throws Exception {
		List<T> dataList = new ArrayList<T>();
		List<Field> fieldList = MappingAnnotationUtil.getAllFields(clazz);

		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsCount = rsmd.getColumnCount();
		List<String> columnNameList = new ArrayList<String>();
		for(int i = 0; i < columnsCount; i++){
			columnNameList.add(rsmd.getColumnLabel(i+1).toLowerCase());
		}

		while (resultSet.next()) {
			T bean = clazz.newInstance();
			for(Field f : fieldList) {
				String columnName = MappingAnnotationUtil.getDBCloumnName(clazz, f).toLowerCase();
				if(columnNameList.contains(columnName)) {
					Object columnValueObj = null;
					Class<?> filedCls = f.getType();
					
					if(filedCls == int.class || filedCls == Integer.class) {
						columnValueObj = resultSet.getInt(columnName);
					} else if(filedCls == String.class) {
						columnValueObj = resultSet.getString(columnName);
					} else if(filedCls == boolean.class || filedCls == Boolean.class) {
						columnValueObj = resultSet.getBoolean(columnName);
					} else if(filedCls == byte.class || filedCls == Byte.class) {
						columnValueObj = resultSet.getByte(columnName);
					} else if(filedCls == short.class || filedCls == Short.class) {
						columnValueObj = resultSet.getShort(columnName);
					} else if(filedCls == long.class || filedCls == Long.class) {
						columnValueObj = resultSet.getLong(columnName);
					} else if(filedCls == float.class || filedCls == Float.class) {
						columnValueObj = resultSet.getFloat(columnName);
					} else if(filedCls == double.class || filedCls == Double.class) {
						columnValueObj = resultSet.getDouble(columnName);
					} else if(filedCls == BigDecimal.class) {
						columnValueObj = resultSet.getBigDecimal(columnName);
					} 
					
					else {
						columnValueObj = resultSet.getObject(columnName);
					}
					
					if (columnValueObj != null) {
						Method setterMethod = MappingAnnotationUtil.getSetterMethod(clazz, f);
						setterMethod.invoke(bean, new Object[] { columnValueObj });
					}
				}
			}
			dataList.add(bean);
		}
		return dataList;
	}

}