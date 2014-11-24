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
package com.bj58.oceanus.exchange.nodes;

import java.io.IOException;
import java.lang.reflect.Modifier;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.QueryTreeNode;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

/**
 * SQL节点解析器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class Analyzers {
	@SuppressWarnings("unchecked")
	static final NodeAnalyzer<QueryTreeNode, AnalyzeResult> analizerArr[] = new NodeAnalyzer[NodeTypes.MAX_NODE_TYPE];
	static {
		initAnalizers();
	}

	@SuppressWarnings("unchecked")
	private static void initAnalizers() {

		ImmutableSet<ClassInfo> classInfos = null;
		try {
			classInfos = ClassPath.from(Analyzers.class.getClassLoader())
					.getTopLevelClassesRecursive(
							Analyzers.class.getPackage().getName());
			for (ClassInfo classInfo : classInfos) {
				Class<?> claz = classInfo.load();
				try {

					if (NodeAnalyzer.class.isAssignableFrom(claz)
							&& !claz.isInterface()
							&& !Modifier.isAbstract(claz.getModifiers())) {
						register((NodeAnalyzer<QueryTreeNode, AnalyzeResult>) claz
								.newInstance());
					}
				} catch (Exception e) {
					// TODO LOG Auto-generated catch block
					e.printStackTrace();
					System.out.println(claz);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private static void register(
			NodeAnalyzer<QueryTreeNode, AnalyzeResult> analizer) {
		if (analizer.getNodeTypes() == null
				|| analizer.getNodeTypes().length <= 0) {
			return;
		}
		for (int nodeType : analizer.getNodeTypes()) {
			analizerArr[nodeType] = analizer;
		}

	}

	public static NodeAnalyzer<QueryTreeNode, AnalyzeResult> get(int type) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer= analizerArr[type];
		if(analyzer==null){
			//TODO log
			System.out.println("no analyzer found!nodeType="+type);
		}
		return analyzer;
	}
	public static NodeAnalyzer<QueryTreeNode, AnalyzeResult> get(QueryTreeNode node) {
		return get(node.getNodeType());
	}

	public static void main(String args[]) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analizer = Analyzers
				.get(NodeTypes.SELECT_NODE);
		System.out.println(analizer.getClass() + " nodeType="
				+ analizer.getNodeTypes() + " input=" + NodeTypes.SELECT_NODE);

	}
}
