package com.jdk8;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description 并行数组
 * @author fangxilin
 * @date 2020-10-12
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class ParallelArrayTest {

    public static void main(String[] args) {
        long[] arr = new long[1000];
        Arrays.parallelSetAll(arr, value -> ThreadLocalRandom.current().nextInt(1000000));
        Arrays.stream(arr).limit(10).forEach(value -> System.out.println(value + " "));

        System.out.println("\n" + "排序后：");

        //parallelSort 默认正序排，可通过parallelSort(T[] a, Comparator<? super T> cmp)方法自定义排序
        Arrays.parallelSort(arr);
        Arrays.stream(arr).limit(10).forEach(value -> System.out.println(value + " "));
    }
}
