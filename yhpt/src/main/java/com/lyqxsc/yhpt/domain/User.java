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
	//昵称
	String nikeName;
	//真实姓名
	@JsonIgnore
	String realName;
	//头像地址
	String headImgUrl;
	//邮箱
	String email;
	//手机号
	@JsonIgnore
	String phone;
	//性别
	@JsonIgnore
	String sex;
	//省份
	String province;
	//城市
	String city;
	//地址
	String address;
	//订单总数
	int totalOrderNum;
	//出售商品订单数
	int orderNum;
	//租赁商品订单数
	int rentOrderNum;
	//这次登录时间
	long thisLoginTime;
	//这次登录IP
	String thisLoginIP;
	//上次登录时间
	long lastLoginTime;
	//上次登录IP
	String lastLoginIP;
	//账户余额
	float wallet;
	//分销商
	long distributor;
	//注册时间
	long addTime;
	//权限
	int authority;
	
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
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
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
	public float getWallet() {
		return wallet;
	}
	public void setWallet(float wallet) {
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
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	public int getTotalOrderNum() {
		return totalOrderNum;
	}
	public void setTotalOrderNum(int totalOrderNum) {
		this.totalOrderNum = totalOrderNum;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public int getRentOrderNum() {
		return rentOrderNum;
	}
	public void setRentOrderNum(int rentOrderNum) {
		this.rentOrderNum = rentOrderNum;
	}
}
