package com.jdk8.annotation;
/**
 * @Description RepeatingAnnotations
 * @author fangxilin
 * @date 2020-10-10
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */


import java.lang.annotation.*;

/**
 * 重复型注解
 * java8之前，同一个注解不能在同一个位置多次声明。Java 8打破了这个限制，引入了重复型注解(repeating annotations)，它支持在注解声明的位置，可以多次声明相同的注解
 */
public class RepeatingAnnotations {
    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    public @interface Filters {
        Filter[] value();
    }

    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    /**
     * @Repeatable 申明成可重复型注解
     */
    @Repeatable( Filters.class )
    public @interface Filter {
        String value();
    };

    /**
     * 使用可重复型注解
     */
    @Filter( "filter1" )
    @Filter( "filter2" )
    public interface Filterable {
    }

    public static void main(String[] args) {
        /**
         * getAnnotationsByType() java8新增的方法
         */
        for( Filter filter: Filterable.class.getAnnotationsByType( Filter.class ) ) {
            System.out.println( filter.value() );
        }
    }
}
