package com.lyqxsc.yhpt.urlclass;

import java.util.List;

import com.lyqxsc.yhpt.domain.Order;

public class OrderBatchInfo {
	String userToken;
	List<Order> order;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public List<Order> getOrder() {
		return order;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
}
