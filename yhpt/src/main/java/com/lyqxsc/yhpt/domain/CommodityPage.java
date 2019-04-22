package com.lyqxsc.yhpt.domain;

import java.util.List;

public class CommodityPage {
	String[] pic;
	List<CommodityBak> commodity;
	
	public String[] getPic() {
		return pic;
	}
	public void setPic(String[] pic) {
		this.pic = pic;
	}
	public List<CommodityBak> getCommodity() {
		return commodity;
	}
	public void setCommodity(List<CommodityBak> commodity) {
		this.commodity = commodity;
	}
}
