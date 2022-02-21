package com.jdk8;

import java.util.Optional;

/**
 * @Description OptionalTest
 * @author fangxilin
 * @date 2020-10-11
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class OptionalTest {

    public static void main(String[] args) {
        String str = null;
        //ofNullable 可接收空值
        Optional<String> ofNullableValue = Optional.ofNullable(str);
        //isPresent 判断是否空值
        System.out.println("ofNullableValue.isPresent() = " + ofNullableValue.isPresent());

        //orElseGet 接受一个能够产生默认值的方法，为空时赋默认值，从而防止Optional值为空
        String orElseGetStr = ofNullableValue.orElseGet(() -> "[none]");
        System.out.println("orElseGetStr = " + orElseGetStr);

        //map 转换Optional并返回一个新的Optional，如果原Optional的值为空，会报错： NoSuchElementException: No value present
        //orElse 接收一个默认值，为空时赋默认值
        String orElseStr = ofNullableValue.map(s -> "hello " + s + "!").orElse("当前值为空！");
//        System.out.println("mapOptional.get() = " + mapOptional.get());
        System.out.println("orElseStr = " + orElseStr);


        System.out.println("\n\n");
        String str1 = null;
//        String str1 = "China🇨🇳";
        //of 不可接收空值，接收到空值时报错：java.lang.NullPointerException
        Optional<Object> ofValue = Optional.of(str1);
        System.out.println("ofValue.isPresent() = " + ofValue.isPresent());
        System.out.println("ofValue.orElseGet() = " + ofValue.orElseGet(() -> "[none]"));
        //flatMap 与map方法类似
        Optional<String> flatMapOptional = ofValue.flatMap(s -> s == null ? Optional.of("empty value") : Optional.of("output for " + s));
        System.out.println("flatMapOptional.get() = " + flatMapOptional.get());


    }

}
