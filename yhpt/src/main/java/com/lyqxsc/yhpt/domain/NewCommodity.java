package com.lyqxsc.yhpt.domain;

import java.util.List;

public class NewCommodity {
	List<String> pic;
	List<Commodity> commodity;
	public List<String> getPic() {
		return pic;
	}
	public void setPic(List<String> pic) {
		this.pic = pic;
	}
	public List<Commodity> getCommodity() {
		return commodity;
	}
	public void setCommodity(List<Commodity> commodity) {
		this.commodity = commodity;
	}
}
