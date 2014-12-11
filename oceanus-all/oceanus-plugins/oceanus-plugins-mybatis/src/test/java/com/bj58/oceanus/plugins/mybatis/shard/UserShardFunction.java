package com.bj58.oceanus.plugins.mybatis.shard;

import java.util.Map;

import com.bj58.oceanus.core.shard.Function;

public class UserShardFunction implements Function{

	@Override
	public int execute(int size, Map<String, Object> parameters) {
		long id = Long.valueOf(parameters.get("ID").toString());
		return (int)(id % size);
	}

}
