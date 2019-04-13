package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Appraise;


@Mapper
@Component
public interface IAppraiseDao {
	@Select("select * from appraiselist where id=#{id}")
	Appraise getAppraiseByID(@Param("id") long id);
	
	@Select("select * from appraiselist where userid=#{id}")
	List<Appraise> getAppraiseByUserID(@Param("id") long id);
	
	@Select("select * from appraiselist where thingID=#{id}")
	List<Appraise> getAppraiseByThingID(@Param("id") long id);
	
	@Select("select max(id) from appraiselist")
	Long getMaxID();
	
	@Insert({"insert into appraiselist(id, userID, username, thingID, text, grade, time)"
			+ "values(#{id}, #{userID}, #{username}, #{thingID}, #{text}, #{grade}, #{time})"})
	int addAppraise(Appraise appraise);
	
	@Delete("delete from appraiselist where id=#{id}")
	int removeAppraise(@Param("id") long id);
}
