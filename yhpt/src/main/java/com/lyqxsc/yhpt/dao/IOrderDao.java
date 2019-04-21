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
	
	@Select("select * from orderlist where orderNumber=#{orderNumber}")
	Order getOrderByID(@Param("orderNumber") String orderNumber);
	
	@Select("select count(*) from orderlist")
	Integer getTotalOrder();
	
	//订单状态 1待支付, 2已支付, 3待发货, 4待收货，5待评价, 6交易完成, 0交易已取消
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
	
	//分销商计算订单进账
	@Select("select sum(payMoney) from orderlist where distributorID=#{distributorID}")
	Float getSumPayMoney(@Param("distributorID") long distributorID);
	
	//计算分销商的订单数量
	@Select("select count(*) from orderlist where distributorID=#{distributorID}")
	Integer getOrderCount(@Param("distributorID") long distributorID);
	
	@Insert("insert into orderlist(orderNumber,owner,ownerName,distributorID,commodityID,url,commodityName,price,count,totalPrice,payMoney,orderPrice,completeTime,payOrdertime,status,payType,payIP,lastPayStatus,addr)"
			+ "values(#{orderNumber},#{owner},#{ownerName},#{distributorID},#{commodityID},#{url},#{commodityName},#{price},#{count},#{totalPrice},#{payMoney},#{orderPrice},#{completeTime},#{payOrdertime},#{status},#{payType},#{payIP},#{lastPayStatus},#{addr})")
	int addOrderList(Order order);
	
	@Delete("delete from orderlist where orderNumber=#{orderNumber}")
	int removeOrderList(@Param("orderNumber") String orderNumber);
	
	//更新订单状态
	@Update("update orderlist set status=#{status} reason=#{reason} where orderNumber=#{orderNumber}")
	int updateOrderList(@Param("status") int status, @Param("orderNumber") String orderNumber, @Param("reason") String reason);
}
