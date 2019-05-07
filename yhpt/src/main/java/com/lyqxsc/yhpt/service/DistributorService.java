package com.lyqxsc.yhpt.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lyqxsc.yhpt.dao.ICommodityClassifyDao;
import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.IDistributorDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IRentOrderDao;
import com.lyqxsc.yhpt.dao.IUserDao;
import com.lyqxsc.yhpt.dao.IDeputyCommodityDao;
import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityClassify;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.DistributorBak;
import com.lyqxsc.yhpt.domain.DistributorHomePage;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.RetProfit;
import com.lyqxsc.yhpt.domain.UserInfo;
import com.lyqxsc.yhpt.urlclass.ClassifyList;
import com.lyqxsc.yhpt.urlclass.CommodityInfo;

@Service
public class DistributorService {

	@Autowired
	IDistributorDao distributorDao;
	
	@Autowired
	ICommodityDao commodityDao;
	
	@Autowired
	IDeputyCommodityDao rentCommodityDao;
	
	@Autowired
	IOrderDao orderDao;
	
	@Autowired
	IRentOrderDao rentOrderDao;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	ICommodityClassifyDao commodityClassifyDao;
	
	@Value("${PicPath}")
	String picPath;
	
	@Value("${SavePicPath}")
	String savePicPath;
	
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	/**
	 *  分销商注册
	 * @param Admin
	 * @return -1 用户名已存在
	 *         -2 注册失败
	 */
	public int signup(Distributor param) {
		//判断用户名是否存在
		DistributorBak distributor = distributorDao.selectDistributorByUsername(param.getUsername());
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
	public DistributorBak login(String username, String password, String ip) {
		DistributorBak distributor = distributorDao.selectDistributor(username, password);
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
	 * 首页
	 */
	public DistributorHomePage homepage(String userToken) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		long id = adminInfo.getId();
		
		float profit = orderDao.getSumPayMoney(id) + rentOrderDao.getSumPayMoney(id);
		int orderCount = orderDao.getOrderCount(id);
		int rentOrderCount = rentOrderDao.getRentOrderCount(id);
		int userCount = userDao.getUserCount(id);
		List<RetProfit> retProfitList= null;
		
		DistributorHomePage home = new DistributorHomePage();
		home.setProfit(profit);
		home.setRentOrderCount(rentOrderCount);
		home.setOrderCount(orderCount);
		home.setUserCount(userCount);
		home.setRetProfitList(retProfitList);
		return home;
	}
	
	/**
	 * 查询分类
	 */
	public ClassifyList selectClassify(String userToken) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		ClassifyList list = new ClassifyList();
		List<CommodityClassify> mechanical = commodityClassifyDao.selectClass(2);
		List<CommodityClassify> agentia = commodityClassifyDao.selectClass(1);
		list.setAgentia(agentia);
		list.setMechanical(mechanical);
		return list;
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
	 * 商品详情
	 */
	public Commodity getCommodityInfo(String userToken, long id) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		Commodity commodity = commodityDao.selectCommodityByID(id);
		return commodity;
	}
	
	/**
	 * 添加商品
	 */
	public boolean addCommodity(String userToken, CommodityInfo param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		long id = adminInfo.getId();
		DistributorBak distributor = distributorDao.selectDistributorByID(id);
		if(distributor == null) {
			return false;
		}
		int grade = distributor.getGrade();
		
		MultipartFile pic = param.getPic();
		
		Commodity commodity = new Commodity();
		commodity.setName(param.getName());
		switch (grade) {
		case 1:
			commodity.setPrice1(param.getPrice());
			commodity.setRentPrice1(param.getRentPrice());
			break;
		case 2:
			commodity.setPrice2(param.getPrice());
			commodity.setRentPrice2(param.getRentPrice());
			break;
		case 3:
			commodity.setPrice3(param.getPrice());
			commodity.setRentPrice3(param.getRentPrice());
			break;
		case 4:
			commodity.setPrice4(param.getPrice());
			commodity.setRentPrice4(param.getRentPrice());
			break;
		case 5:
			commodity.setPrice5(param.getPrice());
			commodity.setRentPrice5(param.getRentPrice());
			break;
		case 6:
			commodity.setPrice6(param.getPrice());
			commodity.setRentPrice6(param.getRentPrice());
			break;
		default:
			break;
		}
		commodity.setType(param.getType());
		commodity.setInventory(param.getInventory());
		commodity.setDeposit(param.getDeposit());
		commodity.setNote(param.getNote());
		commodity.setClassId(param.getClassId());
		commodity.setOnline(param.getOnline());
		commodity.setSales(0);
		commodity.setOrdernumDay(0);
		commodity.setOrdernumMouth(0);
		commodity.setOrdernumTotal(0);
		commodity.setDistributor(id);
		
		String path = picPath;
    	String name = System.currentTimeMillis() + ".png";
    	String filename = path+name;
		if(!savePic(pic, filename)) {
			return false;
		}
		CommodityClassify classify = commodityClassifyDao.selectClassByID(param.getClassId());
		if(classify == null) {
			return false;
		}
		String classStr = classify.getClassStr();
		commodity.setClassStr(classStr);
		commodity.setKind(classify.getKind());
		commodity.setPicurl(savePicPath + name);
		
		int ret = commodityDao.addCommodity(commodity);
		if(ret != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 添加商品时保存图片
	 */
	private boolean savePic(MultipartFile file, String filename) {
		if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filename)));    
                System.out.println(file.getName());
                out.write(file.getBytes());    
                out.flush();    
                out.close();
                return true;
            } catch (IOException e) {    
                e.printStackTrace();    
                return false;
            } 
		}
        return false;  
	}
	
	/**
	 * 添加商品数量
	 */
	public boolean addCommodityCount(String userToken, long commodityID, int count) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}

		if(commodityDao.setCommodityCount(commodityID, count) != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 删除商品
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
	 * 商品上架/下架
	 * @param userToken
	 * @param id
	 * @param option 1 上架
	 *               0 下架
	 * @return
	 */
	public boolean onlineCommodity(String userToken,long commodityID, int option) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		//TODO以后加分销商id判断
		if(commodityDao.updateOnline(commodityID,option) != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 商品库存管理
	 */
	public List<Commodity> inventoryWarning(String userToken,int num){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		
		long id = adminInfo.getId();
		List<Commodity> commodityList = commodityDao.inventoryWarning(num,id);
		return commodityList;
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
	 * 查看订单详情
	 */
	public Order getOrder(String userToken, String id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		Order order = orderDao.getOrderByID(id);
		return order;
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
	
	/**
	 * 商品发货
	 */
	public boolean sendOrder(String userToken, String id, String count) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		
		if(commodityDao.setCommodityCount(Long.parseLong(id),(-1)*Integer.parseInt(count)) != 1) {
			return false;
		}
		if(orderDao.updateOrderList(3,id, null) != 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 租赁订单列表
	 */
	public List<RentOrder> listAllRentOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		long distributorID = adminInfo.getId();
		List<RentOrder> orderList = rentOrderDao.getRentOrderListByDistributorID(distributorID);
		return orderList;
	}
	
	/**
	 * 查看租赁订单详情
	 */
	public RentOrder listOneRentOrder(String userToken, String id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		RentOrder order = rentOrderDao.listOneRentOrder(id);
		return order;
	}
	
	/**
	 * 查看已处理租赁订单
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	public List<RentOrder> listDoRentOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		long id = adminInfo.getId();
		List<RentOrder> orderList = rentOrderDao.getRentOrderStatusByDistributor(id, 3);
		return orderList;
	}
	
	/**
	 * 查看为处理租赁订单
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	public List<RentOrder> listUndoRentOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return null;
		}
		long id = adminInfo.getId();
		List<RentOrder> orderList = rentOrderDao.getRentOrderStatusByDistributor(id, 1);
		return orderList;
	}
	
	/**
	 * 租赁商品发货
	 */
	public boolean sendRentOrder(String userToken, String orderId, String count) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return false;
		}
		String id = orderId.split("O")[2];
		if(commodityDao.setCommodityCount(Long.parseLong(id),(-1)*Integer.parseInt(count)) != 1) {
			return false;
		}
		
		if(rentOrderDao.updateRentOrderList(3,orderId,null) != 1) {
			return false;
		}
		return true;
	}
	
}
