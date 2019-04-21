package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Coupon;

@Mapper
@Component
public interface ICouponDao {
	
	@Select("select max(id) from couponlist")
	Long selectMaxId();

	@Select("select * from couponlist")
	List<Coupon> selectCouponrList();
	
	@Insert("insert into couponlist(id,price,authority,startTime,endTime,condition,number,addTime,addPerson)"
			+ "values(#{id},#{price},#{authority},#{startTime},#{endTime},#{condition},#{number},#{addTime},#{addPerson})")
	int addCoupon(Coupon coupon);
	
	@Delete("delete from couponlist where id=#{id}")
	int removeCoupon(@Param("id") long id);
}
