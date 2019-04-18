package com.lyqxsc.yhpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;

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
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.Test;
import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.service.AdminService;
import com.lyqxsc.yhpt.urlclass.AdminInfo;
import com.lyqxsc.yhpt.urlclass.PasswordLogin;
import com.lyqxsc.yhpt.urlclass.CommodityInfo;
import com.lyqxsc.yhpt.urlclass.DistributorInfo;
import com.lyqxsc.yhpt.urlclass.UserInfo;
import com.lyqxsc.yhpt.urlclass.UserToken;
import com.lyqxsc.yhpt.urlclass.UserTokenOne;
import com.lyqxsc.yhpt.urlclass.UserTokenTwo;
import com.lyqxsc.yhpt.utils.RetJson;

@RestController
@CrossOrigin
public class AdminController {
	@Autowired
	AdminService adminService;
	
	/**
	 *  注册
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/admin/sigup", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson signup(@RequestBody Admin param) {
		if(param == null) {
			RetJson.urlError("sigup error", null);
		}
		
		int ret = adminService.signup(param);
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
	@RequestMapping(value = "/admin/login", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson login(@RequestBody PasswordLogin param) {
		
//		String username = param.getParameter("username");
//		String password = param.getParameter("password");
//		String ip = param.getParameter("ip");
		String username = param.getUsername();
		String password = param.getPassword();
		String ip = param.getIp();
		if((username == null)||(password == null)||(ip == null)) {
			return RetJson.urlError("login error", null);
		}
		
		Admin admin = adminService.login(username, password, ip);
		
		if(admin == null) {
			return RetJson.urlError("login error", null);
		}
		return RetJson.success("success",admin);
	}

	
	/**
	 *  注销
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/admin/logout", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(@RequestBody UserToken  param) {
		
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		
		if(!adminService.logout(userToken)) {
			return RetJson.urlError("logout error", null);
		}
		
		return RetJson.success("logout success",null);
	}
	
	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/admin/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(@RequestBody AdminInfo  param) {
		String userToken = param.getUserToken();
		Admin admin = param.getAdmin();
		if(userToken == null || admin == null) {
			return RetJson.urlError("update error", null);
		}
		
		if(!adminService.updateAdmin(userToken, admin)) {
			return RetJson.urlError("update error", null);
		}
		
		return RetJson.success("update success",null);
	}
	
	
	/**
	 * 添加物品分类
	 * 1药剂 2器械
	 */
	@RequestMapping(value = "/admin/addcommodityclass", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addClassify(@RequestBody UserTokenTwo param) {
		String userToken = param.getUserToken();
		String type = param.getOne();
		String string = param.getTwo();
		if(userToken == null || type == null || string == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.addClassify(userToken,Integer.parseInt(type),string)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 商品列表
	 */
	@RequestMapping(value = "/admin/listcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listCommodity(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Commodity> commodityList = adminService.listCommodity(userToken);
		return RetJson.success("success",commodityList);
	}
	
	/**
	 * 添加商品
	 */
	@RequestMapping(value = "/admin/addcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCommodity(@RequestBody CommodityInfo param) {
		String userToken = param.getUserToken();
		Commodity commodity = param.getCommodity();
		if(userToken == null || commodity == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.addCommodity(userToken, commodity)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 商品下架
	 */
	@RequestMapping(value = "/admin/removecommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCommodity(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String commodityID = param.getString();
		if(userToken == null || commodityID == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.removeCommodity(userToken, Long.parseLong(commodityID))) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 商品租赁列表
	 */
	@RequestMapping(value = "/admin/listrentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listRentCommodity(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Commodity> commodityList = adminService.listRentCommodity(userToken);
		return RetJson.success("success",commodityList);
	}
	
//	/**
//	 * 添加租赁商品
//	 */
//	@RequestMapping(value = "/addrentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson addRentCommodity(ServletRequest param, @RequestBody RentCommodity rentCommodity) {
//		String userToken = param.getParameter("userToken");
//		if(userToken == null || rentCommodity == null) {
//			return RetJson.urlError("add commodity error", null);
//		}
//		
//		if(adminService.addRentCommodity(userToken, rentCommodity)) {
//			return RetJson.success("success");
//		}
//		return RetJson.unknowError("add commodity error", null);
//	}
	
//	/**
//	 * 租赁物品下架
//	 */
//	@RequestMapping(value = "/removerentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson removeRentCommodity(ServletRequest param) {
//		String userToken = param.getParameter("userToken");
//		String rentCommodityID = param.getParameter("rentCommodityID");
//		String rentCommodityName = param.getParameter("rentCommodityName");
//		if(userToken == null || rentCommodityID == null || rentCommodityName == null) {
//			return RetJson.urlError("add commodity error", null);
//		}
//		
//		if(adminService.removeRentCommodity(userToken, Integer.parseInt(rentCommodityID), rentCommodityName)) {
//			return RetJson.success("success");
//		}
//		return RetJson.unknowError("add commodity error", null);
//	}
	
	/**
	 * 用户列表
	 */
	@RequestMapping(value = "/admin/listuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUser(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<User> user = adminService.listAllUser(userToken);
		return RetJson.success("success",user);
	}
	
	/**
	 * 新增用户
	 */
	@RequestMapping(value = "/admin/adduser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addUser(@RequestBody UserInfo param) {
		String userToken = param.getUserToken();
		User user = param.getUser();
		if(userToken == null || user == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.addUser(userToken, user) == 0) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add user error", null);
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/admin/removeuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeUser(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.removeUser(userToken, Long.parseLong(id))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/admin/updateuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateUser(@RequestBody UserInfo param) {
		String userToken = param.getUserToken();
		User user = param.getUser();
		if(userToken == null || user == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.updateUser(userToken, user)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add user error", null);
	}
	
	/**
	 * 分销商列表
	 */
	@RequestMapping(value = "/admin/listdistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllDistributor(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Distributor> distributor = adminService.listAllDistributor(userToken);
		return RetJson.success("success",distributor);
	}
	
	/**
	 * 添加分销商
	 */
	@RequestMapping(value = "/admin/adddistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllDistributor(@RequestBody DistributorInfo param) {
		String userToken = param.getUserToken();
		Distributor distributor = param.getDistributor();
		if(userToken == null || distributor == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.addDistributor(userToken, distributor) == 0) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add distributor error", null);
	}
	
	/**
	 * 删除分销商
	 */
	@RequestMapping(value = "/admin/removedistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeDistributor(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String distributorID = param.getString();
		if(userToken == null || distributorID == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.removeDistributor(userToken, Long.parseLong(distributorID))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 分销商授权
	 */
	@RequestMapping(value = "/admin/authorizedistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson authorizeDistributor(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String distributorID = param.getString();
		if(userToken == null || distributorID == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.authorizeDistributor(userToken, Long.parseLong(distributorID))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 取消授权
	 */
	@RequestMapping(value = "/admin/unauthorizedistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson unAuthorizeDistributor(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String distributorID = param.getString();
		if(userToken == null || distributorID == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.unAuthorizeDistributor(userToken, Long.parseLong(distributorID))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 订单列表
	 */
	@RequestMapping(value = "/admin/listorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = adminService.listAllOrder(userToken);
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看已处理订单
	 */
	@RequestMapping(value = "/admin/listdoorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listDoOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = adminService.listDoOrder(userToken);
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看未处理订单
	 */
	@RequestMapping(value = "/admin/listundoorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUndoOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = adminService.listUndoOrder(userToken);
		return RetJson.success("success",order);
	}
	
}
