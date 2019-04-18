package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Address;

public class AddressInfo {
	String userToken;
	Address address;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
