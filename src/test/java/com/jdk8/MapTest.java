package com.jdk8;

import java.security.Key;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description MapTest
 * @author fangxilin
 * @date 2020-09-12
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */
public class MapTest {

    static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
    }

    /**
     * forEach 遍历
     */
    static void forEach() {
        // Java7以及之前迭代Map
        for(Map.Entry<Integer, String> entry : map.entrySet()){
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        //jdk8使用 forEach 迭代
        map.forEach((key, value) -> {
            System.out.println(key + "=" + value);
        });
    }

    /**
     * getOrDefault 查询Map中指定的值，不存在时使用默认值
     */
    static void getOrDefault() {
        //jdk7做法
        if (map.containsKey(4)) { // 1
            System.out.println(map.get(4));
        } else {
            System.out.println("four");
        }

        //jdk8使用 getOrDefault
        System.out.println(map.getOrDefault(4, "four"));
    }

    /**
     * putIfAbsent
     * 不存在key值的映射或映射值为null时，才将value指定的值放入到Map中，否则不对Map做更改．该方法将条件判断和赋值合二为一，使用起来更加方便
     */
    static void putIfAbsent() {
        map.putIfAbsent(4, "four");
        map.putIfAbsent(3, "four"); //不会生效，因为已经存在key为3的映射
        System.out.println("putIfAbsent后的Map:" + map.toString());
    }

    /**
     * remove(Object key, Object value)方法，
     * 只有在当前Map中key正好映射到value时才删除该映射，否则什么也不做
     */
    static void remove() {
        map.remove(1, "one");
        map.remove(2, "four"); //不会生效，因为key为2的值并不等于four
        System.out.println("remove:" + map.toString());
    }

    /**
     * replace(K key, V value)，只有在当前Map中key的映射存在时才用value去替换原来的值，否则什么也不做．
     * replace(K key, V oldValue, V newValue)，只有在当前Map中key的映射存在且等于oldValue时才用newValue去替换原来的值，否则什么也不做
     */
    static void replace() {
        map.replace(1, "replace one");
        map.replace(4, "replace four"); //不会执行，因为不存在key为4的映射
        map.replace(2, "two", "replace tow");
        map.replace(3, "three threee", "replace tow"); //不会执行，因为oldValue值与three不等
        System.out.println("replace:" + map.toString());
    }

    /**
     * replaceAll
     */
    static void replaceAll() {
        //map.replaceAll((k, v) -> {return v.toUpperCase();});
        map.replaceAll((k, v) -> v.toUpperCase());
        System.out.println("replaceAll:" + map.toString());
    }

    /**
     * merge
     * 如果Map中key对应的映射不存在或者为null，则将value（不能是null）关联到key;
     * 否则执行remappingFunction，如果执行结果非null则用该结果跟key关联，否则在Map中删除key的映射
     */
    static void merge() {
        map.merge(4, "merge four", (s, s2) -> {
            return s + s2;
        }); //不存在值，直接merge，不执行lambda
        map.merge(3, "merge three", (s, s2) -> {
            System.out.println("执行lambda：");
            System.out.println("原始值：" + s);
            System.out.println("需merge的值：" + s2);
            String ret = s + " " + s2;
            System.out.println("最终merge的值：" + ret);
            return ret;
        });//存在值，执行lambda

        System.out.println("merge:" + map.toString());
    }

    /**
     * computer
     * 把remappingFunction的计算结果关联到key上，如果计算结果为null，则在Map中删除key的映射．
     */
    static void computer() {
        //将lambda的计算结果映射给key：4
        map.compute(4, (k, v) -> v == null ? "four" : v.concat("computer"));
        //计算结果为null，删除key为3的映射
        map.compute(3, (k, v) -> null);

        System.out.println("computer:" + map.toString());
    }

    /**
     * computeIfAbsent
     * 在当前Map中不存在key值的映射或映射值为null时，才调用lambda，并在lambda执行结果非null时，将结果跟key关联
     */
    static void computeIfAbsent() {
        Map<Integer, Set<String>> map = new HashMap<>();
        // Java7及以前的实现方式
        if (map.containsKey(1)) {
            map.get(1).add("one");
        } else {
            Set<String> valueSet = new HashSet<String>();
            valueSet.add("one");
            map.put(1, valueSet);
        }
        // Java8的实现方式
        map.computeIfAbsent(2, v -> new HashSet<String>()).add("two-1");
        map.computeIfAbsent(2, v -> new HashSet<String>()).add("two-2");//two-2也会添加进HashSet中

        System.out.println("computeIfAbsent:" + map.toString());
    }

    /**
     * computeIfPresent
     * 作用跟computeIfAbsent()相反，即，只有在当前Map中存在key值的映射且非null时，才调用lambda，如果lambda执行结果为null，则删除key的映射，否则使用该结果替换key原来的映射
     */
    static void computeIfPresent() {

        map.compute(1, (k, v) -> {
            System.out.println("k:" + k);
            System.out.println("v:" + v);
            return "computeIfPresent " + v;
        });
        System.out.println("computeIfPresent：" + map);
    }

    public static void main(String[] args) {
        //forEach();
        //getOrDefault();
        //putIfAbsent();
        //remove();
        //replace();
        //merge();
        //computer();
        //computeIfAbsent();
        computeIfPresent();
    }
}
