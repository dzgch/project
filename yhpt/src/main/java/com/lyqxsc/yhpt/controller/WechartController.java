package com.lyqxsc.yhpt.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lyqxsc.yhpt.domain.User;
import com.lyqxsc.yhpt.domain.UserSignature;
import com.lyqxsc.yhpt.service.UserService;
import com.lyqxsc.yhpt.urlclass.UserLogin;
import com.lyqxsc.yhpt.utils.HttpRequestor;
import com.lyqxsc.yhpt.utils.RetJson;

import net.sf.json.JSONObject;

@RestController
@CrossOrigin
public class WechartController {
	
	@Autowired
	UserService userService;
	
//	@Value("${wechatMpAuthorize}")
//	String wxurl;
	
	@Value("${appid}")
	String wxAppid;
	
	@Value("${secret}")
	String wxSecret;
	
	static final Logger log = LoggerFactory.getLogger(WechartController.class);  

	@RequestMapping(value = "/wxuserinfo", method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RetJson doPost(@RequestBody UserLogin request) throws ServletException, IOException {
		String code = request.getCode();
		String ip = request.getIp();
		String url = request.getUrl();
		String appid = wxAppid;
		String secret = wxSecret;
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		/*第一次请求 获取access_token 和 openid*/
		String oppid = null;
		try {
			oppid = new HttpRequestor().doGet(requestUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*第二次请求，获取用户信息*/
		JSONObject oppidObj = JSONObject.fromObject(oppid);
		String access_token = (String) oppidObj.get("access_token");
		
		String openid = (String) oppidObj.get("openid");
		String requestUrl2 = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		String userInfoStr = null;
		try {
			userInfoStr = new HttpRequestor().doGet(requestUrl2);
			log.info(userInfoStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*用户信息*/
		//{"sex":1,"language":"zh_CN","city":"成都","province":"四川","country":"中国"
		JSONObject wxUserInfo = JSONObject.fromObject(userInfoStr); 
		log.info((String)wxUserInfo.get("openid"));
		log.info((String)wxUserInfo.get("nickname"));
//		log.info((String)wxUserInfo.get("sex"));
		log.info((String)wxUserInfo.get("city"));
		log.info((String)wxUserInfo.get("province"));
		log.info((String)wxUserInfo.get("country"));
		log.info((String)wxUserInfo.get("headimgurl"));
		
		String accessToken = getAccessToken();
		String ticket = JsapiTicket(accessToken);
		
		Map<String,String> signature = getSignature(ticket, url);
		
		User user = userService.login(wxUserInfo, ip);
		String invitationCode = userService.getInvitationCode(user.getDistributor());
		UserSignature ret = new UserSignature();
		ret.setSignature(signature);
		ret.setUser(user);
		ret.setCode(invitationCode);
		return RetJson.success("成功", ret);
	}
	
    // 网页授权接口
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    public String getAccessToken() {
    	String appid = wxAppid;
    	String appsecret = wxSecret;
        String requestUrl = GetPageAccessTokenUrl.replace("APPID", appid).replace("SECRET", appsecret);
        HttpClient client = null;
        String accessToken = null;
        try {
            client = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);
            accessToken = String.valueOf(OpenidJSONO.get("access_token"));
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return accessToken;
    }
    
    public final static String GetPageAccessTokenUrl2 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    
    
    //2、使用access_token获取jsapi_ticket
    public static String JsapiTicket(String accessToken) {
        String requestUrl = GetPageAccessTokenUrl2.replace("ACCESS_TOKEN", accessToken);
        HttpClient client = null;
        Map<String, String> result = new HashMap<String, String>();
        String ticket = null;
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);
            String errcode = String.valueOf(OpenidJSONO.get("errcode"));
            String errmsg = String.valueOf(OpenidJSONO.get("errmsg"));
            ticket = String.valueOf(OpenidJSONO.get("ticket"));
            String expires_in = String.valueOf(OpenidJSONO.get("expires_in"));
            result.put("errcode", errcode);
            result.put("errmsg", errmsg);
            result.put("ticket", ticket);
            result.put("expires_in", expires_in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return ticket;
    }

    public Map<String,String> getSignature(String ticket, String wxurl) {
//    	String ticket = "HoagFKDcsGMVCIY2vOjf9rtVef1QX5qnYjUiQyLkNvp_vtDrphqMpAKoBdCpgG08A7fYUQDYmV9Q36epimVfKA";

    	String noncestr = UUID.randomUUID().toString();
    	String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
    	//4获取url
    	String url = wxurl;
    	//5、将参数排序并拼接字符串
    	String str = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
    	String signature = SHA1(str);
        System.out.println("noncestr=" + noncestr);
        System.out.println("timestamp=" + timestamp);
        System.out.println("signature=" + signature);
        Map<String, String> map = new HashMap<String, String>();
        map.put("appId", wxAppid);
        map.put("timestamp",timestamp);
        map.put("ticket",ticket);
        map.put("noncestr",noncestr);
        map.put("signature",signature);
        return map;
    }
    
    public static String SHA1(String str) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
