package com.lyqxsc.yhpt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.service.DistributorService;
import com.lyqxsc.yhpt.urlclass.AdminInfo;
import com.lyqxsc.yhpt.urlclass.CommodityInfo;
import com.lyqxsc.yhpt.urlclass.DistributorInfo;
import com.lyqxsc.yhpt.urlclass.PasswordLogin;
import com.lyqxsc.yhpt.urlclass.UserToken;
import com.lyqxsc.yhpt.urlclass.UserTokenOne;
import com.lyqxsc.yhpt.urlclass.UserTokenTwo;
import com.lyqxsc.yhpt.utils.RetJson;

@RestController
@CrossOrigin
public class DistributorController {

	@Autowired
	DistributorService distributorService;
	
	/**
	 *  注册
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/distributor/sigup", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson signup(@RequestBody Distributor param) {
		if(param == null) {
			RetJson.urlError("sigup error", null);
		}
		
		int ret = distributorService.signup(param);
		if(ret == -1) {
			return RetJson.urlError("sigup error, username already exists ", null);
		}
		else if(ret == -2) {
			return RetJson.mysqlError("sigup error, atabase disconnected", null);
		}
		else {
			return RetJson.success("sigup seccess");
		}
	}
	
	/**
	 *  登录
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/distributor/login", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson login(@RequestBody PasswordLogin param) {
		String username = param.getUsername();
		String password = param.getPassword();
		String ip = param.getIp();
		if((username == null)||(password == null)||(ip == null)) {
			return RetJson.urlError("login error", null);
		}
		
		Distributor distributor = distributorService.login(username, password, ip);
		
		if(distributor == null) {
			return RetJson.urlError("login error", null);
		}
		return RetJson.success("success",distributor);
	}

	
	/**
	 *  注销
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/distributor/logout", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		
		if(!distributorService.logout(userToken)) {
			return RetJson.urlError("logout error", null);
		}
		
		return RetJson.success("logout success",null);
	}
	
	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/distributor/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(@RequestBody DistributorInfo  param) {
		String userToken = param.getUserToken();
		Distributor admin = param.getDistributor();
		if(userToken == null || admin == null) {
			return RetJson.urlError("update error", null);
		}
		
		if(!distributorService.updateDistributor(userToken, admin)) {
			return RetJson.urlError("update error", null);
		}
		
		return RetJson.success("update success",null);
	}
	
	/**
	 * 商品列表
	 */
	@RequestMapping(value = "/distributor/listcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listCommodity(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Commodity> commodityList = distributorService.listCommodity(userToken);
		return RetJson.success("success",commodityList);
	}
	
//	/**
//	 * 添加商品
//	 */
//	@RequestMapping(value = "/distributor/addcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson addCommodity(@RequestBody CommodityInfo param) {
//		String userToken = param.getUserToken();
//		Commodity commodity = param.getCommodity();
//		if(userToken == null || commodity == null) {
//			return RetJson.urlError("add commodity error", null);
//		}
//		
//		if(distributorService.addCommodity(userToken, commodity)) {
//			return RetJson.success("success");
//		}
//		return RetJson.unknowError("add commodity error", null);
//	}
	
	/**
	 * 商品下架
	 */
	@RequestMapping(value = "/distributor/removecommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCommodity(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String commodityID = param.getString();
		if(userToken == null || commodityID == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(distributorService.removeCommodity(userToken, Long.parseLong(commodityID))) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 商品租赁列表
	 */
	@RequestMapping(value = "/distributor/listrentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listRentCommodity(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Commodity> commodityList = distributorService.listRentCommodity(userToken);
		return RetJson.success("success",commodityList);
	}
	
	/**
	 * 订单列表
	 */
	@RequestMapping(value = "/distributor/listorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = distributorService.listAllOrder(userToken);
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看已处理订单
	 */
	@RequestMapping(value = "/distributor/listdoorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listDoOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = distributorService.listDoOrder(userToken);
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看未处理订单
	 */
	@RequestMapping(value = "/distributor/listundoorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUndoOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = distributorService.listUndoOrder(userToken);
		return RetJson.success("success",order);
	}
}
