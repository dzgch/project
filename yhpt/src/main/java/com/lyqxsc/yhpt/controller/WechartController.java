package com.lyqxsc.yhpt.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lyqxsc.yhpt.config.ProjectUrlConfig;
import com.lyqxsc.yhpt.enums.ResultEnum;
import com.lyqxsc.yhpt.exception.SellException;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

@RestController
@CrossOrigin
public class WechartController {
	
//	@Autowired
//	public WxMpService wxMpService;
	
	@Autowired
	private ProjectUrlConfig projectUrlConfig;
	
	static final Logger log = LoggerFactory.getLogger(WechartController.class);  

	@RequestMapping(value = "/authorize", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String authorize(ServletRequest param){
		String returnUrl = param.getParameter("returnUrl");
		System.out.println("1");
		//1. 配置
		//2.调用方法
		String url=projectUrlConfig.getWechatMpAuthorize()+"/home";
		String redirectUrl = null;
//		try {
//			redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url,WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl,"utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		log.info("【微信网页授权】获取code,redirectUrl={}",redirectUrl);
		return "redirect:"+redirectUrl;//重定向到下面一个方法
	}
	
	@RequestMapping(value = "/userInfo", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String userInfo(@RequestParam("code") String code,
	                   @RequestParam("state") String returnUrl){
	WxMpOAuth2AccessToken wxMpOAuth2AccessToken=new WxMpOAuth2AccessToken();
//		try {
//		    wxMpOAuth2AccessToken=wxMpService.oauth2getAccessToken(code);
//		}catch (WxErrorException e){
//		    log.error("【微信网页授权】,{}",e);
//		    throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
//		}
//		String openId=wxMpOAuth2AccessToken.getOpenId();
//		log.info("【微信网页授权】获取openid,returnUrl={}",returnUrl);
//		return "redirect:"+ returnUrl+"?openid="+openId;
		return null;
	}
	
}
