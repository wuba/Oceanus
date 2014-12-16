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
import com.bj58.oceanus.exchange.jdbc.ProviderDesc;

/**
 * 读写分离的demo
 * 
 * 获取链接时使用Oceanus.getConnection(ProviderDesc) 这个方法，指定从哪个namenode中获取链接
 * 
 * 注：一个namenode中配置多个datanode，一个为WRITEONLY（只写），其余为READONLY(只读)
 * 
 * 这时对sql解析时根据读写类型进行路由，分发给不同的datanode节点
 * 
 */
public class UserRwDemo {
	
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
				connection = Oceanus.getConnection(new ProviderDesc() {
					@Override
					public String getNameNodeId() {
						return "user_source1";
					}
				});
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
			connection = Oceanus.getConnection(new ProviderDesc() {
				@Override
				public String getNameNodeId() {
					return "user_source1";
				}
			});
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
