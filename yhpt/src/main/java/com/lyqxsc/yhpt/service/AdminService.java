package com.lyqxsc.yhpt.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyqxsc.yhpt.dao.IAdminDao;
import com.lyqxsc.yhpt.dao.ICommodityClassifyDao;
import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.IDistributorDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IRentCommodityDao;
import com.lyqxsc.yhpt.dao.IUserDao;
import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityClassify;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.domain.UserInfo;

@Service
public class AdminService {

	@Autowired
	IAdminDao adminDao;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IDistributorDao distributorDao;
	
	@Autowired
	ICommodityDao commodityDao;
	
	@Autowired
	ICommodityClassifyDao commodityClassifyDao;
	
//	@Autowired
//	IRentCommodityDao rentCommodityDao;
	
	@Autowired
	IOrderDao orderDao;
	
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	/**
	 *  管理员注册
	 * @param Admin
	 * @return -1 用户名已存在
	 *         -2 注册失败
	 */
	public int signup(Admin param) {
		//判断用户名是否存在
		Admin admin = adminDao.adminIsExist(param.getUsername());
		if(admin != null) {
			return -1;
		}
		
		Long maxID = adminDao.getMaxID();
		if(maxID == null) {
			return -1;
		}
		param.setAddTime(Calendar.getInstance().getTime().getTime());
		param.setId(maxID+1);
		
		if(adminDao.addAdmin(param) < 0) {
			return -2;
		}
		return 0;
	}
	
	/**
	 *  登录，并添加到在线用户集合中
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public Admin login(String username, String password, String ip) {
		Admin admin = adminDao.selectAdmin(username, password);
		if(admin == null) {
			return null;
		}
		
		/*向数据库更新时间和本次登录的IP*/
		long id = admin.getId();
		long now = Calendar.getInstance().getTime().getTime();
		adminDao.updateLoginState(now, ip, id);
		
		UserInfo adminInfo = new UserInfo();
		adminInfo.setId(id);
		adminInfo.setUsername(username);
		adminInfo.setIp(ip);
		adminInfo.setLoginTime(now);
		
		for(String userToken:onlineMap.keySet()) {
			if(id == Long.parseLong(userToken.split("O")[0])) {
				logout(userToken);
			}
		}
		String userToken = id + "O" + now;
		onlineMap.put(userToken, adminInfo);
		
		/*更新返回前端的admin ip和time*/
		admin.setUserToken(userToken);
		admin.setThisLoginIP(ip);
		admin.setThisLoginTime(now);
		return admin;
	}
	
	/**
	 *  管理员注销,获取当前时间 
	 * @param id
	 * @return
	 */
	public boolean logout(String userToken) {
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		//更新退出时间
		adminDao.updateLogoutState(adminInfo.getLoginTime(), adminInfo.getIp(), adminInfo.getId());
		
		onlineMap.remove(userToken);
		return true;
	}
	
	/**
	 * 修改信息
	 */
	//TODO需要判断字段是否为空
	public boolean updateAdmin(String userToken, Admin param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		adminDao.updateAdmin(adminInfo.getId(), param);
		
		return true;
	}
	
	/**
	 * 添加物品分类
	 */
	public boolean addClassify(String userToken, int type, String classStr) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		int id = commodityClassifyDao.getMaxID();
		CommodityClassify classify = new CommodityClassify();
		classify.setClassId(id+1);
		classify.setType(type);
		classify.setClassStr(classStr);
		if(1 != commodityClassifyDao.insert(classify)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 商品列表
	 */
	public List<Commodity> listCommodity(String userToken){
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		List<Commodity> commodityList = commodityDao.selectAll();
		return commodityList;
	}
	
	/**
	 * 添加商品
	 */
	public boolean addCommodity(String userToken, Commodity commodity) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		Long maxID = commodityDao.getMaxID();
		if(maxID == null) {
			return false;
		}
		commodity.setId(maxID+1);
		//总部分销商编号为0
		commodity.setDistributor(0);
		int ret = commodityDao.addCommodity(commodity);
		if(ret != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 商品下架
	 */
	public boolean removeCommodity(String userToken, long commodityID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		int ret = commodityDao.removeCommodity(commodityID);
		if(ret != 1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 商品租赁列表
	 */
	public List<Commodity> listRentCommodity(String userToken){
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		List<Commodity> rentCommodityList = commodityDao.selectRentCommodity();
		return rentCommodityList;
	}
	
	
//	/**
//	 * 添加租赁商品
//	 */
//	public boolean addRentCommodity(String userToken, RentCommodity rentCommodity) {
//		UserInfo adminInfo = onlineMap.get(userToken);
//		if(adminInfo == null) {
//			return false;
//		}
//		
//		Long maxID = rentCommodityDao.getMaxID();
//		if(maxID == null) {
//			return false;
//		}
//		rentCommodity.setId(maxID+1);
//		
//		int ret = rentCommodityDao.addRentCommodity(rentCommodity);
//		if(ret != 1) {
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * 租赁商品下架
	 */
//	public boolean removeRentCommodity(String userToken, int rentCommodityID, String rentCommodityName) {
//		UserInfo adminInfo = onlineMap.get(userToken);
//		if(adminInfo == null) {
//			return false;
//		}
//		int ret = rentCommodityDao.removeRentCommodity(rentCommodityID, rentCommodityName);
//		if(ret != 1) {
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * 用户列表
	 */
	public List<User> listAllUser(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		List<User> userList = userDao.selectAllUser();
		return userList;
	}
	
	
	/**
	 * 新增用户
	 */
	public int addUser(String userToken, User user) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return -1;
		}
		
		if(userDao.selectUser(user.getOpenID()) != null) {
			return -2;
		}
		
		long maxID = userDao.getMaxID();
		user.setId(maxID+1);
		
		if(userDao.addUser(user) != 1) {
			return -3;
		}
		return 0;
	}
	
	/**
	 * 删除用户
	 */
	public boolean removeUser(String userToken, long id) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		int ret = userDao.removeUser(id);
		if(ret != 1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 修改用户
	 */
	public boolean updateUser(String userToken, User user) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		int ret = userDao.updateUser(user);
		if(ret != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 分销商列表
	 */
	public List<Distributor> listAllDistributor(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		List<Distributor> distributorList = distributorDao.selectAllDistributor();
		return distributorList;
	}
	
	
	/**
	 * 新增经销商
	 */
	public int addDistributor(String userToken, Distributor distributor) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return -1;
		}
		
		if(distributorDao.selectDistributorByUsername(distributor.getUsername()) != null) {
			return -2;
		}
		long maxID = distributorDao.getMaxID();
		distributor.setId(maxID+1);
		
		if(distributorDao.addDistributor(distributor) != 1) {
			return -3;
		}
		
		return 0; 
	}
	
	
	/**
	 * 删除经销商
	 */
	public boolean removeDistributor(String userToken, long distributorID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		if(distributorDao.removeDistributor(distributorID) != 1) {
			return false;
		}
		
		return true; 
	}
	
	
	/**
	 * 允许经销商开设店铺
	 */
	public boolean authorizeDistributor(String userToken, long distributorID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		if(distributorDao.authorizeDistributor(distributorID,1) != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取消分销商资格
	 */
	public boolean unAuthorizeDistributor(String userToken, long distributorID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		if(distributorDao.authorizeDistributor(distributorID,0) != 1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 订单列表
	 */
	public List<Order> listAllOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		List<Order> orderList = orderDao.selectAllOrderList();
		return orderList;
	}
	
	/**
	 * 查看已处理订单
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	public List<Order> listDoOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		List<Order> orderList = orderDao.getOrderListByStatus(3);
		return orderList;
	}
	
	/**
	 * 查看未处理订单
	 */
	public List<Order> listUndoOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		List<Order> orderList = orderDao.getOrderListByStatus(1);
		return orderList;
	}
	
	/**
	 * 商品库存管理
	 */
	//TODO
}
