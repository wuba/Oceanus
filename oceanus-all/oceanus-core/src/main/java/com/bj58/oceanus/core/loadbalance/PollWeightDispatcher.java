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

import java.util.List;

import com.bj58.oceanus.core.context.StatementType;
import com.bj58.oceanus.core.context.StatementContext.BatchItem;
import com.bj58.oceanus.core.resource.DataNode;
import com.bj58.oceanus.core.resource.DataNodeHolder;
import com.bj58.oceanus.core.resource.NameNode;

/**
 * 分发器：带有权重的轮询
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class PollWeightDispatcher implements Dispatcher{
	
	@Override
	public DataNode dispatch(NameNode nameNode, BatchItem batchItem) {
		StatementType statementType = batchItem.getAnalyzeResult()
				.getStatementType();

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
		//排序，取出权重最高的一个dataNode  需要在取之后，改变weight的值，改变下次获取的排序结果
		long weight = 0;
		int selected = 0;
		for(int i = 0; i < dataNodes.size(); i++) {	
			if(dataNodes.get(i).getWeight() > weight) {
				weight = dataNodes.get(i).getWeight();
				selected = i;
			}
		}
		dataNode = dataNodes.get(selected);
		return dataNode;
	}
}
