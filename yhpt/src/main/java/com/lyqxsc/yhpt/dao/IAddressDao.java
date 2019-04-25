package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Address;

@Mapper
@Component
public interface IAddressDao {
	
	@Select("select max(id) from address")
	Long getMaxID();
	
	@Select("select * from address where id=#{id}")
	Address selectAddress(@Param("id")long id);
	
	@Select("select * from address where userId=#{userId}")
	List<Address> selectAddressByUser(@Param("userId")long id);
	
	@Insert("insert into address(id,userId,username,phone,addr,main)"
			+ "values(#{id},#{userId},#{username},#{phone},#{addr},#{main})")
	int addAddress(Address addr);
	
	@Delete("delete from address where userId=#{userId} and id=#{id}")
	int removeAddress(@Param("userId")long userId,@Param("id")long id);
	
	@Update("update address set username=#{username},phone=#{phone},addr=#{addr},main=#{main} where id=#{id} and userId=#{userId}")
	int updateAddress(Address addr);
}
