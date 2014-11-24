package com.bj58.jdbc.metadata.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class JdbcTest {
	
	public Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "123456");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Test
	public void getMetadata(){
		Connection conn=getConnection();
		try {
			DatabaseMetaData meta=conn.getMetaData();
			System.out.println(conn.getCatalog());
			System.out.println(meta.getDriverName());
			System.out.println(meta.getMaxStatements());
			System.out.println(meta.getURL()); 
			
			ResultSet result=meta.getSchemas();
			while(result.next()){
				for(int i=1;i<10;i++){
					try{
						System.out.println(result.getString(i));
					}catch(Exception e){
						break;
					}
				}
			}
		  
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void getCatelog(){
		Connection conn=getConnection();
		try {
			System.out.println(conn.getCatalog());
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void getSchemas(){}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
