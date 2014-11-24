package com.bj58.oceanus.exchange.builder.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.core.shard.TableInfo;
import com.bj58.oceanus.exchange.builder.StatementHelper;
import com.bj58.sql.parser.CursorNode;
import com.bj58.sql.parser.SQLParser;
import com.bj58.sql.parser.SelectNode;

public class StatementHelperTest {
	@Test
	public void testJoinTables() throws Exception {
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser
				.parseStatement("select * from orders o join shop s on o.id=s.oid join cart t on o.id=t.oid where id in(1,2)");
		SelectNode selNode=(SelectNode)node.getResultSetNode();
		List<TableInfo> tables=StatementHelper.getTableNames(selNode.getFromList());
		System.out.println(tables);
		Assert.assertEquals(3, tables.size());
		

	}
	@Test
	public void testSubQuery() throws Exception {
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser
				.parseStatement("select * from (select * from tablesub) as  o join shop s on o.id=s.oid join cart t on o.id=t.oid where id in(1,2)");
		SelectNode selNode=(SelectNode)node.getResultSetNode();
		List<TableInfo> tables=StatementHelper.getTableNames(selNode.getFromList());
		System.out.println(tables);
		Assert.assertEquals(3, tables.size());
		

	}
	@Test
	public void testWhereSubQuery() throws Exception {
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser
				.parseStatement("select * from (select * from tablesub) as  o join shop s on o.id=s.oid join cart t on o.id=t.oid where id in(1,2) and name in ( select name from users u)");
		SelectNode selNode=(SelectNode)node.getResultSetNode();
		List<TableInfo> tables=StatementHelper.getTableNames(selNode);
		System.out.println(tables);
		Assert.assertEquals(4, tables.size());
		

	}
	@Test
	public void testExist() throws Exception {
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser
				.parseStatement("select * from table1 where exists (select 1 from dual)");
		SelectNode selNode=(SelectNode)node.getResultSetNode();
		List<TableInfo> tables=StatementHelper.getTableNames(selNode);
		System.out.println(tables);
		Assert.assertEquals(2, tables.size());
		

	}
}
