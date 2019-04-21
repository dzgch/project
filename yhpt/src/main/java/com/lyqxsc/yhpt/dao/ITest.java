package com.lyqxsc.yhpt.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Commodity;

@Mapper
@Component
public interface ITest {
	@Insert({"insert into commoditylist(id) values(#{id})"})
	int addCommodity(Commodity commodity);
}
