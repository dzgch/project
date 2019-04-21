package com.lyqxsc.yhpt.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.AdminHomePage;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.Coupon;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.DistributorHomePage;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.Test;
import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.service.AdminService;
import com.lyqxsc.yhpt.service.DistributorService;
import com.lyqxsc.yhpt.urlclass.AdminInfo;
import com.lyqxsc.yhpt.urlclass.ClassifyList;
import com.lyqxsc.yhpt.urlclass.PasswordLogin;
import com.lyqxsc.yhpt.urlclass.PictureInfo;
import com.lyqxsc.yhpt.urlclass.CommodityInfo;
import com.lyqxsc.yhpt.urlclass.CouponInfo;
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
	
	@Autowired
	DistributorService distributorService;
	
	/**
	 *  注册
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/admin/sigup", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson signup(@RequestBody AdminInfo param) {
		String userToken = param.getUserToken();
		Admin admin = param.getAdmin();
		if(userToken == null || admin == null) {
			RetJson.urlError("sigup error", null);
		}
		
		int ret = adminService.signupAdmin(userToken,admin);
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
		String username = param.getUsername();
		String password = param.getPassword();
		String ip = param.getIp();
		if((username == null)||(password == null)||(ip == null)) {
			return RetJson.urlError("login error", null);
		}
		
		//TODO
		
		Admin admin = adminService.login(username, password, ip);
		
		if(admin == null) {
			return RetJson.urlError("login error", null);
		}
		System.out.println(admin.getUserToken());
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
		
		if(isAdmin(userToken)) {
			if(!adminService.logout(userToken)) {
				return RetJson.urlError("logout error", null);
			}
		}
		else{
			if(!distributorService.logout(userToken)) {
				return RetJson.urlError("logout error", null);
			}
		}
		return RetJson.success("logout success",null);
	}
	
	/**
	 * 首页
	 */
	@RequestMapping(value = "/admin/home", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson home(@RequestBody UserToken  param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		
		if(isAdmin(userToken)) {
			AdminHomePage home = adminService.homepage(userToken, 0);
			return RetJson.success("logout success",home);
		}
		else{
			DistributorHomePage home = distributorService.homepage(userToken);
			return RetJson.success("logout success",home);
		}
	}
	
	/**
	 * 修改信息
	 */
	@RequestMapping(value = "/admin/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(@RequestBody AdminInfo  param) {
		String userToken = param.getUserToken();
		Admin admin = param.getAdmin();
		Distributor distributor = param.getDistributor();
		if(userToken == null) {
			return RetJson.urlError("update error", null);
		}
		
		if(isAdmin(userToken)) {
			if(!adminService.updateAdmin(userToken, admin)) {
				return RetJson.urlError("update error", null);
			}
		}
		else{
			if(!distributorService.updateDistributor(userToken, distributor)) {
				return RetJson.urlError("update error", null);
			}
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
			return RetJson.urlError("参数错误", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.addClassify(userToken,Integer.parseInt(type),string)) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 删除分类
	 * 1药剂 2器械
	 */
	@RequestMapping(value = "/admin/removecommodityclass", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeClassify(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.removeClassify(userToken, Integer.parseInt(id))) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 查询分类
	 */
	@RequestMapping(value = "/admin/selectcommodityclass", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectClassify(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		if(isAdmin(userToken)) {
			ClassifyList classList = adminService.selectClassify(userToken);
			if(classList == null) {
				return RetJson.unknowError("查询失败", null);
			}
			return RetJson.success("成功",classList);
		}
		else {
			ClassifyList classList = distributorService.selectClassify(userToken);
			if(classList == null) {
				return RetJson.unknowError("查询失败", null);
			}
			return RetJson.success("成功",classList);
		}
	}
	
	
	/**
	 * 上传图片
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/commodity/sendpic", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson recvPic(PictureInfo param) {//@RequestParam("pic") MultipartFile file) {
		String path = "D:\\test1\\";
    	String name = System.currentTimeMillis() + ".png";
    	String filename = path+name;

    	MultipartFile file = param.getPic();
    	String usertoken = param.getUserToken();
    	System.out.println(usertoken);
		if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filename)));    
                System.out.println(file.getName());
                out.write(file.getBytes());    
                out.flush();    
                out.close();    
            } catch (FileNotFoundException e) {    
                e.printStackTrace();    
                return RetJson.unknowError("error", null); 
            } catch (IOException e) {
                e.printStackTrace();    
                return RetJson.unknowError("error", null);
            }    
    
            return RetJson.success("success" );    
    
        } else {    
            return RetJson.unknowError("error", null);    
        }
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
		List<Commodity> commodityList = null;
		if(isAdmin(userToken)) {
			commodityList = adminService.listCommodity(userToken);
		}
		else{
			commodityList = distributorService.listCommodity(userToken);
		}
		
		return RetJson.success("success",commodityList);
	}
	
	/**
	 * 商品详情
	 */
	@RequestMapping(value = "/admin/commodity/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson getCommodityInfo(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		Commodity commodity = null;
		if(isAdmin(userToken)) {
			commodity = adminService.getCommodityInfo(userToken,Long.parseLong(id));
		}
		else{
			commodity = distributorService.getCommodityInfo(userToken,Long.parseLong(id));
		}
		
		return RetJson.success("success",commodity);
	}
	
	/**
	 * 添加商品
	 */
	@ResponseBody
	@RequestMapping(value = "/admin/addcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCommodity(CommodityInfo param) {
		String userToken = param.getUserToken();
		System.out.println(userToken);
//		Commodity commodity = param.getCommodity();
		String name = param.getName();
		MultipartFile pic = param.getPic();
		Commodity commodity = new Commodity();
		commodity.setName(name);
		if(userToken == null || commodity == null || pic == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.addCommodity(userToken, commodity, pic)) {
				return RetJson.success("success");
			}
		}
		else{
			if(distributorService.addCommodity(userToken, commodity, pic)) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 添加商品数量
	 */
	@RequestMapping(value = "/admin/commodity/addcount", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCommodityCount(@RequestBody UserTokenTwo param) {
		String userToken = param.getUserToken();
		String one = param.getOne();
		String two = param.getTwo();
		
		long commodityID = Long.parseLong(one);
		int count = Integer.parseInt(two);
		
		if(isAdmin(userToken)) {
			if(adminService.addCommodityCount(userToken, commodityID, count)) {
				return RetJson.success("success");
			}
		}
		else{
			if(distributorService.addCommodityCount(userToken, commodityID, count)) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("add count error", null);
	}
	
	/**
	 * 商品删除
	 */
	@RequestMapping(value = "/admin/removecommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCommodity(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String commodityID = param.getString();
		if(userToken == null || commodityID == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.removeCommodity(userToken, Long.parseLong(commodityID))) {
				return RetJson.success("success");
			}
		}
		else{
			if(distributorService.removeCommodity(userToken, Long.parseLong(commodityID))) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("add commodity error", null);
	}
	
	/**
	 * 商品上架
	 */
	@RequestMapping(value = "/admin/commodity/online", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson onlineCommodity(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String commodityID = param.getString();
		if(userToken == null || commodityID == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.onlineCommodity(userToken, Long.parseLong(commodityID), 1)) {
				return RetJson.success("success");
			}
		}
		else{
			if(distributorService.onlineCommodity(userToken, Long.parseLong(commodityID), 1)) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("online commodity error", null);
	}
	
	/**
	 * 商品下架
	 */
	@RequestMapping(value = "/admin/commodity/downline", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson downlineCommodity(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String commodityID = param.getString();
		if(userToken == null || commodityID == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.onlineCommodity(userToken, Long.parseLong(commodityID), 0)) {
				return RetJson.success("success");
			}
		}
		else{
			if(distributorService.onlineCommodity(userToken, Long.parseLong(commodityID), 0)) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("downline commodity error", null);
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
		
		List<Commodity> commodityList = null;
		if(isAdmin(userToken)) {
			commodityList = adminService.listRentCommodity(userToken);
		}
		else{
			commodityList = distributorService.listRentCommodity(userToken);
		}
		
		
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
	 * 库存管理
	 */
	@RequestMapping(value = "/admin/inventoryWarning", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson inventoryWarning(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		List<Commodity> commodityList = null;
		if(isAdmin(userToken)) {
			commodityList = adminService.inventoryWarning(userToken, 10);
		}
		else{
			commodityList = distributorService.inventoryWarning(userToken, 10);
		}
		
		return RetJson.success("success", commodityList);
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
		List<Order> order = null;
		if(isAdmin(userToken)) {
			order = adminService.listAllOrder(userToken);
		}
		else{
			order = distributorService.listAllOrder(userToken);
		}
		return RetJson.success("success",order);
	}
	
	/**
	 * 订单详情
	 */
	@RequestMapping(value = "/admin/listorder/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listOneOrder(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		Order order = null;
		if(isAdmin(userToken)) {
			order = adminService.getOrder(userToken,id);
		}
		else{
			order = distributorService.getOrder(userToken,id);
		}
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
		
		List<Order> order = null;
		if(isAdmin(userToken)) {
			order = adminService.listDoOrder(userToken);
		}
		else{
			order = distributorService.listDoOrder(userToken);
		}

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
		
		List<Order> order = null;
		if(isAdmin(userToken)) {
			order = adminService.listUndoOrder(userToken);
		}
		else{
			order = distributorService.listUndoOrder(userToken);
		}
		return RetJson.success("success",order);
	}
	
	/**
	 * 租赁订单列表
	 */
	@RequestMapping(value = "/admin/rentorder/list", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<RentOrder> order = null;
		if(isAdmin(userToken)) {
			order = adminService.listAllRentOrder(userToken);
		}
		else{
			order = distributorService.listAllRentOrder(userToken);
		}
		return RetJson.success("success",order);
	}
	
	/**
	 * 租赁订单详情 
	 */
	@RequestMapping(value = "/admin/rentorder/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listOneRentOrder(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		RentOrder order = null;
		if(isAdmin(userToken)) {
			order = adminService.listOneRentOrder(userToken,id);
		}
		else{
			order = distributorService.listOneRentOrder(userToken,id);
		}
		return RetJson.success("success",order);
	}
	
	/**
	 * 查看已处理租赁订单
	 */
	@RequestMapping(value = "/admin/rentorder/do", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson ListDoRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<RentOrder> order = null;
		if(isAdmin(userToken)) {
			order = adminService.listDoRentOrder(userToken);
		}
		else{
			order = distributorService.listDoRentOrder(userToken);
		}

		return RetJson.success("success",order);
	}
	
	/**
	 * 查看未处理订单
	 */
	@RequestMapping(value = "/admin/rentorder/undo", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUndoRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		List<RentOrder> order = null;
		if(isAdmin(userToken)) {
			order = adminService.listUndoRentOrder(userToken);
		}
		else{
			order = distributorService.listUndoRentOrder(userToken);
		}
		return RetJson.success("success",order);
	}
	
	/**
	 * 租赁物品发货
	 */
//	@RequestMapping(value = "/admin/rentorder/send", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson sendrent(@RequestBody UserTokenOne param) {
//		String userToken = param.getUserToken();
//		String id = param.getString();
//		if(userToken == null || id == null) {
//			return RetJson.urlError("error, please give me userToken", null);
//		}
//		
//		if(isAdmin(userToken)) {
//			if(adminService.sendRentOrder(userToken, id)) {
//				return RetJson.success("success");
//			}
//		}
//		else{
//			if(distributorService.sendRentOrder(userToken, id)) {
//				return RetJson.success("success");
//			}
//		}
//		return RetJson.unknowError("set order status error",null);
//	}
	
	/**
	 * 商品发货
	 */
	@RequestMapping(value = "/admin/order/send", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson sendCommodify(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(isAdmin(userToken)) {
			if(adminService.sendOrder(userToken, id)) {
				return RetJson.success("success");
			}
		}
		else{
			if(distributorService.sendOrder(userToken, id)) {
				return RetJson.success("success");
			}
		}
		return RetJson.unknowError("set order status error",null);
	}
	
	/**
	 * 优惠券 列表
	 */
	@RequestMapping(value = "/admin/coupon/list", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listCoupon(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(!isAdmin(userToken)) {
			return RetJson.success("list error", null);
		}
		List<Coupon> coupon = adminService.listCoupon(userToken);
		return RetJson.success("success", coupon);
	}
	
	/**
	 * 优惠券 添加 
	 */
	@RequestMapping(value = "/admin/coupon/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCoupon(@RequestBody CouponInfo param) {
		String userToken = param.getUserToken();
		Coupon coupon = param.getCoupon();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		if(adminService.addCoupon(userToken, coupon)) {
			return RetJson.success("success");
		}
		
		return RetJson.unknowError("add coupon error", null);
	}
	
	
	/**
	 * 优惠券 删除
	 */
	@RequestMapping(value = "/admin/coupon/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCoupon(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		if(adminService.removeCoupon(userToken, Long.parseLong(id))) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("remove coupon error", null);
	}
	
	
	/**
	 * 用户列表
	 */
	@RequestMapping(value = "/admin/listuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listUser(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}

		List<User> user = adminService.listAllUser(userToken);
		if(user == null) {
			return RetJson.unknowError("登陆过期或访问数据失败", null);
		}
		return RetJson.success("成功",user);
	}
	
	/**
	 * 用户详情
	 */
	@RequestMapping(value = "/admin/user/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson getUserParticulars(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null ||id == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		User user = adminService.getUser(userToken, Long.parseLong(id));
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		if(adminService.removeUser(userToken, Long.parseLong(id))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	
	/**
	 * 修改用户权限
	 * 启用1/禁用2/删除4/拉黑3
	 */
	@RequestMapping(value = "/admin/updateuser", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateUser(@RequestBody UserTokenTwo param) {
		String userToken = param.getUserToken();
		String id = param.getOne();
		String code = param.getTwo();
		if(userToken == null || id == null || code == null) {
			return RetJson.urlError("add commodity error", null);
		}
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		if(adminService.updateUser(userToken, Long.parseLong(id), Integer.parseInt(code))) {
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		List<Distributor> distributor = adminService.listAllDistributor(userToken);
		return RetJson.success("success",distributor);
	}
	
	/**
	 * 分销商详情
	 */
	@RequestMapping(value = "/admin/distributor/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson getDistributorParticulars(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		Distributor distributor = adminService.getDistributor(userToken, Long.parseLong(id));
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		if(adminService.removeDistributor(userToken, Long.parseLong(distributorID))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	/**
	 * 修改分销商
	 */
	@RequestMapping(value = "/admin/distributor/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateDistributor(@RequestBody DistributorInfo param) {
		String userToken = param.getUserToken();
		Distributor distributor = param.getDistributor();
		if(userToken == null || distributor == null) {
			return RetJson.urlError("error, please give me userToken", null);
		}
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		if(adminService.updateDistributor(userToken, distributor)){
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
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
		
		if(!isAdmin(userToken)) {
			return  RetJson.unknowError("unknow error", null);
		}
		
		if(adminService.unAuthorizeDistributor(userToken, Long.parseLong(distributorID))){
			return RetJson.success("success");
		}
		return RetJson.unknowError("error", null);
	}
	
	
	
	
	private boolean isAdmin(String userToken){
		String idStr = userToken.split("O")[0];
		if(Integer.parseInt(idStr) < 100) {
			return true;
		}
		return false;
	}
}
