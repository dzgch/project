package com.lyqxsc.yhpt.domain;

public class RetProfit {
	//商品id
	long commodityID;
	//商品名称
	String commodityName;
	//日期
	long time;
	//销量
	int sales;
	//金额
	float price;
	public long getCommodityID() {
		return commodityID;
	}
	public void setCommodityID(long commodityID) {
		this.commodityID = commodityID;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
}
