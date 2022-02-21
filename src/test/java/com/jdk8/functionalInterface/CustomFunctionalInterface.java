package com.jdk8.functionalInterface;
/**
 * @Description 函数式接口
 * @author fangxilin
 * @date 2020-09-29
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */

/**
 * 函数式接口，要求接口中有且仅有一个抽象方法，因此经常使用的Runnable，Callable接口就是典型的函数式接口,
 * 如果一个接口满足函数式接口的定义，会默认转换成函数式接口。但是，最好是使用@FunctionalInterface注解显式声明,
 * 因为如果开发人员无意间新增了其他方法，就破坏了函数式接口的要求，代码中用到的地方就会编译出错
 *
 * java.util.function包下有许多常用的函数式接口，比如java.util.function.BiConsumer 可参考编辑自定义的接口
 */
@FunctionalInterface
public interface CustomFunctionalInterface<T> {

    void print(T t);

    /**
     * 如果再定义一个非default方法，就会编译报错
     */
    //void handle1();

    /**
     * 函数是接口，只能定义一个抽象方法，但是可以允许多个default方法
     */
    default void run() {
        System.out.println("run");
    }
}
