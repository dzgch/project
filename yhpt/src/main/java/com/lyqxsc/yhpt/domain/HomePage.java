package com.lyqxsc.yhpt.domain;

import java.util.List;

public class HomePage {

	//首页图片3张
	String pic1;
	String pic2;
	String pic3;
	
	//商品列表
	List<Commodity> commodityList;
	
	//租赁列表
	List<RentCommodity> rentCommodityList;

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public String getPic3() {
		return pic3;
	}

	public void setPic3(String pic3) {
		this.pic3 = pic3;
	}

	public List<Commodity> getCommodityList() {
		return commodityList;
	}

	public void setCommodityList(List<Commodity> commodityList) {
		this.commodityList = commodityList;
	}

	public List<RentCommodity> getRentCommodityList() {
		return rentCommodityList;
	}

	public void setRentCommodityList(List<RentCommodity> rentCommodityList) {
		this.rentCommodityList = rentCommodityList;
	}
	
}
