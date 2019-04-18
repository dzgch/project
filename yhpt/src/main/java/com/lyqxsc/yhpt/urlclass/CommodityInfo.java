package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Commodity;

public class CommodityInfo {
	String userToken;
	Commodity commodity;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Commodity getCommodity() {
		return commodity;
	}
	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}
}
