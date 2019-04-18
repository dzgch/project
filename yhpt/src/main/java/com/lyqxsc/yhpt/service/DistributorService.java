package com.lyqxsc.yhpt.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.IDistributorDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IRentCommodityDao;
import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.UserInfo;

@Service
public class DistributorService {

	@Autowired
	IDistributorDao distributorDao;
	
	@Autowired
	ICommodityDao commodityDao;
	
	@Autowired
	IRentCommodityDao rentCommodityDao;
	
	@Autowired
	IOrderDao orderDao;
	
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	/**
	 *  分销商注册
	 * @param Admin
	 * @return -1 用户名已存在
	 *         -2 注册失败
	 */
	public int signup(Distributor param) {
		//判断用户名是否存在
		Distributor distributor = distributorDao.selectDistributorByUsername(param.getUsername());
		if(distributor != null) {
			return -1;
		}
		
		Long maxID = distributorDao.getMaxID();
		if(maxID == null) {
			return -1;
		}
		
		param.setAddTime(Calendar.getInstance().getTime().getTime());
		param.setId(maxID+1);
		
		if(distributorDao.addDistributor(param) < 0) {
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
	public Distributor login(String username, String password, String ip) {
		Distributor distributor = distributorDao.selectDistributor(username, password);
		if(distributor == null) {
			return null;
		}
		
		/*向数据库更新时间和本次登录的IP*/
		long id = distributor.getId();
		long now = Calendar.getInstance().getTime().getTime();
		distributorDao.updateLoginState(now, ip, id);
		
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
		distributor.setThisLoginIP(ip);
		distributor.setThisLoginTime(now);
		return distributor;
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
		distributorDao.updateLogoutState(adminInfo.getLoginTime(), adminInfo.getIp(), adminInfo.getId());
		
		onlineMap.remove(userToken);
		return true;
	}
	
	/**
	 * 修改信息
	 */
	//TODO需要判断字段是否为空
	public boolean updateDistributor(String userToken, Distributor param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		distributorDao.updateDistributor(param);
		
		return true;
	}
	
	/**
	 * 商品列表
	 */
	public List<Commodity> listCommodity(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		List<Commodity> commodityList = commodityDao.selectCommodityForUser(adminInfo.getId());
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
		int ret = commodityDao.removeCommodityByDistributor(adminInfo.getId(), commodityID);
		if(ret != 1) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 商品租赁列表
	 */
	public List<Commodity> listRentCommodity(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		List<Commodity> rentCommodityList = commodityDao.selectRentCommodityForUser(adminInfo.getId());
		return rentCommodityList;
	}
	
	/**
	 * 添加租赁商品
	 */
//	public boolean addRentCommodity(String userToken, RentCommodity rentCommodity) {
//		UserInfo adminInfo = onlineMap.get(userToken);
//		if(adminInfo == null) {
//			return false;
//		}
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
	 * 订单列表
	 */
	public List<Order> listAllOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		List<Order> orderList = orderDao.getOrderByDistributor(adminInfo.getId());
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
		List<Order> orderList = orderDao.getOrderStatusByDistributor(adminInfo.getId(), 3);
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
		List<Order> orderList = orderDao.getOrderStatusByDistributor(adminInfo.getId(), 1);
		return orderList;
	}
	
}
