package com.bj58.oceanus.demo;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.core.utils.RandomUtil;

/**
 * 分库分表demo
 * 
 */
public class UserShardDemo {
	
	private static boolean init = false;

	@Before
	public void before() {
		try {
			if(init)
				return;
			
			URL url = UserShardDemo.class.getClassLoader().getResource("configurations_demo.xml");
			File file = new File(url.toURI());
			// 进程启动时要对 Oceanus 进行初始化
			Oceanus.init(file.getAbsolutePath());
			init = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Test
	public void insertTest() throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		for (long id = 1; id < 1001; id++) {
			String sql = "insert into t_user(id, uname, age) values (?, ?, ?)";
			try {
				connection = Oceanus.getConnection();
				statement = connection.prepareStatement(sql);
				statement.setLong(1, id);
				statement.setString(2, "name" + id);
				statement.setInt(3, RandomUtil.nextInt(10, 30));
				statement.execute();
			} finally {
				Oceanus.close(null, statement, connection);
			}
		}
	}

	@Test
	public void selectTest() throws SQLException {
		
	}
	
	@Test
	public void deleteTest() throws SQLException{
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = "delete from t_user";
		try {
			connection = Oceanus.getConnection();
			statement = connection.prepareStatement(sql);
			statement.execute();
		} finally {
			Oceanus.close(null, statement, connection);
		}
	}

	@After
	public void after() {

	}

}
