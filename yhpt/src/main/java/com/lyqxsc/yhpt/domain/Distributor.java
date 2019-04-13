package com.lyqxsc.yhpt.domain;

public class Distributor {
	//ID
	long id;
	//用户名
	String username;
	//密码
	String password;
	//真实姓名
	String realname;
	//性别
	int sex;
	//电话
	String phone;
	//这次登录IP
	String thisLoginIP;
	//这次登录时间
	long thisLoginTime;
	//上次登录IP
	String lastLoginIP;
	//上次登录时间
	long lastLoginTime;
	//注册时间
	long addTime;
	//权限
	String authority;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getThisLoginIP() {
		return thisLoginIP;
	}
	public void setThisLoginIP(String thisLoginIP) {
		this.thisLoginIP = thisLoginIP;
	}
	public long getThisLoginTime() {
		return thisLoginTime;
	}
	public void setThisLoginTime(long thisLoginTime) {
		this.thisLoginTime = thisLoginTime;
	}
	public String getLastLoginIP() {
		return lastLoginIP;
	}
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
