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
package com.bj58.oceanus.core.jdbc.result;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.converters.ConvertUtils;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.jdbc.aggregate.Aggregate;
 

/**
 * 类型转换工具
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class TypeConvertUtils {
	static Logger logger=LoggerFactory.getLogger(TypeConvertUtils.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convert(Object value, Class<T> claz) {
		if(value instanceof Aggregate){
			try {
				value=((Aggregate)value).value(); 
			} catch (SQLException e) {
				logger.error("Aggregate error!",e);
				throw new ShardException("shard error",e);
			}
		}
		return (T) ConvertUtils.convert(value, claz);
	}

}
