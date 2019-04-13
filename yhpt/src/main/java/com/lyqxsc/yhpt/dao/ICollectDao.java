package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Collect;

@Mapper
@Component
public interface ICollectDao {
	@Select("select * from collectlist where userid=#{id}")
	List<Collect> getShoppingByID(@Param("id") long id);
	
	@Select("select max(id) from collectlist")
	Long getMaxID();
	
	@Insert({"insert into collectlist(id, userid, commodityid, name, picurl, price, note)"
			+ "values(#{id}, #{userid}, #{commodityid}, #{name}, #{picurl}, #{price}, #{note})"})
	int addCollect(Collect collect);
	
	@Delete("delete from collectlist where id=#{id}")
	int removeCollect(@Param("id") long id);
}
