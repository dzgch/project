package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Order;

public class PresentOrder {
	String userToken;
	String addr;
	Order order;
	
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
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
