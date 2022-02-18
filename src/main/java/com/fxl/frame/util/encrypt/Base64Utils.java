package com.fxl.frame.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Utils {
    
    public static String encrypt(String source) {
        byte[] bytes = source.getBytes();
        return Base64.encodeBase64String(bytes);
    }
    
    public static String decode(String enc) {
        byte[] bytes = Base64.decodeBase64(enc);
        return new String(bytes);
    }

    public static String encryptJava8(String source) {
        String encodeStr = java.util.Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
        return encodeStr;
    }

    public static String decodeJava8(String enc) {
        String decodeStr = new String(java.util.Base64.getDecoder().decode(enc), StandardCharsets.UTF_8);
        return decodeStr;
    }
    
    public static void main(String[] args) {
        String source = "adfsdfsdfwe2";
        String enc = encrypt(source);
        String dec = decode(enc);
        System.out.println(enc);
        System.out.println(dec);

        String encryptJava8 = encryptJava8(source);
        System.out.println("encryptJava8 = " + encryptJava8);
        String decodeJava8 = decodeJava8(encryptJava8);
        System.out.println("decodeJava8 = " + decodeJava8);

    }
}
