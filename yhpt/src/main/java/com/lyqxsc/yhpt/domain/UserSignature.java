package com.lyqxsc.yhpt.domain;

import java.util.Map;

/**
 * wx数据
 */
public class UserSignature {
	Map<String,String> signature;
	User user;
	String code;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
