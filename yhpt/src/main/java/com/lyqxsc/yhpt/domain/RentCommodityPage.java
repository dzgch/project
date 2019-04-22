package com.lyqxsc.yhpt.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RentCommodityPage {
	List<String> pic;
	
	List<RentCommodity> rentCommodity;
	public List<String> getPic() {
		return pic;
	}
	public void setPic(List<String> pic) {
		this.pic = pic;
	}
	public List<RentCommodity> getRentCommodity() {
		return rentCommodity;
	}
	public void setRentCommodity(List<RentCommodity> rentCommodity) {
		this.rentCommodity = rentCommodity;
	}
}
