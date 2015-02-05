/**
 * 
 */
package com.bj58.jdbc.parser.test;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.core.script.InterpretedScriptExecutor;
import com.bj58.oceanus.core.script.ScriptExecutor;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.HavingInfo;
import com.bj58.oceanus.core.shard.TableColumn;
import com.bj58.oceanus.exchange.builder.StatementHelper;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.AggregateNode;
import com.bj58.sql.parser.ColumnReference;
import com.bj58.sql.parser.CursorNode;
import com.bj58.sql.parser.NodeTypes;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.ResultColumn;
import com.bj58.sql.parser.ResultColumnList;
import com.bj58.sql.parser.SQLParser;
import com.bj58.sql.parser.SelectNode;
import com.bj58.sql.parser.StatementNode;
import com.bj58.sql.parser.ValueNode;
import com.bj58.sql.unparser.NodeToString;

/**
 * @author luolishu
 * 
 */
public class TestParser {
	NodeToString nodeToStr = new NodeToString();

	ResultColumn create(String columnName, String aggregateClaz,
			String aggregateName) throws Exception {
		ResultColumn resultColumn = new ResultColumn();
		AggregateNode aggregateNode = new AggregateNode();
		aggregateNode.setNodeType(NodeTypes.AGGREGATE_NODE);// SumAvgAggregateDefinition,MaxMinAggregateDefinition,CountAggregateDefinition
		aggregateNode.init(null, aggregateClaz, false, aggregateName);
		resultColumn.init(null, aggregateNode);
		resultColumn.setNodeType(NodeTypes.RESULT_COLUMN);
		if (columnName != null) {
			ColumnReference ref = new ColumnReference();
			ref.init(columnName, null);
			ref.setNodeType(NodeTypes.COLUMN_REFERENCE);
			aggregateNode.setOperand(ref);
		}

		return resultColumn;
	}

	@Test
	public void testAddResultColumn() throws Exception {
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser
				.parseStatement("select 1 from orders where id in(1,2)");
		ResultColumnList resultList = node.getResultSetNode()
				.getResultColumns();
		ResultColumn resultColumn = new ResultColumn();
		String aggregateClaz = "CountAggregateDefinition";
		String aggregateName = "COUNT";
		resultColumn = this.create("abc", aggregateClaz, aggregateName);
		resultList.addResultColumn(resultColumn);
		System.out.println(nodeToStr.toString(node));
		Assert.assertNotNull(node);
		node = (CursorNode) parser
				.parseStatement("select * from orders o join member m on o.order_id =m.order_id where id in(1,2) and order_id='helo' or abc='fdasfdas'");
		Assert.assertNotNull(node);

	}

	@Test
	public void test() throws Exception {
		SQLParser parser = new SQLParser();
		StatementNode node = parser
				.parseStatement("select * from orders where id in(1,2)");
		Assert.assertNotNull(node);
		node = parser
				.parseStatement("select * from orders o join member m on o.order_id =m.order_id where id in(1,2) and order_id='helo' or abc='fdasfdas'");
		Assert.assertNotNull(node);
		node = parser.parseStatement("delete from table1 where id=1");
		node = parser.parseStatement("insert into table2 values(1)");
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(node));
	}

	@Test
	public void testInsert() throws Exception {
		SQLParser parser = new SQLParser();
		StatementNode node = parser.parseStatement("insert into t values(1)");
		Assert.assertNotNull(node);
		node = parser
				.parseStatement("delete from orders where id between 1 and 10");
		Assert.assertNotNull(node);
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(node));
	}

	@Test
	public void testDelete() throws Exception {
		SQLParser parser = new SQLParser();
		StatementNode node = parser
				.parseStatement("delete from orders where name='' and id in(1,2)");
		Assert.assertNotNull(node);
		node = parser
				.parseStatement("delete from orders where id between 1 and 10");
		Assert.assertNotNull(node);
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(node));
	}

	@Test
	public void testJoin() throws Exception {
		SQLParser parser = new SQLParser();
		StatementNode node = parser
				.parseStatement("select * from table1 join table2 on t=a join table3 on t=b");
		Assert.assertNotNull(node);
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(node));
	}

	@Test
	public void testExist() throws Exception {
		SQLParser parser = new SQLParser();
		StatementNode node = parser
				.parseStatement("select * from table1 where exists (select 1 from dual)");
		Assert.assertNotNull(node);
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(node));
	}

	@Test
	public void testHaving() throws Exception {
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser
				.parseStatement("select count(*) from table1  having count(*) =1 and sum(age)>10 and count(name) and count(1)");
		Assert.assertNotNull(node);
		SelectNode selectNode = (SelectNode) node.getResultSetNode();
		ValueNode valueNode = selectNode.getHavingClause();
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node);
		AnalyzeResult result = analyzer.analyze(node);
		selectNode.setHavingClause(null);
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(valueNode));
		System.out.println(nodeToStr.toString(node));
	}
	
	@Test
	public void testHaving1() throws Exception {
		String sql = "SELECT AVG(age), MIN(age), MAX(age), age FROM t_user GROUP BY age HAVING age > 20 ORDER BY age DESC LIMIT 0, 15";
		SQLParser parser = new SQLParser();
		CursorNode node = (CursorNode) parser.parseStatement(sql);
		Assert.assertNotNull(node);
		SelectNode selectNode = (SelectNode) node.getResultSetNode();
		ValueNode valueNode = selectNode.getHavingClause();
		
		ScriptExecutor<Boolean> executor = new InterpretedScriptExecutor<Boolean>();
	}

	@Test
	public void testHavingExp() {
		String exp = "count(*) =1 and sum(age)>=10 and a<>b and count(name) and count(1)";
		System.out.println(getScriptExp(exp));
	}

	private String getScriptExp(String exp) {
		exp = exp.replaceAll("AND", "&&");
		exp = exp.replaceAll("OR", "||");
		StringBuilder sb = new StringBuilder();
		char[] chars = exp.toCharArray();
		for (int i = 0; i < chars.length; i++) {

			char c = chars[i];
			switch (c) {
			case '<':
				char pre = chars[i - 1],
				after = chars[i + 1];
				int j = i + 1;
				while (after == ' ') {// 排除空格
					after = chars[++j];
				}
				if (after == '>') {
					i=j;
					sb.append("!=");
					continue;
				}
				sb.append(c);
				break;
			case '=':
				pre = chars[i - 1];
				after = chars[i + 1];
				j = i - 1;
				while (pre == ' ') {
					pre = chars[--j];
				}
				if (pre == '>' || pre == '<') {// 排除空格,< =
					sb.append(c);
					continue;
				}
				sb.append(c);
				sb.append(c);// append 变成＝＝
				break;
			default:
				sb.append(c);

			}

		}

		return sb.toString();

	}

	@Test
	public void testOrderBy() throws Exception {
		SQLParser parser = new SQLParser();
		StatementNode node = parser
				.parseStatement("select count(*) from table1 join table2 on t=a join table3 on t=b order by name desc");
		Assert.assertNotNull(node);
		NodeToString nodeToStr = new NodeToString();
		System.out.println(nodeToStr.toString(node));
	}
	
	public static void main(String[] args) {
		String sql1 = "select * from t_userdynamic where uid>? or uid<? order by ver limit ?,?";
		String sql2 = "insert into users values(1, 'zhangsan', 14)";
		String sql3 = "delete from users where id = 2";
		String sql4	= "select * from users for update";
		String sql5 = "select 1+2";
		String sql6 = "select func1(id, age, name) from users";
		String sql7 = "select func1(id, age, name)";
		String sql8 = "{call proc1(?, ?, ?)}";
		String sql9 = "select * from usertable u, infotable i where u.uid=i.uid and u.uid=?";
		String sql10 = "SELECT * FROM CreditItem WHERE uid=? ORDER BY addtime desc LIMIT ?,?";
		String sql11 = "insert into users(id, name, age) values(1, 'zhangsan', 20)";
		String sql12 = "insert into users(id, name, age) values(1, 'zhangsan', 20),(2, 'lisi', 19)";
		
		
		
		String[] sqls = {sql1,sql2,sql3,sql4,sql5,sql6,sql7,sql8, sql9, sql10, sql11, sql12};
		
		
		for(String sql : sqls){
			SQLParser parser = StatementHelper.createSQLParser();
			try {
				StatementNode statementNode = parser.parseStatement(sql);
				System.err.println("sql: "+sql);
				System.out.println(statementNode);
				System.out.println(statementNode.getClass().getName());
				statementNode.treePrint();
				System.out.println("=========================================");
				
				NodeToString nodeToString = new NodeToString();
				
				System.out.println(nodeToString.toString(statementNode));
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				System.out.println("==============");
			}
		}
		
	}
}
