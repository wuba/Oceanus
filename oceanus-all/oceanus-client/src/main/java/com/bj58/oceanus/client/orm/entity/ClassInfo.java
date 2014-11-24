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
package com.bj58.oceanus.client.orm.entity;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.bj58.oceanus.client.orm.annotation.Column;
import com.bj58.oceanus.client.orm.annotation.NotColumn;

/**
 * 持久化对象类信息
 * <p>
 * 解析被标注的对象，摘取映射关系
 * </p>
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ClassInfo {

	/**
	 * key: field.getName()  value:field
	 */
	private Map<String, Field> mapAllDBField;
	
	/**
	 * key: field.getName()  value:db column name
	 */
	private Map<String, String> mapDBColumnName;
	
	/**
	 * key: field.getName()  value:Method
	 */
	private Map<String, Method> mapSetMethod;
	
	/**
	 * key: field.getName()  value:Method
	 */
	private Map<String, Method> mapGetMethod;
	
	public ClassInfo(Class<?> clazz) throws Exception {
		this.setMapAllDBField(getAllDBFields(clazz));
		this.setMapDBColumnName(getCloumnName(clazz));
		this.setMapSetMethod(getSetterMethod(clazz));
		this.setMapGetMethod(getGetterMethod(clazz));
	}
	
	/**
	 * 获得DB所有字段
	 * @param bean
	 * @return
	 */
	private Map<String, Field> getAllDBFields(Class<?> clazz) {
		Map<String, Field> mapFfields = new HashMap<String, Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if(f.isAnnotationPresent(NotColumn.class) ||
					"serialVersionUID".equalsIgnoreCase(f.getName())){
				continue;
			}
			mapFfields.put(f.getName(), f);
			
		}
		return mapFfields;
	}
	
	/**
	 * 获得字段名
	 * @param fList
	 * @return
	 */
	private Map<String, String> getCloumnName(Class<?> clazz){
		Map<String, String> mapNames = new HashMap<String, String>();
		Collection<Field> fList = mapAllDBField.values();
		for(Field f : fList){
			if (f.isAnnotationPresent(Column.class)) {
				Column column = (Column) f.getAnnotation(Column.class);
				if(!column.name().equalsIgnoreCase("fieldName")){
					mapNames.put(f.getName(), column.name());
				}
				else{
					mapNames.put(f.getName(), f.getName());
				}
			}
			else{
				mapNames.put(f.getName(), f.getName());
			}
		}
		return mapNames;
	}
	
	
	/*
	 * 获得字段对应的set方法
	 */
	private Map<String, Method> getSetterMethod(Class<?> clazz) throws Exception {
		Map<String, Method> mapMethod = new HashMap<String, Method>();
		Collection<Field> fList = mapAllDBField.values();
		PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
		
		for(Field f : fList) {
			for (PropertyDescriptor aLPropDesc : lPropDesc) {
				if (aLPropDesc.getName().equalsIgnoreCase(f.getName())) {
					Method setMethod = aLPropDesc.getWriteMethod();
					mapMethod.put(f.getName(), setMethod);
					break;
				}
			}
		}
		
		for(Field f : fList) {
			Method setterMethod = mapMethod.get(f.getName());
			if(setterMethod == null) {
				String setFunName = "";
				if(f.isAnnotationPresent(Column.class)){
					Column column = (Column) f.getAnnotation(Column.class);
					if(!column.setFuncName().equalsIgnoreCase("setField")){
						setFunName = column.setFuncName();
					}
					else{
						setFunName = "set" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
					}
				}
				else{
					setFunName = "set" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
				}
		
				for(Method m : clazz.getMethods()){
					if(m.getName().equals(setFunName)){
						setterMethod = m;
						break;
					}
				}
				
				mapMethod.put(f.getName(), setterMethod);
			}
		}
		
		for(Field f : fList) {
			Method setterMethod = mapMethod.get(f.getName());
			if(setterMethod == null) {
				throw new Exception("can't find set method field:"+ f.getName() + "  class:" + clazz.getName());
			}
		}
		
		return mapMethod;
	}
	
	/*
	 * 获得字段对应的get方法
	 */
	private Map<String, Method> getGetterMethod(Class<?> clazz) throws Exception {
		Map<String, Method> mapMethod = new HashMap<String, Method>();
		Collection<Field> fList = mapAllDBField.values();
		PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(clazz, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
		
		for(Field f : fList) {
			for (PropertyDescriptor aLPropDesc : lPropDesc) {
				if (aLPropDesc.getName().equalsIgnoreCase(f.getName())) {
					Method getMethod = aLPropDesc.getReadMethod();
					mapMethod.put(f.getName(), getMethod);
					break;
				}
			}
		}
		
		for(Field f : fList) {
			Method getterMethod = mapMethod.get(f.getName());
			String getFunName = "";
			if(f.isAnnotationPresent(Column.class)){
				Column column = (Column) f.getAnnotation(Column.class);
				if(!column.getFuncName().equalsIgnoreCase("getField")){
					getFunName = column.getFuncName();
				}
				else{
					getFunName = "get" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
				}
			}
			else{
				getFunName = "get" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
			}
			
			for(Method m : clazz.getMethods()){
				if(m.getName().equals(getFunName)){
					getterMethod = m;
					break;
				}
			}
			mapMethod.put(f.getName(), getterMethod);
		}
		
		for(Field f : fList) {
			Method getterMethod = mapMethod.get(f.getName());
			if(getterMethod == null) {
				throw new Exception("can't find get method field:"+ f.getName() + "  class:" + clazz.getName());
			}
		}
		
		return mapMethod;
	}
	
	public Map<String, Field> getMapAllDBField() {
		return mapAllDBField;
	}

	public void setMapAllDBField(Map<String, Field> mapAllDBField) {
		this.mapAllDBField = mapAllDBField;
	}


	public Map<String, String> getMapDBColumnName() {
		return mapDBColumnName;
	}

	public void setMapDBColumnName(Map<String, String> mapDBColumnName) {
		this.mapDBColumnName = mapDBColumnName;
	}

	public Map<String, Method> getMapSetMethod() {
		return mapSetMethod;
	}

	public void setMapSetMethod(Map<String, Method> mapSetMethod) {
		this.mapSetMethod = mapSetMethod;
	}

	public Map<String, Method> getMapGetMethod() {
		return mapGetMethod;
	}

	public void setMapGetMethod(Map<String, Method> mapGetMethod) {
		this.mapGetMethod = mapGetMethod;
	}

}