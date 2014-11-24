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
package com.bj58.oceanus.config.factory;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.config.FunctionConfig;
import com.bj58.oceanus.config.NameNodeReferenceConfig;
import com.bj58.oceanus.config.TableConfig;
import com.bj58.oceanus.core.exception.ConfigurationException;
import com.bj58.oceanus.core.factory.ObjectFactory;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.resource.TableDescription;
import com.bj58.oceanus.core.shard.Function;
import com.bj58.oceanus.core.shard.Script;
import com.bj58.oceanus.core.shard.ScriptFunction;
import com.bj58.oceanus.core.shard.ShardType;

/**
 * Table配置工厂
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TableDescriptionFactory implements ObjectFactory<TableConfig> {

	@Override
	public Object create(TableConfig config) {
		TableDescription desc = new TableDescription(config.getName());
		for (NameNodeReferenceConfig ref : config.getReferenceList()) {
			NameNode nameNode = Configurations.getInstance().getNameNodeById(
					ref.getRef());
			if (nameNode == null) {
				throw new ConfigurationException(
						"namenode not found!could not find namenode id=["
								+ ref.getRef() + "],please check your table=["
								+ config.getName() + "] configuration!");
			}
			NameNodeHolder holder = new NameNodeHolder(nameNode);
			holder.setTableName(ref.getTableName());
			holder.setOrgTableName(ref.getOrgTableName());
			holder.setIndex(ref.getIndex());
			holder.setSchema(ref.getSchema());
			desc.addNameNode(holder);
		}
		
		desc.setShardType(config.getShardType());
		desc.setDifferentName(config.isDifferName());
		if(config.getShardType() == ShardType.NO_SHARD) {
			desc.setFunction(Function.NO_SHARD_FUNCTION);
			desc.setColumns(new String[]{});
		} else {
			Function function = createFunction(config.getFunction());
			desc.setFunction(function);
			desc.setColumns(config.getColumns());
		}
		return desc;
	}

	Function createFunction(FunctionConfig config) {
		Function func = null;
		if (config.getScript() != null) {
			func = createScriptFunction(config.getScript());
		} else {
			func = (Function) Configurations.getInstance().getBean(
					config.getRef());
		}
		return func;

	}

	private Function createScriptFunction(Script script) {
		return new ScriptFunction(script);
	}
}
