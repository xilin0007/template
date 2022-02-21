package com.jdk8.functionalInterface;

import com.jdk8.functionalInterface.msg.MsgHandler;
import com.jdk8.functionalInterface.msg.SmsMsgHandler;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Description FunctionalInterfaceTest
 * @author fangxilin
 * @date 2020/9/29
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020/9/29
 */
public class FunctionalInterfaceTest {

    static void testHandle(CustomFunctionalInterface custom, String s) {
        custom.print(s);
    }

    static void testSend(MsgHandler handler, String content, String account) {
        handler.send(content, account);
    }

    public static void main(String[] args) {
        //使用函数式接口
//        testHandle(s -> {
//            System.out.println(s);
//        }, "aaaa");


        MsgHandler handler = new SmsMsgHandler();
        testSend(handler::send, "hello!", "18320995792");
        //使用场景，可将当前私有方法定义成一个变量function，传给外部类，这样外部类就可以调用到其他类的私有方法了
        BiFunction<String, String, Boolean> function = handler::send;
        Boolean flag = function.apply("hello!", "18320995792");


    }
}
