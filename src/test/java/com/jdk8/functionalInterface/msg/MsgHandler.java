package com.jdk8.functionalInterface.msg;
/**
 * @Description MsgHandler
 * @author fangxilin
 * @date 2020-11-04
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */

@FunctionalInterface
public interface MsgHandler {

    Boolean send(String content, String account);
}
