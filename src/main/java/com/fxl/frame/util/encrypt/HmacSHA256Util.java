package com.fxl.frame.util.encrypt;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Description HmacSHA256Util
 * @author fangxilin
 * @date 2020-09-22
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class HmacSHA256Util {

    private final static char[] hexArray = "0123456789abcdef".toCharArray();


    /**
     * HmacSHA256加密
     * @date 2020/9/22 18:18
     * @auther fangxilin
     * @param secret 密钥
     * @param content 需加密的内容
     * @return java.lang.String
     */
    public static String encrypt(String secret, String content) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"),
                "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return bytesToHex(sha256_HMAC.doFinal(content.getBytes("UTF-8")));

    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}
