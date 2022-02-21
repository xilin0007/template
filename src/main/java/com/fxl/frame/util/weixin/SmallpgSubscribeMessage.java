package com.fxl.frame.util.weixin;

import com.alibaba.fastjson.JSONObject;
import com.fxl.frame.util.http.HttpClientUtil;

/**
 * @Description 小程序订阅消息发送
 * @author fangxilin
 * @date 2020-11-09
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class SmallpgSubscribeMessage {

    /**
     * 发送订阅消息
     */
    public static final String SEND_SUBSCRIBE_PATH = "/cgi-bin/message/subscribe/send";

    /**
     * 发送统一服务消息
     */
    public static final String SEND_UNIFORM_PATH = "/cgi-bin/message/wxopen/template/uniform_send";


    /**
     * 发送订阅消息，需获取下发权限，小程序前端代码中添加 wx.requestSubscribeMessage
     * @param accessToken
     * @param touser
     * @param templateId
     * @param templateCode
     * @param data
     * @return java.lang.String
     */
    public static String sendSubscribeMsg(String accessToken, String touser, String templateId, String templateCode, JSONObject data) {
        String url = WeixinUtil.WEIXIN_URL + SEND_SUBSCRIBE_PATH + "?access_token=" + accessToken;
        JSONObject params = new JSONObject();
        params.put("touser", touser);
        params.put("template_id", templateId);
        params.put("page", "pages/home/home"); //跳转的小程序页面
        params.put("miniprogram_state", "developer");
//        params.put("lang", "zh_CN");
        params.put("data", data);
        String result = HttpClientUtil.sendHttpPostJson(url, params.toJSONString());
        System.out.println("入参：" + params.toJSONString());
        System.out.println("出参：" + result);
        return result;
    }

    public static void main(String[] args) {
//        String accessToken = WeixinUtil.getAccessToken("wx62b338a3bce04805", "d04278b039af677516f7f5d01670e62e");
//        System.out.println("accessToken = " + accessToken);


        String accessToken = "39_B4vc6LITJpZHsW4Yz-KijfUzwlZHRmN2nox5WpvhT5GFnjA5SAueAdjL6XO4u6aC7KW29SqWimMy-s7HiwpHF1mf_KfMwMiW7GD-kAdFf5EgesgtvpxUqC2fSPrw4uw-KxrzrhItDob-RrmXRCCjAGAKVV";
        String touser = "oso6R4tBjjuFbCyYTGxDrOaJRLoA";
        String templateId = "8kVzAg8TqXAEvIAk1um9H5FKn6w5N4qKWwm34veJL5o"; //缴费成功通知 一次性订阅
        String templateCode = "xxx";

        JSONObject data =  new JSONObject();
        JSONObject character_string1 = new JSONObject(); //缴费编号
        character_string1.put("value", "jfbh123456789");
        JSONObject name2 = new JSONObject(); //医院名称
        name2.put("value", "南山医院");
        JSONObject name3 = new JSONObject(); //患者姓名
        name3.put("value", "张三");
        JSONObject amount4 = new JSONObject(); //缴费金额
        amount4.put("value", "100元");
        JSONObject date5 = new JSONObject(); //缴费时间
        date5.put("value", "2020年11月26日 18点");
        data.put("character_string1", character_string1);
        data.put("name2", name2);
        data.put("name3", name3);
        data.put("amount4", amount4);
        data.put("date5", date5);
        /**
         * 入参：{"touser":"oso6R4tBjjuFbCyYTGxDrOaJRLoA","data":{"name3":{"value":"张三"},"amount4":{"value":"100元"},"character_string1":{"value":"jfbh123456789"},"date5":{"value":"jfbh123456789"},"name2":{"value":"南山医院"}},"template_id":"8kVzAg8TqXAEvIAk1um9H5FKn6w5N4qKWwm34veJL5o"}
         * 出参：{"errcode":43101,"errmsg":"user refuse to accept the msg rid: 5fa9085a-66330da4-3a3dfdb2"}
         */
        sendSubscribeMsg(accessToken, touser, templateId, templateCode, data);

    }

}
