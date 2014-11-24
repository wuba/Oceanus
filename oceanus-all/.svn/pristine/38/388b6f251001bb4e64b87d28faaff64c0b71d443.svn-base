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

public class AnalyzersUpdateTest {
	@Test
	public void testUpdate() throws Exception {
		String sql = "update table1 set a=1,b=? where id=1";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Iterator<TableInfo> tableIterator = result.getTableInfos().iterator();

		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals(2, result.getResultColumns().size());

		TableInfo info = tableIterator.next();
		Assert.assertEquals("table1", info.getOrgName());
		Assert.assertEquals("table1", info.getName());

		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("?", column.getValue());
		iterator = result.getConditionColumns().iterator();
		column = iterator.next();
		Assert.assertEquals("id", column.getName());
		Assert.assertEquals(1, column.getValue()); 

	}
	@Test
	public void testUpdate2() throws Exception {
		String sql = "update table1 t set a=1,b=? where id=1 and name in('a','b')";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Iterator<TableInfo> tableIterator = result.getTableInfos().iterator();

		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals(2, result.getResultColumns().size());

		TableInfo info = tableIterator.next();
		Assert.assertEquals("table1", info.getOrgName());
		Assert.assertEquals("t", info.getName());

		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("?", column.getValue());
		Assert.assertEquals(3, result.getConditionColumns().size());
		iterator = result.getConditionColumns().iterator();
		column = iterator.next();
		Assert.assertEquals("id", column.getName());
		Assert.assertEquals(1, column.getValue()); 
		column = iterator.next();
		Assert.assertEquals("name", column.getName());
		Assert.assertEquals("a", column.getValue()); 
		column = iterator.next();
		Assert.assertEquals("name", column.getName());
		Assert.assertEquals("b", column.getValue()); 

	}
}
