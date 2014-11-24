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
package com.bj58.oceanus.client.orm.bean.repository;

import java.util.List;

import com.bj58.oceanus.client.orm.BaseDao;
import com.bj58.oceanus.client.orm.bean.UserDynamic;

public class UserDynamicDao extends BaseDao{
	
	public static final UserDynamicDao instance = new UserDynamicDao();
	
	public List<UserDynamic> getUserDynamicPage() throws Exception{
		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver limit ?,?";
		List<UserDynamic> userDynamics = excuteQuery(UserDynamic.class, sql, 2000, 1000, 0, 4);
		return userDynamics;
	}

	public UserDynamic getUserDynamic(long uid) throws Exception {
		String sql = "select * from t_userdynamic where uid=? limit 1";
		List<UserDynamic> userDynamics = excuteQuery(UserDynamic.class, sql, uid);
		return userDynamics.get(0);
	}

}

