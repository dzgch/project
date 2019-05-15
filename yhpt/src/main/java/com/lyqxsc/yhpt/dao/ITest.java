package com.lyqxsc.yhpt.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.Commodity;
import com.lyqxsc.yhpt.domain.Test;

@Mapper
@Component
public interface ITest {
	@Insert({"insert into test(name) values(#{name})"})
	int addCommodity(Test t);
}
