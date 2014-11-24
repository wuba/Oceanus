package com.bj58.oceanus.exchange.unparse.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bj58.oceanus.core.resource.DataNodeHolder;
import com.bj58.oceanus.core.resource.LoadBanlance;
import com.bj58.oceanus.core.resource.NameNode;
import com.bj58.oceanus.core.resource.NameNodeHolder;
import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.core.shard.ShardQueryGenerator;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.oceanus.exchange.unparse.DefaultShardQueryGenerator;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.SQLParser;

public class ShardQueryGeneratorTest {
	ShardQueryGenerator generator = new DefaultShardQueryGenerator();

	private NameNode createNameNode(final int i) {
		NameNode nameNode = new NameNode() {
			Map<String, String> tablesMap = new HashMap<String, String>();
			{
				tablesMap.put("member".toUpperCase(), "member" + i);
				tablesMap.put("orders".toUpperCase(), "orders" + i);
				tablesMap.put("customer".toUpperCase(), "customer" + i);
				tablesMap.put("users".toUpperCase(), "users" + i);
			}
 

			 
			@Override
			public List<DataNodeHolder> getDataNodes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<DataNodeHolder> getWriteNodes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<DataNodeHolder> getReadNodes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataNodeHolder remove(DataNodeHolder dataNode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LoadBanlance getLoadBanlance() {
				// TODO Auto-generated method stub
				return null;
			}
 
		};
		NameNodeHolder holder=new NameNodeHolder(nameNode);
		return holder;
	}

	@Test
	public void testSelect() throws Exception {
		NameNode nameNode = createNameNode(1);
		String sql = "select t.a as aa,t.b,c,d dd from member t join orders t2 on t.a=t2.b join customer t3 on t2.aa=t3.dd where a=1 and b in(2,3) and 1=1 and p=? AND name in (select name from users u where u!='hello' and 1=age)";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		String generateSql = generator.generate((NameNodeHolder) nameNode, result);

		System.out.println(generateSql);
		
		result = analyzer.analyze(node);
		nameNode = createNameNode(2);
		generateSql = generator.generate((NameNodeHolder)nameNode, result);

		System.out.println(generateSql);
		result = analyzer.analyze(node);
		nameNode = createNameNode(3);
		generateSql = generator.generate((NameNodeHolder)nameNode, result);

		System.out.println(generateSql);
	}
}
