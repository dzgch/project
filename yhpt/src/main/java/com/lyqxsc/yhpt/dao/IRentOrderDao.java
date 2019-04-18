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
	
	@Insert("insert into rentorderlist(orderNumber,owner,ownerName,distributorID,rentCommodityID,rentCommodityName,url,price,deposit,count,totalDeposit,totalPrice,orderPrice,payMoney,completeTime,makeOrdertime,status,payType,payIP,lastPayStatus,addr)"
			+ "values(#{orderNumber},#{owner},#{ownerName},#{distributorID},#{rentCommodityID},#{rentCommodityName},#{url},#{price},#{deposit},#{count},#{totalDeposit},#{totalPrice},#{orderPrice},#{payMoney},#{completeTime},#{makeOrdertime},#{status},#{payType},#{payIP},#{lastPayStatus},#{addr})")
	int addRentOrderList(RentOrder order);
	
	@Delete("delete from rentorderlist where orderNumber=#{orderNumber}")
	int removeRentOrderList(@Param("orderNumber") String orderNumber);
	
	@Update("update rentorderlist set status=#{status}, lastPayStatus=#{lastPayStatus}"
			+ "where orderNumber=#{orderNumber}")
	int updateRentOrderList(RentOrder order);
}
