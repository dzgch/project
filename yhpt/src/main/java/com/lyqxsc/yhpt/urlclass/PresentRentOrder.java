package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.RentOrder;

public class PresentRentOrder {
	String userToken;
	String addr;
	RentOrder rentOrder;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public RentOrder getRentOrder() {
		return rentOrder;
	}
	public void setRentOrder(RentOrder rentOrder) {
		this.rentOrder = rentOrder;
	}
}
