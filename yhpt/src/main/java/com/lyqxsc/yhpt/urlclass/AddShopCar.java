package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.ShopCar;

public class AddShopCar {
	String userToken;
	ShopCar shopCar;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public ShopCar getShopCar() {
		return shopCar;
	}
	public void setShopCar(ShopCar shopCar) {
		this.shopCar = shopCar;
	}
}
