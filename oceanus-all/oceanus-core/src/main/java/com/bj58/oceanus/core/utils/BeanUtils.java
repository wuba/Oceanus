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
package com.bj58.oceanus.core.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.bj58.oceanus.core.converters.ConvertUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 通过反射对规范的JavaBean做set、get方法调用
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BeanUtils {
	
	private static final Map<Class<?>, BeanInfo> BEAN_CACHE = Maps.newConcurrentMap();
	
	public static void setProperty(Object object, String name, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		BeanInfo beanInfo = getBeanInfo(object);
		
		Method setter = beanInfo.getSetterMapping().get(name);
		if(setter == null)
			return;
		
		setter.invoke(object, ConvertUtils.convert(value, setter.getParameterTypes()[0]));
	}
	
	public static Object getProperty(Object object, String name) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		BeanInfo beanInfo = getBeanInfo(object);
		
		Method getter = beanInfo.getGetterMapping().get(name);
		
		if(getter == null)
			return null;
		
		return getter.invoke(object);
	}
	
	private static BeanInfo getBeanInfo(Object object) {
		BeanInfo beanInfo = BEAN_CACHE.get(object.getClass());
		if(beanInfo == null){
			try {
				beanInfo = new BeanInfo(object.getClass());
				BEAN_CACHE.put(object.getClass(), beanInfo);
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
		}
		return beanInfo;
	}
	
	private static class BeanInfo {
		private Class<?> bean;
		private Set<Field> fieldSet = Sets.newHashSet();
		private Map<String, Field> fieldMapping = Maps.newHashMap();
		private Map<String, Method> setterMapping = Maps.newHashMap();
		private Map<String, Method> getterMapping = Maps.newHashMap();
		
		BeanInfo(Class<?> bean) throws IntrospectionException {
			this.bean = bean;
			parseFieldMapping();
			parseSetterMapping();
			parseGetterMapping();
		}

		private void parseFieldMapping() {
			Field[] fields = bean.getDeclaredFields();
			for (Field field : fields) {
				if("serialVersionUID".equalsIgnoreCase(field.getName())){
					continue;
				}
				fieldMapping.put(field.getName(), field);
				fieldSet.add(field);
			}
		}
		
		private void parseSetterMapping() throws IntrospectionException {
			Map<String, Method> mapMethod = new HashMap<String, Method>();
			
			for(Field f : fieldSet) {
				Method setterMethod = mapMethod.get(f.getName());
				if(setterMethod == null) {
					String setFunName = "set" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
			
					for(Method m : bean.getMethods()){
						if(m.getName().equals(setFunName)){
							setterMethod = m;
							break;
						}
					}
					
					mapMethod.put(f.getName(), setterMethod);
				}
			}
			
			setterMapping.putAll(mapMethod);
		}
		
		private void parseGetterMapping() throws IntrospectionException {
			Map<String, Method> mapMethod = new HashMap<String, Method>();
			
			for(Field f : fieldSet) {
				Method getterMethod = mapMethod.get(f.getName());
				if(getterMethod == null) {
					String getFunName = "get" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
			
					for(Method m : bean.getMethods()){
						if(m.getName().equals(getFunName)){
							getterMethod = m;
							break;
						}
					}
					
					mapMethod.put(f.getName(), getterMethod);
				}
			}
			
			getterMapping.putAll(mapMethod);
		}

		Map<String, Method> getSetterMapping() {
			return setterMapping;
		}

		Map<String, Method> getGetterMapping() {
			return getterMapping;
		}
		
	}

}
