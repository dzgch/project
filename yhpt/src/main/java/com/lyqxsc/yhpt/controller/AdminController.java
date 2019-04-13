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
import com.lyqxsc.yhpt.urlclass.AdminLogin;
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
	@RequestMapping(value = "/adminsigup", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson signup(Admin param) {
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
	@RequestMapping(value = "/adminlogin", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson login(@RequestBody AdminLogin param) {
		
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
	@RequestMapping(value = "/adminlogout", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(ServletRequest param) {
		
		String id = param.getParameter("userToken");
		if(id == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		
		if(!adminService.logout(id)) {
			return RetJson.urlError("logout error", null);
		}
		
		return RetJson.success("logout success",null);
	}
	
	/**
	 * 修改信息
	 */
	
	
	/**
	 * 商品列表
	 */
	@RequestMapping(value = "/listcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Commodity> commodityList = adminService.listCommodity(userToken);
		return RetJson.success("success",commodityList);
	}
	
	/**
	 * 添加商品
	 */
	@RequestMapping(value = "/addcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCommodity(ServletRequest param, @RequestBody Commodity commodity) {
		String userToken = param.getParameter("userToken");
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
	@RequestMapping(value = "/removecommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String commodityID = param.getParameter("commodityID");
		String commodityName = param.getParameter("commodityName");
		if(userToken == null || commodityID == null || commodityName == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.removeCommodity(userToken, Integer.parseInt(commodityID), commodityName)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 商品租赁列表
	 */
	@RequestMapping(value = "/listrentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listRentCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Commodity> commodityList = adminService.listCommodity(userToken);
		return RetJson.success("success",commodityList);
	}
	
	/**
	 * 添加租赁商品
	 */
	@RequestMapping(value = "/addrentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addRentCommodity(ServletRequest param, @RequestBody RentCommodity rentCommodity) {
		String userToken = param.getParameter("userToken");
		if(userToken == null || rentCommodity == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.addRentCommodity(userToken, rentCommodity)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 租赁物品下架
	 */
	@RequestMapping(value = "/removerentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeRentCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String rentCommodityID = param.getParameter("rentCommodityID");
		String rentCommodityName = param.getParameter("rentCommodityName");
		if(userToken == null || rentCommodityID == null || rentCommodityName == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.removeRentCommodity(userToken, Integer.parseInt(rentCommodityID), rentCommodityName)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 用户列表
	 */
	@RequestMapping(value = "/listuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUser(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<User> user = adminService.listAllUser(userToken);
		return RetJson.success("success",user);
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/removeuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeUser(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String openID = param.getParameter("openID");
		if(userToken == null || openID == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.removeUser(userToken, openID)){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 新增用户
	 */
	@RequestMapping(value = "/adduser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addUser(ServletRequest param, @RequestBody User user) {
		String userToken = param.getParameter("userToken");
		if(userToken == null || user == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.addUser(userToken, user) == 0) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add user error", null);
	}
	
	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/updateuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateUser(ServletRequest param, @RequestBody User user) {
		String userToken = param.getParameter("userToken");
		String userID = param.getParameter("userID");
		if(userToken == null || userID == null || user == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(adminService.updateUser(userToken, Long.parseLong(userID), user)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("add user error", null);
	}
	
	/**
	 * 分销商列表
	 */
	@RequestMapping(value = "/listdistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllDistributor(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Distributor> distributor = adminService.listAllDistributor(userToken);
		return RetJson.success("success",distributor);
	}
	
	/**
	 * 添加分销商
	 */
	@RequestMapping(value = "/adddistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllDistributor(ServletRequest param, @RequestBody Distributor distributor) {
		String userToken = param.getParameter("userToken");
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
	@RequestMapping(value = "/removedistributor", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeDistributor(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String distributorID = param.getParameter("distributorID");
		if(userToken == null || distributorID == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(adminService.removeDistributor(userToken, Long.parseLong(distributorID))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 订单列表
	 */
	@RequestMapping(value = "/listorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAllOrder(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = adminService.listAllOrder(userToken);
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看已处理订单
	 */
	@RequestMapping(value = "/listdoorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listDoOrder(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = adminService.listDoOrder(userToken);
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看未处理订单
	 */
	@RequestMapping(value = "/listundoorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUndoOrder(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<Order> order = adminService.listUndoOrder(userToken);
		return RetJson.success("success",order);
	}
	
}
