package com.bj58.oceanus.exchange.unparse.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bj58.oceanus.exchange.unparse.NodeTreeToSql;
import com.bj58.sql.parser.InsertNode;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.RowResultSetNode;
import com.bj58.sql.parser.RowsResultSetNode;
import com.bj58.sql.parser.SQLParser;

public class NodeTreeToSqlTest {
	@Test
	public void testToStr() throws Exception {
		String sql = "select distinct(name),test from table1 t";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeTreeToSql sqlTree=new NodeTreeToSql();
		System.out.println(sqlTree.toString(node)); 
	}
	
	@Test
	public void testInsertToStr() throws Exception {
		String sql = "insert into t values(1),(2)";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeTreeToSql sqlTree=new NodeTreeToSql();
		System.out.println(sqlTree.toString(node)); 
	}
	@Test
	public void testInsertToStr2() throws Exception {
		String sql = "insert into t(a) values(1),(2)";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		InsertNode insertNode=(InsertNode)node;
		RowsResultSetNode rowsNode=(RowsResultSetNode)insertNode.getResultSetNode();
		List<RowResultSetNode> rows=rowsNode.getRows();
		List<RowResultSetNode> tempRows=new ArrayList(rows);
		for(int i=0;i<tempRows.size();i++){
			rows.clear();
			rows.add(tempRows.get(i));
			NodeTreeToSql sqlTree=new NodeTreeToSql();
			System.out.println(sqlTree.toString(node)); 
		}
		
	}
}
