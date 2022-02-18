package com.fxl.frame.util.encrypt;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加解密工具类
 * @author fangxilin
 * @Description TODO
 * @date 2019-03-08
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2019
 */
public class AESEncryptUtils {

    /**
     * 加密用的 SecretKey
     */
    private static String sKeyDefault = "ningyuan160Bazyy";
    /**
     * 偏移量
     */
    private static String ivParameterDefault = "0102030405060708";
    /**
     * 算法方式
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 算法/模式/填充
     */
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    /**
     * 私钥大小128/192/256(bits)位 即：16/24/32bytes，暂时使用128，如果扩大需要更换java/jre里面的jar包
     */
    private static final Integer PRIVATE_KEY_SIZE_BIT = 128;


    /**
     * 加密
     * @date 2019/3/11 15:24
     * @auther fangxilin
     * @param plainText 明文：要加密的内容
     * @return java.lang.String
     */
    public static String encrypt(String plainText) {
        return encrypt(plainText, sKeyDefault, ivParameterDefault);
    }

    public static String encrypt(String plainText, String sKey, String ivParameter) {
        // 密文字符串
        String cipherText = "";
        try {
            // 加密模式初始化参数
            Cipher cipher = initParam(Cipher.ENCRYPT_MODE, sKey, ivParameter);
            // 获取加密内容的字节数组
            byte[] bytePlainText = plainText.getBytes("UTF-8");
            // 执行加密
            byte[] byteCipherText = cipher.doFinal(bytePlainText);
            //cipherText = Base64.encodeBase64String(byteCipherText);
            cipherText = parseByte2HexStr(byteCipherText);
        } catch (Exception e) {
            throw new RuntimeException("encrypt fail!", e);
        }
        return cipherText;
    }

    public static String encrypt1(String sSrc, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return parseByte2HexStr(encrypted);
    }

    /**
     * 解密
     * @date 2019/3/11 17:54
     * @auther fangxilin
     * @param cipherText
     * @return java.lang.String
     */
    public static String decrypt(String cipherText) {
        // 明文字符串
        String plainText = "";
        try {
            Cipher cipher = initParam(Cipher.DECRYPT_MODE);
            // 将加密并编码后的内容解码成字节数组
            //byte[] byteCipherText = Base64.decodeBase64(cipherText);
            byte[] byteCipherText = parseHexStr2Byte(cipherText);
            // 解密
            byte[] bytePlainText = cipher.doFinal(byteCipherText);
            plainText = new String(bytePlainText, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("decrypt fail!", e);
        }
        return plainText;
    }

    /**
     * 字节数组转为16进制字符串
     * @date 2019/3/11 17:56
     * @auther fangxilin
     * @param buf
     * @return java.lang.String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为字节数组
     * @date 2019/3/11 18:01
     * @auther fangxilin
     * @param hexStr
     * @return byte[]
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        byte digest[] = new byte[hexStr.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = hexStr.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    /**
     * 初始化参数
     * @date 2019/3/12 14:23
     * @auther fangxilin
     * @param mode
     * @return javax.crypto.Cipher
     */
    public static Cipher initParam(int mode) {
        return initParam(mode, sKeyDefault, ivParameterDefault);
    }

    public static Cipher initParam(int mode, String sKey, String ivParameter) {
        Cipher cipher = null;
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(sKey.getBytes());
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            //kgen.init(128, new SecureRandom(key.getBytes()));
            kgen.init(PRIVATE_KEY_SIZE_BIT, secureRandom);
            //3.产生原始对称密钥
            SecretKey secretKey = kgen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] enCodeFormat = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
            //6.根据指定算法AES自成密码器
            cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);

            /*SecretKeySpec keySpec = new SecretKeySpec(sKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");*/

            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(mode, keySpec, iv);
        } catch (Exception e) {
            throw new RuntimeException("initParam fail!", e);
        }
        return cipher;
    }




    public static void main(String[] args) throws Exception {
//        String text = "{\"Input\":\"<Request><\\/Request>\"}";
//        String miwen = encrypt(text);
//        System.out.println("密文为：" + miwen);
//        String mingwen = decrypt(miwen);
//        System.out.println("明文为：" + mingwen);

        JSONObject object = new JSONObject(3);
        object.put("idCardNo", "440303196603071233");
        object.put("phone", "18320995792");
        object.put("patientName", "张三");
        System.out.println("明文：" + object.toJSONString());

        String sKey = "7A7C089A905B46F3";
        String ivParameter = "7A7C089A905B46F3";
        String encrypt = encrypt1(object.toJSONString(), sKey, ivParameter);
        System.out.println("密文：" + encrypt);
        String url = "https://zhfy.sdyunban.com/weixin/redirectWx?subHId=bcf9a49b87ff7591&hospitalId=100010&kdParam=" + encrypt;
        System.out.println("url = " + url);

    }

}
