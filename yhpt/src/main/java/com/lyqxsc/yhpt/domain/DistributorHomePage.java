package com.lyqxsc.yhpt.domain;

import java.util.List;

public class DistributorHomePage {
	//收入
	float profit;
	//订单数
	int orderCount;
	//租赁订单数
	int rentOrderCount;
	//用户数
	int userCount;
	//返利
	List<RetProfit> retProfitList;
	
	public float getProfit() {
		return profit;
	}
	public void setProfit(float profit) {
		this.profit = profit;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public List<RetProfit> getRetProfitList() {
		return retProfitList;
	}
	public void setRetProfitList(List<RetProfit> retProfitList) {
		this.retProfitList = retProfitList;
	}
	public int getRentOrderCount() {
		return rentOrderCount;
	}
	public void setRentOrderCount(int rentOrderCount) {
		this.rentOrderCount = rentOrderCount;
	} 
}
