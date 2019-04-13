package com.lyqxsc.yhpt.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyqxsc.yhpt.dao.IAppraiseDao;
import com.lyqxsc.yhpt.dao.ICollectDao;
import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IRentCommodityDao;
import com.lyqxsc.yhpt.dao.IRentOrderDao;
import com.lyqxsc.yhpt.dao.IShopCarDao;
import com.lyqxsc.yhpt.dao.IUserDao;
import com.lyqxsc.yhpt.domain.Appraise;
import com.lyqxsc.yhpt.domain.Collect;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.HomePage;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.ShopCar;
import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.domain.UserInfo;

@Service
public class UserService {
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IOrderDao orderDao;
	
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
	IRentCommodityDao rentCommodityDao;
	
	static int NEWSHOPCOUNT = 10;
	
	// 在线用户集合<id,用户信息>
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	// 可出售的商品集合
	List<Commodity> commodityList = new ArrayList<Commodity>();
	
	// 可租赁的商品集合
	List<RentCommodity> rentCommodityList = new ArrayList<RentCommodity>();
	
	/**
	 *  用户注册
	 * @param User
	 * @return -1 用户已存在
	 *         -2 注册失败
	 */
	public int signup(User param) {
		//判断用户名是否存在
		User user = userDao.selectOpenID(param.getOpenID());
		if(user != null) {
			return -1;
		}
		
		Long maxID = userDao.getMaxID();
		if(maxID == null) {
			return -1;
		}
		
		String openID = param.getOpenID();
		String realname = param.getRealname();
		String email = param.getEmail();
		String phone = param.getPhone();
		int sex = param.getSex();
		String province = param.getProvince();
		String city = param.getCity();
		String address = param.getAddress();
		long distributor = param.getDistributor();
		
		if(openID == null || realname == null || email == null || phone == null || sex == 0 || province == null || city == null || address == null || distributor == 0) {
			return -2;
		}
		
		param.setAddTime(Calendar.getInstance().getTime().getTime());
		param.setId(maxID+1);
		//TODO
		if(userDao.addUser(param) < 0) {
			return -2;
		}
		
		return 0;
	}

	/**
	 *  用户登录，并添加到在线用户集合中
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public User login(String openID, String ip) {
		User user = userDao.selectUser(openID);
		if(user == null) {
			return null;
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
	 * 首页
	 * @param userToken
	 * @return
	 */
	public HomePage homePage(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			System.out.println("logout error");
			return null;
		}
		
		HomePage homePage = new HomePage();
		homePage.setPic1("./pic1");
		homePage.setPic2("./pic2");
		homePage.setPic3("./pic3");
		
		Long userID = Long.parseLong(userToken.split("O")[0]);
		long distributorID = userDao.selectDistributor(userID);
		List<Commodity> commodity = commodityDao.selectCommodityByDistributor(distributorID);
		List<RentCommodity> rentcommodity = rentCommodityDao.selectRentCommodityByDistributor(distributorID);
		
		homePage.setCommodityList(commodity);
		homePage.setRentCommodityList(rentcommodity);
		return homePage;
	}
	
	/**
	 * 关于我们
	 */
	public String aboutUs(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			System.out.println("logout error");
			return null;
		}
		String msg = "关于我们";
		return msg;
	}
	
	/**
	 * 商城
	 */
	//TODO copy HomePage
	public HomePage shop(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			System.out.println("logout error");
			return null;
		}
		
		HomePage homePage = new HomePage();
		homePage.setPic1("./pic1");
		homePage.setPic2("./pic2");
		homePage.setPic3("./pic3");
		
		Long userID = Long.parseLong(userToken.split("O")[0]);
		long distributorID = userDao.selectDistributor(userID);
		List<Commodity> commodity = commodityDao.selectCommodityByDistributor(distributorID);
		List<RentCommodity> rentcommodity = rentCommodityDao.selectRentCommodityByDistributor(distributorID);
		
		homePage.setCommodityList(commodity);
		homePage.setRentCommodityList(rentcommodity);
		return homePage;
	}
	
	/**
	 * 新品   展示出售商品 maxID-10 范围内的商品
	 */
	public List<Commodity> newShopShow(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			System.out.println("logout error");
			return null;
		}
		Long userID = Long.parseLong(userToken.split("O")[0]);
		long distributorID = userDao.selectDistributor(userID);
		long commodityMaxID = commodityDao.getMaxID();
		List<Commodity> commodity = commodityDao.selectNewCommodityByDistributor(distributorID,commodityMaxID - NEWSHOPCOUNT);
		return commodity;
	}
	
	/**
	 * 热卖品   
	 */
	//copy newShop
	public List<Commodity> hotShopShow(String userToken) {
		//确定用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			System.out.println("logout error");
			return null;
		}
		Long userID = Long.parseLong(userToken.split("O")[0]);
		long distributorID = userDao.selectDistributor(userID);
		long commodityMaxID = commodityDao.getMaxID();
		List<Commodity> commodity = commodityDao.selectNewCommodityByDistributor(distributorID,commodityMaxID - NEWSHOPCOUNT);
		return commodity;
	}
	
	/**
	 *  查看可出售商品
	 */
	//TODO
	public List<Commodity> selectCommodity(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long distributor = userDao.selectDistributor(userInfo.getId());
		commodityList = commodityDao.selectCommodityByDistributor(distributor);
		return commodityList;
	}
	
	
	/**
	 *  查看可租赁商品
	 */
	//TODO
	public List<RentCommodity> selectRentCommodity(String userToken) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		Long distributor = userDao.selectDistributor(userInfo.getId());
		rentCommodityList = rentCommodityDao.selectRentCommodityByDistributor(distributor);
		return rentCommodityList;
	}
	
	/**
	 *  点击购买，生成订单
	 * @param id
	 * @param commodityid
	 * @return
	 */
	public Order makeCommodityOrder(String userToken, int commodityid, int count, String ip) {
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
		order.setOrderType(1);
		order.setSchedule(0);
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
	//TODO 点击租赁，生成订单
	public RentOrder makeRentCommodityOrder(String userToken, int id, int count, String ip) {
		//判断用户是否在线
		UserInfo userInfo = onlineMap.get(userToken);
		if(userInfo == null) {
			return null;
		}
		long now = Calendar.getInstance().getTime().getTime();
		RentCommodity rentCommodity = rentCommodityDao.selectRentCommodityByID(id);
		String username = userDao.selectUsername(userInfo.getId());
		float price = rentCommodity.getPrice();
		float deposit = rentCommodity.getDeposit(); 
		
		RentOrder rentOrder = new RentOrder();
		rentOrder.setOrderNumber(userInfo.getId() + "II" + now);
		rentOrder.setOwner(userInfo.getId());
		rentOrder.setOwnerName(username);
		rentOrder.setRentCommodityID(rentCommodity.getId());
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
		rentOrder.setOrderType(1);
		rentOrder.setSchedule(0);
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
}
