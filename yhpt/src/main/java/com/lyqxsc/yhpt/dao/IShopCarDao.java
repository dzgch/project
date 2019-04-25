package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.ShopCar;

@Mapper
@Component
public interface IShopCarDao {
	@Select("select * from shopcarlist where userid=#{id}")
	List<ShopCar> getShoppingByUserID(@Param("id") long id);
	
	@Select("select * from shopcarlist where carid=#{id}")
	ShopCar getShoppingByID(@Param("id") long id);
	
	@Select("select max(id) from shopcarlist")
	Long getMaxID();
	
	@Insert({"insert into shopcarlist(userid,commodityid,name,picurl,price,count,inventory,note)"
			+ "values(#{userid}, #{commodityid}, #{name}, #{picurl}, #{price}, #{count}, #{inventory}, #{note})"})
	int addShopCar(ShopCar shopCar);
	
	@Delete("delete from shopcarlist where carid=#{id}")
	int removeShopCar(@Param("id") long id);
	
	@Update("update shopcarlist set count=#{count} where carid=#{carid}")
	int updateShopCar(ShopCar shopCar);
}
