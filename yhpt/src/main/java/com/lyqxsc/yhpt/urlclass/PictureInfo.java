package com.lyqxsc.yhpt.urlclass;

import org.springframework.web.multipart.MultipartFile;

public class PictureInfo {
	String userToken;
	MultipartFile pic;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public MultipartFile getPic() {
		return pic;
	}
	public void setPic(MultipartFile pic) {
		this.pic = pic;
	}
	
}
