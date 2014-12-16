package com.bj58.oceanus.demo.entity;

import java.util.Date;

public class Product {
	
	private long id;
	
	private String sn;

	private Date sale;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Date getSale() {
		return sale;
	}

	public void setSale(Date sale) {
		this.sale = sale;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", sn=" + sn + ", sale=" + sale + "]";
	}
	
}
