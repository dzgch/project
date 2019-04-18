package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.User;

public class UserInfo {
	String userToken;
	User user;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
