package com.lyqxsc.yhpt.domain;

import java.util.List;

public class HomePage {

	//首页图片3张
	String[] pic;

	//商品列表
	List<Commodity> commodityList;

	public String[] getPic() {
		return pic;
	}

	public void setPic(String[] pic) {
		this.pic = pic;
	}

	public List<Commodity> getCommodityList() {
		return commodityList;
	}

	public void setCommodityList(List<Commodity> commodityList) {
		this.commodityList = commodityList;
	}
}
