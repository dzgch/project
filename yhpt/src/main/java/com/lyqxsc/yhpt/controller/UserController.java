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
import com.lyqxsc.yhpt.domain.Address;
import com.lyqxsc.yhpt.domain.Appraise;
import com.lyqxsc.yhpt.domain.Collect;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityBak;
import com.lyqxsc.yhpt.domain.CommodityClassify;
import com.lyqxsc.yhpt.domain.CommodityPage;
import com.lyqxsc.yhpt.domain.WxHomePage;
import com.lyqxsc.yhpt.domain.HotCommodity;
import com.lyqxsc.yhpt.domain.NewCommodity;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.ShopCar;
import com.lyqxsc.yhpt.service.UserService;
import com.lyqxsc.yhpt.urlclass.AddressInfo;
import com.lyqxsc.yhpt.urlclass.AppraiseInfo;
import com.lyqxsc.yhpt.urlclass.CollectInfo;
import com.lyqxsc.yhpt.urlclass.OrderBatchInfo;
import com.lyqxsc.yhpt.urlclass.ShopCarInfo;
import com.lyqxsc.yhpt.urlclass.BuyCommodity;
import com.lyqxsc.yhpt.urlclass.ClassifyList;
import com.lyqxsc.yhpt.urlclass.UserToken;
import com.lyqxsc.yhpt.urlclass.OrderInfo;
import com.lyqxsc.yhpt.urlclass.RentOrderBatchInfo;
import com.lyqxsc.yhpt.urlclass.RentOrderInfo;
import com.lyqxsc.yhpt.urlclass.UserLogin;
import com.lyqxsc.yhpt.urlclass.UserTokenOne;
import com.lyqxsc.yhpt.urlclass.UserTokenTwo;
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
//	@RequestMapping(value = "/userlogin", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson login(@RequestBody UserLogin param) {
//		String openID = param.getParameter("openID");
//		String ip = param.getParameter("ip");
//		String openID = param.getOpenID();
//		String ip = "0.0.0.0";
//		
//		if((openID == null)||(ip == null)) {
//			return RetJson.urlError("login error", null);
//		}
//		
//		User user = userService.login(openID,ip);
//		
//		if(user == null) {
//			return RetJson.urlError("login error", null);
//		}
//		return RetJson.success("success",user);
//		return RetJson.success("success");
//	}
	
	/**
	 * root权限
	 */
	@RequestMapping(value = "/root", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson login() {
		if(userService.root()) {
			return RetJson.success("root success");
		}
		return RetJson.unknowError("root error", null);
	}
	
	/**
	 * 注销
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/userlogout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson logout(@RequestBody UserToken param) {
		
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.logout(userToken);
	}
	
	
	/**
	 * 判断凭证过期
	 */
	@RequestMapping(value = "/user/isout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson isOut(@RequestBody UserToken param) {
		
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.isOutOfDate(userToken);
	}
	
	/**
	 * 注册
	 */
//	@RequestMapping(value = "/usersigup",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson signup(User param) {
//		if(param == null) {
//			RetJson.urlError("sigup error", null);
//		}
//		
//		int ret = userService.signup(param);
//		if(ret == -1) {
//			return RetJson.urlError("sigup error, user already exists ", null);
//		}
//		else if(ret == -2) {
//			return RetJson.mysqlError("sigup error", null);
//		}
//		else {
//			return RetJson.success("sigup seccess");
//		}
//	}
	
	/**
	 * 填写邀请码
	 */
	@RequestMapping(value = "/addinvitationcode", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addInvitationCode(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String code = param.getString();
		if(userToken == null || code == null) {
			return RetJson.urlError("参数错误", null);
		}
		return userService.addInvitationCode(userToken,code);
	}
	
	/**
	 *	不填写邀请码，直接进入
	 */
	@RequestMapping(value = "/noinvitationcode", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson noInvitationCode(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		return userService.noInvitationCode(userToken);
	}
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping(value = "/home", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson homePage(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.homePage(userToken);
	}
	
	/**
	 * 关于我们
	 */
	@RequestMapping(value = "/home/aboutus", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson aboutUs(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.aboutUs(userToken);
	}
	
	/**
	 * 商城
	 * @return
	 */
	//TODO 暂时用首页界面
	@RequestMapping(value = "/shop", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson shop(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.shop(userToken);
	}
	
	/**
	 * 查询分类列表
	 */
	@RequestMapping(value = "/shop/classlist", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson classList(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.classList(userToken);
	}
	
	/**
	 * 分类查询物品
	 */
	@RequestMapping(value = "/shop/classlist/select", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectCommodityByClass(@RequestBody UserTokenTwo param) {
		String userToken = param.getUserToken();
		String classId = param.getOne();
		String price = param.getTwo();
		if(userToken == null || classId == null || price == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.selectCommodityByClass(userToken,classId,price);
	}
	
	/**
	 * 名称查询物品
	 */
	@RequestMapping(value = "/shop/classlist/selectbyname", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectCommodityByName(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String name = param.getString();
		if(userToken == null || name == null) {
			return RetJson.urlError("参数错误", null);
		}
		
		return userService.selectCommodityByName(userToken,name);
	}
	
	/**
	 * 根据商品id查询商品，多个物品用！隔开
	 */
	@RequestMapping(value = "/shop/selectcommoditybyid", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectCommodityByID(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		
		if(userToken == null || id == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		return userService.selectCommodityByID(userToken,id);
	}
	
	/**
	 * 新品  展示出售商品 最新10条
	 */
	@RequestMapping(value = "/shop/newshop", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson newShopShow(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		return userService.newShopShow(userToken);
	}
	
	/**
	 * 热卖  最出售最多10条
	 */
	@RequestMapping(value = "/shop/hotshop", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson hotShopShow(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("no userToken", null);
		}
		
		return userService.hotShopShow(userToken);
	}
	
	/**
	 *  查看可出售商品
	 */
	@RequestMapping(value = "/shop/selectcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectCommodity(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		return userService.selectCommodity(userToken);
	}
	
	/**
	 *  查看可租赁商品
	 */
	@RequestMapping(value = "/shop/selectrentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectRentCommodity(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		return userService.selectRentCommodity(userToken);
	}
	
	
	
//	/**
//	 * 购买
//	 */
//	@RequestMapping(value = "/shop/buycommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson buyCommodity(@RequestBody UserTokenOne param) {
//		String userToken = param.getUserToken();
//		String commodityID = param.getString();
//		if((userToken == null) || (commodityID == null)) {
//			return RetJson.urlError("参数错误", null);
//		}
//		Commodity commodity = userService.makeCommodityOrder(userToken,commodityID,count,ip);
//		if(order == null) {
//			return RetJson.unknowError("buy commodity error", null);
//		}
//		return RetJson.success("success", order);
//	}
	
//	/**
//	 *  提交购买订单
//	 */
//	@RequestMapping(value = "/shop/pushorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson presentOrder(@RequestBody OrderInfo param) {
//		String userToken = param.getUserToken();
//		Order order = param.getOrder();
//		if(userToken == null || order == null) {
//			return RetJson.urlError("push order error", null);
//		}
//		if(!userService.presentOrder(userToken,order)) {
//			return RetJson.urlError("push order error", null);
//		}
//		return RetJson.success("success");
//	}
	
	/**
	 * 批量提交订单
	 */
	@RequestMapping(value = "/shop/commodity/batchorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson batchPresentOrder(@RequestBody OrderBatchInfo param) {
		String userToken = param.getUserToken();
		List<Order> orderList = param.getOrder();
		if(userToken == null || orderList == null) {
			return RetJson.urlError("push order error", null);
		}
		return userService.batchPresentOrder(userToken,orderList);
	}
	
	
	/**
	 * 租赁
	 */
//	@RequestMapping(value = "/shop/rentcommodity", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson rentCommodity(@RequestBody BuyCommodity param) {
//		String userToken = param.getUserToken();
//		long rentCommodityID = param.getCommodityID();
//		int count = param.getCount();
//		String ip = param.getIp();
//		if((userToken == null) || (rentCommodityID == 0) || (count == 0)) {
//			return RetJson.urlError("buy commodity error", null);
//		}
//		RentOrder rentOrder = userService.makeRentCommodityOrder(userToken,rentCommodityID,count,ip);
//		if(rentOrder == null) {
//			return RetJson.unknowError("用户不在线", null);
//		}
//		return RetJson.success("success", rentOrder);
//	}
	
//	/**
//	 *  提交租赁订单
//	 */
//	@RequestMapping(value = "/shop/pushrentorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson presentRentOrder(@RequestBody RentOrderInfo param) {
//		String userToken = param.getUserToken();
//		RentOrder rentOrder = param.getRentOrder();
//		if(userToken == null || rentOrder == null) {
//			return RetJson.urlError("push order error", null);
//		}
//		if(!userService.presentRentOrder(userToken,rentOrder)) {
//			return RetJson.urlError("push order error", null);
//		}
//		return RetJson.success("success");
//	}
	
	/**
	 * 批量提交租赁订单
	 */
	@RequestMapping(value = "/shop/commodity/batchrentorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson batchPresentOrder(@RequestBody RentOrderBatchInfo param) {
		String userToken = param.getUserToken();
		List<RentOrder> orderList = param.getRentOrder();
		if(userToken == null || orderList == null) {
			return RetJson.urlError("push order error", null);
		}
		return userService.batchPresentRentOrder(userToken,orderList);
	}
	
	
	/**
	 * 我的全部购买订单
	 */
	@RequestMapping(value = "/usercenter/order", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson allOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getAllOrder(userToken);
	}
	
	/**
	 * 订单详情
	 */
	@RequestMapping(value = "/usercenter/order/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson orderParticulars(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		
		if(userToken == null || id == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		return userService.getOrder(userToken,id);
	}
	
	
	/**
	 * 待付款订单列表
	 */
	@RequestMapping(value = "/usercenter/order/nopay", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  noPayOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 1);
	}
	
	/**
	 * 已付款订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/order/pay", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  isPayOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 2);
	}
	
	/**
	 * 待发货订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/order/nosend", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  noSendOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 3);
	}
	
	/**
	 * 待收货订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/order/issend", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  isSendOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 4);
	}
	
	/**
	 * 待评价订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/order/appraise", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  appraiseOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 5);
	}
	
	/**
	 * 交易完成
	 */
	@RequestMapping(value = "/usercenter/order/end", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  endOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 6);
	}
	
	
	/**
	 * 已取消订单
	 */
	@RequestMapping(value = "/usercenter/order/getcancel", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  getCancelOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeOrder(userToken, 0);
	}
	
	/**
	 * 取消订单
	 */
	@RequestMapping(value = "/usercenter/order/cancel", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  cancelOrder(@RequestBody UserTokenTwo param) {
		String userToken = param.getUserToken();
		String id = param.getOne();
		String reason = param.getTwo();
		
		if(userToken == null || id == null || reason == null) {
			return RetJson.urlError("缺少参数", null);
		}

		return userService.cancelOrder(userToken, id, reason);
	}
	
	/**
	 * 所有租赁订单
	 */
	@RequestMapping(value = "/usercenter/rentorder", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  allRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getAllRentOrder(userToken);
	}
	
	/**
	 * 租赁订单详情
	 */
	@RequestMapping(value = "/usercenter/rentorder/particulars", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson rentOrderParticulars(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		
		if(userToken == null || id == null) {
			return RetJson.urlError("logout error, please give me userToken", null);
		}
		return userService.getRentOrder(userToken,id);
	}
	
	/**
	 * 待付款租赁订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/rentorder/nopay", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  noPayRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 1);
	}
	
	/**
	 * 已付款订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/rentorder/pay", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  isPayRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 2);
	}
	
	/**
	 * 待发货订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/rentorder/nosend", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  noSendRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 3);
	}
	
	/**
	 * 待收货订单列表
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	@RequestMapping(value = "/usercenter/rentorder/issend", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  isSendRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 4);
	}
	
	/**
	 * 待评价订单列表
	 * 订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
	 */
	@RequestMapping(value = "/usercenter/rentorder/appraise", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  appraiseRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 5);
	}
	
	/**
	 * 租赁交易完成
	 */
	public RetJson  endRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 6);
	}
	
	/**
	 * 查看取消订单
	 */
	@RequestMapping(value = "/usercenter/rentorder/getcancel", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  getCancelRentOrder(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("present order error", null);
		}
		return userService.getTypeRentOrder(userToken, 0);
	}
	
	/**
	 * 取消租赁订单
	 */
	@RequestMapping(value = "/usercenter/rentorder/cancel", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson  cancelRentOrder(@RequestBody UserTokenTwo param) {
		String userToken = param.getUserToken();
		String id = param.getOne();
		String reason = param.getTwo();
		
		if(userToken == null || id == null || reason == null) {
			return RetJson.urlError("缺少参数", null);
		}
		return userService.cancelRentOrder(userToken, id, reason);
	}
	
	
	/**
	 * 购物车
	 */
	@RequestMapping(value = "/usercenter/shopcar", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson shopCar(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("list shopcar error", null);
		}
		return userService.getShopCar(userToken);
	}
	
	/**
	 * 购物车 增
	 */
	@RequestMapping(value = "/usercenter/shopcar/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addShopCar(@RequestBody ShopCarInfo param) {
		String userToken = param.getUserToken();
		ShopCar shopCar = param.getShopCar();
		if(userToken == null || shopCar == null) {
			return RetJson.urlError("add shopcar error", null);
		}
		return userService.addShopCar(userToken, shopCar);
	}
	
	/**
	 * 购物车 删
	 */
	@RequestMapping(value = "/usercenter/shopcar/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeShopCar(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("remove shopcar error", null);
		}
		return userService.removeShopCar(userToken, id);
	}
	
	/**
	 * 购物车 改
	 */
	@RequestMapping(value = "/usercenter/shopcar/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateShopCar(@RequestBody ShopCarInfo param) {
		String userToken = param.getUserToken();
		ShopCar shopCar = param.getShopCar();
		if(userToken == null || shopCar == null) {
			return RetJson.urlError("update shopcar error", null);
		}
		return userService.updateShopCar(userToken, shopCar);
	}

	
//	/**
//	 * 购物车结算
//	 */
//	@RequestMapping(value = "/usercenter/shopcar/settle", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public RetJson settleShopCar(@RequestBody UserTokenOne param) {
//		String userToken = param.getUserToken();
//		String id = param.getString();
//		if(userToken == null || id == null) {
//			return RetJson.urlError("remove shopcar error", null);
//		}
//		List<Order> list = userService.settleShopCar(userToken, id);
//		if(list == null) {
//			RetJson.unknowError("settle error", null);
//		}
//		
//		return RetJson.success("success",list);
//	}
	
	
	/**
	 * 收藏夹列表
	 */
	@RequestMapping(value = "/usercenter/collect", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listCollect(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("list collect error", null);
		}
		return userService.getCollect(userToken);
	}
	
	/**
	 * 收藏夹 增
	 */
	@RequestMapping(value = "/usercenter/collect/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addCollect(@RequestBody CollectInfo param) {
		String userToken = param.getUserToken();
		Collect collect = param.getCollect();
		if(userToken == null || collect == null) {
			return RetJson.urlError("add collect error", null);
		}
		return userService.addCollect(userToken, collect);
	}
	
	/**
	 * 收藏夹 删
	 */
	@RequestMapping(value = "/usercenter/collect/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeCollect(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("remove collect error", null);
		}
		
		return userService.removeCollect(userToken, id);
	}
	
	/**
	 * 判断商品是否已收藏
	 */
	@RequestMapping(value = "/usercenter/collect/iscollect", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson isCollect(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("参数错误", null);
		}
		return userService.isCollect(userToken, Long.parseLong(id));
		
	}
	
	/**
	 * 评价列表,根据物品ID查看评论
	 */
	@RequestMapping(value = "/usercenter/appraise", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listAppraise(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		long id = Long.parseLong(param.getString());
		if(userToken == null || id == 0) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.listAppraise(userToken, id);
	}
	
	/**
	 * 我的评价列表，根据用户ID查看评论
	 */
	@RequestMapping(value = "/usercenter/appraise/myappraise", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson listMyAppraise(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.listMyAppraise(userToken);
	}
	
	/**
	 * 评价 增
	 */
	@RequestMapping(value = "/usercenter/appraise/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addAppraise(@RequestBody AppraiseInfo param) {
		String userToken = param.getUserToken();
		Appraise appraise = param.getAppraise();
		if(userToken == null || appraise == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.addAppraise(userToken, appraise);
	}
	
	/**
	 * 评价 删
	 */
	@RequestMapping(value = "/usercenter/appraise/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeAppraise(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		long id = Long.parseLong(param.getString());
		if(userToken == null || id == 0) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.removeAppraise(userToken, id);
	}
	
	/**
	 * 地址 增
	 */
	@RequestMapping(value = "/usercenter/address/add", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson addAddress(@RequestBody AddressInfo param) {
		String userToken = param.getUserToken();
		Address address = param.getAddr();
		if(userToken == null || address == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.addAddress(userToken, address);
	}
	
	
	/**
	 * 地址 删
	 */
	@RequestMapping(value = "/usercenter/address/remove", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson removeAddress(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.removeAddress(userToken, id);
	}
	
	
	/**
	 * 地址 改
	 */
	@RequestMapping(value = "/usercenter/address/update", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson updateAddress(@RequestBody AddressInfo param) {
		String userToken = param.getUserToken();
		Address address = param.getAddr();
		if(userToken == null || address == null) {
			return RetJson.urlError("update shopcar error", null);
		}
		return userService.updateAddress(userToken, address);
	}
	
	/**
	 * 地址 查
	 */
	@RequestMapping(value = "/usercenter/address/select", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson selectAddress(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.selectAddress(userToken);
	}
	
	/**
	 * 设置默认地址
	 */
	@RequestMapping(value = "/usercenter/address/setmain", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson setMainAddr(@RequestBody UserTokenOne param) {
		String userToken = param.getUserToken();
		String id = param.getString();
		if(userToken == null || id == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.setMainAddr(userToken, Long.parseLong(id));
	}

	
	/**
	 * 获取默认地址
	 */
	@RequestMapping(value = "/usercenter/address/getmain", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson getMainAddr(@RequestBody UserToken param) {
		String userToken = param.getUserToken();
		if(userToken == null) {
			return RetJson.urlError("list appraise error", null);
		}
		return userService.getMainAddr(userToken);
	}
	
}
