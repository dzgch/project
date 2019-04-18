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
	
	@Select("select max(id) from commoditylist")
	Long getMaxID();
	
	@Select("select max(ordernum) from commoditylist")
	Integer getMaxOrdernum();
	
	@Select("select * from commoditylist where id=#{id}")
	Commodity selectCommodityByID(@Param("id") long id);
	
	/*
	 * 管理员三连
	 */
	@Select("select * from commoditylist")
	List<Commodity> selectAll();
	
	@Select("select * from commoditylist where type=2 or type=3")
	List<Commodity> selectCommodity();
	
	@Select("select * from commoditylist where type=1 or type=3")
	List<Commodity> selectRentCommodity();
	
	/*
	 * 分销商三连
	 */
	@Select("select * from commoditylist where distributor=#{distributor}")
	List<Commodity> selectAllByDistributor(@Param("distributor") long distributor);
	
	
	
	/*
	 * 用户四连
	 */
	@Select("select * from commoditylist where (type=2 or type=3) and (distributor=#{distributor} or distributor=0)")
	List<Commodity> selectCommodityForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where (type=1 or type=3) and (distributor=#{distributor} or distributor=0)")
	List<Commodity> selectRentCommodityForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where distributor=#{distributor} or distributor=0")
	List<Commodity> selectAllCommodityForUser(@Param("distributor") long distributor);
	
	@Select("select * from commoditylist where id=#{id} and (distributor=#{distributor} or distributor=0)")
	Commodity selectCommodityByIDForUser(@Param("id")long id, @Param("distributor") long distributor);
	
	@Select("select * from commoditylist where distributor=#{distributor} and id=#{id}")
	Commodity selectNewCommodityByDistributor(@Param("distributor") long distributor,@Param("id") long id);
	
	@Select("select * from commoditylist where distributor=#{distributor} and ordernum=#{ordernum}")
	Commodity selectHotCommodityByDistributor(@Param("distributor") long distributor,@Param("ordernum") int ordernum);

	@Select("select * from commoditylist where distributor=#{distributor} and classId=#{classId}")
	List<Commodity> selectCommodityByClass(@Param("distributor") long distributor, @Param("classId") int classId);
	
	@Select("select * from commoditylist where distributor=#{distributor} and name=#{name}")
	List<Commodity> selectCommodityByName(@Param("distributor") long distributor, @Param("name") String name);

	
	@Insert("insert into commoditylist(id,name,picurl,price,type,inventory,ordernum,deposit,note,distributor,classId,classStr)"
			+ "values(#{id},#{name},#{picurl},#{price},#{type},#{inventory},#{ordernum},#{deposit},#{note},#{distributor},#{classId},#{classStr})")
	int addCommodity(Commodity commodity);
	
	@Delete("delete from commoditylist where id=#{id}")
	int removeCommodity(@Param("id") long id);
	
	@Delete("delete from commoditylist where id=#{id} and distributor=#{distributor}")
	int removeCommodityByDistributor(@Param("id") long id, @Param("distributor")long distributor);
	
	
	@Update("update commoditylist set name=#{name},picurl=#{picurl},price=#{price},type=#{type},inventory=#{inventory},ordernum=#{ordernum},deposit=#{deposit},note=#{note}"
			+ "where id=#{id}")
	int updateCommodity(Commodity commodity);
}
