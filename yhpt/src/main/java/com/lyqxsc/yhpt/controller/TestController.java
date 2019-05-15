package com.lyqxsc.yhpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.ITest;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.Test;

@RestController
@CrossOrigin
public class TestController {
	@Autowired
	ITest testdao;
	
	@RequestMapping(value = "/test")
	public String testt() {
		Test commodity = new Test();
		commodity.setName("成都");
		testdao.addCommodity(commodity);
		return "aa";
	}
	
}
