package com.jdk8.annotation;
/**
 * @Description ExtendAnnotations
 * @author fangxilin
 * @date 2020-10-10
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Java 8扩展了注解的适用范围。从现在开始，绝大部分的东西都可以被注解：局部变量、泛型、超类、和接口实现，甚至可以是方法的异常声明
 */
public class ExtendAnnotations {

    @Retention( RetentionPolicy.RUNTIME )
    @Target( { ElementType.TYPE_USE, ElementType.TYPE_PARAMETER } )
    public @interface NonEmpty {
    }

    public static class Holder< @NonEmpty T > extends @NonEmpty Object {
        public void method() throws @NonEmpty Exception {
        }
    }

    @SuppressWarnings( "unused" )
    public static void main(String[] args) {
        final Holder< String > holder = new @NonEmpty Holder< String >();
        @NonEmpty Collection< @NonEmpty String > strings = new ArrayList<>();
    }
}
