package com.bj58.oceanus.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bj58.oceanus.core.shard.Function;
import com.bj58.oceanus.demo.shard.UserShardFunction;

public class ScaledOut {
	
	private static final Function shardFunction = new UserShardFunction();
	
	private static final int oldDbSize = 2;
	
	private static final int newDbSize = 16;
	
	private static final List<Map<String, Object>> params = new ArrayList<Map<String,Object>>();
	
	private static final Map<Integer, List<Integer>> indexMapping = new HashMap<Integer, List<Integer>>();
	
	static {
		for(int i=0; i<100; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", Long.valueOf(i));
			
			params.add(map);
		}
	}
	
	private static void addMapping(Integer oldIndex, Integer newIndex){
		if(!indexMapping.containsKey(oldIndex)){
			indexMapping.put(oldIndex, new ArrayList<Integer>());
		}
		
		if(!indexMapping.get(oldIndex).contains(newIndex)){
			indexMapping.get(oldIndex).add(newIndex);
		}
	}
	
	public static void main(String[] args) {
		for(Map<String, Object> paramMap : params){
			int oldIndex = shardFunction.execute(oldDbSize, paramMap);
			int newIndex = shardFunction.execute(newDbSize, paramMap);
			
			System.out.println(oldIndex + "  ==>  " + newIndex);
			addMapping(oldIndex, newIndex);
		}
		
		System.out.println("==============");
		
		for(Iterator<Entry<Integer, List<Integer>>> it = indexMapping.entrySet().iterator(); it.hasNext();){
			Entry<Integer, List<Integer>> entry = it.next();
			System.out.println(entry.getKey() + "  ==>  " + entry.getValue());
		}
		
	}

}
