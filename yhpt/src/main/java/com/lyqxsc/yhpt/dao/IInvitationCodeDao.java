package com.lyqxsc.yhpt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lyqxsc.yhpt.domain.InvitationCode;

public interface IInvitationCodeDao {
	@Select("select * from invitationcode where code=#{code}")
	InvitationCode selectInvitationCodeByCode(@Param("code") String code);
	
	@Update("update invitationcode set isBind=1 where code=#{code}")
	int bindInvitationCode(@Param("code")String code);
	
	
	@Select("select * from invitationcode where distributorID=#{id}")
	List<InvitationCode> selectInvitationCodeByDistributor(@Param("id") long id);
	
	@Select("select * from invitationcode where distributorID=#{id} and isBind=0")
	List<InvitationCode> selectInvitationCodeNoBindByDistributor(@Param("id") long id);
	
	@Select("select * from invitationcode where distributorID=#{id} and isBind=1")
	List<InvitationCode> selectInvitationCodeIsBindByDistributor(@Param("id") long id);
	
	
}
