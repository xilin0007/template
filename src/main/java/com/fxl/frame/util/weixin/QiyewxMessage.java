package com.fxl.frame.util.weixin;

import com.fxl.frame.util.http.HttpClientUtil;

/**
 * @Description 企业微信消息发送
 * @author fangxilin
 * @date 2020-12-11
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class QiyewxMessage {

	/**
	 * 企业微信接口域名
	 */
	public static final String QIYEWX_URL = "https://qyapi.weixin.qq.com";

	/**
	 * 获取access_token
	 */
	public static final String ACCESS_TOKEN_PATH = "/cgi-bin/gettoken";

	/**
	 * 发送应用消息
	 */
	public static final String SEND_PGMSG_PATH = "/cgi-bin/message/send";

	/**
	 * 获取accessToken
	 */
	public static String getAccessToken(String appId, String appSecret) {
		/**
		 * 出参
		 * {"errcode":0,"errmsg":"ok","access_token":"lakGmIIL6wXG_7dZ6zByf1lWgNi7QrtcwCaIGQjUmUnuwLp3K2_3-sZVyglnlBSfFi1LI-uSTz4TifGqiGqmkp1Z_iow-u5pVO_VXHXpFme7AB_0kZIn_glfHQY5a13qfOIXwsWqYV2yL4ydIyNYJZKDsEdUArkxMgSbgYyEEP_eOhgWTdMMlcDA6ML22URZkydfUQzM0pK6XKuWlgIxBA","expires_in":7200}
		 */
		String url = QIYEWX_URL + ACCESS_TOKEN_PATH + "?corpid=" + appId + "&corpsecret=" + appSecret;
		String accessToken = HttpClientUtil.sendHttpGet(url);
		return accessToken;
	}

	public static void sendPgMsg(String accessToken) {
		//文档：https://work.weixin.qq.com/api/doc/90000/90135/90236#%E6%96%87%E6%9C%AC%E5%8D%A1%E7%89%87%E6%B6%88%E6%81%AF
		String data = "{\n" +
				"  \"touser\" : \"FangXiLin\",\n" +   //用户
				"  \"toparty\" : \"\",\n" +  //部门
				"  \"totag\" : \"\",\n" +    //标签
				"  \"msgtype\" : \"textcard\",\n" +     //文本卡片消息固定传 textcard
				"  \"agentid\" : 1000013,\n" +
				"  \"textcard\" : {\n" +
				"\t\"title\" : \"领奖通知\",\n" +
				"\t\"description\" : \"<div class=\\\"gray\\\">2016年9月26日</div> <div class=\\\"normal\\\">恭喜你抽中iPhone 7一台，领奖码：xxxx</div><div class=\\\"highlight\\\">请于2016年10月10日前联系行政同事领取</div>\",\n" +
				"\t\"url\" : \"https://www.baidu.com/\",\n" +
				"\t\"btntxt\":\"更多\"\n" +
				"  },\n" +
				"  \"enable_id_trans\": 0,\n" +
				"  \"enable_duplicate_check\": 0,\n" +
				"  \"duplicate_check_interval\": 1800\n" +
				"}";

		/**
		 * 出参
		 * {"errcode":0,"errmsg":"ok","invaliduser":""}
		 */
		String url = QIYEWX_URL + SEND_PGMSG_PATH + "?access_token=" + accessToken;
		String result = HttpClientUtil.sendHttpPostJson(url, data);
//		System.out.println("入参：" + data);
		System.out.println("出参：" + result);
	}

	public static void main(String[] args) {
//		String corpid = "wx318b2fe79f8d951b";
//		String corpsecret = "_-dvLdjwVaxuWoby-6qbwlDcDC_18pf51CUBooRSgug"; //测试应用消息的secret，各个应用都不一样，需区分开
//		String accessToken = getAccessToken(corpid, corpsecret);
//		System.out.println("accessToken = " + accessToken);

		String accessToken = "lakGmIIL6wXG_7dZ6zByf1lWgNi7QrtcwCaIGQjUmUnuwLp3K2_3-sZVyglnlBSfFi1LI-uSTz4TifGqiGqmkp1Z_iow-u5pVO_VXHXpFme7AB_0kZIn_glfHQY5a13qfOIXwsWqYV2yL4ydIyNYJZKDsEdUArkxMgSbgYyEEP_eOhgWTdMMlcDA6ML22URZkydfUQzM0pK6XKuWlgIxBA";
		sendPgMsg(accessToken);

	}
}
