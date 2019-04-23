package com.lyqxsc.yhpt.domain;

import java.util.List;

public class WxHomePage {

	//首页图片3张
	String[] pic;

	//商品列表
	List<CommodityBak> commodityList;

	public String[] getPic() {
		return pic;
	}

	public void setPic(String[] pic) {
		this.pic = pic;
	}

	public List<CommodityBak> getCommodityList() {
		return commodityList;
	}

	public void setCommodityList(List<CommodityBak> commodityList) {
		this.commodityList = commodityList;
	}
}
