package com.lyqxsc.yhpt.domain;

public class RentOrder {
	//订单号
	String orderNumber;
	//租赁者
	long owner;
	//租赁者姓名
	String ownerName;
	//商品ID
	long rentCommodityID;
	//商品名称
	String rentCommodityName;
	//商品单价
	float price;
	//押金
	float deposit;
	//租赁数量
	int count;
	//总押金
	float totalDeposit;
	//总金额
	float totalPrice;
	//订单金额
	float orderPrice;
	//实际支付金额
	float payMoney;
	//支付完成时间
	long completeTime;
	//订单提交时间
	long makeOrdertime;
	//订单状态0未支付，1已支付，2已取消，3已冻结
	int status;
	//支付类型0微信
	int payType;
	//支付IP
	String payIP;
	//订单类型，1购买，2租赁
	int orderType;
	//处理进度，1已处理，2未处理
	int schedule;
	//收货地址
	String addr;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public long getRentCommodityID() {
		return rentCommodityID;
	}
	public void setRentCommodityID(long rentCommodityID) {
		this.rentCommodityID = rentCommodityID;
	}
	public String getRentCommodityName() {
		return rentCommodityName;
	}
	public void setRentCommodityName(String rentCommodityName) {
		this.rentCommodityName = rentCommodityName;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getDeposit() {
		return deposit;
	}
	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public float getTotalDeposit() {
		return totalDeposit;
	}
	public void setTotalDeposit(float totalDeposit) {
		this.totalDeposit = totalDeposit;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public float getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}
	public float getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(float payMoney) {
		this.payMoney = payMoney;
	}
	public long getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
	public long getMakeOrdertime() {
		return makeOrdertime;
	}
	public void setMakeOrdertime(long makeOrdertime) {
		this.makeOrdertime = makeOrdertime;
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
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public int getSchedule() {
		return schedule;
	}
	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
}
