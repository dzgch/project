package com.lyqxsc.yhpt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyqxsc.yhpt.controller.WechartController;
import com.lyqxsc.yhpt.dao.IAddressDao;
import com.lyqxsc.yhpt.dao.IAppraiseDao;
import com.lyqxsc.yhpt.dao.ICollectDao;
import com.lyqxsc.yhpt.dao.ICommodityClassifyDao;
import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.IInvitationCodeDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IRentCommodityDao;
import com.lyqxsc.yhpt.dao.IRentOrderDao;
import com.lyqxsc.yhpt.dao.IShopCarDao;
import com.lyqxsc.yhpt.dao.IUserDao;
import com.lyqxsc.yhpt.domain.Address;
import com.lyqxsc.yhpt.domain.Appraise;
import com.lyqxsc.yhpt.domain.Collect;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityClassify;
import com.lyqxsc.yhpt.domain.HomePage;
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
public class UserService implements InitializingBean{
	
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
	
//	@Autowired
//	IRentCommodityDao rentCommodityDao;
	
	@Autowired
	IInvitationCodeDao invitationCodeDao;
	
	@Autowired
	ICommodityClassifyDao commodityClassifyDao;
	
	static int NEWSHOPCOUNT = 10;
	
	// 在线用户集合<id,用户信息>
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	// 可出售的商品集合
	List<Commodity> commodityList = new ArrayList<Commodity>();
	
	static final Logger log = LoggerFactory.getLogger(WechartController.class);
	
	public void afterPropertiesSet() {
		User user = userDao.selectUser("oACat1eA_RKT1zvIOvuZj4Obc3zQ");
		long now = 1554642125630l;
		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getId());
		userInfo.setUsername(user.getOpenID());
		userInfo.setIp(user.getLastLoginIP());
		userInfo.setLoginTime(now);
		
		String userToken = user.getId() + "O" + now;
		onlineMap.put(userToken, userInfo);
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
		
		User user = new User();
		user.setId(maxID+1);
		user.setOpenID((String)wxUserInfo.get("openid"));
		user.setNikeName((String)wxUserInfo.get("nickname"));
//		user.setRealName(String realName);
//		user.setEmail(String email);
//		user.setPhone(String phone);
		user.setSex((int)wxUserInfo.get("sex"));
		user.setProvince((String)wxUserInfo.get("province"));
		user.setCity((String)wxUserInfo.get("city"));
		user.setHeadImgUrl((String)wxUserInfo.get("headimgurl"));
//		user.setAddress(String address);
		user.setWallet(0);

		//TODO 添加分销商（0为总部，记得修改）
		user.setDistributor(0);
		user.setAddTime(System.currentTimeMillis());

		//TODO
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
		String openID = (String)wxUserInfo.get("openid");
		
		User user = userDao.selectUser(openID);
		if(user == null) {
			user = signup(wxUserInfo);
			if(user == null) {
				log.warn("sigup error openID : " + openID);
				return null;
			}
		}
		/*向数据库更新时间和本次登录的IP*/
		long now = Calendar.getInstance().getTime().getTime();
		userDao.updateLoginState(now, ip, openID);
		
		long id = user.getId();
		
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setUsername(openID);
		userInfo.setIp(ip);
		userInfo.setLoginTime(now);
		
		/*如果用户在线，更新集合，如果不在线，添加集合*/
		for(String userToken:onlineMap.keySet()) {
			if(id == Long.parseLong(userToken.split("O")[0])) {
				logout(userToken);
			}
		}
		
		String userToken = id + "O" + now;
		onlineMap.put(userToken, userInfo);
		
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
	public boolean logout(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			System.out.println("logout error");
			return false;
		}
		
		//更新退出时间
		userDao.updateLogoutState(userInfo.getLoginTime(), userInfo.getIp(), userInfo.getUsername());
		
		onlineMap.remove(userToken);
		return true;
	}
	
	/**
	 * 添加邀请码
	 */
	public boolean addInvitationCode(String userToken, String code) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		long userID = userInfo.getId();
		InvitationCode invitationCode = invitationCodeDao.selectInvitationCodeByCode(code);
		if(invitationCode == null) {
			return false;
		}
		long distributorID = invitationCode.getDistributorID();
		
		if(1 != userDao.bindDistributor(userID, distributorID)) {
			return false;
		}
		if(1 != invitationCodeDao.bindInvitationCode(code)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 首页
	 * @param userToken
	 * @return
	 */
	public HomePage homePage(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		
		HomePage homePage = new HomePage();
		String[] pic = new String[3];
		pic[0] = "./pic1";
		pic[1] = "./pic2";
		pic[2] = "./pic3";
		homePage.setPic(pic);
		
		Long userID = Long.parseLong(userToken.split("O")[0]);
		long distributorID = userDao.selectDistributor(userID);
		List<Commodity> commodity = commodityDao.selectAllCommodityForUser(distributorID);
		
		homePage.setCommodityList(commodity);
		return homePage;
	}
	
	/**
	 * 关于我们
	 */
	public String aboutUs(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		String msg = "关于我们";
		return msg;
	}
	
	/**
	 * 商城
	 */
	//TODO  home page
	public HomePage shop(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		
		HomePage homePage = new HomePage();
		String[] pic = new String[3];
		pic[0] = "./pic1";
		pic[1] = "./pic2";
		pic[2] = "./pic3";
		homePage.setPic(pic);
		
		Long userID = Long.parseLong(userToken.split("O")[0]);
		long distributorID = userDao.selectDistributor(userID);
		List<Commodity> commodity = commodityDao.selectAllCommodityForUser(distributorID);
		
		homePage.setCommodityList(commodity);
		return homePage;
	}
	
	/**
	 * 分类列表
	 */
	public List<CommodityClassify> classList(String userToken,int type){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		List<CommodityClassify> list = commodityClassifyDao.selectClass(type);
		return list;
	}
	
	/**
	 * 查询一类物品集合
	 */
	public List<Commodity> selectCommodityByClass(String userToken,int classId){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long distributor = userDao.selectDistributor(userInfo.getId());
		if(distributor == 0) {
			return null;
		}
		List<Commodity> list = commodityDao.selectCommodityByClass(distributor,classId);
		return list;
	}
	
	/**
	 * 按名称查询
	 */
	//TODO 后期添加模糊查询
	public List<Commodity> selectCommodityByName(String userToken,String name){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long distributor = userDao.selectDistributor(userInfo.getId());
		if(distributor == 0) {
			return null;
		}
		List<Commodity> list = commodityDao.selectCommodityByName(distributor,name);
		return list;
	}
	
	/**
	 * 新品   展示出售商品 maxID-10 范围内的商品
	 */
	//TODO 效率低，后期改为后端定时统计数据，有前端直接取值
	public NewCommodity newShopShow(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long userID = userInfo.getId();
		long distributorID = userDao.selectDistributor(userID);
		long commodityMaxID = commodityDao.getMaxID();
		
		NewCommodity commodity = new NewCommodity();
		List<String> pic = new ArrayList<String>();
		List<Commodity> commodityList = new ArrayList<Commodity>();
		
		//TODO  添加图片，后期改为读取配置文件
		pic.add("");
		while(commodityList.size() < 10) {
			Commodity temp =  commodityDao.selectNewCommodityByDistributor(distributorID,commodityMaxID);
			commodityList.add(temp);
			commodityMaxID = commodityMaxID - 1;
			if(commodityMaxID < 1) {
				break;
			}
		}
		commodity.setCommodity(commodityList);
		commodity.setPic(pic);
		return commodity;
	}
	
	/**
	 * 热卖品   
	 */
	//TODO 效率低，后期改为后端定时统计数据，有前端直接取值
	public HotCommodity hotShopShow(String userToken) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long userID = userInfo.getId();
		long distributorID = userDao.selectDistributor(userID);
		Integer maxNum = commodityDao.getMaxOrdernum();
		
		HotCommodity commodity = new HotCommodity();
		List<String> pic = new ArrayList<String>();
		List<Commodity> commodityList = new ArrayList<Commodity>();
		
		//TODO  添加图片，后期改为读取配置文件
		pic.add("");
		while(commodityList.size() < 10) {
			Commodity temp =  commodityDao.selectHotCommodityByDistributor(distributorID,maxNum);
			commodityList.add(temp);
			maxNum = maxNum - 1;
			if(maxNum < 1) {
				break;
			}
		}
		commodity.setCommodity(commodityList);
		commodity.setPic(pic);
		return commodity;
	}
	
	/**
	 *  查看可出售商品
	 */
	public List<Commodity> selectCommodity(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long distributor = userDao.selectDistributor(userInfo.getId());
		commodityList = commodityDao.selectCommodityForUser(distributor);
		return commodityList;
	}
	
	
	/**
	 *  查看可租赁商品
	 */
	public List<Commodity> selectRentCommodity(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long distributor = userDao.selectDistributor(userInfo.getId());
		commodityList = commodityDao.selectRentCommodityForUser(distributor);
		return commodityList;
	}
	
	public Commodity selectCommodityByID(String userToken,long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long distributor = userDao.selectDistributor(userInfo.getId());
		Commodity commodity = commodityDao.selectCommodityByIDForUser(id,distributor);
		return commodity;
	}
	
	/**
	 *  点击购买，生成订单
	 * @param id
	 * @param commodityid
	 * @return
	 */
	public Order makeCommodityOrder(String userToken, long commodityid, int count, String ip) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
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
		order.setAddr(null);
		
		return order;
	}
	
	/**
	 *  提交订单
	 */
	public boolean presentOrder(String userToken, Order order, String addr) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		order.setAddr(addr);
		
		if(orderDao.addOrderList(order) != 1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 点击租赁，生成订单
	 * @param id
	 * @param commodityid
	 * @return
	 */
	public RentOrder makeRentCommodityOrder(String userToken, long id, int count, String ip) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long now = Calendar.getInstance().getTime().getTime();
		Commodity rentCommodity = commodityDao.selectCommodityByID(id);
		if(rentCommodity.getDeposit() == 0) {
			return null;
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
		rentOrder.setPrice(price);
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
		
		return rentOrder;
	}
	
	/**
	 *  提交租赁订单
	 */
	public boolean presentRentOrder(String userToken, RentOrder rentOrder, String addr) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		rentOrder.setAddr(addr);
		
		if(rentOrderDao.addRentOrderList(rentOrder) != 1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 获取全部订单
	 */
	public List<Order> getAllOrder(String userToken){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long id = userInfo.getId();
		List<Order> orderList = orderDao.getAllOrderByID(id);
		return orderList;
	}
	
	/**
	 * 返回待付款/已付款订单
	 */
	public List<Order> getTypeOrder(String userToken, int type){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long id = userInfo.getId();
		List<Order> orderList = orderDao.getTypeOrderByID(id,type);
		return orderList;
	}
	
	/**
	 * 购物车清单
	 */
	public List<ShopCar> getShopCar(String userToken){
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long id = userInfo.getId();
		List<ShopCar> shopList = shopCarDao.getShoppingByID(id);
		return shopList;
	}
	
	/**
	 * 购物车 增
	 */
	public boolean addShopCar(String userToken, ShopCar shopCar) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		Long maxID = shopCarDao.getMaxID();
		if(maxID == null) {
			return false;
		}
		shopCar.setCarid(maxID+1);
		int ret = shopCarDao.addShopCar(shopCar);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 购物车 删
	 */
	public boolean removeShopCar(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		int ret = shopCarDao.removeShopCar(id);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 购物车 改
	 */
	public boolean updateShopCar(String userToken, ShopCar shopCar) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		int ret = shopCarDao.updateShopCar(shopCar);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 收藏夹清单 
	 */
	public List<Collect> getCollect(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long id = userInfo.getId();
		List<Collect> collectList = collectDao.getShoppingByID(id);
		return collectList;
	}
	
	/**
	 * 收藏夹 增
	 */
	public boolean addCollect(String userToken, Collect collect) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		Long maxID = collectDao.getMaxID();
		if(maxID == null) {
			return false;
		}
		collect.setId(maxID+1);
		int ret = collectDao.addCollect(collect);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 收藏夹 删
	 */
	public boolean removeCollect(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		int ret = collectDao.removeCollect(id);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 评价列表,根据物品ID查看评论
	 */
	public List<Appraise> listAppraise(String userToken, long id){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		
		List<Appraise> appraiseList = appraiseDao.getAppraiseByThingID(id);
		return appraiseList;
	}
	
	/**
	 * 我的评价列表，根据用户ID查看评论
	 */
	public List<Appraise> listMyAppraise(String userToken){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long id = userInfo.getId();
		
		List<Appraise> appraiseList = appraiseDao.getAppraiseByUserID(id);
		return appraiseList;
	}
	
	/**
	 * 评价 增
	 */
	public boolean addAppraise(String userToken, Appraise appraise) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		Long maxID = appraiseDao.getMaxID();
		if(maxID == null) {
			return false;
		}
		appraise.setId(maxID+1);
		int ret = appraiseDao.addAppraise(appraise);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 评价 删
	 */
	public boolean removeAppraise(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		
		long userID = userInfo.getId();
		int ret = appraiseDao.removeAppraise(id);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 收货地址 增
	 */
	public boolean addAddress(String userToken, Address addr) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		Long maxID = addressDao.getMaxID();
		if(maxID == null) {
			return false;
		}
		addr.setId(maxID + 1);
		addr.setUserId(userInfo.getId());
		int ret = addressDao.addAddress(addr);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 收货地址 删
	 */
	public boolean removeAddress(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		long userId = userInfo.getId();
		int ret = addressDao.removeAddress(userId, id);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 收货地址 改
	 */
	public boolean updateAddress(String userToken, Address addr) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
		}
		int ret = addressDao.updateAddress(addr);
		if(ret == 1) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 收货地址 查
	 */
	public List<Address> selectAddress(String userToken){
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long userId = userInfo.getId();
		List<Address> addr = addressDao.selectAddressByUser(userId);
		return addr;
	}
	
	/**
	 * 设置默认地址
	 */
	public boolean setMainAddr(String userToken, long id) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return false;
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
		return true;
	}
	
	/**
	 * 获取默认地址
	 */
	public Address getMainAddr(String userToken) {
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
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
		return ret;
	}
}
