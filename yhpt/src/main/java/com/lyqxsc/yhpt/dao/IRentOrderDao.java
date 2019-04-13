package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.RentOrder;


@Mapper
@Component
public interface IRentOrderDao {
	@Select("select * from rentorderlist")
	List<RentOrder> selectAllRentOrderList();
	
	@Select("select * from rentorderlist where owner=#{id}")
	List<RentOrder> getAllRentOrderByID(@Param("id") long id);
	
	@Select("select * from rentorderlist where status=#{type} and owner=#{id}")
	List<RentOrder> getTypeRentOrderByID(@Param("id") long id, @Param("type") int type);
	
	@Select("select * from rentorderlist where schedule=1")
	List<RentOrder> selectDoRentOrderList();

	@Select("select * from rentorderlist where schedule=0")
	List<RentOrder> selectUndoRentOrderList();
	
	@Insert("insert into rentorderlist(orderNumber,owner,ownerName,commodityID,commodityName,price,count,totalPrice,payMoney,orderPrice,completeTime,payOrdertime,status,payType,payIP,orderType,schedule,addr)"
			+ "values({orderNumber}, #{owner}, #{ownerName}, #{commodityID}, #{commodityName}, #{price}, #{count}, #{totalPrice}, #{payMoney}, #{orderPrice}, #{completeTime}, #{payOrdertime}, #{status}, #{payType}, #{payIP}, #{orderType}, #{schedule}, #{addr})")
	int addRentOrderList(RentOrder order);
	
	@Delete("delete from rentorderlist where orderNumber=#{orderNumber}")
	int removeRentOrderList(@Param("orderNumber") String orderNumber);
	
	@Update("update rentorderlist set owner=#{owner}, commodityID=#{commodityID}, commodityName=#{commodityName}, price=#{price}, count=#{count}, totalPrice=#{totalPrice}, payMoney=#{payMoney}, orderPrice=#{orderPrice}, completeTime=#{completeTime}, payOrdertime=#{payOrdertime}, status=#{status}, payType=#{payType}, payIP=#{payIP}, orderType=#{orderType}, schedule=#{schedule}, addr=#{addr}"
			+ "where orderNumber=#{orderNumber}")
	int updateRentOrderList(RentOrder order);
}
