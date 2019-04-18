package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Collect;

public class CollectInfo {
	String userToken;
	Collect collect;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Collect getCollect() {
		return collect;
	}
	public void setCollect(Collect collect) {
		this.collect = collect;
	}
}
