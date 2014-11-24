package com.bj58.oceanus.exchange.nodes.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.SQLParser;

public class AnalyzersDeleteTest {
	@Test
	public void testDelete() throws Exception {
		String sql = "delete from table1 t";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals("table1", result.getTableInfos().iterator().next()
				.getOrgName());
		Assert.assertEquals("t", result.getTableInfos().iterator().next()
				.getName());
		Assert.assertEquals(0, result.getResultColumns().size());

	}

	@Test
	public void testDeleteWhere() throws Exception {
		String sql = "delete from table1 t where 1=1 and a=2 and t.c=3 and t.d in(1,2,3,?)";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals("table1", result.getTableInfos().iterator().next()
				.getOrgName());
		Assert.assertEquals("t", result.getTableInfos().iterator().next()
				.getName());
		Assert.assertEquals(0, result.getResultColumns().size());

		Assert.assertEquals(6, result.getConditionColumns().size());
		Iterator<TableColumn> iterator = result.getConditionColumns()
				.iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("?", column.getValue());

	}

	@Test
	public void testDeleteNexted() throws Exception {
		String sql = "delete from table1 t where 1=1 and a=2 and t.c=3 and t.d in(1,2,3,?) or name in (select name from users where a=5)";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Iterator<TableInfo> tableIterator = result.getTableInfos().iterator();

		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(2, result.getTableInfos().size());
		Assert.assertEquals(0, result.getResultColumns().size());

		TableInfo info = tableIterator.next();
		Assert.assertEquals("table1", info.getOrgName());
		Assert.assertEquals("t", info.getName());

		info = tableIterator.next();
		Assert.assertEquals("users", info.getOrgName());
		Assert.assertEquals("users", info.getName());
		
		Assert.assertEquals(7, result.getConditionColumns().size());
		Iterator<TableColumn> iterator = result.getConditionColumns()
				.iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("?", column.getValue());
		column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(5, column.getValue());
		System.out.println(column);

	}
}
