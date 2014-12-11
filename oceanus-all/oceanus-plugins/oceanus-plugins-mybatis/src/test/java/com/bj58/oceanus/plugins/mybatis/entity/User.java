package com.bj58.oceanus.plugins.mybatis.entity;

import com.bj58.oceanus.client.orm.annotation.Column;

public class User {
	
	@Column(name="id")
	private long id;
	
	@Column(name="uname")
	private String uname;
	
	@Column(name="age")
	private int age;
	
	public User() {}
	
	public User(long id, String uname, int age) {
		this.id = id;
		this.uname = uname;
		this.age = age;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", uname=" + uname + ", age=" + age + "]";
	}

}
