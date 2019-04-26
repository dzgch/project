package com.lyqxsc.yhpt.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Admin;
import com.lyqxsc.yhpt.domain.AdminBak;

@Mapper
@Component
public interface IAdminDao {
	
	@Select("select * from admin where username=#{username} and password=#{password}")
	Admin selectAdmin(@Param("username") String username, @Param("password") String password);
	
	@Select("select * from admin where username=#{username}")
	AdminBak adminIsExist(@Param("username") String username);
	
	@Select("select * from admin where id=#{id}")
	AdminBak selectAdminByID(@Param("id") long id);
	
	@Select("select max(id) from admin where id<100")
	Long getMaxAdminID();
	
	@Select("select max(id) from admin")
	Long getMaxID();
	
	@Update("update admin set thisLogintime=#{now} , thisLoginIP=#{ip} WHERE id=#{id}")
	int updateLoginState(@Param("now") long now, @Param("ip") String ip, @Param("id") long id);
	
	@Update("update admin set lastLogintime=#{now} , lastLoginIP=#{ip} WHERE id=#{id}")
	int updateLogoutState(@Param("now") long now, @Param("ip") String ip, @Param("id") long id);
	
	@Update("update admin set username=#{username} , password=#{password} , realname=#{realname} , sex=#{sex} , phone=#{phone}"
			+ "where id=#{id}")
	int updateAdmin(@Param("id") long id, Admin admin); 
	
	@Insert("insert into admin(username,password,realName,sex,phone,addTime,authority) "
			+ "values(#{username},#{password},#{realName},#{sex},#{phone},#{addTime},#{authority})")
	int addAdmin(Admin admin);
	
}
