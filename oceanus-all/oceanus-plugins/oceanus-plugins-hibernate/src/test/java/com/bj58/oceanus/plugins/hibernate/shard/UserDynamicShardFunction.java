package com.bj58.oceanus.plugins.hibernate.shard;

import java.util.Map;

import com.bj58.oceanus.core.shard.Function;

public class UserDynamicShardFunction implements Function {

	public int execute(int size, Map<String, Object> parameters) {
		long uid = Long.parseLong(parameters.get("UID").toString());
		return (int)(uid % size);
	}

}
