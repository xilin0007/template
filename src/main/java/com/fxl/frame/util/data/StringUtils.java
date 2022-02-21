package com.fxl.frame.util.data;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Description StringUtils
 * @author fangxilin
 * @date 2021-04-07
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2021
 */
public class StringUtils {

	/**
	 * 获取到中文的utf-8编码字符串
	 * @date 2021/4/7 下午4:09
	 * @auther fangxilin
	 * @param str
	 * @return java.lang.String
	 */
	public static String getCNUtf8String(String str) {
		if (str != null && str.length() > 0) {
			if (StandardCharsets.ISO_8859_1.newEncoder().canEncode(str)) {//这个方法是关键，可以判断乱码字符串是否为指定的编码
				System.out.println("进行编码转换...");
				str = new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
			}
		}
		return str;
	}
}
