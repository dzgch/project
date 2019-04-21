package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.CommodityClassify;

@Mapper
@Component
public interface ICommodityClassifyDao {
	//添加分类
	@Insert({"insert into commodityclass(type,classId,classStr) "
			+ "values(#{type},#{classId},#{classStr})"})
	int insert(CommodityClassify commodityClassify);
	
	//删除分类
	@Delete("delete from commodityclass where classId=#{classId}")
	int delete(@Param("classId") int classId);
	
	//查询分类
	@Select("select * from commodityclass where type=#{type}")
	List<CommodityClassify> selectClass(@Param("type") int type);
	
	@Select("select max(classId) from commodityclass")
	Integer getMaxID();
}
