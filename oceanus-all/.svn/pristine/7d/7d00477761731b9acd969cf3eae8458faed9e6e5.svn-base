package com.bj58.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.exchange.jdbc.ConnectionWrapper;

public class AvgTest {
	
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
	public void testAvg() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select avg(ver) from t_userdynamic";
		PreparedStatement statement = wrapper.prepareStatement(sql);
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

