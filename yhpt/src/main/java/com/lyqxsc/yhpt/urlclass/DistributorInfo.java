package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Distributor;

public class DistributorInfo {
	String userToken;
	Distributor distributor;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Distributor getDistributor() {
		return distributor;
	}
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
}
