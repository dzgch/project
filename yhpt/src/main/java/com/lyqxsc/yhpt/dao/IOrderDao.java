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
	
	@Select("select * from orderlist where owner=#{id}")
	List<Order> getAllOrderByID(@Param("id") long id);
	
	@Select("select * from orderlist where status=#{type} and owner=#{id}")
	List<Order> getTypeOrderByID(@Param("id") long id, @Param("type") int type);
	
	@Select("select * from orderlist where schedule=1")
	List<Order> selectDoOrderList();

	@Select("select * from orderlist where schedule=0")
	List<Order> selectUndoOrderList();
	
	@Insert("insert into orderlist(orderNumber,owner,ownerName,commodityID,url,commodityName,price,count,totalPrice,payMoney,orderPrice,completeTime,payOrdertime,status,payType,payIP,orderType,schedule,addr)"
			+ "values({orderNumber}, #{owner}, #{ownerName}, #{commodityID}, #{url}, #{commodityName}, #{price}, #{count}, #{totalPrice}, #{payMoney}, #{orderPrice}, #{completeTime}, #{payOrdertime}, #{status}, #{payType}, #{payIP}, #{orderType}, #{schedule}, #{addr})")
	int addOrderList(Order order);
	
	@Delete("delete from orderlist where orderNumber=#{orderNumber}")
	int removeOrderList(@Param("orderNumber") String orderNumber);
	
	@Update("update orderlist set owner=#{owner}, commodityID=#{commodityID}, url=#{url}, commodityName=#{commodityName}, price=#{price}, count=#{count}, totalPrice=#{totalPrice}, payMoney=#{payMoney}, orderPrice=#{orderPrice}, completeTime=#{completeTime}, payOrdertime=#{payOrdertime}, status=#{status}, payType=#{payType}, payIP=#{payIP}, orderType=#{orderType}, schedule=#{schedule}, addr=#{addr}"
			+ "where orderNumber=#{orderNumber}")
	int updateOrderList(Order order);
}
