package com.bj58.shard;

import java.util.Map;
import java.util.Random;

import com.bj58.oceanus.core.shard.Function;

public class HashFunction implements Function {

	@Override
	public int execute(int size, Map<String, Object> parameters) {
		Random random = new Random();
		return random.nextInt(size)%3;
	}

}
