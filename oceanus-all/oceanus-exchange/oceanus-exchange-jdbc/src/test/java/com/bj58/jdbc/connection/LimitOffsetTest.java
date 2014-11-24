package com.bj58.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.exchange.jdbc.ConnectionWrapper;

public class LimitOffsetTest {

	static {
		Configurations
				.getInstance()
				.init("/Users/luolishu/58source2/oceanus/oceanus-all/oceanus-exchange/oceanus-exchange-jdbc/src/test/resources/configuration.xml");
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/showcase", "root", "123456");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testOrderByLimit1() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit ?,?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 1);
		statement.setInt(3, 30);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}

	@Test
	public void testOrderByLimit2() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit 1,?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		//statement.setInt(2, 1);
		statement.setInt(2, 30);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit3() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit ?,30";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 1);
		//statement.setInt(3, 30);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit4() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit ?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		//statement.setInt(2, 1);
		statement.setInt(2, 30);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit5() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit 1,30 ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49); 
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit6() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit ? offset ?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 30);
		statement.setInt(3, 1);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit7() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name offset ? limit ? ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 1);
		statement.setInt(3, 30);
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit8() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name offset 1 limit ? ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 30); 
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit9() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name offset ? limit 30 ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 1); 
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit10() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name offset 1 limit 30 ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		//statement.setInt(2, 1); 
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit11() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit 30 offset 1  ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		//statement.setInt(2, 1); 
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit99() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name offset ? limit ? ";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 1);
		statement.setInt(3, 30);
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit799() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user where id=? order by name limit ?,?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 49);
		statement.setInt(2, 1);
		statement.setInt(3, 30);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---"
					+ result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
}
