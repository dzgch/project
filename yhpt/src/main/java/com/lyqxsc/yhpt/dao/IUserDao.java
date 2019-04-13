package com.lyqxsc.yhpt.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.User;

@Mapper
@Component
public interface IUserDao {
	@Select("select * from user")
	List<User> selectAllUser();
	
	@Select("select * from user where openID=#{openID}")
	User selectUser(@Param("openID") String openID);
	
	@Select("select realname from user where id=#{id}")
	String selectUsername(@Param("id") long id);
	
	@Select("select distributor from user where id=#{id}")
	Long selectDistributor(@Param("id") long id);
	
	@Select("select * from user where openID=#{openID}")
	User selectOpenID(@Param("openID") String openID);
	
	@Update("update user set thisLogintime=#{now} , thisLoginIP=#{ip} WHERE openid=#{openid}")
	int updateLoginState(@Param("now") long now, @Param("ip") String ip, @Param("openid") String openid);
	
	@Update("update user set lastLogintime=#{now} , lastLoginIP=#{ip} WHERE openid=#{openid}")
	int updateLogoutState(@Param("now") long now, @Param("ip") String ip, @Param("openid") String openid);
	
	@Select("select max(id) from user")
	Long getMaxID();
	
	@Delete("delete from user where openID=#{openid}")
	int removeUser(@Param("openid") String openID);
	
	@Update("update user set realname=#{realname}, email=#{email}, phone=#{phone}, sex=#{sex}, province=#{province}, city=#{city}, address=#{address}, thisLoginTime=#{thisLoginTime}, thisLoginIP=#{thisLoginIP}, lastLoginTime=#{lastLoginTime}, lastLoginIP=#{lastLoginIP}, wallet=#{wallet}, distributor=#{distributor}, addTime=#{addTime}"
			+ "where id=#{userID}")
	int updateUser(@Param("userID")long userID,User user);
	
	@Insert({"insert into user(id, openID, realname, email, phone, sex, province, city, address, thisLoginTime, thisLoginIP, lastLoginTime, lastLoginIP, wallet, distributor, addTime) "
			+ "values(#{id},#{openID},#{realname},#{email},#{phone},#{sex},#{province},#{city},#{address},#{thisLoginTime},#{thisLoginIP},#{lastLoginTime},#{lastLoginIP},#{wallet},#{distributor},#{addTime})"})
	int addUser(User user);
	
	
}
