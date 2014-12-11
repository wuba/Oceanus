package com.bj58.oceanus.plugins.mybatis;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.core.utils.RandomUtil;
import com.bj58.oceanus.plugins.mybatis.entity.User;

public class MybatisTest {

	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	static {
		try {
			URL url = MybatisTest.class.getClassLoader().getResource("configurations_demo.xml");
			File file = new File(url.toURI());
			// 进程启动时要对 Oceanus 进行初始化
			Oceanus.init(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			reader = Resources.getResourceAsReader("mybatis_config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void insertTest() throws SQLException {
		for(long id=1; id<=100; id++){
			SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
			int result = -1;
			try {
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				id++;
				
				result = session.insert("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.insertUser", new User(id, "uname"+id, RandomUtil.nextInt(10, 30)));
				System.out.println(result);
				
				session.commit();
			} catch(Exception e){
				e.printStackTrace();
				session.rollback();
			}finally {
				session.close();
			}
		}
	}
	
	@Test
	public void deleteTest() throws SQLException {
		SqlSession session = sqlSessionFactory.openSession(true);
		session.delete("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.deleteUser");
		session.close();
	}
	
	@Test
	public void selectTest() throws SQLException {
		for(long id=1; id<=100; id++){
			SqlSession session = sqlSessionFactory.openSession(true);
			try {
				User user = session.selectOne("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.selectUserByID", Long.valueOf(id));
				System.out.println(user);
			} catch(Exception e){
				e.printStackTrace();
			}finally {
				session.close();
			}
		}
	}
	
	@Test
	public void selectLimitTest() throws SQLException{
		int pageSize = 10;
		int pageNumber = 1;
		for(;pageNumber<=10; pageNumber++){
			int startIndex = (pageNumber-1) * pageSize;
			Map<String, Integer> params = new HashMap<String, Integer>();
			params.put("startIndex", startIndex);
			params.put("pageSize", pageSize);
			
			SqlSession session = sqlSessionFactory.openSession(true);
			try {
				List<User> users = session.selectList("com.bj58.oceanus.plugins.mybatis.entity.UserMapper.selectUserLimit", params);
				System.out.println(users);
			} catch(Exception e){
				e.printStackTrace();
			}finally {
				session.close();
			}
		}
	}
	
	
//	@Test
//	public void simpleTest() throws SQLException{
//		while(true){
//			for(long id = 1; id<9999; id++){
//				
//				SqlSession session = sqlSessionFactory.openSession();
//				try {
//					User user = (User) session.selectOne("com.bj58.oceanus.plugins.mybatis.entity.User.selectUserByID", id);
//					System.out.println(user);
//					
////					int result = session.update("com.bj58.oceanus.plugins.mybatis.entity.User.updateUserByID", id);
////					System.out.println(result);
//				} finally {
//					session.close();
//				}
//			}
//		}
//	}
	

}
