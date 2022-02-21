package com.jdk8.spliterator;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class NumCounterTest {
    public static void main(String[] args) {
        //String arr = "12%3 21sdas s34d dfsdz45   R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd"; //size 65
        String arr = "12%3 21sdas"; //size 65
        Stream<Character> stream = IntStream.range(0, arr.length()).mapToObj(arr::charAt).sequential(); //.sequential()可省略，默认是串行流
        //串行，单线程计算
        System.out.println("ordered total: " + countNum(stream) + "\n\n\n\n");


        Spliterator<Character> spliterator = new NumCounterSpliterator(0, arr.length() - 1, arr.toCharArray(), true);
        // 传入true表示是并行流
        Stream<Character> parallelStream = StreamSupport.stream(spliterator, true);
//        Stream<Character> parallelStream = IntStream.range(0, arr.length()).mapToObj(arr::charAt).parallel(); //.sequential()可省略，默认是串行流
        //并行，多线程计算
        System.out.println("parallel total: " + countNum(parallelStream));
    }

    private static int countNum(Stream<Character> stream) {
        NumCounter numCounter = stream.reduce(new NumCounter(0, 0, false), NumCounter::accumulate, NumCounter::combine);
        return numCounter.getSum();
    }
}
