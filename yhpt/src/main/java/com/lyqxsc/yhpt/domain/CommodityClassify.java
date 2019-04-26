package com.lyqxsc.yhpt.domain;

public class CommodityClassify {
	//类型 2大类 ： 1药剂 2器械
	int kind;
	//种类编号
	int classId;
	//种类值
	String classStr;
	
	
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getClassStr() {
		return classStr;
	}
	public void setClassStr(String classStr) {
		this.classStr = classStr;
	}
}
