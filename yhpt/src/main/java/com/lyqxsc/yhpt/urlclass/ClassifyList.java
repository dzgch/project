package com.lyqxsc.yhpt.urlclass;

import java.util.List;

import com.lyqxsc.yhpt.domain.CommodityClassify;

public class ClassifyList {
	List<CommodityClassify> mechanical;
	List<CommodityClassify> agentia;
	public List<CommodityClassify> getMechanical() {
		return mechanical;
	}
	public void setMechanical(List<CommodityClassify> mechanical) {
		this.mechanical = mechanical;
	}
	public List<CommodityClassify> getAgentia() {
		return agentia;
	}
	public void setAgentia(List<CommodityClassify> agentia) {
		this.agentia = agentia;
	}
}
