package com.bj58.oceanus.plugins.mybatis;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.plugins.mybatis.datasource.OceanusDataSourceFactory;
import com.bj58.oceanus.plugins.mybatis.datasource.OceanusTransactionFactory;
import com.bj58.oceanus.plugins.mybatis.entity.UserDynamic;

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
	public void simpleTest() throws SQLException{
		while(true){
			for(long uid = 1; uid<9999; uid++){
				
				SqlSession session = sqlSessionFactory.openSession();
				try {
//					UserDynamic userDynamic = (UserDynamic) session.selectOne("com.bj58.oceanus.plugins.mybatis.entity.UserDynamicMapper.selectUserByID", uid);
//					System.out.println(userDynamic);
					
					int result = session.update("com.bj58.oceanus.plugins.mybatis.entity.UserDynamicMapper.updateUserByID", uid);
					System.out.println(result);
				} finally {
					session.close();
				}
			}
		}
	}
	
	@Test
	public void transactionTest() throws SQLException{
		Transaction transaction = null;
		while(true){
			for(long uid = 1; uid<10; uid++){
				SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
				TransactionFactory transactionFactory = new OceanusTransactionFactory();
				transaction = transactionFactory.newTransaction(session.getConnection());
				int result = -1;
				try {
					result = session.update("com.bj58.oceanus.plugins.mybatis.entity.UserDynamicMapper.updateUserByID", uid);
					System.out.println(result);
					uid++;
					
					result = session.update("com.bj58.oceanus.plugins.mybatis.entity.UserDynamicMapper.updateUserByID", uid);
					System.out.println(result);
					uid++;
					
					result = session.update("com.bj58.oceanus.plugins.mybatis.entity.UserDynamicMapper.updateUserByID", uid);
					System.out.println(result);
					uid++;
					
					result = session.update("com.bj58.oceanus.plugins.mybatis.entity.UserDynamicMapper.updateUserByID", uid);
					System.out.println(result);
					
					session.commit();
					transaction.commit();
					session.close();
				} catch(Exception e){
					e.printStackTrace();
					transaction.rollback();
				}finally {
					transaction.close();
				}
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
