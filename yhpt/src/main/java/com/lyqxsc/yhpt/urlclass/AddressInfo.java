package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Address;

public class AddressInfo {
	String userToken;
	Address addr;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Address getAddr() {
		return addr;
	}
	public void setAddr(Address addr) {
		this.addr = addr;
	}
}
