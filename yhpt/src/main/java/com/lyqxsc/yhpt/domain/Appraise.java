package com.lyqxsc.yhpt.domain;

/**
 * 物品评价 
 */
public class Appraise {
	//评价ID
	long id;
	//评价用户ID
	long userID;
	//评价用户名
	long username;
	//商品ID
	String thingID;
	//评价内容
	String text;
	//评价等级
	int grade;
	//评价时间
	long time;
	//描述相符
	int describe;
	//物流服务
	int logistics;
	//服务态度
	int service;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public long getUsername() {
		return username;
	}
	public void setUsername(long username) {
		this.username = username;
	}
	public String getThingID() {
		return thingID;
	}
	public void setThingID(String thingID) {
		this.thingID = thingID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getDescribe() {
		return describe;
	}
	public void setDescribe(int describe) {
		this.describe = describe;
	}
	public int getLogistics() {
		return logistics;
	}
	public void setLogistics(int logistics) {
		this.logistics = logistics;
	}
	public int getService() {
		return service;
	}
	public void setService(int service) {
		this.service = service;
	}
}
