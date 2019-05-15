package com.lyqxsc.yhpt.dao;

public class SalesTabel {
	//ID
	Long id;
	//名称
	String name;
	//图片地址
	String picurl;
	//价格
	Float price;
	//租赁价格
	Float rentPrice;
	//分类 1租赁 2出售 3租赁出售
	Integer type;
	//销量日
	Integer salesVolumeDay;
	//销量月
	Integer salesVolumeMouth;
	//总销量
	Integer sales;
	//销售金额日
	Float salesPriceDay;
	//销售金额月
	Float salesPriceMouth;
	//销售金额总
	Float salesPriceTotal;
	//租赁量日
	Integer rentVolumeDay;
	//租赁量月
	Integer rentVolumeMouth;
	//总租赁量
	Integer rentVolumeTotal;
	//租赁金额日
	Float rentPriceDay;
	//租赁金额月
	Float rentPriceMouth;
	//租赁金额总
	Float rentPriceTotal;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getRentPrice() {
		return rentPrice;
	}
	public void setRentPrice(Float rentPrice) {
		this.rentPrice = rentPrice;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSalesVolumeDay() {
		return salesVolumeDay;
	}
	public void setSalesVolumeDay(Integer salesVolumeDay) {
		this.salesVolumeDay = salesVolumeDay;
	}
	public Integer getSalesVolumeMouth() {
		return salesVolumeMouth;
	}
	public void setSalesVolumeMouth(Integer salesVolumeMouth) {
		this.salesVolumeMouth = salesVolumeMouth;
	}
	public Integer getSales() {
		return sales;
	}
	public void setSales(Integer sales) {
		this.sales = sales;
	}
	public Float getSalesPriceDay() {
		return salesPriceDay;
	}
	public void setSalesPriceDay(Float salesPriceDay) {
		this.salesPriceDay = salesPriceDay;
	}
	public Float getSalesPriceMouth() {
		return salesPriceMouth;
	}
	public void setSalesPriceMouth(Float salesPriceMouth) {
		this.salesPriceMouth = salesPriceMouth;
	}
	public Float getSalesPriceTotal() {
		return salesPriceTotal;
	}
	public void setSalesPriceTotal(Float salesPriceTotal) {
		this.salesPriceTotal = salesPriceTotal;
	}
	public Integer getRentVolumeDay() {
		return rentVolumeDay;
	}
	public void setRentVolumeDay(Integer rentVolumeDay) {
		this.rentVolumeDay = rentVolumeDay;
	}
	public Integer getRentVolumeMouth() {
		return rentVolumeMouth;
	}
	public void setRentVolumeMouth(Integer rentVolumeMouth) {
		this.rentVolumeMouth = rentVolumeMouth;
	}
	public Integer getRentVolumeTotal() {
		return rentVolumeTotal;
	}
	public void setRentVolumeTotal(Integer rentVolumeTotal) {
		this.rentVolumeTotal = rentVolumeTotal;
	}
	public Float getRentPriceDay() {
		return rentPriceDay;
	}
	public void setRentPriceDay(Float rentPriceDay) {
		this.rentPriceDay = rentPriceDay;
	}
	public Float getRentPriceMouth() {
		return rentPriceMouth;
	}
	public void setRentPriceMouth(Float rentPriceMouth) {
		this.rentPriceMouth = rentPriceMouth;
	}
	public Float getRentPriceTotal() {
		return rentPriceTotal;
	}
	public void setRentPriceTotal(Float rentPriceTotal) {
		this.rentPriceTotal = rentPriceTotal;
	}
}
