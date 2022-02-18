package com.fxl.frame.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

/**
 * 获取短信签名工具类
 * 验签时，将参数重新加密，对比客户端传过来的签名，看是否相同
 * @Description TODO
 * @author fangxilin
 * @date 2017-5-31
 * @Copyright: Copyright (c) 2016 Shenzhen Angelsound Technology Co., Ltd. Inc. 
 *             All rights reserved.
 */
public class SignatureUtils {
    
    private static final String SMS_SESSION_KEY = "keys";
    
    private static final String SMS_SESSION_SECRET = "secret";
	
	/**
	 * dubbo获取短信签名Map参数
	 * @param hospId
	 * @param mobile
	 * @param content
	 * @return
	 */
	public static Map<String, String> getSignatureParams(Integer hospId, String mobile, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("hospId", hospId.toString());
		params.put("mobile", mobile);
		params.put("content", content);//短信内容
		params.put("SMS_SESSION_KEY", SMS_SESSION_KEY);
		return params;
	}
	
	/**
	 * http接口获取短信签名Map参数
	 * @param hospId
	 * @param hospName
	 * @param mobile
	 * @param content
	 * @return
	 */
	public static Map<String, String> getHttpSignatureParams(Integer hospId, String hospName, String mobile, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("hospId", hospId.toString());
		params.put("hospName", hospName);
		params.put("mobile", mobile);
		params.put("content", content);//短信内容
		params.put("SMS_SESSION_KEY", SMS_SESSION_KEY);
		return params;
	}
	
	/**
	 * 签名生成算法
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型,params中需要的的参数：hospId，mobile，content，SMS_SESSION_KEY。
	 * @param String secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(Map<String,String> params) throws IOException	{
	    // 先将参数以其参数名的各个char的ASCII码值升序进行排序
	    Map<String, String> sortedParams = new TreeMap<String, String>(params);
	    Set<Entry<String, String>> entrys = sortedParams.entrySet();
	 
	    // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	    StringBuilder basestring = new StringBuilder();
	    for (Entry<String, String> param : entrys) {
	        basestring.append(param.getKey()).append("=").append(String.valueOf(param.getValue()));
	    }
	    //获取密钥
	    //String SMS_SESSION_SECRET = SMS_SESSION_SECRET;
	    basestring.append(SMS_SESSION_SECRET);
	    //logger.info("SMS basestring:"+basestring);
	    // 使用MD5对待签名串求签
	    byte[] bytes = null;
	    try {
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
	    } catch (GeneralSecurityException ex) {
	        throw new IOException(ex);
	    }
	 
	    // 将MD5输出的二进制结果转换为小写的十六进制
	    StringBuilder sign = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(bytes[i] & 0xFF);
	        if (hex.length() == 1) {
	            sign.append("0");
	        }
	        sign.append(hex);
	    }
	    return sign.toString();
	}

	private static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
