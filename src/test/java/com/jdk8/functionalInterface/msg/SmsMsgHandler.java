package com.jdk8.functionalInterface.msg;
/**
 * @Description SmsMsgHandler
 * @author fangxilin
 * @date 2020-11-04
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class SmsMsgHandler implements MsgHandler {

    @Override
    public Boolean send(String content, String account) {
        System.out.println("处理要发送的消息，content：" + content + "，account：" + account);
        return true;
    }
}
