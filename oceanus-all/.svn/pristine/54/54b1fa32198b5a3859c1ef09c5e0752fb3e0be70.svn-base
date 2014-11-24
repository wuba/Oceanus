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
package com.bj58.oceanus.exchange.router;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.jdbc.ConnectionProvider;
import com.bj58.oceanus.core.jdbc.ParameterCallback;
import com.bj58.oceanus.core.resource.DataNodeHolder;
import com.bj58.oceanus.core.resource.DefaultDataNode;
import com.bj58.oceanus.core.resource.DefaultNameNode;
import com.bj58.oceanus.core.resource.LoadBanlance;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.resource.TableDescription;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.AnalyzeResult.SqlValueItem;
import com.bj58.oceanus.core.shard.AnalyzerCallback;
import com.bj58.oceanus.core.shard.DefaultRouteTarget;
import com.bj58.oceanus.core.shard.Function;
import com.bj58.oceanus.core.shard.HavingInfo;
import com.bj58.oceanus.core.shard.RouteTarget;
import com.bj58.oceanus.core.shard.ShardType;
import com.bj58.oceanus.core.shard.SqlExecuteInfo;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.core.utils.StringUtils;

/**
 * 路由：默认分发器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DefaultTargetDispatcher implements TargetDispatcher {

	@Override
	public Set<RouteTarget> dispatch(BatchItem batchItem) {
		processPreparedStatement(batchItem);// 对分裤分表需要的limit
											// offset需要重新修改offset的范围，拿出来再排序截取
		TableInfo tableInfo = RouteHelper
				.getOrgTables(batchItem.getAnalyzeResult().getTableInfos())
				.iterator().next();
		Configurations configurations = Configurations.getInstance();
		TableDescription desc = configurations.getTableDescription(tableInfo
				.getOrgName());
		if (desc == null) {// 没有在分库分表中进行配置
			return this.getDefaultTargets(tableInfo, batchItem);
		}

		if (desc.getShardType() == ShardType.NO_SHARD
				|| isRouteToAll(tableInfo, batchItem)) {
			return getAllTargets(tableInfo, batchItem);
		}

		return this.getSpecifyTargets(tableInfo, batchItem);// 路由到指定的分库分表
	}

	private void processPreparedStatement(BatchItem batchItem) {
		AnalyzeResult result = batchItem.getAnalyzeResult();
		SqlValueItem valueItem = result.getLimit();
		if (valueItem != null && valueItem.getParameterIndex() > 0) {// 必须是大于0的才处理
			ParameterCallback<?> parameterCallback = batchItem
					.getCallback(valueItem.getParameterIndex());
			if(parameterCallback==null){
				throw new IllegalArgumentException("setting PreparedStatement parameters error!ParameterIndex="+valueItem.getParameterIndex() );
			}
			Integer val = ((Number) parameterCallback.getParameter()).intValue();
			valueItem.setValue(val);
		}
		valueItem = result.getOffset();
		if (valueItem != null && valueItem.getParameterIndex() > 0) {
			ParameterCallback<?> parameterCallback = batchItem
					.getCallback(valueItem.getParameterIndex());
			if(parameterCallback==null){
				throw new IllegalArgumentException("setting PreparedStatement parameters error!ParameterIndex="+valueItem.getParameterIndex() );
			}
			Integer val = ((Number) parameterCallback.getParameter()).intValue();
			valueItem.setValue(val);
		}
	}

	Set<RouteTarget> getDefaultTargets(TableInfo tableInfo, BatchItem batchItem) {
		Configurations configurations = Configurations.getInstance();
		NameNodeHolder nameNodeHolder = configurations.findNameNode(tableInfo);
		if (nameNodeHolder == null) {
			return this.getNoShardTargets(tableInfo, batchItem);
		}

		return this.getVisualTargets(nameNodeHolder, tableInfo, batchItem);// 直接找到路由
	}

	/**
	 * 不走分库分表逻辑
	 * 
	 * @param tableInfo
	 * @param batchItem
	 * @return
	 */
	Set<RouteTarget> getNoShardTargets(TableInfo tableInfo, BatchItem batchItem) {

		Set<RouteTarget> targetSet = new LinkedHashSet<RouteTarget>();
		DefaultDataNode dataNode = new DefaultDataNode();
		ConnectionProvider provider = this.createConnectionProvider();
		dataNode.setConnectionProvider(provider);
		DataNodeHolder dataHolder = new DataNodeHolder(dataNode);
		DefaultNameNode nameNode = new DefaultNameNode();
		nameNode.addDataNode(dataHolder);
		nameNode.setLoadBanlance(LoadBanlance.RANDOM);
		NameNodeHolder nameNodeHolder = new NameNodeHolder(nameNode);
		nameNodeHolder.setOrgTableName(tableInfo.getOrgName());
		nameNodeHolder.setSchema(tableInfo.getSchema());
		DefaultRouteTarget target = this
				.createTarget(batchItem, nameNodeHolder);
		targetSet.add(target);
		SqlExecuteInfo info = new SqlExecuteInfo();
		info.setCallbacks(new LinkedHashSet<ParameterCallback<?>>(batchItem
				.getCallbacks()));
		info.setExecuteSql(batchItem.getSql());
		target.setExecuteInfo(info);

		return targetSet;
	}

	private ConnectionProvider createConnectionProvider() {
		ConnectionContext context = ConnectionContext.getContext();
		final Connection connection = context.getOrgConnection();
		ConnectionProvider provider = new ConnectionProvider() {

			@Override
			public Connection getConnection() throws SQLException {

				return connection;
			}

		};
		return provider;
	}

	/**
	 * 在分表中，直接指定某张表的情况
	 * 
	 * @param tableInfo
	 * @param batchItem
	 * @return
	 */
	Set<RouteTarget> getVisualTargets(NameNodeHolder nameNodeHolder,
			TableInfo tableInfo, BatchItem batchItem) {
		Set<RouteTarget> targetSet = new LinkedHashSet<RouteTarget>();
		DefaultRouteTarget target = this
				.createTarget(batchItem, nameNodeHolder);
		targetSet.add(target);
		SqlExecuteInfo info = new SqlExecuteInfo();
		info.setCallbacks(new LinkedHashSet<ParameterCallback<?>>(batchItem
				.getCallbacks()));
		info.setExecuteSql(batchItem.getSql());
		target.setExecuteInfo(info);

		return targetSet;
	}

	Set<RouteTarget> getAllTargets(TableInfo tableInfo, BatchItem batchItem) {
		Set<RouteTarget> targetSet = new LinkedHashSet<RouteTarget>();
		Configurations configurations = Configurations.getInstance();
		TableDescription desc = configurations.getTableDescription(tableInfo
				.getOrgName());
		List<NameNodeHolder> nameNodes = desc.getNameNodes();
		if (nameNodes == null || nameNodes.isEmpty()) {
			throw new ShardException("no sharding config support for table="
					+ tableInfo);
		}
		if ((!batchItem.getAnalyzeResult().getAppendResultColumns().isEmpty() || batchItem
				.getAnalyzeResult().getLimit() != null) && nameNodes.size() > 1) {// 存在limit或者avg等聚集函数,需要重新生成sql
			Collection<AnalyzerCallback> analyzerCallbacks = batchItem
					.getAnalyzeResult().getAnalyzerCallbacks();
			if (batchItem.getAnalyzeResult().getLimit() != null) {
				SqlValueItem limitItem = batchItem.getAnalyzeResult()
						.getLimit();
				SqlValueItem offsetItem = batchItem.getAnalyzeResult()
						.getOffset();
				if(offsetItem==null){
					offsetItem=new SqlValueItem();
					offsetItem.setValue(0);
				}
				if (limitItem.getParameterIndex() > 0
						&& offsetItem.getParameterIndex() > 0) {// limit ?,?
					Integer limitSize = limitItem.getValue()
							+ offsetItem.getValue();
					batchItem.getCallback(limitItem.getParameterIndex())
							.setParameter(limitSize);
					batchItem.getCallback(offsetItem.getParameterIndex())
							.setParameter(0);
					
					if(limitItem.getParameterIndex() > offsetItem.getParameterIndex()){
						batchItem.getCallback(offsetItem.getParameterIndex())
							.setParameterIndex(limitItem.getParameterIndex());
						
						batchItem.getCallback(limitItem.getParameterIndex())
							.setParameterIndex(offsetItem.getParameterIndex());
					}
					
				} else if (limitItem.getParameterIndex() > 0) {// limit 1,?
					Integer limitSize = limitItem.getValue()
							+ offsetItem.getValue();
					batchItem.getCallback(limitItem.getParameterIndex())
							.setParameter(limitSize);
				} else if (offsetItem.getParameterIndex() > 0) {// limit ?,10
					batchItem.getCallback(offsetItem.getParameterIndex())
							.setParameter(0);
				}
			}
			for (AnalyzerCallback callback : analyzerCallbacks) {
				callback.call();
			}
		}
		for (NameNode nameNode : nameNodes) {
			NameNodeHolder holder = (NameNodeHolder) nameNode;
			if (StringUtils.isNotBlank(tableInfo.getSchema())
					&& StringUtils.isNotBlank(holder.getSchema())) {// 带有schema的必须要保证table中的配置每个namenode项要和schema保持一致
				if (!tableInfo.getSchema().equalsIgnoreCase(holder.getSchema())) {
					continue;
				}
			}
			DefaultRouteTarget target = this.createTarget(batchItem, nameNode);
			targetSet.add(target);
		}

		
		if (nameNodes.size() > 0) {
			AnalyzeResult analyzeResult = batchItem.getAnalyzeResult();
			HavingInfo havingInfo = analyzeResult.getHavingInfo();
			if (havingInfo != null) {
				AnalyzerCallback callback = havingInfo.getCallback();
				if (callback != null) {
					callback.call();
				}
			}
		}
		for (RouteTarget item : targetSet) {
			DefaultRouteTarget target = (DefaultRouteTarget) item;
			SqlExecuteInfo info = new SqlExecuteInfo();
			info.setCallbacks(new LinkedHashSet<ParameterCallback<?>>(batchItem
					.getCallbacks()));

			if (desc.isDifferentName()) {
				info.setExecuteSql(configurations.getGenerator().generate(
						(NameNodeHolder) target.getNameNode(),
						batchItem.getAnalyzeResult()));
			} else if ((!batchItem.getAnalyzeResult().getAppendResultColumns().isEmpty() || batchItem
					.getAnalyzeResult().getLimit() != null) && nameNodes.size() > 1) {// 存在limit或者avg等聚集函数,需要重新生成sql,必须要超过1个路由结果
				info.setExecuteSql(configurations.getLimitAvgGenerator()
						.generate((NameNodeHolder) target.getNameNode(),
								batchItem.getAnalyzeResult()));
			} else {
				info.setExecuteSql(batchItem.getSql());
			}
			target.setExecuteInfo(info);
		}

		return targetSet;
	}

	private void checkParameters(Map<String, Object> parameters,
			BatchItem batchItem) {
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (entry.getValue() == null) {
				throw new ShardException(
						"sharded parameter must not null,column name=["
								+ entry.getKey() + "],sql="
								+ batchItem.getSql());
			}
		}
	}

	Set<RouteTarget> getSpecifyTargets(TableInfo tableInfo, BatchItem batchItem) {
		Set<RouteTarget> targetSet = new LinkedHashSet<RouteTarget>();
		Configurations configurations = Configurations.getInstance();

		/**
		 * 解析where中符合分库分表字段的值
		 */
		Map<String, List<TableColumn>> resolveColumns = RouteHelper
				.getResolveColumns(tableInfo.getOrgName(),
						batchItem.getAnalyzeResult());
		List<Map<String, Object>> parameters = RouteHelper
				.getParameterValues(resolveColumns);
		Set<Integer> indexs = new HashSet<Integer>();
		TableDescription desc = configurations.getTableDescription(tableInfo
				.getOrgName());
		List<NameNodeHolder> nameNodes = desc.getNameNodes();
		Function func = desc.getFunction();
		for (Map<String, Object> item : parameters) {
			checkParameters(item, batchItem);
			int i = func.execute(nameNodes.size(), item);
			indexs.add(i);
		}

		if (indexs.size() > 0) {
			AnalyzeResult analyzeResult = batchItem.getAnalyzeResult();
			HavingInfo havingInfo = analyzeResult.getHavingInfo();
			if (havingInfo != null) {
				AnalyzerCallback callback = havingInfo.getCallback();
				if (callback != null) {
					callback.call();
				}
			}
		}
		if ((!batchItem.getAnalyzeResult().getAppendResultColumns().isEmpty() || batchItem
				.getAnalyzeResult().getLimit() != null) && indexs.size() > 1) {// 存在limit或者avg等聚集函数,需要重新生成sql
			Collection<AnalyzerCallback> analyzerCallbacks = batchItem
					.getAnalyzeResult().getAnalyzerCallbacks();
			if (batchItem.getAnalyzeResult().getLimit() != null) {
				SqlValueItem limitItem = batchItem.getAnalyzeResult()
						.getLimit();
				SqlValueItem offsetItem = batchItem.getAnalyzeResult()
						.getOffset();
				if(offsetItem==null){
					offsetItem=new SqlValueItem();
					offsetItem.setValue(0);
				}
				if (limitItem.getParameterIndex() > 0
						&& offsetItem.getParameterIndex() > 0) {// limit ?,?
					Integer limitSize = limitItem.getValue()
							+ offsetItem.getValue();
					batchItem.getCallback(limitItem.getParameterIndex())
							.setParameter(limitSize);
					batchItem.getCallback(offsetItem.getParameterIndex())
							.setParameter(0);
					
					if(limitItem.getParameterIndex() > offsetItem.getParameterIndex()){
						batchItem.getCallback(offsetItem.getParameterIndex())
							.setParameterIndex(limitItem.getParameterIndex());
						
						batchItem.getCallback(limitItem.getParameterIndex())
							.setParameterIndex(offsetItem.getParameterIndex());
					}
				} else if (limitItem.getParameterIndex() > 0) {// limit 1,?
					Integer limitSize = limitItem.getValue()
							+ offsetItem.getValue();
					batchItem.getCallback(limitItem.getParameterIndex())
							.setParameter(limitSize);
				} else if (offsetItem.getParameterIndex() > 0) {// limit ?,10
					batchItem.getCallback(offsetItem.getParameterIndex())
							.setParameter(0);
				}
			}
			for (AnalyzerCallback callback : analyzerCallbacks) {
				callback.call();
			}
		} else {// 在单库路由的情况下，如果检测到是limit ?,?，就置换Parameter顺序
			SqlValueItem limitItem = batchItem.getAnalyzeResult()
					.getLimit();
			SqlValueItem offsetItem = batchItem.getAnalyzeResult()
					.getOffset();
			
			if(limitItem !=null && offsetItem !=null && 
					limitItem.getParameterIndex() > offsetItem.getParameterIndex()){
				
				batchItem.getCallback(offsetItem.getParameterIndex())
					.setParameterIndex(limitItem.getParameterIndex());
				
				batchItem.getCallback(limitItem.getParameterIndex())
					.setParameterIndex(offsetItem.getParameterIndex());
			}
		}
		for (Integer i : indexs) {// 生成target
			NameNode nameNode = configurations.getNameNode(
					tableInfo.getOrgName(), i);
			DefaultRouteTarget target = this.createTarget(batchItem, nameNode);

			targetSet.add(target);
		}

		
		for (RouteTarget item : targetSet) {
			DefaultRouteTarget target = (DefaultRouteTarget) item;
			SqlExecuteInfo info = new SqlExecuteInfo();
			info.setCallbacks(new LinkedHashSet<ParameterCallback<?>>(batchItem
					.getCallbacks()));

			if (desc.isDifferentName()) {
				info.setExecuteSql(configurations.getGenerator().generate(
						(NameNodeHolder) target.getNameNode(),
						batchItem.getAnalyzeResult()));
			} else if ((!batchItem.getAnalyzeResult().getAppendResultColumns().isEmpty() || batchItem
					.getAnalyzeResult().getLimit() != null) && nameNodes.size() > 1) {// 存在limit或者avg等聚集函数,需要重新生成sql,必须要超过1个路由结果
				info.setExecuteSql(configurations.getLimitAvgGenerator()
						.generate((NameNodeHolder) target.getNameNode(),
								batchItem.getAnalyzeResult()));
			} else {
				info.setExecuteSql(batchItem.getSql());
			}
			target.setExecuteInfo(info);
		}
		return targetSet;
	}

	DefaultRouteTarget createTarget(BatchItem batchItem, NameNode nameNode) {
		DefaultRouteTarget target = new DefaultRouteTarget(batchItem, nameNode);
		return target;
	}

	private boolean isRouteToAll(TableInfo tableInfo, BatchItem item) {

		/**
		 * 解析where中符合分库分表字段的值
		 */
		Map<String, List<TableColumn>> resolveColumns = RouteHelper
				.getResolveColumns(tableInfo.getOrgName(),
						item.getAnalyzeResult());
		boolean toAll = false;
		Iterator<List<TableColumn>> iterator = resolveColumns.values()
				.iterator();
		while (iterator.hasNext()) {
			List<TableColumn> columnValues = iterator.next();
			if (columnValues.isEmpty()) {
				toAll = true;
				break;
			}
		}
		return toAll;
	}
}
