package com.bj58.oceanus.plugins.hibernate;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.core.utils.RandomUtil;
import com.bj58.oceanus.plugins.hibernate.entity.User;

public class HibernateTest {
	
	private static Configuration conf = new Configuration();

	static {
		try {
			URL url = HibernateTest.class.getClassLoader().getResource("configurations_demo.xml");
			File file = new File(url.toURI());
			// 进程启动时要对 Oceanus 进行初始化
			Oceanus.init(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		conf.configure();
		
	}
	
	@Test
	public void selectTest() throws SQLException{
		SessionFactory sf = conf.buildSessionFactory();
		for(long id=1; id<99; id++){
			Session session = sf.openSession();
			User user = (User) session.get(User.class, Long.valueOf(id));
			System.out.println(user);
			session.close();
		}
	}
	
	@Test
	public void transactionTest() throws SQLException{
		SessionFactory sf = conf.buildSessionFactory();
		Session session = null;
		Transaction transaction = null;
		for(long uid=1; uid<99; uid++){
			try {
				session = sf.openSession();
				transaction = session.beginTransaction();
				
				User user = new User(uid, "uname"+uid, RandomUtil.nextInt(10, 30));
				
				session.merge(user);
				transaction.commit();
			} catch (Exception e){
				transaction.rollback();
			} finally {
				session.close();
			}
		}
	}
	
}
