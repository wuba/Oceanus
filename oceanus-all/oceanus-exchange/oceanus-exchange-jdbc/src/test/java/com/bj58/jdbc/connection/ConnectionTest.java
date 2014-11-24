package com.bj58.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.junit.Test;

import com.bj58.oceanus.config.Configurations;
import com.bj58.oceanus.exchange.jdbc.ConnectionWrapper;

public class ConnectionTest {

	static {
		Configurations.getInstance().init("/Users/luolishu/58source2/oceanus/oceanus-all/oceanus-exchange/oceanus-exchange-jdbc/src/test/resources/configuration.xml");
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
	public void testWrapper() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select * from t_user where  id in(1,2,4) and name='luolishu'";
		statement.executeQuery(sql);
	}

	@Test
	public void testSelect() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select * from showcase.t_user limit ?,?";
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1) + "--"
					+ resultSet.getString(2) + "--" + resultSet.getString(3));
		}
	}

	@Test
	public void testSelectNoSchema() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select * from t_user ";
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1) + "--"
					+ resultSet.getString(2) + "--" + resultSet.getString(3));
		}
	}

	@Test
	public void testSelectAggregate() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select avg(age) from t_user ";
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1));
		}
	}

	@Test
	public void testSelectDistinct() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select distinct (name),age from t_user ";
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1) + "--"
					+ resultSet.getInt(2));
		}
	}

	@Test
	public void testSelectCount() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select count(*),max(id),max(age),count(age),sum(age),avg(age)  from t_user";
		ResultSet resultSet = statement.executeQuery(sql);
		int count = 0;
		while (resultSet.next()) {
			int i = 1;
			System.out.println(resultSet.getInt(i++) + "=="
					+ resultSet.getInt(i++) + "==" + resultSet.getInt(i++)
					+ "==" + resultSet.getInt(i++) + "=="
					+ resultSet.getInt(i++) + "==" + resultSet.getInt(i++));
			count += resultSet.getInt(1);
		}
		System.out.println(count);
	}

	@Test
	public void testSelectVisual() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "select count(*) from t_user5  where id>1 ";
		ResultSet resultSet = statement.executeQuery(sql);
		int count = 0;
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1));
			count += resultSet.getInt(1);
		}
		System.out.println(count);
	}

	@Test
	public void testUpdate() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		Statement statement = wrapper.createStatement();
		String sql = "update t_user set name='helloworld2' where id=1 and name='helloworld'";
		int result = statement.executeUpdate(sql);
		System.out.println(result);
	}

	@Test
	public void testInsert() throws SQLException {
		Connection conn = this.getConnection();
		
		for (int i = 0; i < 10; i++) {
			ConnectionWrapper wrapper = new ConnectionWrapper(conn);
			Statement statement = wrapper.createStatement();
			String sql = "insert into t_user values(" + 1 + ",'test1" + 1
					+ "'," + 1 + "),(" + i + ",'test2" + i + "',24),(" + i
					+ ",'test3',25)";

			int result = statement.executeUpdate(sql);
			System.out.println(result);
			statement.close();
			wrapper.close();
		}

	}

	@Test
	public void testInsert2() throws SQLException {

		for (int i = 0; i < 1000; i++) {
			Connection conn = this.getConnection();
			ConnectionWrapper wrapper = new ConnectionWrapper(conn);
			Statement statement = wrapper.createStatement();
			String sql = "insert into t_user values(" + i + ",'test1" + i
					+ "'," + (i + 32) + "),(" + (i * 3) + ",'test2" + i
					+ "',24),(" + (i * 7) + ",'test3',25)";

			int result = statement.executeUpdate(sql);
			wrapper.close();
			System.out.println(result + "==" + i);
		}

	}

	@Test
	public void testPreparedInsert() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "insert into t_user values(1,?,?)";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setString(1, "mysqllllllllll");
		statement.setInt(2, 111);
		int result = statement.executeUpdate();
		System.out.println(result);
	}

	@Test
	public void testDelete() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "delete from t_user";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		// statement.setInt(1, 111);
		int result = statement.executeUpdate();
		System.out.println(result);
	}

	@Test
	public void testSelectPrepared() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_user where age=? and name=? and id=?";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		statement.setInt(1, 111);
		statement.setString(2, "helll");
		statement.setInt(3, 111);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.println(result.getInt(1));
			System.out.println(result.getString(2));
		}
	}

	@Test
	public void testBatch() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "insert into t_user values(?,?,?)";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		for (int i = 0; i < 2; i++) {
			statement.setInt(1, i);
			statement.setString(2, "new value" + i);
			statement.setInt(3, (int) ((i + 1) * Math.random() * 1000000));
			statement.addBatch();
		}
		int[] results = statement.executeBatch();
		System.out.println(Arrays.toString(results));
	}

	@Test
	public void testBatch2() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "insert into t_user values(1,'32432',3)";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		for (int i = 0; i < 20; i++) {
			sql = "insert into t_user values(" + i + ",'insert" + i + "'," + i
					+ ")";
			/*
			 * statement.setInt(1, i); statement.setString(2, "new value" + i);
			 * statement.setInt(3, (int) (i * Math.random() * 1000000));
			 */
			statement.addBatch(sql);
		}
		int[] results = statement.executeBatch();
		System.out.println(Arrays.toString(results));
	}

	@Test
	public void testSelectFind() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_user where name in (select name from t_user1 u1)";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)
					+ "---" + result.getString(3));

		}
	}

	@Test
	public void testSelectFindGoup() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_user order by id";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)
					+ "---" + result.getString(3));

		}
	}

	@Test
	public void testSelectFindGoup2() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select * from t_user order by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)
					+ "---" + result.getString(3));

		}
	}

	@Test
	public void testSelectCountAll() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select count(*),name from t_user order by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2));

		}
	}

	@Test
	public void testSelectCountGroupAll2() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select count(*),name from t_user group by name order by name";
		
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		int all=0;
		for (int i = 1; i <=17; i++) {
			sql = "select count(*),name from t_user"+i+" group by name order by name";
			PreparedStatement statement = conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			int count = 0;
			while (result.next()) {
				System.out.println(result.getInt(1) + "---"
						+ result.getString(2)+" t_user"+i+" count="+count);
				count += result.getInt(1);

			}
			System.out.println(count);
			all+=count;
		}
		System.out.println("all="+all);
		
	}

	@Test
	public void testSelectCountGroupAll() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select count(*),name from t_user group by name order by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testMax() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select sum(age),sum(Age),max(age),max(Age),min(age),min(Age),avg(age),avg(AGE) from t_user";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery(); 
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)+ "---" + result.getInt(3)+ "---" + result.getString(4)+ "---" + result.getString(5)+ "---" + result.getString(6)+ "---" + result.getString(7)+ "---" + result.getString(8));  
		} 
	}
	@Test
	public void testMax2() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select count(*),count(age),max(age),min(age),avg(age),avg(age),count(*) from t_user";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery(); 
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)+ "---" + result.getInt(3)+ "---" + result.getString(4)+ "---" + result.getString(5));  
		} 
	}
	@Test
	public void testMaxNum() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select count(*),count(age),max(age),min(age),avg(age),avg(age),count(*) from t_user where name='pppppp'";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery(); 
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)+ "---" + result.getInt(3)+ "---" + result.getString(4)+ "---" + result.getString(5));  
		} 
	}
	@Test
	public void testMaxAggregateGroup() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select sum(age),max(age),min(age),avg(age) avg2,name from t_user group by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery(); 
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)+ "---" + result.getInt(3)+ "---" + result.getString(4)+ "---" + result.getString(5));  
		} 
	}
	@Test
	public void testMaxGroupBy() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select sum(age),max(age),min(age),avg(age),name from t_user group by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery(); 
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2)+ "---" + result.getInt(3)+ "---" + result.getString(4)+ "---" + result.getString(5));  
		} 
	}
	@Test
	public void testAvg() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select avg(age),name from t_user group by name order by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getInt(1) + "---" + result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderBy() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age from t_user  order by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---" + result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testOrderByLimit() throws SQLException {
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
			System.out.println(result.getString(1) + "---" + result.getString(2));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	
	@Test
	public void testHaving() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,age,avg(age) from t_user  order by name";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---" + result.getString(2)+ "---" + result.getString(3));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
	@Test
	public void testHavingGroupBy() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);

		String sql = "select name,max(age),avg(age) from t_user  group by name having max(age)=25 or avg(age)=24";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		/*
		 * statement.setInt(1, 49); statement.setString(2, "new value49");
		 */
		ResultSet result = statement.executeQuery();
		int count = 0;
		while (result.next()) {
			System.out.println(result.getString(1) + "---" + result.getString(2)+ "---" + result.getString(3));
			count += result.getInt(1);

		}
		System.out.println(count);
	}
}
