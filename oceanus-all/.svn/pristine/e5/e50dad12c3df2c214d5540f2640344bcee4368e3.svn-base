package com.bj58.oceanus.exchange.router.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bj58.oceanus.core.shard.TableColumn;

public class GetAllLeafTest {

	private void getLeaf(KeyValue parent,
			Iterator<Map.Entry<String, List<TableColumn>>> iterator,
			List<KeyValue> leaves) {
		if (!iterator.hasNext()) {
			return;
		}
		Map.Entry<String, List<TableColumn>> entry = iterator.next();
		if (!iterator.hasNext()) {
			String key = entry.getKey();
			List<TableColumn> columns = entry.getValue();
			for (TableColumn column : columns) {
				KeyValue kv = new KeyValue();
				kv.parent = parent;
				kv.key = key;
				kv.value = column.getValue();
				leaves.add(kv);
			}
			return;
		} else {
			String key = entry.getKey();
			List<TableColumn> columns = entry.getValue();
			List<Map.Entry<String, List<TableColumn>>> list = cloneIterator(iterator);
			for (int i = 0; i < columns.size(); i++) {
				TableColumn column = columns.get(i);
				KeyValue kv = new KeyValue();
				kv.parent = parent;
				kv.key = key;
				kv.value = column.getValue();
				this.getLeaf(kv, list.iterator(), leaves);
			}
		}

	}

	private List<Map.Entry<String, List<TableColumn>>> cloneIterator(
			Iterator<Map.Entry<String, List<TableColumn>>> iterator) {
		List<Map.Entry<String, List<TableColumn>>> results = new LinkedList<Map.Entry<String, List<TableColumn>>>();
		while (iterator.hasNext()) {
			results.add(iterator.next());
		}
		return results;

	}

	class KeyValue {
		KeyValue parent;
		String key;
		Object value;
		List<KeyValue> childs; 
	}

	public void toMap(Map<String, Object> values, KeyValue kv) {
		values.put(kv.key, kv.value);
		while(kv.parent!=null){
			kv=kv.parent;
			values.put(kv.key, kv.value);
		} 

	}

	List<TableColumn> create(int n, String name,String value) {
		List<TableColumn> columns = new ArrayList<TableColumn>();
		for (int i = 0; i < n; i++) {
			TableColumn column = new TableColumn();
			columns.add(column);
			column.setValue(value + i);
			column.setName(name); 
		}
		return columns;
	}

	@Test
	public void testLeafVisit() {
		String column = "column1";
		List<TableColumn> columns = create(5, column,"test");

		Map<String, List<TableColumn>> datas = new LinkedHashMap<String, List<TableColumn>>();
		datas.put(column, columns);

		column = "column2";
		columns = create(3, column,"value");
		datas.put(column, columns);
		/**column = "column3";
		columns = create(4, column,"alfa");
		datas.put(column, columns);**/
		List<KeyValue> leaves = new ArrayList<KeyValue>();
		Iterator<Map.Entry<String, List<TableColumn>>> it = datas.entrySet()
				.iterator();
		this.getLeaf(null, it, leaves);

		for (KeyValue kv : leaves) {
			Map<String, Object> values = new LinkedHashMap<String, Object>();
			this.toMap(values, kv);
			System.out.println(values);
		}

	}
}
