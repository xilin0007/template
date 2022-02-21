package com.jdk8.ifsDefaultStaticMethod;
/**
 * @Description Defaulable
 * @author fangxilin
 * @date 2020-10-10
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */

/**
 * 接口默认方法和静态方法
 */
public interface Defaulable {

    /**
     * 定义一个接口默认方法，所有的实现类都会默认继承得到这个方法（如果需要也可以重写这个默认实现）
     */
    default String notRequired() {
        return "Default implementation";
    }

    /**
     * 定义一个接口静态方法
     */
    static void run(){
        System.out.println();
    }
}
