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

public class AnalyzersTest {
	@Test
	public void testSelect() throws Exception {
		String sql = "select t.* from table1 t";
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
		Assert.assertEquals(1, result.getResultColumns().size());
		Assert.assertEquals("*", result.getResultColumns().iterator().next()
				.getName());
		Assert.assertEquals("t", result.getResultColumns().iterator().next()
				.getTable());
		sql = "select t.a as aa,t.b,c,d dd from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals("table1", result.getTableInfos().iterator().next()
				.getOrgName());
		Assert.assertEquals("t", result.getTableInfos().iterator().next()
				.getName());
		Assert.assertEquals(4, result.getResultColumns().size());
		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("aa", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("b", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals("c", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("dd", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		sql = "select * from orders where id in(1,2)";

	}
	@Test
	public void testSelectLimit() throws Exception {
		String sql = "select t.* from table1 t limit 10 offset 1";
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
		Assert.assertEquals(1, result.getResultColumns().size());
		Assert.assertEquals("*", result.getResultColumns().iterator().next()
				.getName());
		Assert.assertEquals("t", result.getResultColumns().iterator().next()
				.getTable());
		sql = "select t.a as aa,t.b,c,d dd from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals("table1", result.getTableInfos().iterator().next()
				.getOrgName());
		Assert.assertEquals("t", result.getTableInfos().iterator().next()
				.getName());
		Assert.assertEquals(4, result.getResultColumns().size());
		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("aa", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("b", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals("c", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("dd", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		sql = "select * from orders where id in(1,2)";

	}

	@Test
	public void testSelectWhere() throws Exception {
		String sql = "select t.a as aa,t.b,c,d dd from table1 t where a=1 and b in(2,3) and 1=1 and p=?";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);

		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Assert.assertEquals("table1", result.getTableInfos().iterator().next()
				.getOrgName());
		Assert.assertEquals("t", result.getTableInfos().iterator().next()
				.getName());
		Assert.assertEquals(4, result.getResultColumns().size());
		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("aa", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("b", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals("c", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("dd", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		iterator = result.getConditionColumns().iterator();
		
		Assert.assertEquals(4, result.getConditionColumns().size());
		column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("p", column.getName());
		Assert.assertEquals("?", column.getValue());
		Assert.assertEquals(0,column.getPreparedIndex().intValue());
	}
	@Test
	public void testSelectJoin() throws Exception {
		String sql = "select t.a as aa,t.b,c,d dd from table1 t join table2 t2 on t.a=t2.b join table3 t3 on t2.aa=t3.dd where a=1 and b in(2,3) and 1=1 and p=?";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);

		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(3, result.getTableInfos().size());
		Iterator<TableInfo> tableIterator=result.getTableInfos().iterator();
		TableInfo tableInfo=tableIterator.next();
		Assert.assertEquals("table1",tableInfo.getOrgName());
		Assert.assertEquals("t",tableInfo.getName());
		tableInfo=tableIterator.next();
		Assert.assertEquals("table2",tableInfo.getOrgName());
		Assert.assertEquals("t2",tableInfo.getName());
		tableInfo=tableIterator.next();
		Assert.assertEquals("table3",tableInfo.getOrgName());
		Assert.assertEquals("t3",tableInfo.getName());
		Assert.assertEquals(4, result.getResultColumns().size());
		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("aa", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("b", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals("c", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("dd", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		iterator = result.getConditionColumns().iterator();
		
		Assert.assertEquals(6, result.getConditionColumns().size());
		column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("t", column.getTable());
		Assert.assertEquals("b", ((TableColumn)column.getValue()).getName());
		Assert.assertEquals("t2", ((TableColumn)column.getValue()).getTable());
		column = iterator.next();
		Assert.assertEquals("aa", column.getName());
		Assert.assertEquals("t2", column.getTable());
		Assert.assertEquals("dd", ((TableColumn)column.getValue()).getName());
		Assert.assertEquals("t3", ((TableColumn)column.getValue()).getTable());
		column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("p", column.getName());
		Assert.assertEquals("?", column.getValue());
		Assert.assertEquals(0,column.getPreparedIndex().intValue());
	}
	@Test
	public void testSelectNested() throws Exception{
		String sql = "select t.a as aa,t.b,c,d dd from table1 t join table2 t2 on t.a=t2.b join table3 t3 on t2.aa=t3.dd where a=1 and b in(2,3) and 1=1 and p=? AND name in (select name from users u where u!='hello' and 1=age)";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);

		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(4, result.getTableInfos().size());
		Iterator<TableInfo> tableIterator=result.getTableInfos().iterator();
		TableInfo tableInfo=tableIterator.next();
		Assert.assertEquals("table1",tableInfo.getOrgName());
		Assert.assertEquals("t",tableInfo.getName());
		tableInfo=tableIterator.next();
		Assert.assertEquals("table2",tableInfo.getOrgName());
		Assert.assertEquals("t2",tableInfo.getName());
		tableInfo=tableIterator.next();
		Assert.assertEquals("table3",tableInfo.getOrgName());
		Assert.assertEquals("t3",tableInfo.getName());
		Assert.assertEquals(4, result.getResultColumns().size());
		Iterator<TableColumn> iterator = result.getResultColumns().iterator();
		TableColumn column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("aa", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals("b", column.getAliasName());
		Assert.assertEquals("t", column.getTable());
		column = iterator.next();
		Assert.assertEquals("c", column.getName());
		Assert.assertEquals("c", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		column = iterator.next();
		Assert.assertEquals("d", column.getName());
		Assert.assertEquals("dd", column.getAliasName());
		Assert.assertEquals(null, column.getTable());
		iterator = result.getConditionColumns().iterator();
		
		Assert.assertEquals(7, result.getConditionColumns().size());
		column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals("t", column.getTable());
		Assert.assertEquals("b", ((TableColumn)column.getValue()).getName());
		Assert.assertEquals("t2", ((TableColumn)column.getValue()).getTable());
		column = iterator.next();
		Assert.assertEquals("aa", column.getName());
		Assert.assertEquals("t2", column.getTable());
		Assert.assertEquals("dd", ((TableColumn)column.getValue()).getName());
		Assert.assertEquals("t3", ((TableColumn)column.getValue()).getTable());
		column = iterator.next();
		Assert.assertEquals("a", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals(2, column.getValue());
		column = iterator.next();
		Assert.assertEquals("b", column.getName());
		Assert.assertEquals(3, column.getValue());
		column = iterator.next();
		Assert.assertEquals("p", column.getName());
		Assert.assertEquals("?", column.getValue());
		Assert.assertEquals(0,column.getPreparedIndex().intValue());
		column = iterator.next();
		Assert.assertEquals("age", column.getName());
		Assert.assertEquals(1, column.getValue());
	}
}
