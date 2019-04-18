package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Admin;

public class AdminInfo {
	String userToken;
	Admin admin;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}
