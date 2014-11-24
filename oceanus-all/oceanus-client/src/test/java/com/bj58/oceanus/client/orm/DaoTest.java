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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.client.orm.bean.UserDynamic;
import com.bj58.oceanus.client.orm.bean.repository.UserDynamicDao;

public class DaoTest {
	
	static {
		// 进程启动时要对 Oceanus 进行初始化
		Oceanus.init("d:/configurations_demo.xml");
	}
	
	@Test
	public void queryTest(){
		for(long uid=1; uid<2; uid++){
			try {
				List<UserDynamic> userDynamics = UserDynamicDao.instance.getUserDynamicPage();
				System.out.println(userDynamics);
				TimeUnit.SECONDS.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

