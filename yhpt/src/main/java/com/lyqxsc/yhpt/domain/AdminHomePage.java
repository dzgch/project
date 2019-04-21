package com.lyqxsc.yhpt.domain;

import java.util.List;

public class AdminHomePage {
	//总销量
	int totalSales;
	//订单数
	int totalOrder;
	//关键词
	List<String>  keyword;
	//销售量
	List<Commodity> salesDay;
	//商品热销情况
	List<Commodity> salesHot;
	
	public int getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}
	public int getTotalOrder() {
		return totalOrder;
	}
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	public List<String> getKeyword() {
		return keyword;
	}
	public void setKeyword(List<String> keyword) {
		this.keyword = keyword;
	}
	public List<Commodity> getSalesDay() {
		return salesDay;
	}
	public void setSalesDay(List<Commodity> salesDay) {
		this.salesDay = salesDay;
	}
	public List<Commodity> getSalesHot() {
		return salesHot;
	}
	public void setSalesHot(List<Commodity> salesHot) {
		this.salesHot = salesHot;
	}
}
