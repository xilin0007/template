package com.jdk8;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fxl.template.user.entity.UserInfo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description StreamTest
 * @author fangxilin
 * @date 2020-09-17
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class StreamTest {

    static Stream<String> stream = Stream.of("I", "love", "you", "too");

    static Stream<String> stream2 = Stream.of("I", "love", "you", "too");

    static Stream<String> stream3 = Stream.of("I", "love", "you", "too");

    static List<UserInfo> users = new ArrayList<>();

    static {
        UserInfo u1 = new UserInfo();
        u1.setNickName("用户1");
        u1.setStatus(0);
        UserInfo u2 = new UserInfo();
        u2.setNickName("用户2");
        u2.setStatus(0);
        UserInfo u3 = new UserInfo();
        u3.setNickName("用户3");
        u3.setStatus(1);
        UserInfo u4 = new UserInfo();
        u4.setNickName("用户4");
        u4.setStatus(2);
        users.add(u1);users.add(u2);users.add(u3);users.add(u4);

    }


    /**
     * forEach
     */
    static void forEach() {
        // 使用Stream.forEach()迭代
        stream.forEach(str -> System.out.println("forEach:" + str));
    }

    /**
     * filter 过滤出某条件的数据
     */
    static void filter() {
        stream.filter(s -> s.length() == 3)
                .forEach(s -> System.out.println("filter:" + s));
    }

    /**
     * distinct 去重
     */
    static void distinct() {
        Stream<String> stream = Stream.of("I", "love", "you", "too", "too");
        stream.distinct()
                .forEach(str -> System.out.println("distinct:" + str));
    }

    /**
     * sorted 排序
     */
    static void sorted() {
        stream.sorted((str1, str2) -> str2.compareTo(str1))
                .forEach(str -> System.out.println(str));
    }

    /**
     * map 对每个元素按照某种操作进行转换
     */
    static void map() {
        stream.map(s -> s.toUpperCase())
                .forEach(s -> System.out.println("mapper:" + s));
    }

    /**
     * flatMap
     * 把原stream中的所有元素都”摊平”之后组成的Stream，转换前后元素的个数和类型都可能会改变
     */
    static void flatMap() {
        Stream<List<Integer>> stream = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
        //stream.forEach(s -> System.out.println("forEach:" + s)); //输出 [1, 2]和[3, 4, 5]
        stream.flatMap(list -> list.stream()) //摊平成 [1, 2, 3, 4, 5]
                .forEach(i -> System.out.println("flatMap:" + i));
    }

    /**
     * reduce
     * reduce操作可以实现从一组元素中生成一个值，sum()、max()、min()、count()等都是reduce操作，将他们单独设为函数只是因为常用。
     */
    static void reduce() {
        //求出最长的元素
        Optional<String> reduce = stream.reduce((s, s2) -> s.length() >= s2.length() ? s : s2);
        System.out.println("reduce:" + reduce.get());
        //求出最大的元素
        Optional<String> max = stream2.max(String::compareTo);
        System.out.println("max:" + max.get());
        //求所有元素长度之和
        Integer lengthSum = stream3.reduce(0, //初始值
                (sum, s) -> {
                    System.out.println("sum = " + sum);
                    System.out.println("s = " + s);
                    return sum + s.length();
                }, //累加器
                (integer, integer2) -> {
                    System.out.println("integer = " + integer);
                    System.out.println("integer2 = " + integer2);
                    return integer + integer2;
                });//部分和拼接器，并行执行时才会用到
//        int sum = stream3.mapToInt(value -> value.length()).sum();
        System.out.println("lengthSum:" + lengthSum);

    }


    static void collect() {
        List<String> list = stream.collect(Collectors.toList());
        //效果相等
        //List<String> list = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        //使用collect()生成Collection，指定容器实际类型为 ArrayList
        //ArrayList<String> list = stream.collect(Collectors.toCollection(ArrayList::new));
        System.out.println("list = " + list);

        Set<String> set = stream2.collect(Collectors.toSet());
        //使用collect()生成Collection，指定容器实际类型为 HashSet
        //HashSet<String> set = stream.collect(Collectors.toCollection(HashSet::new));
        System.out.println("set = " + set);

        /**
         * Function中的有default方法和static方法，identity()就是Function接口的一个静态方法
         * Function.identity() 返回一个输出跟输入一样的Lambda表达式对象，等价于形如t -> t形式的Lambda表达式
         * 其中Function的default方法实现是为了解决jdk8新加入方法时，所有实现该结果的类都得新实现该方法
         */
        /**
         * 方法引用
         * 引用静态方法	Integer::sum
         * 引用某个对象的方法	list::add
         * 引用某个类的方法	String::length
         * 引用构造方法	HashMap::new
         */
        Map<String, Integer> map = stream3.collect(Collectors.toMap(Function.identity(), String::length));
        System.out.println("map = " + map);
    }

    /**
     * collect toMap 方法
     */
    static void toMap() {
        //toMap，将姓名作为key，UserInfo对象作为value
        Map<String, UserInfo> map = users.stream().collect(Collectors.toMap(UserInfo::getNickName, Function.identity()));
        //Map<UserInfo, String> map = users.stream().collect(Collectors.toMap(Function.identity(), UserInfo::getNickName));
        System.out.println("map = " + map);

        //任何实现了Stream接口的第三方类都可以使用stream collect
//        JSONArray arr = new JSONArray();
//        Map<Integer, Object> map1 = arr.stream().collect(Collectors.toMap(o -> ((JSONObject) o).getInteger("nickName"), Function.identity()));
//
    }

    /**
     * collect partitioningBy 方法
     * 适用于将Stream中的元素依据某个二值逻辑（满足条件，或不满足）分成互补相交的两部分，比如男女性别、成绩及格与否等
     */
    static void partitioningBy() {
        //partitioningBy，将status是否等于0作为key，UserInfo对象列表作为value
        Map<Boolean, List<UserInfo>> map = users.stream().collect(Collectors.partitioningBy(o -> o.getStatus() == 0));
        System.out.println("map = " + map);

        //任何实现了Stream接口的第三方类都可以使用stream collect
//        JSONArray arr = new JSONArray();
//        Map<Boolean, List<Object>> map1 = arr.stream().collect(Collectors.partitioningBy(o -> ((JSONObject) o).getInteger("status") == 0));
    }

    /**
     * groupingBy
     * 分组，类似于sql中的group by 属性相同的元素会被对应到Map的同一个key上
     */
    static void groupingBy() {
        //groupingBy，将status作为key进行分组，UserInfo对象列表作为value
        //Map<Integer, List<UserInfo>> map = users.stream().collect(Collectors.groupingBy(UserInfo::getStatus));

        //groupingBy 新加下游收集器counting，将status作为key进行分组，数量size统计作为value
//        Map<Integer, Long> map = users.stream().collect(Collectors.groupingBy(UserInfo::getStatus, // 上游收集器
//                Collectors.counting())); //下游收集器

        //groupingBy 新加下游收集器mapping，将status作为key进行分组，指定某字段的list集合作为value
        Map<Integer, List<String>> map = users.stream().collect(Collectors.groupingBy(UserInfo::getStatus,
                Collectors.mapping(UserInfo::getNickName, // 下游收集器 mapping 设置映射关系
                        Collectors.toList()))); // 更下游的收集器

        System.out.println("map = " + map);
    }

    /**
     * join 字符串拼接
     */
    static void join() {
        String joined = stream.collect(Collectors.joining()); //Iloveyoutoo
        System.out.println("直接拼接joined = " + joined);

        String joined2 = stream2.collect(Collectors.joining(",")); //I,love,you,too
        System.out.println("指定分隔符拼接joined2 = " + joined2);

        String joined3 = stream3.collect(Collectors.joining(",", "{", "}")); //{I,love,you,too}
        System.out.println("指定分隔符及前后缀joined3 = " + joined3);
    }

    public static void main(String[] args) {
        //forEach();
        //filter();
        //distinct();
        //sorted();
        //map();
        //flatMap();
        reduce();
        //collect();
        //toMap();
        //partitioningBy();
//        groupingBy();
//        join();

    }


}
