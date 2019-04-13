package com.lyqxsc.yhpt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  管理员实体类
 */
public class Admin {
	//ID
	long id;
	//userToken
	String userToken;
	//用户名
	String username;
	//密码
	@JsonIgnore
	String password;
	//真实姓名
	@JsonIgnore
	String realName;
	//性别
	@JsonIgnore
	String sex;
	//电话
	@JsonIgnore
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
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
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
	
}
