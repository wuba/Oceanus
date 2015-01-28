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
package com.bj58.oceanus.core.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.DataNodeHolder;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.core.utils.RandomUtil;
import com.google.common.collect.Maps;

/**
 * 分发器：带有权重的随机
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RandomWeightDispatcher implements Dispatcher {
	
	private static final Map<String, Long> maxWeightMap = Maps.newConcurrentMap();
	private static final Map<String, List<WeightRangeHolder>> weightRangeHolderMap = Maps.newConcurrentMap();

	private static String getMapKey(NameNode nameNode, StatementType statementType){
		return nameNode.getId() + "_" + statementType.name();
	}
	
	private static long getNameNodeMaxWeight(NameNode nameNode, StatementType statementType, List<DataNodeHolder> dataNodes){
		String key = getMapKey(nameNode, statementType);
		
		if(!maxWeightMap.containsKey(key)){
			weightRangeHolderMap.put(key, new ArrayList<WeightRangeHolder>());
			long maxWeight = 1;
			for(DataNodeHolder dataNode : dataNodes){
				long min = maxWeight;
				maxWeight += dataNode.getWeight();
				long max = maxWeight;
				
				weightRangeHolderMap.get(key).add(new WeightRangeHolder(min, max, dataNode));
			}
			maxWeightMap.put(key, maxWeight);
		}
		return maxWeightMap.get(key);
	}
	
	private static DataNode getDataNodeByWeight(NameNode nameNode, StatementType statementType, long weight) {
		String key = getMapKey(nameNode, statementType);
		for(WeightRangeHolder weightRangeHolder : weightRangeHolderMap.get(key)){
			if(weightRangeHolder.matchWeight(weight))
				return weightRangeHolder.getDataNodeHolder();
		}
		return weightRangeHolderMap.get(key).get(0).getDataNodeHolder();
	}
	
	@Override
	public DataNode dispatch(NameNode nameNode, BatchItem batchItem) {
		StatementType statementType = batchItem.getAnalyzeResult().getStatementType();

		DataNode dataNode = null;
		List<DataNodeHolder> dataNodes = null;
		switch (statementType) {
		case SELECT:
			dataNodes = nameNode.getReadNodes();
			if (dataNodes.isEmpty()) {
				dataNodes = nameNode.getDataNodes();
			}
			break;
		case INSERT:
		case UPDATE:
		case DELETE:
			dataNodes = nameNode.getWriteNodes();
			if (dataNodes.isEmpty()) {
				dataNodes = nameNode.getDataNodes();
			}
			break;
		case BATCH:
			break;
		case CALLABLE:
			break;
		default:
			break;
		}
		
		long randomWeight = RandomUtil.nextLong(1L, getNameNodeMaxWeight(nameNode, statementType, dataNodes));
		
		dataNode = getDataNodeByWeight(nameNode, statementType, randomWeight);
		
		return dataNode;
	}
	
	private static class WeightRangeHolder {
		private long min;
		private long max;
		private DataNodeHolder dataNode;
		
		WeightRangeHolder(long min, long max, DataNodeHolder dataNode) {
			this.min = min;
			this.max = max;
			this.dataNode = dataNode;
		}
		
		public boolean matchWeight(long weight){
			return weight >= this.min && weight < this.max;
		}
		
		public DataNodeHolder getDataNodeHolder(){
			return this.dataNode;
		}

		@Override
		public String toString() {
			return "WeightRangeHolder [min=" + min + ", max=" + max
					+ ", dataNode=" + dataNode.getId() + "]";
		}
	}

}
