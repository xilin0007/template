package com.jdk8;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @Description jdk8 Lambda表达式
 * @author fangxilin
 * @date 2020-09-04
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */

/**
 * Lambda写法可省略接口名、方法名、参数类型
 * 得益于javac的类型推断机制，编译器能够根据上下文信息推断出参数的类型，
 * 所以，响应的函数接口再内部中只能有一个抽象方法，否则无法推断出具体调哪个方法.
 * @FunctionalInterface是可选的，但加上该标注编译器会帮你检查接口是否符合函数接口规范。就像加入@Override标注会检查是否重载了函数一样
 *
 * Lambda和匿名内部内的区别：
 * 1、匿名内部类仍然是一个类，只是不需要程序员显示指定类名，编译器会自动为该类取名，编译之后将会产生两个class文件；
 * 而书写Lambda表达式不会产生新的类，编译后，Lambda表达式被封装成了主类的一个私有方法，并通过invokedynamic指令进行调用
 */
public class LambdaTest {

    public static void main(String[] args) {
        //testThread();
        //testCompare();

        //等效于new了个实例，一个实现Runnable接口中Run()方法的内部内实例
        Runnable run = () -> System.out.println("展示了无参函数的简写");
        Runnable multiRun = () -> {
            System.out.println("展示了无参函数代码块的简写");
        };

        //等效于new了个实例，一个实现ActionListener接口中actionPerformed(ActionEvent e)方法的内部内实例
        ActionListener listener = e -> System.out.println("展示了有参函数的简写");

        //展示了类型推断机制
        BinaryOperator<Integer> add = (Integer i, Integer i2) -> i + i2;
        BinaryOperator<Integer> addImplicit = (i, i2) -> i + i2;
        Integer apply = add.apply(1, 2);
        System.out.println("实现BinaryOperator继承至BiFunction的apply方法：" + apply);

        //自定义函数接口的使用
        ConsumerInterface<String> comsumer = s -> System.out.println(s);
    }


    public static void testThread() {
        //jdk7 匿名内部内写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("jdk7 执行线程");
            }
        }).start();

        //JDK8 Lambda表达式写法
        new Thread(() -> System.out.println("jdk8 执行线程")).start();
        new Thread(() -> {
            System.out.println("jdk8 执行线程多条语句1");
            System.out.println("jdk8 执行线程多条语句1");
        }).start();
    }

    public static void testCompare() {
        List<String> list = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        System.out.println("jdk7 Collections写法：" + list);

        Collections.sort(list, (s1, s2) -> {
            return s1.compareTo(s2);
        });
        System.out.println("jdk8 Collections写法：" + list);
    }

    /**
     * 自定义函数接口
     */
    @FunctionalInterface
    interface ConsumerInterface<T> {
        void accept(T t);
    }
}
