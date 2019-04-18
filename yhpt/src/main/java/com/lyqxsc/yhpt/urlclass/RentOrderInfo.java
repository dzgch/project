package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.RentOrder;

public class RentOrderInfo {
	String userToken;
	RentOrder rentOrder;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public RentOrder getRentOrder() {
		return rentOrder;
	}
	public void setRentOrder(RentOrder rentOrder) {
		this.rentOrder = rentOrder;
	}
}
