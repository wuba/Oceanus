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
package com.bj58.oceanus.exchange.unparse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.ShardQueryGenerator;
import com.bj58.sql.StandardException;
import com.bj58.sql.parser.QueryTreeNode;

/**
 * LimitAggregateQueryGenerator
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class LimitAggregateQueryGenerator implements ShardQueryGenerator {
	static Logger logger = LoggerFactory
			.getLogger(LimitAggregateQueryGenerator.class);
	NodeTreeToSql sqlGenerator = new NodeTreeToSql();

	@Override
	public String generate(NameNodeHolder nameNode, AnalyzeResult analyzeResult) {
		try {
			String sql = sqlGenerator.toString((QueryTreeNode) analyzeResult
					.getTreeNode());
			return sql;
		} catch (StandardException e) {
			logger.error("sql StandardException,limit generate!", e);
		}
		// TODO Auto-generated method stub
		return null;
	}

}
