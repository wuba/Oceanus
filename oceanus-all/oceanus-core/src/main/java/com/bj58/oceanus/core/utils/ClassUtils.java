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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射的Util函数集合.
 * 
 * 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性,转换字符串到对象等Util函数.
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ClassUtils {

	private static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object object, final String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常" + e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:" + e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes,
			final Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null)
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");

		method.setAccessible(true);

		try {
			return method.invoke(object, parameters);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getDeclaredField(final Object object, final String fieldName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 强行设置Field可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
	 * extends HibernateDao<UserEntity>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<UserEntity,Long>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		}

		return new RuntimeException("Unexpected Exception.", e);
	}

	public static Object getPrivateField(Object o, String fieldName) {

		if (o == null) {
			return null;
		}
		// Go and find the private field...
		final Field fields[] = o.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; ++i) {
				if (fieldName.equals(fields[i].getName())) {

					fields[i].setAccessible(true);
					if (fields[i].get(o) != null) {
						return fields[i].get(o);
					} else {
						try {
							String capitalized = "get" + Character.toUpperCase(fieldName.charAt(0))+ fieldName.substring(1);//StringUtils.capitalize(fieldName)
							Method method = getMethod(o, capitalized);
							return method.invoke(o);
						} catch (SecurityException e) {
							logger.error("getPrivateField(Object, String)", e); //$NON-NLS-1$

						} catch (IllegalArgumentException e) {
							logger.error("getPrivateField(Object, String)", e); //$NON-NLS-1$

							e.printStackTrace();
						} catch (InvocationTargetException e) {
							logger.error("getPrivateField(Object, String)", e); //$NON-NLS-1$

							e.printStackTrace();
						}
						return null;
					}
				}

			}
		} catch (IllegalAccessException ex) {
			logger.warn("(Object, String) - exception ignored", ex); //$NON-NLS-1$
			throw new RuntimeException(ex);
		}
		return null;
	}

	private static Method getMethod(Object o, String methodName) {
		final Method methods[] = o.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; ++i) {
			if (methodName.equalsIgnoreCase(methods[i].getName())) {
				return methods[i];
			}
		}

		return null;
	}

	public static Object invokePrivateMethod(Object o, String methodName, Object[] params) {
		if (logger.isInfoEnabled()) {
			logger.info("invokePrivateMethod(Object, String, Object[]) - start"); //$NON-NLS-1$
		}

		// Go and find the private method...
		final Method methods[] = o.getClass().getDeclaredMethods();
		try {
			for (int i = 0; i < methods.length; ++i) {
				if (methodName.equals(methods[i].getName())) {

					methods[i].setAccessible(true);
					Object returnObject = methods[i].invoke(o, params);
					return returnObject;

				}
			}
		} catch (IllegalAccessException ex) {
			logger.warn("invokePrivateMethod(Object, String, Object[]) - exception ignored", ex); //$NON-NLS-1$
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ite) {
			logger.warn("invokePrivateMethod(Object, String, Object[]) - exception ignored", ite); //$NON-NLS-1$
			throw new RuntimeException(ite);
		}

		if (logger.isInfoEnabled()) {
			logger.info("invokePrivateMethod(Object, String, Object[]) - end"); //$NON-NLS-1$
		}
		return null;
	}
}
