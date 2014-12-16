package com.bj58.oceanus.demo;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bj58.oceanus.client.Oceanus;
import com.bj58.oceanus.client.orm.BaseDao;
import com.bj58.oceanus.demo.entity.Product;

/**
 * 同一DB服务器中有多个schema时，为了能够共用连接池，可以只配一个datanode，table的分库规则用by_table。
 * 
 * table标签的属性：
 * 	differ-name设置为true
 * 	shard-type设置为BY_TABLE
 * 
 * 子节点namenode列表中，为每个 namenode指定各自的schema+tablename
 * 
 * 这样当sql解析时就可以对sql进行重组，路由到不同的schema中
 */
public class ProductSchemaDemo extends BaseDao{
	
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
	public void insertTest() throws Exception {
		for (long id = 1; id < 1001; id++) {
			String sql = "insert into t_product(id, sn, sale) values (?, ?, ?)";
			super.excute(sql, id, "sn_" + id, new Date());
		}
	}

	@Test
	public void selectTest() throws Exception {
		String sql = "select * from t_product where id=?";
		for(long id = 1; id<=100; id++){
			List<Product> products = super.excuteQuery(Product.class, sql, id);
			System.out.println(products);
		}
	}
	
	@Test
	public void deleteTest() throws Exception{
		String sql = "delete from t_product";
		super.excute(sql);
	}

	@After
	public void after() {

	}

}
