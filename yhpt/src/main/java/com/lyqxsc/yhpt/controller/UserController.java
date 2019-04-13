package com.lyqxsc.yhpt.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.domain.Appraise;
import com.lyqxsc.yhpt.domain.Collect;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.HomePage;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.ShopCar;
import com.lyqxsc.yhpt.service.UserService;
import com.lyqxsc.yhpt.urlclass.UserLogin;
import com.lyqxsc.yhpt.utils.RetJson;

@RestController
@CrossOrigin
public class UserController {
	@Autowired
	UserService userService;
	
	/**
	 *  登录
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/userlogin", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson login(@RequestBody UserLogin param) {
		
//		String openID = param.getParameter("openID");
//		String ip = param.getParameter("ip");
		String openID = param.getOpenID();
		String ip = param.getIp();
		
		if((openID == null)||(ip == null)) {
			return RetJson.urlError("login error", null);
		}
		
		User user = userService.login(openID,ip);
		
		if(user == null) {
			return RetJson.urlError("login error", null);
		}
		return RetJson.success("success",user);
	}
	
	
	/**
	 *  注销
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/userlogout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(ServletRequest param) {
		
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		
		if(!userService.logout(userToken)) {
			return RetJson.urlError("logout error", null);
		}
		
		return RetJson.success("logout success",null);
	}
	
	
	/**
	 *  注册
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/usersigup",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson signup(User param) {
		if(param == null) {
			RetJson.urlError("sigup error", null);
		}
		
		int ret = userService.signup(param);
		if(ret == -1) {
			return RetJson.urlError("sigup error, user already exists ", null);
		}
		else if(ret == -2) {
			return RetJson.mysqlError("sigup error", null);
		}
		else {
			return RetJson.success("sigup seccess");
		}
	}
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value = "/home", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson homePage(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		HomePage homePage = userService.homePage(userToken);
		if(homePage == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		
		return RetJson.success("success", homePage);
	}
	
	/**
	 * 关于我们
	 */
	@RequestMapping(value = "/home/aboutus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson aboutUs(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		String msg = userService.aboutUs(userToken);
		if(msg == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		
		return RetJson.success("success", msg);
	}
	
	/**
	 * 商城
	 * @return
	 */
	//TODO 暂时用首页界面
	@RequestMapping(value = "/shop", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson shop(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		HomePage homePage = userService.shop(userToken);
		if(homePage == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		
		return RetJson.success("success", homePage);
	}
	
	/**
	 * 新品  展示出售商品 最新10条
	 */
	@RequestMapping(value = "/shop/newshop", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson newShopShow(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		List<Commodity> commodity = userService.newShopShow(userToken);
		if(commodity == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		
		return RetJson.success("success", commodity);
	}
	
	/**
	 * 热卖  最出售最多10条
	 */
	@RequestMapping(value = "/shop/hotshop", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson hotShopShow(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		List<Commodity> commodity = userService.hotShopShow(userToken);
		if(commodity == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		
		return RetJson.success("success", commodity);
	}
	
	/**
	 *  查看可出售商品
	 */
	@RequestMapping(value = "/shop/selectcommodity",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		List<Commodity> commodityList = userService.selectCommodity(userToken);
		if(commodityList == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		return RetJson.success("success", commodityList);
	}
	
	/**
	 *  查看可租赁商品
	 */
	@RequestMapping(value = "/shop/selectrentcommodity",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectRentCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		List<RentCommodity> rentCommodityList = userService.selectRentCommodity(userToken);
		if(rentCommodityList == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		return RetJson.success("success", rentCommodityList);
	}
	
	/**
	 * 购买
	 */
	@RequestMapping(value = "/shop/buycommodity",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson buyCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String commodityID = param.getParameter("commodityID");
		String count = param.getParameter("count");
		String ip = param.getParameter("ip");
		if((userToken == null) || (commodityID == null) || (count == null) || (ip == null)) {
			return RetJson.urlError("buy commodity error", null);
		}
		Order order = userService.makeCommodityOrder(userToken,Integer.parseInt(commodityID),Integer.parseInt(count),ip);
		if(order == null) {
			return RetJson.unknowError("buy commodity error", null);
		}
		return RetJson.success("success", order);
	}
	
	/**
	 *  提交订单
	 */
	@RequestMapping(value = "/shop/pushorder",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson presentOrder(ServletRequest param,@RequestBody Order order) {
		String userToken = param.getParameter("userToken");
		String addr = param.getParameter("addr");
		if(userToken == null || order == null || addr == null) {
			return RetJson.urlError("push order error", null);
		}
		if(!userService.presentOrder(userToken,order,addr)) {
			return RetJson.urlError("push order error", null);
		}
		return RetJson.success("success");
	}
	
	/**
	 * 租赁
	 */
	@RequestMapping(value = "/shop/rentcommodity",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson rentCommodity(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String rentCommodityID = param.getParameter("rentCommodityid");
		String count = param.getParameter("count");
		String ip = param.getParameter("ip");
		if((userToken == null) || (rentCommodityID == null) || (count == null) || (ip == null)) {
			return RetJson.urlError("buy commodity error", null);
		}
		RentOrder rentOrder = userService.makeRentCommodityOrder(userToken,Integer.parseInt(rentCommodityID),Integer.parseInt(count),ip);
		if(rentOrder == null) {
			return RetJson.unknowError("用户不在线", null);
		}
		return RetJson.success("success", rentOrder);
	}
	
	/**
	 *  提交租赁订单
	 */
	@RequestMapping(value = "/shop/pushrentorder",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson presentRentOrder(ServletRequest param,@RequestBody RentOrder rentOrder) {
		String userToken = param.getParameter("userToken");
		String addr = param.getParameter("addr");
		if(userToken == null || rentOrder == null || addr == null) {
			return RetJson.urlError("push order error", null);
		}
		if(!userService.presentRentOrder(userToken,rentOrder,addr)) {
			return RetJson.urlError("push order error", null);
		}
		return RetJson.success("success");
	}
	
	
	/**
	 * 我的全部订单
	 */
	@RequestMapping(value = "/usercenter/order", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  allOrder(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		List<Order> orderList = userService.getAllOrder(userToken);
		if(orderList == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success", orderList);
	}
	
	/**
	 * 待付款订单列表
	 */
	@RequestMapping(value = "/usercenter/order/nopay", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  noPayOrder(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		List<Order> orderList = userService.getTypeOrder(userToken, 0);
		if(orderList == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success", orderList);
	}
	
	/**
	 * 已付款订单列表
	 */
	@RequestMapping(value = "/usercenter/order/pay", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  isPayOrder(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		List<Order> orderList = userService.getTypeOrder(userToken, 1);
		if(orderList == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success", orderList);
	}
	
	/**
	 * 购物车
	 */
	@RequestMapping(value = "/usercenter/shopcar", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson shopCar(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("list shopcar error", null);
		}
		List<ShopCar> shopList = userService.getShopCar(userToken);
		if(shopList == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success", shopList);
	}
	
	/**
	 * 购物车 增
	 */
	@RequestMapping(value = "/usercenter/shopcar/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addShopCar(ServletRequest param, @RequestBody ShopCar shopCar) {
		String userToken = param.getParameter("userToken");
		if(userToken == null || shopCar == null) {
			return RetJson.urlError("add shopcar error", null);
		}
		if(userService.addShopCar(userToken, shopCar)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("false", null);
	}
	
	/**
	 * 购物车 删
	 */
	@RequestMapping(value = "/usercenter/shopcar/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeShopCar(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String id = param.getParameter("id");
		if(userToken == null || id == null) {
			return RetJson.urlError("remove shopcar error", null);
		}
		if(userService.removeShopCar(userToken, Long.parseLong(id))) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("false", null);
	}
	
	/**
	 * 购物车 改
	 */
	@RequestMapping(value = "/usercenter/shopcar/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateShopCar(ServletRequest param, @RequestBody ShopCar shopCar) {
		String userToken = param.getParameter("userToken");
		if(userToken == null || shopCar == null) {
			return RetJson.urlError("update shopcar error", null);
		}
		if(userService.updateShopCar(userToken, shopCar)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("false", null);
	}
	
	/**
	 * 收藏夹列表
	 */
	@RequestMapping(value = "/usercenter/collect", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listCollect(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("list collect error", null);
		}
		List<Collect> shopList = userService.getCollect(userToken);
		if(shopList == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success", shopList);
	}
	
	/**
	 * 收藏夹 增
	 */
	@RequestMapping(value = "/usercenter/collect/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCollect(ServletRequest param, @RequestBody Collect collect) {
		String userToken = param.getParameter("userToken");
		if(userToken == null || collect == null) {
			return RetJson.urlError("add collect error", null);
		}
		if(userService.addCollect(userToken, collect)) {
			return RetJson.success("success");
		}
		return RetJson.unknowError("false", null);
	}
	
	/**
	 * 收藏夹 删
	 */
	@RequestMapping(value = "/usercenter/collect/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCollect(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String id = param.getParameter("id");
		if(userToken == null || id == null) {
			return RetJson.urlError("remove collect error", null);
		}
		if(userService.removeCollect(userToken, Long.parseLong(id))) {
			return RetJson.success("success");
		}
		
		return RetJson.success("success");
	}
	
	/**
	 * 评价列表,根据物品ID查看评论
	 */
	@RequestMapping(value = "/usercenter/appraise", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAppraise(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String id = param.getParameter("id");
		if(userToken == null || id == null) {
			return RetJson.urlError("list appraise error", null);
		}
		List<Appraise> appraise = userService.listAppraise(userToken, Long.parseLong(id));
		if(appraise == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success",appraise);
	}
	
	/**
	 * 我的评价列表，根据用户ID查看评论
	 */
	@RequestMapping(value = "/usercenter/appraise/myappraise", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listMyAppraise(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		if(userToken == null) {
			return RetJson.urlError("list appraise error", null);
		}
		List<Appraise> appraise = userService.listMyAppraise(userToken);
		if(appraise == null) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success",appraise);
	}
	
	/**
	 * 评价 增
	 */
	@RequestMapping(value = "/usercenter/appraise/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addAppraise(ServletRequest param, @RequestBody Appraise appraise) {
		String userToken = param.getParameter("userToken");
		if(userToken == null || appraise == null) {
			return RetJson.urlError("list appraise error", null);
		}
		if(userService.addAppraise(userToken, appraise)) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success",null);
	}
	
	/**
	 * 评价 删
	 */
	@RequestMapping(value = "/usercenter/appraise/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeAppraise(ServletRequest param) {
		String userToken = param.getParameter("userToken");
		String id = param.getParameter("id");
		if(userToken == null || id == null) {
			return RetJson.urlError("list appraise error", null);
		}
		if(userService.removeAppraise(userToken, Long.parseLong(id))) {
			return RetJson.unknowError("false", null);
		}
		return RetJson.success("success",null);
	}
}
