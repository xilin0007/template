package com.jdk8;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Description Base64Test
 * @author fangxilin
 * @date 2020-10-12
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class Base64Test {

    public static void main(String[] args) {
        String text = "https://www.javacodegeeks.com/2015/02/java-8特性指南.html";
        String encodeStr = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        System.out.println("encodeStr = " + encodeStr);
        String decodeStr = new String(Base64.getDecoder().decode(encodeStr), StandardCharsets.UTF_8);
        System.out.println("decodeStr = " + decodeStr);


        String urlEncodeStr = Base64.getUrlEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        System.out.println("urlEncodeStr = " + urlEncodeStr);
        String urlDecodeStr = new String(Base64.getUrlDecoder().decode(urlEncodeStr), StandardCharsets.UTF_8);
        System.out.println("urlDecodeStr = " + urlDecodeStr);

    }
}
