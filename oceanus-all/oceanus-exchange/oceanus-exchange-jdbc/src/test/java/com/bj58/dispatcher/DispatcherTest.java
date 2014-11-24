package com.bj58.dispatcher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.bj58.oceanus.core.context.ConnectionContext;
import com.bj58.oceanus.core.context.StatementContext;
import com.bj58.oceanus.exchange.builder.DefaultStatementContextBuilder;
import com.bj58.oceanus.exchange.builder.StatementContextBuilder;
import com.bj58.oceanus.exchange.executors.Executor;
import com.bj58.oceanus.exchange.jdbc.ConnectionWrapper;
import com.bj58.oceanus.exchange.router.Router;
import com.bj58.oceanus.exchange.router.RouterFactory;

public class DispatcherTest {
	
	@Test
	public void dispathcer() throws SQLException{
		String sql = "select * from usertable u, infotable i where u.uid=i.uid and u.uid=?";
		ConnectionContext connectionContext = new ConnectionContext(new ConnectionWrapper(null), null);
		ConnectionContext.setContext(connectionContext);
		StatementContextBuilder builder = new DefaultStatementContextBuilder();
		StatementContext context = builder.build(sql,StatementContext.getContext());
		Router router = RouterFactory.createRouter(context);
		// TODO event && parameters
		Executor<ResultSet> executor = (Executor<ResultSet>) router.route(context);
	}

}

