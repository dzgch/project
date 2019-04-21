package com.lyqxsc.yhpt.urlclass;

import com.lyqxsc.yhpt.domain.Coupon;

public class CouponInfo {
	String userToken;
	Coupon coupon;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
}
