package com.jdk8;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

/**
 * @Description jdk8 Collection新增的方法
 * @author fangxilin
 * @date 2020-09-04
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class CollectionTest {

    static ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
    static ArrayList<String> list1 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
    static ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));


    /**
     * 新增的forEach方法
     */
    public static void testForEach() {

        //jdk7 使用增强for循环迭代
        for (String s : list) {
            System.out.println("jdk7 使用增强for循环迭代：" + s);
        }

        //jdk8 使用forEach循环迭代
        list.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("jdk8 使用forEach循环迭代：" + s);
            }
        });

        //jdk8 使用forEach+Lambda表达式循环迭代
        list.forEach(s -> {
            System.out.println("jdk8 使用forEach+Lambda表达式循环迭代：" + s);
        });
    }

    /**
     * 新增的removeIf方法
     */
    public static void testRemoveIf() {
        //jdk7 使用迭代器删除列表元素
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().length() > 3) {
                //it.remove();
            }
        }

        //jdk8 使用removeIf+匿名内部内删除
        list.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.length() > 3;
            }
        });

        //jdk8 使用removeIf+Lambda删除
        list.removeIf(s -> s.length() > 3);

        System.out.println("移除后的list：" + list);
    }

    /**
     * 新增的 replaceAll 方法
     */
    public static void replaceAll() {
        //jdk7 使用下标实现元素替换-长度大于3的字符转换为大写
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (s.length() > 3) {
                list.set(i, s.toUpperCase());
            }
        }
        System.out.println("replaceAll 后的list：" + list);

        //jdk8 使用 replaceAll + 匿名内部类
        list1.replaceAll(new UnaryOperator<String>() {
            @Override
            public String apply(String s) {
                if (s.length() > 3) {
                    return s.toUpperCase();
                }
                return s;
            }
        });
        System.out.println("replaceAll 后的list：" + list);

        //jdk8 使用 replaceAll + Lambda
        list2.replaceAll(s -> {
            if (s.length() > 3) {
                return s.toUpperCase();
            }
            return s;
        });
        System.out.println("replaceAll 后的list：" + list);
    }

    /**
     * 新增的 sort 方法
     */
    public static void sort() {
        //jdk7 使用Collections工具类中的sort()方法
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        System.out.println("sort 后的list：" + list);

        //jdk8 使用 sort + Lambda
        list2.sort((o1, o2) -> o2.length() - o1.length());
        System.out.println("sort 后的list：" + list);
    }


    /**
     * 新增的 spliterator 方法
     *
     *
     */
    public static void spliterator() {

        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too", "how", "are", "you"));
        //数组也可以使用spliterator
        //Arrays.spliterator(arr, 1, 5);

        Spliterator<String> sp = list2.spliterator(); //得到一个可拆分的Spliterator迭代器
        Spliterator<String> sp1 = sp.trySplit();
        Spliterator<String> sp2 = sp.trySplit();

        /**
         * sp.tryAdvance(s -> {}); tryAdvance 尝试消费一个元素
         * forEachRemaining，循环消费迭代器元素
         * estimateSize 返回未被消费的元素数量
         * characteristics 返回这个Spliterator和他的原色的一个特征集。具体的可以查看源码来搭配特征。
         */

        System.out.println("消费前："  + sp1.estimateSize());
        //此处可以用多线程处理分解后的3个任务
        sp1.forEachRemaining(s -> {
            System.out.println("sp1:" + s);
        });
        System.out.println("消费后："  + sp1.estimateSize());


        sp2.forEachRemaining(s -> {
            System.out.println("sp2:" + s);
        });

        sp.forEachRemaining(s -> {
            System.out.println("sp:" + s);
        });

    }

    public static void main(String[] args) {
        //testForEach();
        //testRemoveIf();
        //replaceAll();
        //sort();
        spliterator();
    }
}
