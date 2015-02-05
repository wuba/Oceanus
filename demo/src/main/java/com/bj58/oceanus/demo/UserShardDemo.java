package com.bj58.oceanus.demo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	public void insertTest() throws SQLException, IOException {
		for (long id = 1; id < 1001; id++) {
			Connection connection = null;
			PreparedStatement statement = null;
			String sql = "insert into t_user(id, uname, age) values (?, ?, ?)";
			try {
				connection = Oceanus.getConnection();
				statement = connection.prepareStatement(sql);
				statement.setLong(1, id);
				statement.setString(2, "name" + id);
				statement.setInt(3, RandomUtil.nextInt(10, 30));
				statement.execute();
			} catch(Exception e){
				e.printStackTrace();
			}finally {
				Oceanus.close(null, statement, connection);
			}
		}
	}

	@Test
	public void selectTest() throws SQLException {
		for (long id = 1; id < 1001; id++) {
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			String sql = "select * from t_user where id=?";
			try {
				connection = Oceanus.getConnection();
				statement = connection.prepareStatement(sql);
				statement.setLong(1, id);
				resultSet = statement.executeQuery();
			} catch(Exception e){
				e.printStackTrace();
			}finally {
				Oceanus.close(resultSet, statement, connection);
			}
		}
	}
	
	@Test
	public void selectTest1() throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "SELECT AVG(u.age), MIN(u.age), MAX(u.age), age FROM t_user u GROUP BY u.age HAVING MAX(u.age) > 20 ORDER BY u.age DESC LIMIT 0, 5";
		try {
			connection = Oceanus.getConnection();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				System.out.print(resultSet.getObject("AVG(u.age)") + "\t");
				System.out.print(resultSet.getObject("MIN(u.age)") + "\t");
				System.out.print(resultSet.getObject("MAX(u.age)") + "\t");
				System.out.println(resultSet.getObject("age"));
			}
		} finally {
			Oceanus.close(null, statement, connection);
		}
	}
	
	@Test
	public void updateTest() throws SQLException{
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = "update t_user set uname=?,age=? where id=?";
		try {
			connection = Oceanus.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, "zhangsan11");
			statement.setInt(2, 11);
			statement.setLong(3, 101L);
			int result = statement.executeUpdate();
			System.out.println(result);
		} finally {
			Oceanus.close(null, statement, connection);
		}
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
	public void after() throws SQLException{
//		deleteTest();
	}

}
