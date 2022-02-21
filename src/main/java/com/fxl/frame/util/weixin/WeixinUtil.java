package com.fxl.frame.util.weixin;

import com.fxl.frame.util.http.HttpClientUtil;

/**
 * @Description WeixinUtil
 * @author fangxilin
 * @date 2020-11-09
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class WeixinUtil {

    public static final String WEIXIN_URL = "https://api.weixin.qq.com";

    /**
     * 公众号、小程序获取access_token方法
     */
    public static final String ACCESS_TOKEN_PATH = "/cgi-bin/token";

    /**
     * 公众号获取用户openid方法
     */
    public static final String GZH_OPENID_PATH = "/sns/oauth2/access_token";

    /**
     * 小程序获取用户openid方法
     */
    public static final String smallpg_openid_path = "/sns/jscode2session";

    /**
     * 获取accessToken
     */
    public static String getAccessToken(String appId, String appSecret) {
        String url = WEIXIN_URL + ACCESS_TOKEN_PATH + "?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        String accessToken = HttpClientUtil.sendHttpGet(url);
        return accessToken;
    }

    public static void main(String[] args) {
        String accessToken = getAccessToken("wx62b338a3bce04805", "d04278b039af677516f7f5d01670e62e");
        /**
         * {"access_token":"39_k_fOMgIK6rIjMJsRAlMf1cBHSgDdfWXIXP3pUW8uDbS_VP4CxFvgeEIxFDVuPAwWesQ7LBrHkSIKPumXpSNfrqgmLKvWQL8kjRpWHHe-KQstBvbWzOfYVLuxi5ZS71X6_yMZO1jjedEmfwdjJSHgACAXGV","expires_in":7200}
         */
        System.out.println("accessToken = " + accessToken);
    }
}
