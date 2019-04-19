package com.lyqxsc.yhpt.domain;

import java.util.Map;

public class UserSignature {
	Map<String,String> signature;
	User user;
	public Map<String, String> getSignature() {
		return signature;
	}
	public void setSignature(Map<String, String> signature) {
		this.signature = signature;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
