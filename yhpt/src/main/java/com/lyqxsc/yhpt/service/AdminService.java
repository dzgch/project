package com.lyqxsc.yhpt.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lyqxsc.yhpt.dao.IAdminDao;
import com.lyqxsc.yhpt.dao.ICommodityClassifyDao;
import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.dao.ICouponDao;
import com.lyqxsc.yhpt.dao.IDistributorDao;
import com.lyqxsc.yhpt.dao.IOrderDao;
import com.lyqxsc.yhpt.dao.IDeputyCommodityDao;
import com.lyqxsc.yhpt.dao.IRentOrderDao;
import com.lyqxsc.yhpt.dao.IUserDao;
import com.lyqxsc.yhpt.dao.SalesTabel;
import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.AdminBak;
import com.lyqxsc.yhpt.domain.AdminHomePage;
import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.CommodityClassify;
import com.lyqxsc.yhpt.domain.Coupon;
import com.lyqxsc.yhpt.domain.Distributor;
import com.lyqxsc.yhpt.domain.DistributorBak;
import com.lyqxsc.yhpt.domain.Order;
import com.lyqxsc.yhpt.domain.RentCommodity;
import com.lyqxsc.yhpt.domain.RentOrder;
import com.lyqxsc.yhpt.domain.SimpleDistributor;
import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.domain.UserInfo;
import com.lyqxsc.yhpt.urlclass.ClassifyList;
import com.lyqxsc.yhpt.urlclass.CommodityInfo;
import com.lyqxsc.yhpt.utils.RetJson;

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
	
	@Autowired
	IOrderDao orderDao;
	
	@Autowired
	IRentOrderDao rentOrderDao;
	
	@Autowired
	ICouponDao couponDao;
	
	@Value("${PicPath}")
	String picPath;
	
	@Value("${SavePicPath}")
	String savePicPath;
	
	Map<String, UserInfo> onlineMap = new HashMap<String, UserInfo>();
	
	/**
	 *  管理员注册
	 * @param Admin
	 * @return -1 用户名已存在
	 *         -2 注册失败
	 *         后端必填字段
	 *         addTime
	 *         authority
	 */
	public RetJson signupAdmin(String userToken, Admin param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		//判断用户名是否存在
		AdminBak admin = adminDao.adminIsExist(param.getUsername());
		if(admin != null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		Long maxID = adminDao.getMaxAdminID();
		if(maxID == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		if(maxID >= 100) {
			return RetJson.unknowError("未知错误", null);
		}
		param.setAddTime(Calendar.getInstance().getTime().getTime());
		param.setAuthority(1);
		
		if(adminDao.addAdmin(param) < 0) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 注册分销商
	 */
	public RetJson signupDistributor(String userToken, Admin param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		//root id = 0
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		//判断用户名是否存在
		AdminBak ret = adminDao.adminIsExist(param.getUsername());
		if(ret != null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		AdminBak admin = adminDao.selectAdminByID(adminInfo.getId());
		
		Long maxID = adminDao.getMaxID();
		if(maxID == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		if(maxID < 100) {
			maxID = 100l;
		}
		param.setAddTime(Calendar.getInstance().getTime().getTime());
		param.setId(maxID+1);
		
		param.setParent(adminInfo.getId());
		param.setGrade(admin.getGrade()+1);
		param.setAuthority(1);
		
		if(adminDao.addAdmin(param) < 0) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	/**
	 *  登录，并添加到在线用户集合中
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public RetJson login(String username, String password, String ip) {
		Admin admin = adminDao.selectAdmin(username, password);
		if(admin == null) {
			return RetJson.overdueError("凭证失效", null);
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
		return RetJson.success("成功", admin);
	}
	
	/**
	 *  管理员注销,获取当前时间 
	 * @param id
	 * @return
	 */
	public RetJson logout(String userToken) {
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		//更新退出时间
		adminDao.updateLogoutState(adminInfo.getLoginTime(), adminInfo.getIp(), adminInfo.getId());
		
		onlineMap.remove(userToken);
		return RetJson.success("成功");
	}
	
	/**
	 * 修改信息
	 */
	//TODO需要判断字段是否为空
	public RetJson updateAdmin(String userToken, Admin param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		adminDao.updateAdmin(adminInfo.getId(), param);
		return RetJson.success("成功");
	}
	
	/**
	 * 首页
	 * @param userToken
	 */
	//后期改为定时统计，由前端直接取数据
	//TODO  完善
	public RetJson homepage(String userToken, int classId) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		int totalSales = commodityDao.getTotalSales();
		int totalOrder = orderDao.getTotalOrder()+rentOrderDao.getTotalOrder();
		
		List<String>  keyword = null;
		List<Commodity> salesDay = null;
		List<Commodity> salesHot = commodityDao.selectCommodityByClass(0, classId);
		
		AdminHomePage home = new AdminHomePage();
		home.setTotalOrder(totalOrder);
		home.setTotalSales(totalSales);
		home.setKeyword(keyword);
		home.setSalesDay(salesDay);
		home.setSalesHot(salesHot);
		
		return RetJson.success("成功",home);
	}
	
	/**
	 * 添加物品分类
	 */
	public RetJson addClassify(String userToken, int type, String classStr) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		CommodityClassify classify = new CommodityClassify();
		classify.setKind(type);
		classify.setClassStr(classStr);
		if(1 != commodityClassifyDao.insert(classify)) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 删除分类
	 */
	public RetJson removeClassify(String userToken, int id) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}

		if(1 != commodityClassifyDao.delete(id)) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 查询分类
	 */
	public RetJson selectClassify(String userToken) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		ClassifyList list = new ClassifyList();
		List<CommodityClassify> mechanical = commodityClassifyDao.selectClass(2);
		List<CommodityClassify> agentia = commodityClassifyDao.selectClass(1);
		list.setAgentia(agentia);
		list.setMechanical(mechanical);
		return RetJson.success("成功", list);
	}
	
	/**
	 * 商品列表
	 */
	public RetJson listCommodity(String userToken){
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<Commodity> commodityList = commodityDao.selectAll();
		return RetJson.success("成功", commodityList);
	}
	
	/**
	 * 商品详情
	 */
	public RetJson getCommodityInfo(String userToken, long id) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Commodity commodity = commodityDao.selectCommodityByID(id);
		return RetJson.success("成功", commodity);
	}
	
	/**
	 * 添加商品
	 * picurl
	 * sales
	 * ordernumDay
	 * ordernumMouth
	 * ordernumTotal
	 * distributor
	 * classStr
	 */
	public RetJson addCommodity(String userToken, CommodityInfo param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		MultipartFile pic = param.getPic();
		
		Commodity commodity = new Commodity();
		commodity.setName(param.getName());
		commodity.setPrice(param.getPrice());
		commodity.setPrice1(param.getPrice1());
		commodity.setPrice2(param.getPrice2());
		commodity.setPrice3(param.getPrice3());
		commodity.setPrice4(param.getPrice4());
		commodity.setPrice5(param.getPrice5());
		commodity.setPrice6(param.getPrice6());
		commodity.setRentPrice(param.getRentPrice());
		commodity.setRentPrice1(param.getRentPrice1());
		commodity.setRentPrice2(param.getRentPrice2());
		commodity.setRentPrice3(param.getRentPrice3());
		commodity.setRentPrice4(param.getRentPrice4());
		commodity.setRentPrice5(param.getRentPrice5());
		commodity.setRentPrice6(param.getRentPrice6());
		commodity.setType(param.getType());
		commodity.setInventory(param.getInventory());
		commodity.setDeposit(param.getDeposit());
		commodity.setNote(param.getNote());
		commodity.setClassId(param.getClassId());
		int online = param.getOnline();
		if(online == 1) {
			commodity.setOnlineTime(System.currentTimeMillis());
		}
		commodity.setOnline(online);
		commodity.setSales(0);
		commodity.setOrdernumDay(0);
		commodity.setOrdernumMouth(0);
		commodity.setOrdernumTotal(0);
		commodity.setDistributor(0);
		commodity.setAddTime(System.currentTimeMillis());
		commodity.setSalesVolumeDay(0);
		commodity.setSalesVolumeMouth(0);
		commodity.setSalesPriceDay(0);
		commodity.setSalesPriceMouth(0);
		commodity.setSalesPriceTotal(0);
		commodity.setRentOrdernumDay(0);
		commodity.setRentOrdernumMouth(0);
		commodity.setRentOrdernumTotal(0);
		commodity.setRentVolumeDay(0);
		commodity.setRentVolumeMouth(0);
		commodity.setRentVolumeTotal(0);
		commodity.setRentPriceDay(0);
		commodity.setRentPriceMouth(0);
		commodity.setRentPriceTotal(0);

		String path = picPath;
    	String name = System.currentTimeMillis() + ".png";
    	String filename = path+name;
		if(!savePic(pic, filename)) {
			return RetJson.unknowError("保存图片错误", null);
		}
		
		CommodityClassify classify = commodityClassifyDao.selectClassByID(param.getClassId());
		if(classify == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		String classStr = classify.getClassStr();
		commodity.setClassStr(classStr);
		commodity.setKind(classify.getKind());
		commodity.setPicurl(savePicPath + name);

		int ret = commodityDao.addCommodity(commodity);
		if(ret != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 商品修改
	 * picurl
	 * sales
	 * ordernumDay
	 * ordernumMouth
	 * ordernumTotal
	 * distributor
	 * classStr
	 */
	public RetJson updateCommodity(String userToken, CommodityInfo param) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}

		Commodity commodity = new Commodity();
		
		String name = param.getName();
		if(name != null) {
			commodity.setName(name);
		}
		
		float price = param.getPrice();
		if(price != 0) {
			commodity.setPrice(price);
		}
		
		price = param.getPrice1();
		if(price != 0) {
			commodity.setPrice1(price);
		}
		
		price = param.getPrice2();
		if(price != 0) {
			commodity.setPrice2(price);
		}
		
		price = param.getPrice3();
		if(price != 0) {
			commodity.setPrice3(price);
		}
		
		price = param.getPrice4();
		if(price != 0) {
			commodity.setPrice4(price);
		}
		
		price = param.getPrice5();
		if(price != 0) {
			commodity.setPrice5(price);
		}
		
		price = param.getPrice6();
		if(price != 0) {
			commodity.setPrice6(price);
		}

		price = param.getRentPrice();
		if(price != 0) {
			commodity.setRentPrice(price);
		}
		
		price = param.getRentPrice1();
		if(price != 0) {
			commodity.setRentPrice1(price);
		}
		
		price = param.getRentPrice2();
		if(price != 0) {
			commodity.setRentPrice2(price);
		}
		
		price = param.getRentPrice3();
		if(price != 0) {
			commodity.setRentPrice3(price);
		}
		
		price = param.getRentPrice4();
		if(price != 0) {
			commodity.setRentPrice4(price);
		}
		
		price = param.getRentPrice5();
		if(price != 0) {
			commodity.setRentPrice5(price);
		}
		
		price = param.getRentPrice6();
		if(price != 0) {
			commodity.setRentPrice6(price);
		}
		
		String type = param.getType();
		if(type != null) {
			commodity.setType(type);
		}
		
		int inventory = param.getInventory();
		if(inventory != 0) {
			commodity.setInventory(inventory);
		}
		
		float deposit = param.getDeposit();
		if(deposit != 0) {
			commodity.setDeposit(deposit);
		}
		
		String note = param.getNote();
		if(note != null) {
			commodity.setNote(note);
		}
		
		int classId = param.getClassId();
		if(classId != 0) {
			commodity.setClassId(classId);
		}
		
		int online = param.getOnline();
		if(online != 0) {
			commodity.setOnline(online);
		}

		MultipartFile pic = param.getPic();
		if(pic != null) {
			String path = picPath;
	    	String fname = System.currentTimeMillis() + ".png";
	    	String filename = path+fname;
			if(!savePic(pic, filename)) {
				return RetJson.unknowError("保存图片错误", null);
			}
			commodity.setPicurl(savePicPath + name);
		}
		
		CommodityClassify classify = commodityClassifyDao.selectClassByID(param.getClassId());
		if(classify == null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		commodity.setClassStr(classify.getClassStr());
		commodity.setKind(classify.getKind());
		

		int ret = commodityDao.updateCommodity(commodity);
		if(ret != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	/**
	 * 添加商品时保存图片
	 */
	private boolean savePic(MultipartFile file, String filename) {
		if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(filename)));    
                out.write(file.getBytes());    
                out.flush();    
                out.close();
//                log.info("save pic seccess");
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
	public RetJson addCommodityCount(String userToken, long commodityID, int count) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(commodityDao.setCommodityCount(commodityID, count) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 商品删除
	 */
	public RetJson removeCommodity(String userToken, long commodityID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		int ret = commodityDao.removeCommodity(commodityID);
		if(ret != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	/**
	 * 商品租赁列表
	 */
	public RetJson listRentCommodity(String userToken){
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<Commodity> rentCommodityList = commodityDao.selectRentCommodity();
		return RetJson.success("成功",rentCommodityList);
	}
	
	/**
	 * 商品上架/下架
	 * @param userToken
	 * @param id
	 * @param option 1 上架
	 *               0 下架
	 * @return
	 */
	public RetJson onlineCommodity(String userToken, long id, int option){
		//确定用户是否在线
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(commodityDao.updateOnline(id,option) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 商品销售情况
	 */
	public RetJson getSalesTabel(String userToken) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<SalesTabel> salesTabelList = commodityDao.getSalesTabel();
		return RetJson.success("成功", salesTabelList);
	}
	
	/**
	 * 商品库存管理
	 */
	public RetJson inventoryWarning(String userToken,int num){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<Commodity> commodityList = commodityDao.inventoryWarning(num,0);
		List<Commodity> commodityList1 = commodityDao.inventoryNoWarning(num,0);
		commodityList.addAll(commodityList1);
		return RetJson.success("成功",commodityList);
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
	public RetJson listAllUser(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<User> userList = userDao.selectAllUser();
		return RetJson.success("成功", userList);
	}
	
	/**
	 * 用户详情
	 */
	public RetJson getUser(String userToken, long id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		User user = userDao.selectUserByID(id);
		return RetJson.success("成功", user);
	}
	
	
	/**
	 * 新增用户
	 */
	public RetJson addUser(String userToken, User user) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(userDao.selectUserByOpenID(user.getOpenID()) != null) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		long maxID = userDao.getMaxID();
		user.setId(maxID+1);
		
		if(userDao.addUser(user) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 删除用户
	 */
	public RetJson removeUser(String userToken, long id) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		int ret = userDao.removeUser(id);
		if(ret != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	/**
	 * 修改用户
	 * 启用1/禁用2/删除4/拉黑3
	 */
	public RetJson updateUser(String userToken, long userId, int code) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		int ret;
		if(code == 4) {
			ret = userDao.removeUser(userId);
		}
		else {
			ret = userDao.updateUserAuthority(userId, code);
		}
		if(ret != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 分销商列表
	 */
	public RetJson listAllDistributor(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<DistributorBak> distributorList = distributorDao.selectAllDistributor();
		return RetJson.success("成功",distributorList);
	}
	
	
	/**
	 * 新增经销商
	 * 后端填写
	 * orderNum
	 * rentOrderNum
	 * addTime
	 * grandParent
	 * userNum
	 * addId
	 */
	//TODO添加邀请码
	public RetJson addDistributor(String userToken, Distributor distributor) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(distributorDao.selectDistributorByUsername(distributor.getUsername()) != null) {
			return RetJson.unknowError("已存在分销商", null);
		}
		
		long maxID = distributorDao.getMaxID();
		distributor.setId(maxID+1);
		distributor.setOrderNum(0);
		distributor.setRentOrderNum(0);
		distributor.setAddTime(System.currentTimeMillis());
		if(distributor.getGrade() == 1) {
			distributor.setGrandParent(distributor.getId());
		}
		else {
			distributor.setGrandParent(maxID+1);
		}
		
		distributor.setUserNum(0);
		distributor.setAddId(adminInfo.getId());
		
		if(distributorDao.addDistributor(distributor) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		return RetJson.success("成功");
	}
	
	
	/**
	 * 删除经销商
	 */
	public RetJson removeDistributor(String userToken, long distributorID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(distributorDao.removeDistributor(distributorID) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		return RetJson.success("成功");
	}
	
	/**
	 * 获取子分销商
	 */
	public RetJson getChildDistributor(String userToken, String distributorID){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<DistributorBak> list = distributorDao.getChildDistributor(Long.parseLong(distributorID));
		return RetJson.success("成功", list);
	}
	
	/**
	 * 获取父分销商
	 */
	public RetJson getParentDistributor(String userToken, int grade){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<SimpleDistributor> simpleList = new ArrayList<SimpleDistributor>();
		if(grade == 1) {
			SimpleDistributor simple = new SimpleDistributor();
			simple.setId(0l);
			simple.setName("总部");
			simpleList.add(simple);
			return RetJson.success("成功", simpleList);
		}
		
		int gradeTemp = distributorDao.getLowGrade();
		if(grade < 1 || grade > 6 || grade > gradeTemp+1) {
			return RetJson.success("分销商等级错误", null);
		}
		
		List<DistributorBak> list = distributorDao.getParentDistributor(grade-1);
		
		for(DistributorBak obj:list) {
			SimpleDistributor simple = new SimpleDistributor();
			simple.setId(obj.getId());
			simple.setName(obj.getDistributorName());
			simpleList.add(simple);
		}
		return RetJson.success("成功", simpleList);
	}
	
	/**
	 * 删除经销商
	 */
	public RetJson updateDistributor(String userToken, Distributor distributor) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		if(distributorDao.updateDistributor(distributor) == 1) {
			return RetJson.success("成功");
		}
		return RetJson.mysqlError("数据库连接异常", null);
	}
	
	/**
	 * 经销商详情
	 */
	public RetJson getDistributor(String userToken, long id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		DistributorBak distributor = distributorDao.selectDistributorByID(id);
		return RetJson.success("成功", distributor);
	}
	
	
	/**
	 * 允许经销商开设店铺
	 */
	public RetJson authorizeDistributor(String userToken, long distributorID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(distributorDao.authorizeDistributor(distributorID,1) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 取消分销商资格
	 */
	public RetJson unAuthorizeDistributor(String userToken, long distributorID) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(distributorDao.authorizeDistributor(distributorID,0) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	/**
	 * 商品订单列表
	 */
	public RetJson listAllOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<Order> orderList = orderDao.selectAllOrderList();
		return RetJson.success("成功", orderList);
	}
		
	/**
	 * 查看已处理订单
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	public RetJson listDoOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<Order> orderList = orderDao.getOrderListByStatus(3);
		return RetJson.success("成功", orderList);
	}
	
	/**
	 * 查看未处理订单
	 */
	public RetJson listUndoOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<Order> orderList = orderDao.getOrderListByStatus(1);
		return RetJson.success("成功", orderList);
	}
	
	/**
	 * 查看订单详情
	 */
	public RetJson getOrder(String userToken, String id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		Order order = orderDao.getOrderByID(id);
		return RetJson.success("成功", order);
	}
	
	/**
	 * 商品发货
	 */
	public RetJson sendOrder(String userToken, String orderId, String count) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		String id = orderId.split("O")[2];
		if(commodityDao.setCommodityCount(Long.parseLong(id),(-1)*Integer.parseInt(count)) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		if(orderDao.updateOrderList(3,orderId,null) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	/**
	 * 处理订单
	 */
	//TODO
	
	/**
	 * 租赁订单列表
	 */
	public RetJson listAllRentOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		List<RentOrder> orderList = rentOrderDao.selectAllRentOrderList();
		return RetJson.success("成功", orderList);
	}
	
	/**
	 * 查看已处理租赁订单
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	public RetJson listDoRentOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<RentOrder> orderList = rentOrderDao.getRentOrderListByStatus(3);
		return RetJson.success("成功", orderList);
	}
	
	/**
	 * 查看为处理租赁订单
	 * 订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	 */
	public RetJson listUndoRentOrder(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<RentOrder> orderList = rentOrderDao.getRentOrderListByStatus(1);
		return RetJson.success("成功", orderList);
	}
	
	/**
	 * 查看租赁订单详情
	 */
	public RetJson listOneRentOrder(String userToken, String id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		RentOrder order = rentOrderDao.listOneRentOrder(id);
		return RetJson.success("成功", order);
	}
	
	/**
	 * 租赁商品发货
	 */
	public RetJson sendRentOrder(String userToken, String orderId, String count) {
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		String id = orderId.split("O")[2];
		if(commodityDao.setCommodityCount(Long.parseLong(id),(-1)*Integer.parseInt(count)) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		
		if(rentOrderDao.updateRentOrderList(3,orderId,null) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	
	
	/**
	 * 处理租赁订单
	 */
	//TODO
	
	/**
	 * 优惠券列表
	 */
	public RetJson listCoupon(String userToken){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		List<Coupon> list = couponDao.selectCouponrList();
		return RetJson.success("成功", list);
	}
	
	/**
	 * 添加优惠券
	 */
	public RetJson addCoupon(String userToken, Coupon coupon){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		long id = couponDao.selectMaxId();
		long now = System.currentTimeMillis();
		long addPerson = adminInfo.getId();
		coupon.setId(id+1);
		coupon.setAddTime(now);
		coupon.setAddPerson(addPerson);
		
		if(couponDao.addCoupon(coupon) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
	
	
	/**
	 * 删除优惠券
	 */
	public RetJson removeCoupon(String userToken, long id){
		UserInfo adminInfo = onlineMap.get(userToken);
		if(adminInfo == null) {
			return RetJson.overdueError("凭证失效", null);
		}
		
		if(couponDao.removeCoupon(id) != 1) {
			return RetJson.mysqlError("数据库连接异常", null);
		}
		return RetJson.success("成功");
	}
}
