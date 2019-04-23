package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Distributor;

@Mapper
@Component
public interface IDistributorDao {

	@Select("select * from distributor")
	List<Distributor> selectAllDistributor();
	
	@Select("select * from distributor where username=#{username}")
	Distributor selectDistributorByUsername(@Param("username") String username);
	
	@Select("select * from distributor where id=#{id}")
	Distributor selectDistributorByID(@Param("id") long id);

	@Select("select * from distributor where username=#{username} and password=#{password}")
	Distributor selectDistributor(@Param("username") String username,@Param("password") String password);
	
	//根据城市查询分销商
	@Select("select * from distributor where city=#{city}")
	List<Distributor> getDistributorByCity(@Param("city") String city);
	
	//根据省份查询分销商
	@Select("select * from distributor where province=#{province}")
	List<Distributor> getDistributorByProvince(@Param("province") String province);
	
	//根据用户最多搜索分销商
	@Select(" select * from distributor where userNum=(select max(userNum) from distributor);")
	List<Distributor> getDistributorByUserNum();
	
	@Select("select max(id) from distributor")
	Long getMaxID();
	
	@Insert("insert into distributor(id,username,password,realname,sex,phone,thisLoginIP,thisLoginTime,lastLoginIP,lastLoginTime,addTime,authority)"
			+ "values(#{id},#{username},#{password},#{realname},#{sex},#{phone},#{thisLoginIP},#{thisLoginTime},#{lastLoginIP},#{lastLoginTime},#{addTime},#{authority})")
	int addDistributor(Distributor distributor);

	@Delete("delete from distributor where id=#{id}")
	int removeDistributor(@Param("id") long distributorID);

	@Update("update distributor set authority=#{authority} where id=#{id}")
	int authorizeDistributor(@Param("id") long distributorID, @Param("authority")int authority);
	
	@Update("update distributor set authority=0 where id=#{id}")
	int unAuthorizeDistributor(@Param("id") int distributorID);
	
	@Update("update distributor set thisLogintime=#{now} , thisLoginIP=#{ip} WHERE id=#{id}")
	int updateLoginState(@Param("now") long now, @Param("ip") String ip, @Param("id") long id);
	
	@Update("update distributor set lastLogintime=#{now} , lastLoginIP=#{ip} WHERE id=#{id}")
	int updateLogoutState(@Param("now") long now, @Param("ip") String ip, @Param("id") long id);
	
	@Update("update distributor set distributorName=#{distributorName},password=#{password},realname=#{realname},sex=#{sex},phone=#{phone},province=#{province},city=#{city},address=#{address},grade=#{grade},grandParent=#{grandParent},parent=#{parent},authority=#{authority}"
			+ "where id=#{id}")
	int updateDistributor(Distributor param);
	
	//分销商的用户数+1
	@Update("update distributor set userNum=userNum+1 where id=#{id}")
	int updateUserNum(@Param("id") long id);
	
	//购买订单数自增
	@Update("update distributor set orderNum=orderNum+#{num} where id=#{id}")
	int addOrderNum(@Param("num") int num, @Param("id") long id);
	
	//购买订单数自增
	@Update("update distributor set rentOrderNum=rentOrderNum+#{num} where id=#{id}")
	int addRentOrderNum(@Param("num") int num, @Param("id") long id);
	
}
