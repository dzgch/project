package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Commodity;

@Mapper
@Component
public interface ICommodityDao {

	@Select("select * from commoditylist")
	List<Commodity> selectAllCommodity();
	
	@Select("select * from commoditylist where distributor=#{distributor}")
	List<Commodity> selectCommodityByDistributor(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where id=#{id}")
	Commodity selectCommodityByID(@Param("id") int id);
	
	@Select("select * from commoditylist where distributor=#{distributor} and id > #{id}")
	List<Commodity> selectNewCommodityByDistributor(@Param("distributor") long distributor,@Param("id") long id);
	
	@Select("select max(id) from commoditylist")
	Long getMaxID();
	
	@Insert("insert into commoditylist(id,name,picurl,price,type,inventory,ordernum,note,distributor)"
			+ "values(#{id},#{name},#{picurl},#{price},#{type},#{inventory},#{ordernum},#{note},#{distributor})")
	int addCommodity(Commodity commodity);
	
	@Delete("delete from commoditylist where id = #{id} and name = #{name}")
	int removeCommodity(@Param("id") int id,@Param("name")String name);
	
	@Update("update commoditylist set name=#{name}, picurl=#{picurl}, price=#{price}, type=#{type}, inventory=#{inventory}, ordernum=#{ordernum}, note=#{note}, distributor=#{distributor} "
			+ "where id=#{id}")
	int updateCommodity(Commodity commodity);
}
