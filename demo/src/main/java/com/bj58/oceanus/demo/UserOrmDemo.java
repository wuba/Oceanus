package com.bj58.oceanus.demo;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.client.orm.BaseDao;
import com.bj58.oceanus.demo.entity.User;

/**
 * Oceanus对ORM的支持，数据访问层继承 BaseDao
 * 
 * 使用其中的  excute、excuteQuery、excuteUpdate 方法，不需要关注connection、statement、resultset的逻辑
 * 
 * excuteQuery 方法返回的是一个对象list，父类根据实体的注解进行映射结果，只支持一对一的映射
 */
public class UserOrmDemo extends BaseDao{
	
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
	public void selectTest() throws Exception {
		String sql = "select * from t_user where id=?";
		for(long id = 1; id<=100; id++){
			List<User> users = super.excuteQuery(User.class, sql, id);
			System.out.println(users);
		}
	}
	

	@After
	public void after() {

	}



}
