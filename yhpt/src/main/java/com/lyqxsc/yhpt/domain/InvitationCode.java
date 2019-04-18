package com.lyqxsc.yhpt.domain;

public class InvitationCode {
	//分销商
	long distributorID;
	//用户
	long userID;
	//邀请码
	String code;
	//是否绑定用户
	int isBind;
	
	public long getDistributorID() {
		return distributorID;
	}
	public void setDistributorID(long distributorID) {
		this.distributorID = distributorID;
	}
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getIsBind() {
		return isBind;
	}
	public void setIsBind(int isBind) {
		this.isBind = isBind;
	}
}
