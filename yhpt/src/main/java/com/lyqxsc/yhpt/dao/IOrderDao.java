package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Order;

@Mapper
@Component
public interface IOrderDao {
	
	@Select("select * from orderlist")
	List<Order> selectAllOrderList();
	
	//订单状态 0待支付, 1已支付, 2待发货, 3待收货，4待评价, 5交易完成, 6交易已取消
	@Select("select * from orderlist where status=#{status}")
	List<Order> getOrderListByStatus(@Param("status") int status);
	
	@Select("select * from orderlist where distributorID=#{id}")
	List<Order> getOrderByDistributor(@Param("id") long id);
	
	@Select("select * from orderlist where distributorID=#{id} and status=#{status}")
	List<Order> getOrderStatusByDistributor(@Param("id") long id,@Param("status") int status);
	
	@Select("select * from orderlist where owner=#{id}")
	List<Order> getAllOrderByUser(@Param("id") long id);
	
	@Select("select * from orderlist where owner=#{id} and status=#{status}")
	List<Order> getOrderStatusByUser(@Param("id") long id, @Param("status") int status);
	
	@Insert("insert into orderlist(orderNumber,owner,ownerName,distributorID,commodityID,url,commodityName,price,count,totalPrice,payMoney,orderPrice,completeTime,payOrdertime,status,payType,payIP,lastPayStatus,addr)"
			+ "values(#{orderNumber},#{owner},#{ownerName},#{distributorID},#{commodityID},#{url},#{commodityName},#{price},#{count},#{totalPrice},#{payMoney},#{orderPrice},#{completeTime},#{payOrdertime},#{status},#{payType},#{payIP},#{lastPayStatus},#{addr})")
	int addOrderList(Order order);
	
	@Delete("delete from orderlist where orderNumber=#{orderNumber}")
	int removeOrderList(@Param("orderNumber") String orderNumber);
	
	@Update("update orderlist set status=#{status}, lastPayStatus=#{lastPayStatus}"
			+ "where orderNumber=#{orderNumber}")
	int updateOrderList(Order order);
}
