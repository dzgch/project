package com.lyqxsc.yhpt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  用户实体类
 */
public class User {
	
	//id
	long id;
	//userToken
	String userToken;
	//openid
	@JsonIgnore
	String openID;
	//真实姓名
	@JsonIgnore
	String realname;
	//邮箱
	@JsonIgnore
	String email;
	//手机号
	@JsonIgnore
	String phone;
	//性别
	@JsonIgnore
	int sex;
	//省份
	String province;
	//城市
	String city;
	//地址
	@JsonIgnore
	String address;
	//这次登录时间
	long thisLoginTime;
	//这次登录IP
	String thisLoginIP;
	//上次登录时间
	long lastLoginTime;
	//上次登录IP
	String lastLoginIP;
	//账户余额
	String wallet;
	//分销商
	long distributor;
	//注册时间
	long addTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOpenID() {
		return openID;
	}
	public void setOpenID(String openID) {
		this.openID = openID;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getThisLoginTime() {
		return thisLoginTime;
	}
	public void setThisLoginTime(long thisLoginTime) {
		this.thisLoginTime = thisLoginTime;
	}
	public String getThisLoginIP() {
		return thisLoginIP;
	}
	public void setThisLoginIP(String thisLoginIP) {
		this.thisLoginIP = thisLoginIP;
	}
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIP() {
		return lastLoginIP;
	}
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	public String getWallet() {
		return wallet;
	}
	public void setWallet(String wallet) {
		this.wallet = wallet;
	}
	public long getDistributor() {
		return distributor;
	}
	public void setDistributor(long distributor) {
		this.distributor = distributor;
	}
	public long getAddTime() {
		return addTime;
	}
	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
