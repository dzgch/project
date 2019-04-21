package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.Distributor;

public class AdminInfo {
	String userToken;
	Admin admin;
	Distributor distributor;
	
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
	public Distributor getDistributor() {
		return distributor;
	}
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
}
