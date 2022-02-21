package com.fxl.frame.util.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageSingleSendRequest;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.JsonUtil;
import com.alipay.easysdk.marketing.openlife.models.AlipayOpenPublicMessageSingleSendResponse;
import com.alipay.easysdk.marketing.openlife.models.Context;
import com.alipay.easysdk.marketing.openlife.models.Keyword;
import com.alipay.easysdk.marketing.openlife.models.Template;
import com.aliyun.tea.TeaConverter;
import com.aliyun.tea.TeaPair;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description AlipayTemplateMessage
 * @author fangxilin
 * @date 2020-12-03
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class AlipayTemplateMessage {

    public static final String appid = "2017082408360207";
    //RSA1密钥
    public static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANkQr05EI7zEuZbBnA6pbDhGFvpRrfQEypBC/k5QRzA3zvhHaHHOP8BJzrRHxSaAkoUWrs48XS1eogUBlq8TC0GMq8VzvzVJNP0PZWXmegjiSfApaYXRe8VwmtXarpLofsMoTSSj5hHLzFqxxEZoLyVuL8xIryqzqyOKg5zn4a97AgMBAAECgYBhpbnXMZwKDU2X3h0AE4NVhWJM1XW1snoVj62rOT3tGQ4Ibsczex7+t7U/MJImEezX5i5ZJJqeEFY+fkp1E9IZpJS63TkL3TBRiq0fVUlM4NCvGWrWoZbZXodooHFjHbRtbieZdKacQBV438t1OmfRyoMgU3VMu3qHB/k99d2KkQJBAPV60/5exvrnJa+L1KlBlC61gRLTpNCm/5H4eESacXrrkBM+gWff+csiDqH5+47s9uf7ooyRgLpHC+2dpwJT4JcCQQDiXh5MnkN+zqR7Wkm4YOI7LnVbTiwOw3wjBFHzs5OWCwBZfV64qa27FHe+r4WMeNUeU5tFqXrwWXWhg/HFbiC9AkEAhL8XRMcb8zJsPrHvHlhIOboNQpEQ34RvUBB9eRvLoh0gnEeA0xtbiW2xXAZC8tdR7cE19a35XsJswOIwHiIdoQJBAJDF1Ob3iP8+tZ9WfTN4cveanN/KPQ8sIuZaSvdrcAZT/uM89SlxC3VMf9DqfKT8WoDMw89ntwKklEC6HBRCfJkCQC5fsPGJ0ueWMZ5ayjcg8gzmroaACqW2gEtxCTvvyp+/CqGpUZlBgAfDm/h1yR0GQtDMG0cwrsbb9WNzkTI46QQ=";
    public static final String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    //RSA2密钥
    public static final String privateKey2 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDE+TG4uon1ZcY+xVNEywyIZQcG8GfAXq4g/acXzpM2a17YBDzRZP65vusUKUebO9Us5rcC/1VGtn/JDoSqL0Owf2mBlmrjiX3YSN3NVriwFURiZqMnnWfVYGsIOC5OESVu5NofTOUt6Px5USoQOBg3EbvyMuHd4RwaLtxbq0yTYhnyS3FB0KkAKB2Es01OQfr0l/Wy4HlPHyaef4SpTvOWFiOdal+I7hapUsYPuxko8/0E8c9nsROJlLXn7kz3tA+0D5aDCK5p2mo197UcTqAGPq8E4b4KjsrNmgchspNShphrQ5t0N2rNMhBvdMJktVQJuttC0xVhrPdg0uVMCAxrAgMBAAECggEAHeVgqZQOemOPGlni0I8tAMqsrV3xsV3tee5l3RQqfdkAFTNXc8SrcM/MaEv3pVUIfv8pidftPoVWkdKVSitQT8qW3edRo+VrZ1dv6smd0IP51CGV495dMyKmPCIizRCHKPStMpD+O+Cf6V3rT3jkG/+iEhms9HtFQ6gXJAa04a1+1DIkDxTmUpLK3hETkYhp17E9dbYc+13S8kb/TgfkDiPwv6eYpEaYQyG5gvf4KoH21jO1NfpOu0/r3MbY8EEgBKL3hE2h+Src9vxqvjjm6o19JwCTRbD61aJRrnYYw07FYQ+XvQ+k9A9rpN97nfR/p2IskdH0wQzPsiBJwS84SQKBgQDtKjM+C7FEAAExUk6n6/I8o6sv5W3RTYFlh+zwVweM3b99HolrYJbhwasoU1NFsoBInpOxCawBDdpbdG1Lvf5N3ilOnd0AUeM/Y6GKbV5U4wZ71dCsa+He+nuy3iwIred0s5UI5bY2lzS6mCQwsdOU4dGVjvv15eRrwTblfGOaPwKBgQDUndyjsxLtfoph6zUr+ECTNHVTe5DNLt2mUdhPX3NvG/1s1KzN3Xele6OwCt0NR7YYgpy5g68/r6xFYX70BSD+ZkFiPgjaAUvCabVSOUfsIf/e29eXpe05Ec7zMRQI2A4STFyiCHxDHrBDLLRwg2Uqthv5ZOtCgre4aAt+D83K1QKBgH/wVfhQom4Gep46woe/tsuTl1OdNuLEJtu4uc7lhAMv1I8QnB062+opacW1PaTJqH6EXTQR/aIL8RGI+zTy527mO+5XJtN3WVnmy0w7t7mfRu4i6BE7mXuKMJTLWYHNHqFMB0fnc6Z7Z0yJqmxCYdtc/ozpt25teA3RC2Y89hZNAoGBAKSz5UeqYO85J1nTKs108hEWggy5m+JWe71riXVzK6jWteqolXSkKxs2PAcTY0n2U0mJZH8RU6CyJXFZpU7C6lwoOl4wUAsRFe70lFGtrdluPRgWlVwUXT/oW7hoPGzdcYkbPmcGy7vy+fgPmXfPUX9UszbuxpAvUsK3Vq/A7lpBAoGAUP3qdNc8PtZ7iDdOMc4v5Uly0Z9oFojr8h/ILf32yDTjBynnv4xSzAgomEZCxD+47Un1a7OVLwmqGpcprzm/9GmXDihW3Vp4Pwb7Z7BW8Scwdb5Hj+XW2YaM3Yd59oONDSkUJ/Z0BpSxvFO0Lx6IbPPHnFiN7lG/YfH3OLO0jIo=";
    public static final String alipayPublicKey2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsSegZA9mTDcYum/zTDoRxP69J860NM3Xx+f34aWaoPBEdFhpupk2W/AyH/6C0zk8UEEs/xlQPdDYiiqieV+MTt8fdSxIKwNSzxhDWVKiQRRgoUiaKfxAP6fFbWKXpEjHO3xJR5RVzHTgbeTyeMh+T7xc96pSqAB1oT96GWpQJBmXrL/3Mp2pN75KdYSus/GaXS2kynyHQsD64sZmNsc1Yo5v9qpDasnYrXDurv4wyGJMxgw3mhvQp+bSp7ynthkzl5RT9iyYsWX6aW0ZBIGS9vuTK6UZ8lUfx5Rq/Vp/2zoA89/ctnis5w0MSXB/sZmAUDoZZGloAC70GmmRhf3VNwIDAQAB";



    public static void testSendAlipayMsgOld() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, privateKey, "json", "", alipayPublicKey, "RSA");
        AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
        request.setBizContent("{" +
                "\"to_user_id\":\"2088802620126559\"," +
                "\"template\":{" +
                "\"template_id\":\"d2b06389acf346af92172ee0a91939ea\"," +
                "\"context\":{" +
                "\"head_color\":\"#FF0000\"," +
                "\"url\":\"http://m.baidu.com\"," +
                "\"action_name\":\"查看详情\"," +
                "\"keyword1\":{" +
                "\"color\":\"#FF0000\"," +
                "\"value\":\"2020-12-04 17:02\"" +
                "        }," +
                "\"keyword2\":{" +
                "\"color\":\"#85be53\"," +
                "\"value\":\"您的预约申请已提交成功，张三医生将为你服务\"" +
                "        }," +
                "\"first\":{" +
                "\"color\":\"#85be53\"," +
                "\"value\":\"这是一条测试的医院通知，请忽略！\"" +
                "        }," +
                "\"remark\":{" +
                "\"color\":\"#85be53\"," +
                "\"value\":\"南山人民医院\"" +
                "        }" +
                "      }" +
                "    }" +
                "  }");
        com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    public static void testSendAlipayMsgOld1() {
        String toUserId = "2088802620126559";
        String templateId = "c2e061be9e464d1f8cb47df7b7b2b5ef"; //预约挂号成功通知
        // 1. 设置参数（全局只需设置一次）
        try {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid, privateKey2, "json", "", alipayPublicKey2, "RSA2");
            AlipayOpenPublicMessageSingleSendRequest request = new AlipayOpenPublicMessageSingleSendRequest();
            Map<String, Object> bizMap = new HashMap<String, Object>();
            bizMap.put("to_user_id", toUserId);
            Map<String, Object> templateMap = new HashMap<String, Object>();
            templateMap.put("template_id", templateId);
            Map<String, Object> contextMap = new HashMap<String, Object>();
//            contextMap.put("head_color", "#FF0000"); //颜色不生效，可不设置
//            contextMap.put("url", "http://m.baidu.com");
            contextMap.put("url", null);
            contextMap.put("action_name", "查看详情");
            Map<String, Object> first = new HashMap<String, Object>();
            first.put("value", "这是一条测试的预约挂号成功通知，请忽略！");
            Map<String, Object> keyword1 = new HashMap<String, Object>();
            keyword1.put("value", "张三");
//            keyword1.put("color", "#FF0000");
            Map<String, Object> keyword2 = new HashMap<String, Object>();
            keyword2.put("value", "南山人民医院");
            keyword2.put("color", "#85be53");
            Map<String, Object> keyword3 = new HashMap<String, Object>();
            keyword3.put("value", "口腔科");
            keyword3.put("color", "#85be53");
            Map<String, Object> keyword4 = new HashMap<String, Object>();
            keyword4.put("value", "华佗");
            keyword4.put("color", "#85be53");
            Map<String, Object> keyword5 = new HashMap<String, Object>();
            keyword5.put("value", "11123832000");
            keyword5.put("color", "#85be53");
            Map<String, Object> remark = new HashMap<String, Object>();
            remark.put("value", "end-哈哈哈哈");
            contextMap.put("first", first);
            contextMap.put("keyword1", keyword1);
            contextMap.put("keyword2", keyword2);
            contextMap.put("keyword3", keyword3);
            contextMap.put("keyword4", keyword4);
            contextMap.put("keyword5", keyword5);
            contextMap.put("remark", remark);
            templateMap.put("context", contextMap);
            bizMap.put("template", templateMap);

            request.setBizContent(JSON.toJSONString(bizMap));
            System.out.println("入参：" + request.getBizContent());

            com.alipay.api.response.AlipayOpenPublicMessageSingleSendResponse response = alipayClient.execute(request);
            System.out.println("出参：" + response.getBody());
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }


    }

    public static void testSendAlipayMsg() {
        String toUserId = "2088802620126559";
        String templateId = "d2b06389acf346af92172ee0a91939ea"; //医院通知
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
        try {
            Template template = new Template()
                    .setTemplateId(templateId)
                    .setContext(new Context().setHeadColor("#FF0000") //红色，颜色未生效，先写死
                            .setUrl("http://m.baidu.com").setActionName("查看详情")
                            .setKeyword1(new Keyword().setColor("#FF0000").setValue("2020-12-04 17:01"))
                            .setKeyword2(new Keyword().setColor("#85be53").setValue("您的预约申请已提交成功，张三医生将为你服务"))
                            .setFirst(new Keyword().setColor("#85be53").setValue("这是一条测试的医院通知，请忽略！"))
                            .setRemark(new Keyword().setColor("#85be53").setValue("南山人民医院")));

            AlipayOpenPublicMessageSingleSendResponse response = Factory.Marketing.OpenLife().sendSingleMessage(toUserId, template);

            System.out.println(response.getHttpBody());
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2"; //新版sdk只支持RSA2

        config.appId = appid;

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = privateKey2;

        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
//        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
//        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
//        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";

        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = alipayPublicKey2;

        //可设置异步通知接收服务地址（可选）
//        config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";

        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
//        config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

        return config;
    }




    public static void main(String[] args) throws Exception {
//        testSendAlipayMsgOld();
//        testSendAlipayMsg();

        testSendAlipayMsgOld1();
    }
}
