package com.lyqxsc.yhpt.service;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyqxsc.yhpt.dao.ICommodityDao;
import com.lyqxsc.yhpt.domain.Commodity;

/**
 * 维护商品数据库的日订单和月订单
 * 日订单每天0点清零
 * 月订单每30天清零
 */
@Service
public class CommodityManage {

	@Autowired
	ICommodityDao commodityDao;
	
	TimerTask taskDay = new TimerTask() {
		@Override
		public void run() {
			List<Commodity> commodityList = commodityDao.selectAll();
			for(Commodity commodity:commodityList) {
				int countDay = commodity.getOrdernumDay();
				int countMouth = commodity.getOrdernumMouth();
				countMouth += countDay;
				commodityDao.updateOrderNum(0,countMouth,commodity.getId());
			}
		}
	};
	
	TimerTask taskMonth = new TimerTask() {
		@Override
		public void run() {
			List<Commodity> commodityList = commodityDao.selectAll();
			for(Commodity commodity:commodityList) {
				commodityDao.updateOrderNum(0,0,commodity.getId());
			}
		}
	};
	
	public CommodityManage() {
		Calendar timeDay = Calendar.getInstance();
		timeDay.set(Calendar.HOUR_OF_DAY, 23);
		timeDay.set(Calendar.MINUTE,59);
		timeDay.set(Calendar.SECOND,59);
		timeDay.add(Calendar.SECOND, 1);
		Timer timerDay = new Timer("Commodity order day");
		timerDay.scheduleAtFixedRate(taskDay, timeDay.getTime(), 24*60*60*1000);
		
		Calendar timeMonth = Calendar.getInstance();
		timeMonth.set(Calendar.DAY_OF_MONTH, 0);
		timeMonth.set(Calendar.HOUR_OF_DAY, 0);
		timeMonth.set(Calendar.MINUTE, 20);
		timeMonth.set(Calendar.SECOND, 0);
		timeMonth.add(Calendar.MONTH, 1);
		timeMonth.add(Calendar.DAY_OF_MONTH, 1);
		Timer timerMonth = new Timer("Commodity order month");
		timerMonth.scheduleAtFixedRate(taskMonth, timeMonth.getTime(), 24*60*60*30437l);
	}
}
