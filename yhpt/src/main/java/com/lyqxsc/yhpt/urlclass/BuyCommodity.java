package com.lyqxsc.yhpt.urlclass;

public class BuyCommodity {
	String userToken;
	long commodityID;
	int count;
	String ip;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public long getCommodityID() {
		return commodityID;
	}
	public void setCommodityID(long commodityID) {
		this.commodityID = commodityID;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
