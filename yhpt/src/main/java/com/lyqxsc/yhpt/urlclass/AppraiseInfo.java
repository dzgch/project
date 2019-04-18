package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Appraise;

public class AppraiseInfo {
	String userToken;
	Appraise appraise;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Appraise getAppraise() {
		return appraise;
	}
	public void setAppraise(Appraise appraise) {
		this.appraise = appraise;
	}
}
