package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.lyqxsc.yhpt.domain.InvitationCode;

@Mapper
@Component
public interface IInvitationCodeDao {
	@Select("select * from invitationcode where code=#{code}")
	InvitationCode selectInvitationCodeByCode(@Param("code") String code);
	
	@Update("update invitationcode set isBind=0,userID=#{userID} where code=#{code}")
	int bindInvitationCode(@Param("userID") long userID, @Param("code")String code);
	
	//通过分销商ID获取邀请码
	@Select("select code from invitationcode where distributorID=#{id}")
	String getInvitationCode(@Param("id") long id);
	
	@Select("select * from invitationcode where distributorID=#{id}")
	List<InvitationCode> selectInvitationCodeByDistributor(@Param("id") long id);
	
	@Select("select * from invitationcode where distributorID=#{id} and isBind=0")
	List<InvitationCode> selectInvitationCodeNoBindByDistributor(@Param("id") long id);
	
	@Select("select * from invitationcode where distributorID=#{id} and isBind=1")
	List<InvitationCode> selectInvitationCodeIsBindByDistributor(@Param("id") long id);
	
	
}
