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
	User selectUserByOpenID(@Param("openID") String openID);
	
	@Select("select * from user where id=#{id}")
	User selectUserByID(@Param("id") long id);
	
	@Select("select realname from user where id=#{id}")
	String selectUsername(@Param("id") long id);
	
	@Select("select distributor from user where id=#{id}")
	Long selectDistributor(@Param("id") long id);
	
	//计算绑定分销商的用户数
	@Select("select Count(*) from user where distributor=#{id}")
	Integer getUserCount(@Param("id") long id);
	
	@Update("update user set thisLogintime=#{now} , thisLoginIP=#{ip} WHERE openid=#{openid}")
	int updateLoginState(@Param("now") long now, @Param("ip") String ip, @Param("openid") String openid);
	
	@Update("update user set lastLogintime=#{now} , lastLoginIP=#{ip} WHERE openid=#{openid}")
	int updateLogoutState(@Param("now") long now, @Param("ip") String ip, @Param("openid") String openid);
	
	@Select("select max(id) from user")
	Long getMaxID();
	
	@Delete("delete from user where id=#{id}")
	int removeUser(@Param("id") long id);
	
	@Update("update user set email=#{email}, phone=#{phone}, province=#{province}, city=#{city}, address=#{address}, wallet=#{wallet}"
			+ "where id=#{userID}")
	int updateUser(User user);
	
	//修改用户权限
	@Update("update user set authority=#{authority} where id=#{id}")
	int updateUserAuthority(@Param("id") long id,@Param("authority") int code);
	
	//绑定分销商
	@Update("update user set distributor=#{distributorID} where id=#{userID}")
	int bindDistributor(@Param("userID") long userID,@Param("distributorID") long distributorID);
	
	//购买订单数自增
	@Update("update user set totalOrderNum=totalOrderNum+#{num},orderNum=orderNum+#{num} where id=#{id}")
	int addOrderNum(@Param("num") int num, @Param("id") long id);
	
	//租赁订单数自增
	@Update("update user set totalOrderNum=totalOrderNum+#{num},rentOrderNum=rentOrderNum+#{num} where id=#{id}")
	int addRentOrderNum(@Param("num") int num, @Param("id") long id);
	
	@Insert({"insert into user(id,userToken,openID,nikeName,realName,headImgUrl,email,phone,sex,province,city,address,totalOrderNum,orderNum,rentOrderNum,thisLoginTime,thisLoginIP,lastLoginTime,lastLoginIP,wallet,distributor,addTime,authority) "
			+ "values(#{id},#{userToken},#{openID},#{nikeName},#{realName},#{headImgUrl},#{email},#{phone},#{sex},#{province},#{city},#{address},#{totalOrderNum},#{orderNum},#{rentOrderNum},#{thisLoginTime},#{thisLoginIP},#{lastLoginTime},#{lastLoginIP},#{wallet},#{distributor},#{addTime},#{authority})"})
	int addUser(User user);
	
}
