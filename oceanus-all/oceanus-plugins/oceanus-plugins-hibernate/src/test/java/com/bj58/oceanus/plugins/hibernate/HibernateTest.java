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
import com.bj58.oceanus.plugins.hibernate.entity.UserDynamic;

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
	public void simpleTest() throws SQLException{
		SessionFactory sf = conf.buildSessionFactory();
		for(long uid=1; uid<999; uid++){
			Session session = sf.openSession();
			UserDynamic userDynamic = (UserDynamic) session.get(UserDynamic.class, Long.valueOf(uid));
			System.out.println(userDynamic);
			session.close();
		}
	}
	
	@Test
	public void transactionTest() throws SQLException{

	}
	
	public static void main(String[] args) throws SQLException {
		new HibernateTest().simpleTest();
	}

}
