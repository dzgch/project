package com.lyqxsc.yhpt.urlclass;

import java.util.List;

import com.lyqxsc.yhpt.domain.RentOrder;

public class RentOrderBatchInfo {
	String userToken;
	List<RentOrder> rentOrder;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public List<RentOrder> getRentOrder() {
		return rentOrder;
	}
	public void setRentOrder(List<RentOrder> rentOrder) {
		this.rentOrder = rentOrder;
	}
}
