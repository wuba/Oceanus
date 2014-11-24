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
package com.bj58.oceanus.client.routeinfo.test;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.client.routeinfo.DefaultRouterInfoService;
import com.bj58.oceanus.client.routeinfo.RouterInfoService;
import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.shard.RouteTarget;

public class RouterInfoServiceTest {
	RouterInfoService routeSerice = new DefaultRouterInfoService();
	static {
		Configurations.getInstance().init("");
	}

	@Test
	public void testRoute() throws SQLException {
		String sql = "select * from t_user";
		Map<BatchItem, Set<RouteTarget>> results = routeSerice.route(sql);

		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		BatchItem batchItem = results.keySet().iterator().next(); 
		Assert.assertNotNull(batchItem);
		Assert.assertNotNull(batchItem.getAnalyzeResult());
		Assert.assertEquals(sql, batchItem.getSql());
		Assert.assertEquals(0, batchItem.getCallbacks().size()); 
		
		Set<RouteTarget> targets = results.values().iterator().next();
		Assert.assertEquals(17, targets.size());
		Iterator<RouteTarget> iterator=targets.iterator(); 
		RouteTarget target=iterator.next();
		BatchItem comparedItem=target.getBatchItem();
		Assert.assertEquals(batchItem, comparedItem);
		NameNodeHolder nameNodehoder=(NameNodeHolder) target.getNameNode();
		Assert.assertEquals("users1".toUpperCase(), nameNodehoder.getId().toUpperCase());
		Assert.assertEquals("t_user11".toUpperCase(), nameNodehoder.getTableName().toUpperCase());
	}

}
