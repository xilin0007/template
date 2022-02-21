package com.fxl.frame.util.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fxl.frame.util.encrypt.Sha1Util;
import com.fxl.frame.util.http.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 秒信发送短信工具类
 *
 * 账号：SZNY2
 * 密码：NY123456
 *
 * 深圳宁远-三网-行业2
 * 连接协议：HTTP
 * 发送账号：10012s
 * 发送密码：heav29f8y4
 * 提交速度：无限制
 * 发送速度：500/秒
 * 接受客户IP：无限制
 * 在线文档：http://docs.51miaoxin.com/
 *
 * @author fangxilin
 * @date 2020-10-21
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
@Slf4j
public class MiaoxinSmsUtil {

    /**
     * 账号
     */
    public static final String ACCOUNT = "10012s";
    /**
     * 密码
     */
    public static final String SECRET = "heav29f8y4";
    /**
     * 秒信短信发送地址
     */
//    private static final String SEND_URL = "http://www.51miaoxin.com/sms/send";
    //将地址设置错误，模拟重试机制
    private static final String SEND_URL = "http://www.51miaoxin.com/sms/sends";
    /**
     * 秒信短信状态报告及上行主动拉取HTTP接口
     */
    private static final String UPWARD_URL = "http://www.51miaoxin.com/sms/pull";


    /**
     * 发送短信
     * @date 2020/10/22 16:21
     * @auther fangxilin
     * @param msgText
     * @param mobile
     * @param signContent
     * @return java.lang.String
     */
    public static String send(String msgText, String mobile, String signContent) {
        String orderId = "";
        String res;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("account", ACCOUNT);
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            params.put("ts", ts);
            String token = Sha1Util.getSha1("account=" + ACCOUNT + "&ts=" + ts + "&secret=" + SECRET);
            params.put("token", token);
            params.put("mobiles", mobile);
            //signContent 短信头部签名，必须要的
            signContent = "【" + signContent + "】";
            params.put("content", signContent + URLEncoder.encode(msgText, "UTF-8"));
            res = HttpClientUtil.sendHttpPost(SEND_URL, params);
            /**
             * 成功：{"code":0,"msg":"提交成功","total":1,"result":[{"order_id":"2010281650498865736","mobile":"18320995792","code":0,"msg":"处理中","receive_time":"2020-10-28 16:50:49"}]}
             * 失败：{"code":0,"msg":"提交成功","total":1,"result":[{"order_id":-9202,"mobile":"183209957921","code":-9202,"msg":"暂只支持国内手机","receive_time":"2020-10-28 17:09:27"}]}
             */
            System.out.println("入参：" + params + ", 出参：" + res + ", 请求地址：" + SEND_URL);
            //解析结果
//            log.info("秒信发送短信，入参：{}，出参：{}，请求地址：{}", params, res, SEND_URL);
            if (StringUtils.isEmpty(res)) {
//                log.info("秒信发送短信失败，获取响应为空，入参：{}", params);
                return res;
            }
            //解析结果
            com.alibaba.fastjson.JSONObject resJson = JSON.parseObject(res);
            if (resJson.getInteger("code") != 0) {
//                log.info("秒信发送短信失败，code_1未返回0成功状态，入参：{}，出参：{}", params, res);
                return res;
            }
            JSONObject resJsonResult = resJson.getJSONArray("result").getJSONObject(0);
            if (resJsonResult.getInteger("code") != 0) {
//                log.info("秒信发送短信失败，code_2未返回0成功状态，入参：{}，出参：{}", params, res);
                return res;
            }
            //秒信返回成功
            String thirdId = resJson.getJSONArray("result").getJSONObject(0).getString("order_id");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }


    /**
     * 秒信-短信状态报告及上行主动拉取HTTP接口
     * 客户主动拉取，每次返回最多100条
     * 建议客户3秒拉取一次，如果返回100条可以不停立刻继续
     * 数据保留一星期，一个星期不拉取，自动删除
     *
     * 出参格式：
     * {"code":0,"msg":"拉取状态成功","total":0,"total_mt_report":0,"total_mo_report":0,"reports":[],"mos":[]}
     * {"code":0,"msg":"拉取状态成功","total":1,"total_mt_report":1,"total_mo_report":0,"reports":[{"order_id":"2010221625238759811","mobile":"18320995792","status":1,"msg":"DELIVRD","received_time":"2020-10-22 16:25:22","done_time":"2020-10-22 16:25:23","pieces":1,"ext":"10692313","ref":""}],"mos":[]}
     * @date 2020/10/22 16:20
     * @auther fangxilin
     * @return java.lang.String
     */
    public static String getSmsUpwarData() {
        String res = "";
        Map<String, Object> params = new HashMap<>();
        params.put("account", ACCOUNT);
        String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        params.put("ts", ts);
        String token = Sha1Util.getSha1("account=" + ACCOUNT + "&ts=" + ts + "&secret=" + SECRET);
        params.put("token", token);
        try {
            res = HttpClientUtil.sendHttpPost(UPWARD_URL, params);
            System.out.println("入参：" + params + ", 出参：" + res + ", 请求地址：" + UPWARD_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        String signContent = "宁远科技三网行业";
//        String signContent = "深圳市人民医院";
        String result = send("发送一条测试短信，请忽略！请忽略！请忽略！", "18320995792", signContent);

//        getSmsUpwarData();
    }
}
