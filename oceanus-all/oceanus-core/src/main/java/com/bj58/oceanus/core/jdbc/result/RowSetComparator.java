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

import java.util.Comparator;

import com.bj58.oceanus.core.exception.ShardException;
import com.bj58.oceanus.core.shard.OrderByType;
import com.bj58.oceanus.core.shard.TableColumn;
import com.google.common.collect.ComparisonChain;

/**
 * RowSet 比较器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RowSetComparator implements Comparator<RowSet> {
	
	private final TableColumn sortedColumns[];

	public RowSetComparator(TableColumn sortedColumns[]) {
		this.sortedColumns = sortedColumns;
	}

	@Override
	public int compare(RowSet o1, RowSet o2) {
		ComparisonChain chain = ComparisonChain.start();
		try {
			for (int i = 0; i < sortedColumns.length; i++) {
				TableColumn column = sortedColumns[i];
				OrderByType orderByType = column.getOrderByType();
				Object target1 = o1.getObject(column.getResultIndex());
				Object target2 = o2.getObject(column.getResultIndex());
				if (target1 == null) {// 将null值转化为string进行处理
					target1 = "";
					if (target2 != null) {
						target2 = target2.toString();
					} else {
						target2 = "";
					}
				} else if (target2 == null) {
					target2 = "";
					target1 = target1.toString();
				}

				if (orderByType == null || orderByType == OrderByType.ASC) {
					chain=chain.compare((Comparable<?>) target1, (Comparable<?>) target2);
				} else {
					chain=chain.compare((Comparable<?>) target2, (Comparable<?>) target1);
				}

			}
		} catch (Exception e) {
			throw new ShardException("compare error!row=" + this, e);
		}
		return chain.result();
	}

}
