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
	//分销商id
	long distributor;
	//分销商等级
	int grade;
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
	public long getDistributor() {
		return distributor;
	}
	public void setDistributor(long distributor) {
		this.distributor = distributor;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
}
