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
	Distributor selectDistributorByID(@Param("username") long id);

	@Select("select * from distributor where username=#{username} and password=#{password}")
	Distributor selectDistributor(@Param("username") String username,@Param("password") String password);
	
	@Select("select max(id) from distributor")
	Long getMaxID();
	
	@Insert("insert into distributor(id,username,password,realname,sex,phone,thisloginip,thislogintime,lastloginip,lastlogintime,addtime,authority)"
			+ "values(#{id}, #{username}, #{password}, #{realname}, #{sex}, #{phone}, #{thisloginip}, #{thislogintime}, #{lastloginip}, #{lastlogintime}, #{addtime}, #{authority})")
	int addDistributor(Distributor distributor);

	@Delete("delete from distributor where id=#{id}")
	int removeDistributor(@Param("id") long distributorID);

	@Update("update distributor set authority=1 where id=#{id}")
	int authorizeDistributor(@Param("id") int distributorID);
	
	@Update("update distributor set authority=0 where id=#{id}")
	int unAuthorizeDistributor(@Param("id") int distributorID);
	
	@Update("update distributor set thisLogintime=#{now} , thisLoginIP=#{ip} WHERE id=#{id}")
	int updateLoginState(@Param("now") long now, @Param("ip") String ip, @Param("id") long id);
	
	@Update("update distributor set lastLogintime=#{now} , lastLoginIP=#{ip} WHERE id=#{id}")
	int updateLogoutState(@Param("now") long now, @Param("ip") String ip, @Param("id") long id);
}
