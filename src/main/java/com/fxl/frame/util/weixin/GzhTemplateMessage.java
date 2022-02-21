package com.fxl.frame.util.weixin;

import com.alibaba.fastjson.JSONObject;
import com.fxl.frame.util.http.HttpClientUtil;

/**
 * @Description 公众号模板消息
 * @author fangxilin
 * @date 2020-11-09
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class GzhTemplateMessage {

    /**
     * 公众号发送模板消息方法
     */
    public static final String SEND_TEMPLATE_PATH = "/cgi-bin/message/template/send";

    /**
     * 发送订阅消息，需获取下发权限，小程序前端代码中添加 wx.requestSubscribeMessage
     * @param accessToken
     * @param touser
     * @param templateId
     * @param templateCode
     * @param data
     * @return java.lang.String
     */
    public static String sendTemplateMsg(String accessToken, String touser, String templateId, String templateCode, String redirtUrl, JSONObject data) {
        String url = WeixinUtil.WEIXIN_URL + SEND_TEMPLATE_PATH + "?access_token=" + accessToken;
        JSONObject params = new JSONObject();
        params.put("touser", touser);
        params.put("template_id", templateId);
//        params.put("url", redirtUrl);
//        params.put("miniprogram", null); //跳转小程序所需，非必填
        params.put("data", data);
        String result = HttpClientUtil.sendHttpPostJson(url, params.toJSONString());
        System.out.println("入参：" + params.toJSONString());
        System.out.println("出参：" + result);
        return result;
    }


    public static void main(String[] args) {
//        String accessToken = WeixinUtil.getAccessToken("wx6491ed54ee2b175a", "e65dcc3f0c07e67ab8fe0b909454dbcc");
//        System.out.println("accessToken = " + accessToken);


        String accessToken = "39_NBvRb1FveQSBTXgUXvBzlnRmzv6uQTfYAJU9RpgspxmSnv3i8KP8Qa6CsqoOJcy71_xnNwRyxODVYyfgMxrFa4ZPVB6xx-tgvHAAxC2gmzvJoybragZIZklbsXL-eoCQcmdi_8_QU_DQogoxWJSeCBAZMV";
        String touser = "oLXSXwBnz02eiMrdj9tfavz3FVZ8";
        String templateId = "hq49DTkhWUESqTB5-6lAzw8bE6qtyersA_9JRhnDwAQ"; //医院通知
        String templateCode = "xxx";

        JSONObject data =  new JSONObject();
        JSONObject first = new JSONObject(); //标题
        first.put("value", "您的预约申请已经受理。");
        JSONObject keyword1 = new JSONObject(); //通知时间
        keyword1.put("value", "2020年11月9日");
        JSONObject keyword2 = new JSONObject(); //通知内容
        keyword2.put("value", "您的预约申请已经受理，张XX医生将在XX日为您提供当面咨询服务。");
        JSONObject remark = new JSONObject(); //备注
        remark.put("value", "深圳市第三人民医院");
        data.put("first", first);
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("remark", remark);


        sendTemplateMsg(accessToken, touser, templateId, templateCode, "", data);

    }
}
