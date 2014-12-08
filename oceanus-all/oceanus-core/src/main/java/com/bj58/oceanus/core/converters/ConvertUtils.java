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
package com.bj58.oceanus.core.converters;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

public class ConvertUtils {
	private static final Integer ZERO = new Integer(0);
	private static final Character SPACE = new Character(' ');
	private static final HashMap<Class<?>, Converter> converters = new HashMap<Class<?>, Converter>();
	static {
		registerPrimitives(true);
		registerStandard(true, true);
		registerOther(true);
	}

	public static Object convert(Object content, Class<?> claz) {
		Converter converter = converters.get(claz);
		if (converter == null) {
			return null;
		}
		return converter.convert(claz, content);
	}

	private static void register(Converter converter, Class<?> clazz) {

		converters.put(clazz, converter);

	}

	private static void register(Class<?> clazz, Converter converter) {
		register(converter, clazz);
	}

	private static void registerPrimitives(boolean throwException) {
		register(Boolean.TYPE, throwException ? new BooleanConverter()
				: new BooleanConverter(Boolean.FALSE));
		register(Byte.TYPE, throwException ? new ByteConverter()
				: new ByteConverter(ZERO));
		register(Character.TYPE, throwException ? new CharacterConverter()
				: new CharacterConverter(SPACE));
		register(Double.TYPE, throwException ? new DoubleConverter()
				: new DoubleConverter(ZERO));
		register(Float.TYPE, throwException ? new FloatConverter()
				: new FloatConverter(ZERO));
		register(Integer.TYPE, throwException ? new IntegerConverter()
				: new IntegerConverter(ZERO));
		register(Long.TYPE, throwException ? new LongConverter()
				: new LongConverter(ZERO));
		register(Short.TYPE, throwException ? new ShortConverter()
				: new ShortConverter(ZERO));
	}

	/**
	 * Register the converters for standard types. </p> This method registers
	 * the following converters:
	 * <ul>
	 * <li><code>BigDecimal.class</code> - {@link BigDecimalConverter}</li>
	 * <li><code>BigInteger.class</code> - {@link BigIntegerConverter}</li>
	 * <li><code>Boolean.class</code> - {@link BooleanConverter}</li>
	 * <li><code>Byte.class</code> - {@link ByteConverter}</li>
	 * <li><code>Character.class</code> - {@link CharacterConverter}</li>
	 * <li><code>Double.class</code> - {@link DoubleConverter}</li>
	 * <li><code>Float.class</code> - {@link FloatConverter}</li>
	 * <li><code>Integer.class</code> - {@link IntegerConverter}</li>
	 * <li><code>Long.class</code> - {@link LongConverter}</li>
	 * <li><code>Short.class</code> - {@link ShortConverter}</li>
	 * <li><code>String.class</code> - {@link StringConverter}</li>
	 * </ul>
	 * 
	 * @param throwException
	 *            <code>true</code> if the converters should throw an exception
	 *            when a conversion error occurs, otherwise <code>
	 * <code>false</code> if a default value should be used.
	 * @param defaultNull
	 *            <code>true</code>if the <i>standard</i> converters (see
	 *            {@link ConvertUtilsBean#registerStandard(boolean, boolean)})
	 *            should use a default value of <code>null</code>, otherwise
	 *            <code>false</code>. N.B. This values is ignored if
	 *            <code>throwException</code> is <code>true</code>
	 */
	private static void registerStandard(boolean throwException,
			boolean defaultNull) {

		Number defaultNumber = defaultNull ? null : ZERO;
		BigDecimal bigDecDeflt = defaultNull ? null : new BigDecimal("0.0");
		BigInteger bigIntDeflt = defaultNull ? null : new BigInteger("0");
		Boolean booleanDefault = defaultNull ? null : Boolean.FALSE;
		Character charDefault = defaultNull ? null : SPACE;
		String stringDefault = defaultNull ? null : "";

		register(BigDecimal.class, throwException ? new BigDecimalConverter()
				: new BigDecimalConverter(bigDecDeflt));
		register(BigInteger.class, throwException ? new BigIntegerConverter()
				: new BigIntegerConverter(bigIntDeflt));
		register(Boolean.class, throwException ? new BooleanConverter()
				: new BooleanConverter(booleanDefault));
		register(Byte.class, throwException ? new ByteConverter()
				: new ByteConverter(defaultNumber));
		register(Character.class, throwException ? new CharacterConverter()
				: new CharacterConverter(charDefault));
		register(Double.class, throwException ? new DoubleConverter()
				: new DoubleConverter(defaultNumber));
		register(Float.class, throwException ? new FloatConverter()
				: new FloatConverter(defaultNumber));
		register(Integer.class, throwException ? new IntegerConverter()
				: new IntegerConverter(defaultNumber));
		register(Long.class, throwException ? new LongConverter()
				: new LongConverter(defaultNumber));
		register(Short.class, throwException ? new ShortConverter()
				: new ShortConverter(defaultNumber));
		register(String.class, throwException ? new StringConverter()
				: new StringConverter(stringDefault));

	}

	/**
	 * Register the converters for other types. </p> This method registers the
	 * following converters:
	 * <ul>
	 * <li><code>Class.class</code> - {@link ClassConverter}</li>
	 * <li><code>java.util.Date.class</code> - {@link DateConverter}</li>
	 * <li><code>java.util.Calendar.class</code> - {@link CalendarConverter}</li>
	 * <li><code>File.class</code> - {@link FileConverter}</li>
	 * <li><code>java.sql.Date.class</code> - {@link SqlDateConverter}</li>
	 * <li><code>java.sql.Time.class</code> - {@link SqlTimeConverter}</li>
	 * <li><code>java.sql.Timestamp.class</code> - {@link SqlTimestampConverter}
	 * </li>
	 * <li><code>URL.class</code> - {@link URLConverter}</li>
	 * </ul>
	 * 
	 * @param throwException
	 *            <code>true</code> if the converters should throw an exception
	 *            when a conversion error occurs, otherwise <code>
	 * <code>false</code> if a default value should be used.
	 */
	private static void registerOther(boolean throwException) {
		register(Class.class, throwException ? new ClassConverter()
				: new ClassConverter(null));
		register(java.util.Date.class, throwException ? new DateConverter()
				: new DateConverter(null));
		register(Calendar.class, throwException ? new CalendarConverter()
				: new CalendarConverter(null));
		register(File.class, throwException ? new FileConverter()
				: new FileConverter(null));
		register(java.sql.Date.class, throwException ? new SqlDateConverter()
				: new SqlDateConverter(null));
		register(java.sql.Time.class, throwException ? new SqlTimeConverter()
				: new SqlTimeConverter(null));
		register(Timestamp.class, throwException ? new SqlTimestampConverter()
				: new SqlTimestampConverter(null));
		register(URL.class, throwException ? new URLConverter()
				: new URLConverter(null));
	}

}
