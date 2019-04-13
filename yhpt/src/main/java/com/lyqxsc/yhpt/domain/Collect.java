package com.lyqxsc.yhpt.domain;

/**
 * 收藏夹 
 */
public class Collect {
	long id;
	long userid;
	long commodityid;
	String name;
	String picurl;
	float price;
	String note;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getCommodityid() {
		return commodityid;
	}
	public void setCommodityid(long commodityid) {
		this.commodityid = commodityid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
