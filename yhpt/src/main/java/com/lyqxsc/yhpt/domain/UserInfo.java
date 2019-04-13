package com.lyqxsc.yhpt.domain;

/**
 * 用为维护在线用户
 */
public class UserInfo {
	//id
	long id;
	//openid'username
	String username;
	//本次登录的IP
	String ip;
	//本次登录的时间
	long loginTime;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	
}
