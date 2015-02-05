package com.bj58.oceanus.demo.sqlparser;

import com.bj58.oceanus.exchange.builder.StatementHelper;
import com.bj58.sql.parser.SQLParser;
import com.bj58.sql.parser.StatementNode;
import com.bj58.sql.unparser.NodeToString;

public class SqlParser {
	
	private static final String[] sqls = {
		"selEct * from t_user where uid=1 and name='张三' and age=?",
		"update t_user set uid=1,name=?,age=6",
		"sele/**/ct * from t_user limit 0,10",
		"select * from admin where 1=1 order by 5",
		"select * from admin where 1=1 union select 1,2,3,4,5",
		"select * from admin where 1=1 union select 1,2,3,4,5 from admin",
		"select version()>'4.0'",
		"select benchmark(md5(1),10000)"
	};
	
	public static void main(String[] args) {
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
