package com.lyqxsc.yhpt.urlclass;

import org.springframework.web.multipart.MultipartFile;

import com.lyqxsc.yhpt.domain.Commodity;

public class CommodityInfo {
	String userToken;
//	Commodity commodity;
	String name;
	MultipartFile pic;
	
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
//	public Commodity getCommodity() {
//		return commodity;
//	}
//	public void setCommodity(Commodity commodity) {
//		this.commodity = commodity;
//	}
	
	public MultipartFile getPic() {
		return pic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPic(MultipartFile pic) {
		this.pic = pic;
	}
}
