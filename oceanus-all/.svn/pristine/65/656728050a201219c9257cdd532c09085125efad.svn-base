package com.bj58.oceanus.exchange.nodes.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.SQLParser;
import com.bj58.sql.unparser.NodeToString;

public class AnalyzersSelectComplexTest {
	NodeToString nodeToStr = new NodeToString();
	@Test
	public void testDistinct() throws Exception {
		String sql = "select distinct(name),atest from table1 t";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Assert.assertEquals(true, result.isDistinct());

	}

	@Test
	public void testCount() throws Exception {
		String sql = "select count(name) as count1,atest from table1 t";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Iterator<TableColumn> columnsIt = result.getResultColumns().iterator();
		TableColumn column = columnsIt.next();
		Assert.assertEquals("count1", column.getAliasName());
		Assert.assertEquals("count".toUpperCase(), column.getAggregate());
		Assert.assertEquals("name", column.getName());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());
		sql = "select count(name),atest from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		columnsIt = result.getResultColumns().iterator();
		column = columnsIt.next();
		Assert.assertEquals("count".toUpperCase(), column.getAliasName());
		Assert.assertEquals("count".toUpperCase(), column.getAggregate());
		Assert.assertEquals("name", column.getName());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());
		sql = "select count(1),atest from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		columnsIt = result.getResultColumns().iterator();
		column = columnsIt.next();
		Assert.assertEquals("count".toUpperCase(), column.getAliasName());
		Assert.assertEquals("count".toUpperCase(), column.getAggregate());
		Assert.assertEquals(1, column.getValue());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());
		sql = "select count('abc'),atest from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		columnsIt = result.getResultColumns().iterator();
		column = columnsIt.next();
		Assert.assertEquals("count".toUpperCase(), column.getAliasName());
		Assert.assertEquals("count".toUpperCase(), column.getAggregate());
		Assert.assertEquals("abc", column.getValue());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());


	}
	@Test
	public void testCountAll() throws Exception {
		String sql = "select count(    *) as count1,atest from table1 t";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Iterator<TableColumn> columnsIt = result.getResultColumns().iterator();
		TableColumn column = columnsIt.next();
		Assert.assertEquals("count1", column.getAliasName());
		Assert.assertEquals("count(*)".toUpperCase(), column.getAggregate());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());
		sql = "select count(name) testcount,atest from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		columnsIt = result.getResultColumns().iterator();
		column = columnsIt.next();
		Assert.assertEquals("testcount", column.getAliasName());
		Assert.assertEquals("count".toUpperCase(), column.getAggregate());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());

	}

	@Test
	public void testMax() throws Exception {
		String sql = "select max(name1) as maxname,atest from table1 t";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Iterator<TableColumn> columnsIt = result.getResultColumns().iterator();
		TableColumn column = columnsIt.next();
		Assert.assertEquals("maxname", column.getAliasName());
		Assert.assertEquals("max".toUpperCase(), column.getAggregate());
		Assert.assertEquals("name1", column.getName());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex());
		sql = "select max(name) as maxname,min(age),atest from table1 t";
		node = parser.parseStatement(sql);
		analyzer = Analyzers.get(node.getNodeType());
		result = analyzer.analyze(node);
		columnsIt = result.getResultColumns().iterator();
		column = columnsIt.next();
		Assert.assertEquals("maxname", column.getAliasName());
		Assert.assertEquals("max".toUpperCase(), column.getAggregate());
		Assert.assertEquals("name", column.getName());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("min".toUpperCase(), column.getAliasName());
		Assert.assertEquals("min".toUpperCase(), column.getAggregate());
		Assert.assertEquals("age", column.getName());
		Assert.assertEquals(2, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(3, column.getResultIndex());

	}
	
	@Test
	public void testAverage() throws Exception {
		String sql = "select avg(name1) as avg1,atest,avg(name2) as avg2 from table1 t limit 10 offset 11";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Iterator<TableColumn> columnsIt = result.getResultColumns().iterator();
		TableColumn column = columnsIt.next();
		Assert.assertEquals("avg1", column.getAliasName());
		Assert.assertEquals("avg".toUpperCase(), column.getAggregate());
		Assert.assertEquals("name1", column.getName());
		Assert.assertEquals(1, column.getResultIndex());
		column = columnsIt.next();
		Assert.assertEquals("atest", column.getAliasName());
		Assert.assertEquals(null, column.getAggregate());
		Assert.assertEquals(2, column.getResultIndex()); 
		result.getAnalyzerCallbacks().iterator().next().call();
		System.out.println(nodeToStr.toString(node));

	}

}
