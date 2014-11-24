package com.bj58.oceanus.exchange.nodes.test;

import org.junit.Assert;
import org.junit.Test;

import com.bj58.oceanus.core.shard.AnalyzeResult;
import com.bj58.oceanus.exchange.nodes.Analyzers;
import com.bj58.oceanus.exchange.nodes.NodeAnalyzer;
import com.bj58.sql.parser.QueryTreeNode;
import com.bj58.sql.parser.SQLParser;

public class AnalyzersShardUtilTest {
	@Test
	public void testResolve() throws Exception {
		String sql = "select distinct(name),atest from table1 t where t.id=1 and userName=2";
		SQLParser parser = new SQLParser();
		QueryTreeNode node = parser.parseStatement(sql);
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(node.getNodeType());
		AnalyzeResult result = analyzer.analyze(node);
		Assert.assertEquals(true, result.isDistinct());
 

	}

}
