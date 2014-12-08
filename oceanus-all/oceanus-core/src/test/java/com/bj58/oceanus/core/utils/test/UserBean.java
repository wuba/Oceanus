package com.bj58.oceanus.core.utils.test;

import java.util.Arrays;

public class UserBean {
	
	private long uid;
	
	private String name;
	
	private double salary;
	
	private String[] addresses;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String[] getAddresses() {
		return addresses;
	}

	public void setAddresses(String[] addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "UserBean [uid=" + uid + ", name=" + name + ", salary=" + salary
				+ ", addresses=" + Arrays.toString(addresses) + "]";
	}

}
