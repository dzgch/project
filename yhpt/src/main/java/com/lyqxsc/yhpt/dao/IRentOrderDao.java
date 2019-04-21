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
	@Select("select count(*) from rentorderlist")
	Integer getTotalOrder();
	
	@Select("select * from rentorderlist")
	List<RentOrder> selectAllRentOrderList();
	
	@Select("select * from rentorderlist where status=#{type}")
	List<RentOrder> getRentOrderListByStatus(@Param("type") int type);
	
	//分销商获取租赁订单
	@Select("select * from rentorderlist where distributorID=#{distributorID}")
	List<RentOrder> getRentOrderListByDistributorID(@Param("distributorID") long distributorID);
	
	@Select("select * from rentorderlist where status=#{status} and distributorID=#{distributorID}")
	List<RentOrder> getRentOrderStatusByDistributor(@Param("distributorID") long distributorID,@Param("status") int type);
	
	@Select("select * from rentorderlist where orderNumber=#{orderNumber}")
	RentOrder listOneRentOrder(@Param("orderNumber") String id);
	
	
	@Select("select * from rentorderlist where owner=#{id}")
	List<RentOrder> getAllRentOrderByID(@Param("id") long id);
	
	@Select("select * from rentorderlist where status=#{type} and owner=#{id}")
	List<RentOrder> getTypeRentOrderByID(@Param("id") long id, @Param("type") int type);
	
	//获取订单状态
	@Select("select status from rentorderlist where orderNumber=#{orderNumber}")
	Integer getRentOrderStatus(@Param("orderNumber") String id);
	
	//分销商计算租赁订单进账
	@Select("select sum(totalPrice) from rentorderlist where distributorID=#{distributorID}")
	Float getSumPayMoney(@Param("distributorID") long distributorID);

	//计算分销商的租赁订单数量
	@Select("select count(*) from rentorderlist where distributorID=#{distributorID}")
	Integer getRentOrderCount(@Param("distributorID") long distributorID);
	
	@Insert("insert into rentorderlist(orderNumber,owner,ownerName,distributorID,rentCommodityID,rentCommodityName,url,price,deposit,count,totalDeposit,totalPrice,orderPrice,payMoney,completeTime,makeOrdertime,status,payType,payIP,lastPayStatus,addr)"
			+ "values(#{orderNumber},#{owner},#{ownerName},#{distributorID},#{rentCommodityID},#{rentCommodityName},#{url},#{price},#{deposit},#{count},#{totalDeposit},#{totalPrice},#{orderPrice},#{payMoney},#{completeTime},#{makeOrdertime},#{status},#{payType},#{payIP},#{lastPayStatus},#{addr})")
	int addRentOrderList(RentOrder order);
	
	@Delete("delete from rentorderlist where orderNumber=#{orderNumber}")
	int removeRentOrderList(@Param("orderNumber") String orderNumber);
	
	//更新订单状态
	@Update("update rentorderlist set status=#{status} reason=#{reason} where orderNumber=#{orderNumber}")
	int updateRentOrderList(@Param("status") int status, @Param("orderNumber") String orderNumber, @Param("reason") String reason);
}
