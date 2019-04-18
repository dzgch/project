package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Order;

public class OrderInfo {
	String userToken;
	Order order;
	
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
