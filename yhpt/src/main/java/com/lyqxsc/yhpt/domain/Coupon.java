package com.lyqxsc.yhpt.domain;

public class Coupon {
	//id
	long id;
	//价格
	float price;
	//权限
	int authority;
	//起始时间
	long startTime;
	//结束时间
	long endTime;
	//使用条件，满减
	int condition;
	//剩余数量
	int number;
	//添加时间
	long addTime;
	//添加者
	long addPerson;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public long getAddPerson() {
		return addPerson;
	}
	public void setAddPerson(long addPerson) {
		this.addPerson = addPerson;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
}
