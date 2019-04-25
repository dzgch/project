package com.lyqxsc.yhpt.domain;

public class Order {
	//订单号
	String orderNumber;
	//购买者
	long owner;
	//购买者名字
	String ownerName;
	//店家ID
	long distributorID;
	//商品
	long commodityID;
	//商品图片
	String url;
	//商品名称
	String commodityName;
	//商品单价
	float price;
	//购买数量
	int count;
	//总金额
	float totalPrice;
	//实际支付金额
	float payMoney;
	//订单金额
	float orderPrice;
	//支付完成时间
	long completeTime;
	//订单提交时间
	long payOrdertime;
	//订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	int status;
	//支付类型0微信
	int payType;
	//支付IP
	String payIP;
	//上一个交易状态 0待支付, 1已支付, 5交易完成, 6交易已取消
	int lastPayStatus;
	//收货地址id
	long addrId;
	//收货地址
	String addr;
	//取消理由
	String reason;
	public long getDistributorID() {
		return distributorID;
	}
	public void setDistributorID(long distributorID) {
		this.distributorID = distributorID;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public long getOwner() {
		return owner;
	}
	public void setOwner(long owner) {
		this.owner = owner;
	}
	public long getCommodityID() {
		return commodityID;
	}
	public void setCommodityID(long commodityID) {
		this.commodityID = commodityID;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public float getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(float payMoney) {
		this.payMoney = payMoney;
	}
	public float getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}
	public long getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
	public long getPayOrdertime() {
		return payOrdertime;
	}
	public void setPayOrdertime(long payOrdertime) {
		this.payOrdertime = payOrdertime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getPayIP() {
		return payIP;
	}
	public void setPayIP(String payIP) {
		this.payIP = payIP;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getLastPayStatus() {
		return lastPayStatus;
	}
	public void setLastPayStatus(int lastPayStatus) {
		this.lastPayStatus = lastPayStatus;
	}
	public long getAddrId() {
		return addrId;
	}
	public void setAddrId(long addrId) {
		this.addrId = addrId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
