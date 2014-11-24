package com.bj58.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.exchange.jdbc.ConnectionWrapper;

public class ShardLimitOffsetTest {
	
	static {
		Configurations
				.getInstance()
				.init("d:/configurations_demo.xml");
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/showcase", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void testOrderByLimit1() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid=? order by ver limit ?,?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setInt(2, 4);
		statement.setInt(3, 8);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}

	@Test
	public void testOrderByLimit2() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver limit ?,8";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		statement.setInt(3, 4);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	@Test
	public void testOrderByLimit3() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver limit 4,?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		statement.setInt(3, 8);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	@Test
	public void testOrderByLimit4() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver limit ?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		statement.setInt(3, 4);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	
	@Test
	public void testOrderByLimit6() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver limit ? offset ?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		statement.setInt(3, 4);
		statement.setInt(4, 8);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	
	@Test
	public void testOrderByLimit7() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver offset ? limit ?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		statement.setInt(3, 4);
		statement.setInt(4, 8);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	@Test
	public void testOrderByLimit8() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver offset 4 limit ?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		statement.setInt(3, 8);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}

	@Test
	public void testOrderByLimit11() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_userdynamic where uid>? or uid<? order by ver limit 8 offset 4";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setLong(1, 2000);
		statement.setLong(2, 1000);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	

}