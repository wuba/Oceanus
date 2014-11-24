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

public class AnalyzersInsertTest {
	@Test
	public void testInsert() throws Exception {
		String sql = "insert into users(c1,c2) values(1,'a')";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getTableInfos());
		Assert.assertEquals(1, result.getTableInfos().size());
		Iterator<TableInfo> tableInfos = result.getTableInfos().iterator();
		TableInfo tableInfo = tableInfos.next();
		Assert.assertEquals("users", tableInfo.getOrgName());
		Assert.assertEquals("users", tableInfo.getName());
		Assert.assertEquals(2, result.getResultColumns().size());
		Iterator<TableColumn> columnsIterator = result.getResultColumns()
				.iterator();
		TableColumn column = columnsIterator.next();
		Assert.assertEquals("c1", column.getName());
		Assert.assertEquals(1, column.getValue());
		column = columnsIterator.next();
		Assert.assertEquals("c2", column.getName());
		Assert.assertEquals("a", column.getValue());
		/**
		 * sql = "insert into users values(1,'a')"; node =
		 * parser.parseStatement(sql); result = analyzer.analyze(node);
		 * Assert.assertNotNull(result);
		 * Assert.assertNotNull(result.getTableInfos()); Assert.assertEquals(1,
		 * result.getTableInfos().size()); tableInfos =
		 * result.getTableInfos().iterator(); tableInfo = tableInfos.next();
		 * Assert.assertEquals("users", tableInfo.getOrgName());
		 * Assert.assertEquals("users", tableInfo.getName());
		 * Assert.assertEquals(2, result.getResultColumns().size());
		 * Assert.assertEquals("*", result.getResultColumns().iterator().next()
		 * .getName()); Assert.assertEquals("t",
		 * result.getResultColumns().iterator().next() .getTable());
		 **/
	}
}
