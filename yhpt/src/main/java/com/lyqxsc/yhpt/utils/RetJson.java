package com.lyqxsc.yhpt.utils;

public class RetJson {
	
	int code;
	String msg;
	Object data;
	
	public static int CODE_SUCCESS	= 1;
	public static int CODE_ERROR_URL = -1;
	public static int CODE_ERROR_CON = -2;
	public static int CODE_ERROR_UNK = -3;
	
	public RetJson(int code, String msg,Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public static RetJson success(String msg) {
		return new RetJson(CODE_SUCCESS, msg, null);
	}
	
	public static RetJson success(String msg,Object data) {
		return new RetJson(CODE_SUCCESS, msg, data);
	}
	
	public static RetJson urlError(String msg,Object data) {
		return new RetJson(CODE_ERROR_URL, msg, data);
	}
	
	public static RetJson mysqlError(String msg,Object data) {
		return new RetJson(CODE_ERROR_CON, msg, data);
	}

	public static RetJson unknowError(String msg,Object data) {
		return new RetJson(CODE_ERROR_UNK, msg, data);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
