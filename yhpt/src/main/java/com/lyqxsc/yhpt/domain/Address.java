package com.lyqxsc.yhpt.domain;

/*
 * 收货地址
 */
public class Address {
	//地址id
	long id;
	//用户id
	long userId;
	//收货人姓名
	String username;
	//收货人手机
	String phone; 
	//用户地址
	String addr;
	//默认地址
	int main;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getMain() {
		return main;
	}
	public void setMain(int main) {
		this.main = main;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
