package com.lyqxsc.yhpt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lyqxsc.yhpt.controller.WechartController;
import com.lyqxsc.yhpt.dao.IAddressDao;
import com.lyqxsc.yhpt.dao.IAppraiseDao;
import com.lyqxsc.yhpt.dao.ICollectDao;
import com.lyqxsc.yhpt.dao.ICommodityClassifyDao;
import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.IInvitationCodeDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IDeputyCommodityDao;
import com.lyqxsc.yhpt.dao.IDistributorDao;
import com.lyqxsc.yhpt.dao.IRentOrderDao;
import com.lyqxsc.yhpt.dao.IShopCarDao;
import com.lyqxsc.yhpt.dao.IUserDao;
import com.lyqxsc.yhpt.domain.Address;
import com.lyqxsc.yhpt.domain.Appraise;
import com.lyqxsc.yhpt.domain.Collect;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityBak;
import com.lyqxsc.yhpt.domain.CommodityClassify;
import com.lyqxsc.yhpt.domain.CommodityPage;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.DistributorBak;
import com.lyqxsc.yhpt.domain.DistributorCode;
import com.lyqxsc.yhpt.domain.WxHomePage;
import com.lyqxsc.yhpt.urlclass.ClassifyList;
import com.lyqxsc.yhpt.utils.RetJson;
import com.lyqxsc.yhpt.domain.HotCommodity;
import com.lyqxsc.yhpt.domain.InvitationCode;
import com.lyqxsc.yhpt.domain.NewCommodity;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.ShopCar;
import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.domain.UserInfo;

import net.sf.json.JSONObject;
@Service
public class UserService {
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IOrderDao orderDao;
	
	@Autowired
	IAddressDao addressDao;
	
	@Autowired
	IRentOrderDao rentOrderDao;
	
	@Autowired
	ICollectDao collectDao; 
	
	@Autowired
	IShopCarDao shopCarDao;
	
	@Autowired
	IAppraiseDao appraiseDao;
	
	@Autowired
	ICommodityDao commodityDao;
	
	@Autowired
	IDistributorDao distributorDao;
	
	@Autowired
	IInvitationCodeDao invitationCodeDao;
	
	@Autowired
	ICommodityClassifyDao commodityClassifyDao;
	
	@Value("${RentCommodityPic}")
	String rentCommodityPic;
	
	@Value("${CommodityPic}")
	String commodityPic;
	
	@Value("${HotPagePic}")
	String hotPagePic;
	
	@Value("${NewPagePic}")
	String newPagePic;
	
	@Value("${ShopPic}")
	String shopPic;
	
	@Value("${HomePic}")
	String homePic;
	
	static int NEWSHOPCOUNT = 10;
	
	// 在线用户集合<id,用户信息>
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	// 可出售的商品集合
	List<Commodity> commodityList = new ArrayList<Commodity>();
	
	static final Logger log = LoggerFactory.getLogger(WechartController.class);
	
	public boolean root() {
		User user = userDao.selectUserByOpenID("oACat1eA_RKT1zvIOvuZj4Obc3zQ");
		if(user == null) {
			return false;
		}
		long now = 1554642125630l;
		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getId());
		userInfo.setUsername(user.getOpenID());
		userInfo.setIp(user.getLastLoginIP());
		userInfo.setLoginTime(now);
		userInfo.setDistributor(user.getDistributor());
		userInfo.setGrade(2);
		
		String userToken = user.getId() + "O" + now;
		System.out.println(userToken);
		onlineMap.put(userToken, userInfo);
		return true;
	}
	
	/**
	 *  用户注册
	 * @param User
	 * @return -1 用户已存在
	 *         -2 注册失败
	 */
	public User signup(JSONObject wxUserInfo) {
		Long maxID = userDao.getMaxID();
		if(maxID == null) {
			return null;
		}
		
		byte[] result = Base64.decodeBase64((String)wxUserInfo.get("nickname"));
		
		User user = new User();
		user.setId(maxID+1);
		user.setOpenID((String)wxUserInfo.get("openid"));
		user.setNikeName(Base64.encodeBase64String(result));
//		user.setRealName(String realName);
//		user.setEmail(String email);
//		user.setPhone(String phone);
		user.setSex((int)wxUserInfo.get("sex")+"");
		user.setProvince((String)wxUserInfo.get("province"));
		user.setCity((String)wxUserInfo.get("city"));
		user.setHeadImgUrl((String)wxUserInfo.get("headimgurl"));
//		user.setAddress(String address);
		user.setWallet(0);
		user.setAuthority(1);
		
		System.out.println(user.getOpenID());
		System.out.println(user.getNikeName());
		System.out.println(user.getSex());
		System.out.println(user.getProvince());
		System.out.println(user.getCity());
		System.out.println(user.getHeadImgUrl());
		
		user.setAddTime(System.currentTimeMillis());

		if(userDao.addUser(user) < 0) {
			return null;
		}
		return user;
	}

	/**
	 *  用户登录，并添加到在线用户集合中
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public User login(JSONObject wxUserInfo, String ip) {
		UserInfo userInfo = new UserInfo();
		
		String openID = (String)wxUserInfo.get("openid");
		User user = userDao.selectUserByOpenID(openID);
		//如果第一次登录，注册
		if(user == null) {
			log.info("first login");
			user = signup(wxUserInfo);
			if(user == null) {
				log.warn("sigup error openID : " + openID);
				return null;
			}
		}
		//不是第一次登录，判断权限，缓存分销商信息
		else {
			if(user.getAuthority() != 1) {
				return null;
			}
			long distributorID = user.getDistributor();
			DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
			if(distributor == null) {
				return null;
			}
			userInfo.setGrade(distributor.getGrade());
			userInfo.setDistributor(user.getDistributor());
		}
		
		/*向数据库更新时间和本次登录的IP*/
		long now = Calendar.getInstance().getTime().getTime();
		userDao.updateLoginState(now, ip, openID);
		long id = user.getId();
		
		userInfo.setId(id);
		userInfo.setUsername(openID);
		userInfo.setIp(ip);
		userInfo.setLoginTime(now);
		
		
		/*如果用户在线，更新集合，如果不在线，添加集合*/
		Map<String, UserInfo> onlineMapTemp = new HashMap<String, UserInfo>();
		onlineMapTemp.putAll(onlineMap);
		for(String userToken:onlineMapTemp.keySet()) {
			if(id == Long.parseLong(userToken.split("O")[0])) {
				logout(userToken);
			}
		}
		
		String userToken = id + "O" + now;
		if(user.getAuthority() == 1) {
			onlineMap.put(userToken, userInfo);
		}
		/*更新返回前端的user IP*/
		user.setUserToken(userToken);
		user.setThisLoginIP(ip);
		user.setThisLoginTime(now);
		return user;
	}

	/**
	 *  用户注销,获取当前时间 
	 * @param openid
	 * @return
	 */
	public RetJson logout(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		//更新退出时间
		userDao.updateLogoutState(userInfo.getLoginTime(), userInfo.getIp(), userInfo.getUsername());
		
		onlineMap.remove(userToken);
		return RetJson.success("成功");
	}
	
	/**
	 * 判断用户凭证是否在集合中
	 */
	public RetJson isOutOfDate(String userToken) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		return RetJson.success("凭证有效",null);
	}
	
	/**
	 * 添加邀请码
	 */
	public RetJson addInvitationCode(String userToken, String code) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		long userID = userInfo.getId();
		
		//如果已经绑定则返回
		if(userInfo.getDistributor() != 0) {
			return RetJson.unknowError("已绑定分销商", null);
		}
		
		//查询邀请码
		InvitationCode invitationCode = invitationCodeDao.selectInvitationCodeByCode(code);
		if(invitationCode == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		//获取邀请码分销商ID
		long distributorID = invitationCode.getDistributorID();
		if(1 != userDao.bindDistributor(userID, distributorID)) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		//更新绑定分销商的用户数
		if(1 != distributorDao.updateUserNum(distributorID)) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		//绑定邀请码，邀请码失效
		if(1 != invitationCodeDao.bindInvitationCode(userID, code)) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		//更新缓存
		userInfo.setDistributor(distributorID);
		userInfo.setGrade(distributorDao.getGrade(distributorID));
		onlineMap.replace(userToken, userInfo);
		return RetJson.success("成功");
	}
	
	/**
	 *	不填写邀请码，直接进入
	 */
	public RetJson noInvitationCode(String userToken) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证过期", null);
		}
		long userID = userInfo.getId();
		
		//通过ID获取用户信息
		User user = userDao.selectUserByID(userID);
		if(user == null) {
			return RetJson.mysqlError("数据库连接错误", null);
		}
		
		//如果用户已绑定，这返回
		if(user.getDistributor() != 0) {
			return RetJson.success("已绑定分销商", null);
		}
		
		//通过微信地址分配分销商
		String province = user.getProvince();
		String city = user.getCity();
		List<DistributorBak> distributorList = distributorDao.getDistributorByCity(city);
		if(distributorList.isEmpty()) {
			distributorList = distributorDao.getDistributorByProvince(province);
			if(distributorList.isEmpty()) {
				distributorList = distributorDao.getDistributorByUserNum();
			}
		}
		
		//选择符合要求的第一个分销商
		DistributorBak distributor = distributorList.get(0);
		
		//分销商添加用户数
		long distributorID = distributor.getId();
		if(1 != distributorDao.updateUserNum(distributorID)) {
			log.info("分销商添加用户失败");
			return RetJson.mysqlError("数据库连接错误", null);
		}
		
		//用户绑定分销商
		if(1 != userDao.bindDistributor(userID, distributorID)) {
			return RetJson.mysqlError("数据库连接错误", null);
		}
		
		String code = invitationCodeDao.getInvitationCode(distributorID);
		if(code == null) {
			return RetJson.mysqlError("数据库连接错误", null);
		}
		
		//更新缓存
		userInfo.setDistributor(distributorID);
		userInfo.setGrade(distributorDao.getGrade(distributorID));
		onlineMap.replace(userToken, userInfo);
		
		DistributorCode distributorCode = new DistributorCode();
		distributorCode.setCode(code);
		distributorCode.setDistributorID(distributorID);
		//绑定成功，返回分销商ID和邀请码
		return RetJson.success("绑定成功", distributorCode);
	}
	
	/**
	 * 获取邀请码
	 */
	public String getInvitationCode(long distributorID) {
		if(distributorID == 0) {
			return null;
		}
		return invitationCodeDao.getInvitationCode(distributorID);
	}
	
	
	/**
	 * 首页
	 * @param userToken
	 * @return
	 */
	public RetJson homePage(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		WxHomePage homePage = new WxHomePage();
		homePage.setPic(homePic.split(";"));
		
		long distributorID = userInfo.getDistributor();
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
		List<CommodityBak> commodityList = commodityDao.selectAllCommodityForUser(distributorID);
		List<CommodityBak> commodity = hidePriceList(commodityList,distributor.getGrade());
		homePage.setCommodityList(commodity);
		return RetJson.success("成功",homePage);
	}
	
	/**
	 * 关于我们
	 */
	public RetJson aboutUs(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		String msg = "关于我们";
		return RetJson.success("成功", msg);
	}
	
	/**
	 * 商城
	 */
	//TODO  home page
	public RetJson shop(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		WxHomePage homePage = new WxHomePage();
		
		homePage.setPic(shopPic.split(";"));
		
		long distributorID = userInfo.getDistributor();
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
		List<CommodityBak> commodityList = commodityDao.selectAllCommodityForUser(distributorID);
		List<CommodityBak> commodity = hidePriceList(commodityList,distributor.getGrade());
		homePage.setCommodityList(commodity);
		return RetJson.success("成功", homePage);
	}
	
	/**
	 * 分类列表
	 */
	public RetJson classList(String userToken){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		ClassifyList classifyList = new ClassifyList();
		classifyList.setAgentia(commodityClassifyDao.selectClass(1));
		classifyList.setMechanical(commodityClassifyDao.selectClass(2));
		return RetJson.success("成功", classifyList);
	}
	
	/**
	 * 按种类，价格区间 查询物品集合
	 */
	public RetJson selectCommodityByClass(String userToken,String classId, String price){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorID = userInfo.getDistributor();
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
		int grade = distributor.getGrade();
		
		if(distributorID == 0) {
			return RetJson.unknowError("未知错误", null);
		}
		
		String[] pBuf = price.split("!",2);
		float min = 0;
		float max = 0;
		
		if(pBuf[0].isEmpty()) {
			min = 0;
		}
		else {
			min = Float.parseFloat(pBuf[0]);
		}
		if(pBuf[1].isEmpty()) {
			max = 9999999;
		}
		else {
			max = Float.parseFloat(pBuf[1]);
		}

		List<CommodityBak> commodityList = new ArrayList<CommodityBak>();
		String[] classIdBuf = classId.split("!");
		for(String listTemp:classIdBuf) {
			List<CommodityBak> list = new ArrayList<CommodityBak>();
			switch (grade) {
			case 0:
				list = commodityDao.selectCommodityBakByClass(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			case 1:
				list = commodityDao.selectCommodityBakByClass1(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			case 2:
				list = commodityDao.selectCommodityBakByClass2(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			case 3:
				list = commodityDao.selectCommodityBakByClass3(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			case 4:
				list = commodityDao.selectCommodityBakByClass4(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			case 5:
				list = commodityDao.selectCommodityBakByClass5(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			case 6:
				list = commodityDao.selectCommodityBakByClass6(distributorID, Integer.parseInt(listTemp), min, max);
				break;
			default:
				break;
			}
			commodityList.addAll(hidePriceList(list, grade));
		}
		return RetJson.success("成功", commodityList);
	}
	
	/**
	 * 按名称查询
	 */
	//TODO 后期添加模糊查询
	public RetJson selectCommodityByName(String userToken,String name){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorID = userInfo.getDistributor();
		if(distributorID == 0) {
			return RetJson.unknowError("未知错误", null);
		}
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
		int grade = distributor.getGrade();
		List<CommodityBak> listTemp = commodityDao.selectCommodityBakByName(distributorID,name);
		List<CommodityBak> list = hidePriceList(listTemp, grade);
		return RetJson.success("成功", list);
	}
	
	/**
	 * 新品   展示出售商品 maxID-10 范围内的商品
	 */
	//TODO 效率低，后期改为后端定时统计数据，有前端直接取值
	public RetJson newShopShow(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorID = userInfo.getDistributor();
		int grade = userInfo.getGrade();
		long commodityMaxID = commodityDao.getMaxID();
		
		NewCommodity commodity = new NewCommodity();
		List<CommodityBak> commodityList = new ArrayList<CommodityBak>();
		
		while(commodityList.size() < 10) {
			CommodityBak temp =  commodityDao.selectNewCommodityBakByDistributor(distributorID,commodityMaxID);
			if(temp != null) {
				commodityList.add(hidePrice(temp, grade));
			}
			commodityMaxID = commodityMaxID - 1;
			if(commodityMaxID < 1) {
				break;
			}
		}
		commodity.setCommodity(commodityList);
		commodity.setPic(newPagePic.split(";"));
		return RetJson.success("成功",commodity);
	}
	
	/**
	 * 热卖品   
	 */
	//TODO 效率低，后期改为后端定时统计数据，有前端直接取值
	public RetJson hotShopShow(String userToken) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorID = userInfo.getDistributor();
		int grade = userInfo.getGrade();
		Integer maxNum = commodityDao.getMaxOrdernum();
		
		HotCommodity commodity = new HotCommodity();
		List<CommodityBak> commodityList = new ArrayList<CommodityBak>();
		
		while(commodityList.size() < 10) {
			List<CommodityBak> temp =  commodityDao.selectHotCommodityBakByDistributor(distributorID,maxNum);
			if(temp != null) {
				List<CommodityBak> list = hidePriceList(temp, grade);
				for(CommodityBak obj:list) {
					commodityList.add(obj);
				}
			}
			maxNum = maxNum - 1;
			if(maxNum < 2) {
				break;
			}
		}
		commodity.setCommodity(commodityList);
		commodity.setPic(homePic.split(";"));
		return RetJson.success("成功", commodity);
	}
	
	/**
	 *  查看可出售商品
	 */
	public RetJson selectCommodity(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorID = userInfo.getDistributor();
		
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
		int grade = distributor.getGrade();
		List<CommodityBak> commodityListTemp = commodityDao.selectCommodityBakForUser(distributorID);
		List<CommodityBak> commodity = hidePriceList(commodityListTemp, grade);
		
		CommodityPage commodityPage = new CommodityPage();
		commodityPage.setCommodity(commodity);
		commodityPage.setPic(commodityPic.split(";"));
		return RetJson.success("成功", commodityPage);
	}
	
	
	/**
	 *  查看可租赁商品
	 */
	public RetJson selectRentCommodity(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorId = userInfo.getDistributor();
		
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorId);
		int grade = distributor.getGrade();
		List<CommodityBak> commodityListTemp = commodityDao.selectRentCommodityBakForUser(distributorId);
		List<CommodityBak> commodity = hidePriceList(commodityListTemp, grade);
		
		CommodityPage commodityPage = new CommodityPage();
		commodityPage.setCommodity(commodity);
		commodityPage.setPic(rentCommodityPic.split(";"));
		return RetJson.success("成功",commodityPage);
	}
	
	/**
	 * 根据商品id查询商品，多个物品用！隔开
	 */
	public RetJson selectCommodityByID(String userToken,String idbuf) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long distributorId = userInfo.getDistributor();
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorId);
		int grade = distributor.getGrade();
		
		String[] idStr = idbuf.split("!");
		List<CommodityBak> commodityList = new ArrayList<CommodityBak>();
		for(String id:idStr) {
			CommodityBak commodity = commodityDao.selectCommodityBakByIDForUser(Long.parseLong(id),distributorId);
			commodityList.add(commodity);
		}
		List<CommodityBak> commodity = hidePriceList(commodityList, grade);
		return RetJson.success("成功",commodity);
	}
	
	/**
	 *  点击购买，生成订单
	 * @param id
	 * @param commodityid
	 * @return
	 */
	public RetJson makeCommodityOrder(String userToken, long commodityid, int count, String ip) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long now = Calendar.getInstance().getTime().getTime();
		Commodity commodity = commodityDao.selectCommodityByID(commodityid);
		String username = userDao.selectUsername(userInfo.getId());
		float price = commodity.getPrice();
		
		Order order = new Order();
		order.setOrderNumber(userInfo.getId() + "OO" + now);
		order.setOwner(userInfo.getId());
		order.setOwnerName(username);
		order.setCommodityID(commodity.getId());
		order.setUrl(commodity.getPicurl());
		order.setCommodityName(commodity.getName());
		order.setPrice(price);
		order.setCount(count);
		order.setTotalPrice(count*price);
		order.setPayMoney(0);
		order.setOrderPrice(count*price);
		order.setCompleteTime(0);
		order.setPayOrdertime(System.currentTimeMillis());
		order.setStatus(0);
		order.setPayType(0);
		order.setPayIP(ip);
		order.setLastPayStatus(0);
		order.setAddrId(0);
		
		return RetJson.success("成功", order);
	}
	
	/**
	 *  提交订单
	 */
	public RetJson presentOrder(String userToken, Order order) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(orderDao.addOrderList(order) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 批量提交订单
	 * 	订单号	String orderNumber = 用户ID + 系统时间 + 物品id + 索引
	 *	商品		long commodityID;
	 *	商品单价	float price;
	 *	购买数量	int count;
	 *	订单金额	float orderPrice;
	 *	收货地址	long addrid;
	 */
	public RetJson batchPresentOrder(String userToken, List<Order> orderList){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		//读取用户信息
		long userId = userInfo.getId();
		User user = userDao.selectUserByID(userId);
		//读取分销商信息
		long distributorID = userInfo.getDistributor();
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);

		int num = 0;
		for(Order order:orderList) {
			long now = System.currentTimeMillis();
			long commodityID = order.getCommodityID();
			float price = order.getPrice();
			int count = order.getCount();
			long addrid = order.getAddrId();
			float totalPrice = count*price;
			//读取商品信息
			Commodity commodity = commodityDao.selectCommodityByID(commodityID);
			//读取地址信息
			Address address = addressDao.selectAddress(addrid);
			
			order.setOrderNumber(userInfo.getId() + "O" + now + "O" + commodityID + "O" + num);
			order.setOwner(userInfo.getId());
			order.setOwnerName(user.getRealName());
			order.setDistributorID(distributorID);
			order.setCommodityID(commodityID);
			order.setCommodityName(commodity.getName());
			order.setTotalPrice(totalPrice);
			order.setPayMoney(0);
			order.setCompleteTime(0);
			order.setPayOrdertime(now);
			order.setStatus(1);
			order.setPayType(0);
			order.setPayIP("");
			order.setLastPayStatus(0);
			order.setAddr(address.getAddr());
			commodityDao.updatePayOrderNum(commodityID);
			commodityDao.updateSalesVolume(count, totalPrice, commodityID);
			if(orderDao.addOrderList(order) != 1) {
				return RetJson.mysqlError("数据库连接异常", null);
			}
			num++;
		}
		userDao.addOrderNum(num,userInfo.getId());
		distributorDao.addOrderNum(num, distributor.getId());
		
		return RetJson.success("成功");
	}
	
	
	/**
	 * 点击租赁，生成订单
	 * @param id
	 * @param commodityid
	 * @return
	 */
	public RetJson makeRentCommodityOrder(String userToken, long id, int count, String ip) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long now = Calendar.getInstance().getTime().getTime();
		Commodity rentCommodity = commodityDao.selectCommodityByID(id);
		if(rentCommodity.getDeposit() == 0) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		String username = userDao.selectUsername(userInfo.getId());
		float price = rentCommodity.getPrice();
		float deposit = rentCommodity.getDeposit(); 
		
		RentOrder rentOrder = new RentOrder();
		rentOrder.setOrderNumber(userInfo.getId() + "II" + now);
		rentOrder.setOwner(userInfo.getId());
		rentOrder.setOwnerName(username);
		rentOrder.setRentCommodityID(rentCommodity.getId());
		rentOrder.setUrl(rentCommodity.getPicurl());
		rentOrder.setRentCommodityName(rentCommodity.getName());
		rentOrder.setRentPrice(price);
		rentOrder.setDeposit(deposit);
		rentOrder.setCount(count);
		rentOrder.setTotalDeposit(count*deposit);
		rentOrder.setTotalPrice(count*price);
		rentOrder.setOrderPrice(count*price + count*deposit);
		rentOrder.setPayMoney(0);
		rentOrder.setCompleteTime(0);
		rentOrder.setMakeOrdertime(System.currentTimeMillis());
		rentOrder.setStatus(0);
		rentOrder.setPayType(0);
		rentOrder.setPayIP(ip);
		rentOrder.setLastPayStatus(0);
		rentOrder.setAddr(null);
		
		return RetJson.success("成功");
	}
	
	/**
	 *  提交租赁订单
	 */
	public RetJson presentRentOrder(String userToken, RentOrder rentOrder) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(rentOrderDao.addRentOrderList(rentOrder) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 批量提交租赁订单
	 * 	商品ID		long rentCommodityID;
	 *	商品单价		float rentPrice;
	 *	租赁数量		int count;
	 *	订单金额		float orderPrice;
	 *	收货地址ID	long addrId;
	 */
	public RetJson batchPresentRentOrder(String userToken, List<RentOrder> rentOrderList) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		//读取用户信息
		long userId = userInfo.getId();
		User user = userDao.selectUserByID(userId);
		
		//读取分销商信息
		long distributorID = userInfo.getDistributor();
		DistributorBak distributor = distributorDao.selectDistributorByID(distributorID);
		
		int num = 0;
		for(RentOrder order:rentOrderList) {
			long now = System.currentTimeMillis();
			long rentCommodityID = order.getRentCommodityID();
			float rentPrice = order.getRentPrice();
			int count = order.getCount();
			float orderPrice = order.getOrderPrice();
			long addrid = order.getAddrId();
			
			//读取商品信息
			Commodity commodity = commodityDao.selectCommodityByID(rentCommodityID);
			float deposit = commodity.getDeposit();
			//读取地址信息
			Address address = addressDao.selectAddress(addrid);
			
			order.setOrderNumber(userInfo.getId() + "O" + now + "O" + rentCommodityID + "O" + num);
			order.setOwner(userInfo.getId());
			order.setOwnerName(user.getRealName());
			order.setDistributorID(distributorID);
			order.setRentCommodityID(rentCommodityID);
			order.setRentCommodityName(commodity.getName());
			order.setDeposit(deposit);
//			order.setFreight();
			order.setTotalDeposit(deposit*count);
			order.setTotalPrice((rentPrice+deposit)*count);
			order.setMakeOrdertime(now);
			order.setStatus(1);
			order.setPayType(0);
			order.setLastPayStatus(0);
			order.setAddr(address.getAddr());

			commodityDao.updateRentOrderNum(rentCommodityID);
			commodityDao.updateRentVolume(count,rentPrice*count,rentCommodityID);
			if(rentOrderDao.addRentOrderList(order) != 1) {
				return RetJson.mysqlError("数据库连接异常", null);
			}
			num++;
		}
		userDao.addRentOrderNum(num,userInfo.getId());
		distributorDao.addRentOrderNum(num, distributor.getId());
		return RetJson.success("成功");
	}
	
	/**
	 * 获取全部购买订单
	 */
	public RetJson getAllOrder(String userToken){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long id = userInfo.getId();
		List<Order> orderList = orderDao.getAllOrderByUser(id);
		return RetJson.success("成功",orderList);
	}
	
	/**
	 * 购买订单详情
	 */
	public RetJson getOrder(String userToken, String id){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Order order = orderDao.getOrderByID(id);
		return RetJson.success("成功",order);
	}
	
	/**
	 * 取消购买订单
	 */
	//TODO 向管理员推送消息
	public RetJson cancelOrder(String userToken,String id,String reason) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		if(orderDao.updateOrderList(0,id,reason) == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	/**
	 * 返回待付款/已付款订单
	 */
	public RetJson getTypeOrder(String userToken, int type){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long id = userInfo.getId();
		List<Order> orderList = orderDao.getOrderStatusByUser(id,type);
		return RetJson.success("成功",orderList);
	}
	
	/**
	 * 租赁订单列表
	 */
	public RetJson getAllRentOrder(String userToken){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long id = userInfo.getId();
		List<RentOrder> orderList = rentOrderDao.getAllRentOrderByID(id);
		return RetJson.success("成功",orderList);
	}
	
	/**
	 * 租赁订单详情
	 */
	public RetJson getRentOrder(String userToken, String id){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		RentOrder order = rentOrderDao.listOneRentOrder(id);
		return RetJson.success("成功", order);
	}
	
	
	/**
	 * 取消租赁订单
	 */
	public RetJson cancelRentOrder(String userToken,String id,String reason) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		if(rentOrderDao.updateRentOrderList(0,id,reason) == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	/**
	 * 返回待付款/已付款订单
	 */
	public RetJson getTypeRentOrder(String userToken, int type){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long id = userInfo.getId();
		List<RentOrder> orderList = rentOrderDao.getTypeRentOrderByID(id,type);
		return RetJson.success("成功",orderList);
	}
	
	
	/**
	 * 购物车清单
	 */
	public RetJson getShopCar(String userToken){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long id = userInfo.getId();
		List<ShopCar> shopList = shopCarDao.getShoppingByUserID(id);
		return RetJson.success("成功", shopList);
	}
	
	/**
	 * 购物车 增
	 */
	public RetJson addShopCar(String userToken, ShopCar shopCar) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		long userid = userInfo.getId();
		long commodityid = shopCar.getCommodityid();
		ShopCar shopCarTemp = shopCarDao.isExist(userid, commodityid);
		if(shopCarTemp != null) {
			shopCarTemp.setCount(shopCarTemp.getCount()+1);
			shopCarDao.updateShopCar(shopCarTemp);
			return RetJson.success("购物车已存在");
		}
		
		Commodity commodity = commodityDao.selectCommodityByID(commodityid);
		
		String name = commodity.getName();
		String picurl = commodity.getPicurl();
		int inventory = commodity.getInventory();
		String note = commodity.getNote();
		
		shopCar.setUserid(userid);
		shopCar.setCount(1);
		shopCar.setName(name);
		shopCar.setPicurl(picurl);
		shopCar.setInventory(inventory);
		shopCar.setNote(note);
		
		int ret = shopCarDao.addShopCar(shopCar);
		if(ret == 1) {
			return RetJson.success("购物车添加成功");
		}
		return RetJson.mysqlError("购物车添加失败", null);
	}
	
	/**
	 * 购物车 删
	 */
	public RetJson removeShopCar(String userToken, String idStr) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		String[] idbuf = idStr.split("!");
		
		for(String id:idbuf) {
			int ret = shopCarDao.removeShopCar(Long.parseLong(id));
			if(ret != 1) {
				return RetJson.mysqlError("数据库连接异常", null);
			}
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 购物车 改
	 */
	public RetJson updateShopCar(String userToken, ShopCar shopCar) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		int ret = shopCarDao.updateShopCar(shopCar);
		if(ret == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
//	/**
//	 * 购物车结账
//	 */
//	public List<Order> settleShopCar(String userToken, String idbuf){
//		UserInfo userInfo = onlineMap.get(userToken);
//		if(userInfo == null) {
//			return null;
//		}
//		String[] idStr = idbuf.split("!");
//		List<Order> orderList = new ArrayList<Order>();
//		for(String id:idStr) {
//			ShopCar shopCar = shopCarDao.getShoppingByID(Long.parseLong(id));
//			orderList.add(makeCommodityOrder(userToken,shopCar.getCommodityid(),shopCar.getCount(),userInfo.getIp()));
//		}
//		return orderList;
//	}
	
	/**
	 * 收藏夹清单 
	 */
	public RetJson getCollect(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long id = userInfo.getId();
		List<Collect> collectList = collectDao.getShoppingByID(id);
		return RetJson.success("成功",collectList);
	}
	
	/**
	 * 收藏夹 增
	 */
	public RetJson addCollect(String userToken, Collect collect) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long userid = userInfo.getId();
		long commodityId = collect.getCommodityid();
		if(collectDao.isExist(commodityId, userid) != null) {
			return RetJson.unknowError("物品已收藏", null);
		}
		
		Commodity commodity = commodityDao.selectCommodityByID(commodityId);
		
		String name = commodity.getName();
		String picurl = commodity.getPicurl();
		collect.setUserid(userid);
		collect.setName(name);
		collect.setPicurl(picurl);
		
		collectDao.addCollect(collect);
		return RetJson.success("收藏商品成功");
	}
	
	/**
	 * 收藏夹 删
	 */
	public RetJson removeCollect(String userToken, String idStr) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		String[] idbuf = idStr.split("!");
		
		for(String id:idbuf) {
			int ret = collectDao.removeCollect(Long.parseLong(id));
			if(ret != 1) {
				return RetJson.mysqlError("数据库连接异常", null);
			}
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 判断商品是否已收藏
	 */
	public RetJson isCollect(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Collect collect = collectDao.isExist(id, userInfo.getId());
		if(collect == null) {
			return RetJson.success("商品未收藏");
		}
		return RetJson.success("商品已收藏", collect);
	}
	
	/**
	 * 评价列表,根据物品ID查看评论
	 */
	public RetJson listAppraise(String userToken, long id){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<Appraise> appraiseList = appraiseDao.getAppraiseByThingID(id);
		return RetJson.success("成功", appraiseList);
	}
	
	/**
	 * 我的评价列表，根据用户ID查看评论
	 */
	public RetJson listMyAppraise(String userToken){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long id = userInfo.getId();
		
		List<Appraise> appraiseList = appraiseDao.getAppraiseByUserID(id);
		return RetJson.success("成功",appraiseList);
	}
	
	/**
	 * 评价 增
	 * 后端自填：id,userID,username,time
	 * 前端必填：thingID,text,grade,describe,logistics,service
	 */
	public RetJson addAppraise(String userToken, Appraise appraise) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long maxID = appraiseDao.getMaxID();
		if(maxID == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		long userID = userInfo.getId();
		User user = userDao.selectUserByID(userID);
		String username = user.getNikeName();
		
		appraise.setId(maxID+1);
		appraise.setUserID(userID);
		appraise.setUsername(username);
		appraise.setTime(System.currentTimeMillis());
		System.out.println(appraise.getDescribe());
		int ret = appraiseDao.addAppraise(appraise);
		if(ret == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	/**
	 * 评价 删
	 */
	public RetJson removeAppraise(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		
		//TODO 检查
		long userID = userInfo.getId();
		int ret = appraiseDao.removeAppraise(id);
		if(ret == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	/**
	 * 收货地址 增
	 */
	public RetJson addAddress(String userToken, Address addr) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Long maxID = addressDao.getMaxID();
		if(maxID == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		addr.setId(maxID + 1);
		addr.setUserId(userInfo.getId());
		int ret = addressDao.addAddress(addr);
		System.out.println(ret);
		if(ret == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	/**
	 * 收货地址 删
	 */
	public RetJson removeAddress(String userToken, String idStr) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long userId = userInfo.getId();
		String[] idBuf = idStr.split("!");
		for(String id:idBuf) {
			if(addressDao.removeAddress(userId, Long.parseLong(id)) != 1) {
				return RetJson.mysqlError("数据库连接异常", null);
			}
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 收货地址 改
	 */
	//TODO 默认地址
	public RetJson updateAddress(String userToken, Address addrTemp) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long id = addrTemp.getId();
		Address address = addressDao.selectAddress(id);
		
		String username = addrTemp.getUsername();
		String phone = addrTemp.getPhone();
		String addr = addrTemp.getAddr();
		int main = addrTemp.getMain();
		
		if(username != null) {
			address.setUsername(username);
		}
		if(phone != null) {
			address.setPhone(phone);
		}
		if(addr != null) {
			address.setAddr(addr);
		}
		int ret = addressDao.updateAddress(address);
		if(ret == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	
	/**
	 * 收货地址 查
	 */
	public RetJson selectAddress(String userToken){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long userId = userInfo.getId();
		List<Address> addr = addressDao.selectAddressByUser(userId);
		return RetJson.success("成功", addr);
	}
	
	/**
	 * 设置默认地址
	 */
	public RetJson setMainAddr(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long userId = userInfo.getId();
		List<Address> addrList = addressDao.selectAddressByUser(userId);
		for(Address addr:addrList) {
			if(addr.getId() == id) {
				addr.setMain(1);
				addressDao.updateAddress(addr);
				continue;
			}
			if(addr.getMain() == 1) {
				addr.setMain(0);
				addressDao.updateAddress(addr);
			}
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 获取默认地址
	 */
	public RetJson getMainAddr(String userToken) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		long userId = userInfo.getId();
		Address ret = null;
		List<Address> addrList = addressDao.selectAddressByUser(userId);
		for(Address addr:addrList) {
			ret = addr;
			if(addr.getMain() == 1) {
				break;
			}
		}
		return RetJson.success("成功",ret);
	}
	/**
	 * 伪造列表价格
	 */
	private List<CommodityBak> hidePriceList(List<CommodityBak> commodityListTemp,int grade){
		List<CommodityBak> commodity = new ArrayList<CommodityBak>();
		for(CommodityBak commodityBak:commodityListTemp) {
			switch (grade) {
			case 1:
				commodityBak.setPrice(commodityBak.getPrice1());
				commodityBak.setRentPrice(commodityBak.getRentPrice1());
				break;
			case 2:
				commodityBak.setPrice(commodityBak.getPrice2());
				commodityBak.setRentPrice(commodityBak.getRentPrice2());
				break;
			case 3:
				commodityBak.setPrice(commodityBak.getPrice3());
				commodityBak.setRentPrice(commodityBak.getRentPrice3());
				break;
			case 4:
				commodityBak.setPrice(commodityBak.getPrice4());
				commodityBak.setRentPrice(commodityBak.getRentPrice4());
				break;
			case 5:
				commodityBak.setPrice(commodityBak.getPrice5());
				commodityBak.setRentPrice(commodityBak.getRentPrice5());
			case 6:
				commodityBak.setPrice(commodityBak.getPrice6());
				commodityBak.setRentPrice(commodityBak.getRentPrice6());
				break;
			default:
				break;
			}
			commodity.add(commodityBak);
		}
		return commodity;
	}
	
	/**
	 * 伪造价格
	 */
	private CommodityBak hidePrice(CommodityBak commodityBak, int grade){
		switch (grade) {
		case 1:
			commodityBak.setPrice(commodityBak.getPrice1());
			commodityBak.setRentPrice(commodityBak.getRentPrice1());
			break;
		case 2:
			commodityBak.setPrice(commodityBak.getPrice2());
			commodityBak.setRentPrice(commodityBak.getRentPrice2());
			break;
		case 3:
			commodityBak.setPrice(commodityBak.getPrice3());
			commodityBak.setRentPrice(commodityBak.getRentPrice3());
			break;
		case 4:
			commodityBak.setPrice(commodityBak.getPrice4());
			commodityBak.setRentPrice(commodityBak.getRentPrice4());
			break;
		case 5:
			commodityBak.setPrice(commodityBak.getPrice5());
			commodityBak.setRentPrice(commodityBak.getRentPrice5());
		case 6:
			commodityBak.setPrice(commodityBak.getPrice6());
			commodityBak.setRentPrice(commodityBak.getRentPrice6());
			break;
		default:
			break;
		}
		return commodityBak;
	}

//	@Override
//	public void afterPropertiesSet() throws Exception {
//		User user = userDao.selectUser("oACat1eA_RKT1zvIOvuZj4Obc3zQ");
//		long now = 1554642125630l;
//		UserInfo userInfo = new UserInfo();
//		userInfo.setId(user.getId());
//		userInfo.setUsername(user.getOpenID());
//		userInfo.setIp(user.getLastLoginIP());
//		userInfo.setLoginTime(now);
//		
//		String userToken = user.getId() + "O" + now;
//		System.out.println(userToken);
//		onlineMap.put(userToken, userInfo);
//	}
}
