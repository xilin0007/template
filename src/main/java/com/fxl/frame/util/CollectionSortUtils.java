package com.fxl.frame.util;

import java.util.*;

/**
 * @Description CollectionSortUtils
 * @author fangxilin
 * @date 2021-12-02
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2021
 */
public class CollectionSortUtils {

	/**
	 * 通过List排序
	 * @param data
	 * @return
	 */
	public static List<Map.Entry<String, Object>> sort(Map<String, Object> data) {
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(data.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
			@Override
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				//升序，前边加负号变为降序
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		return list;
	}

	/**
	 * 通过TreeMap排序
	 * @param data
	 * @return
	 */
	public static TreeMap<String, Object> sortByTreeMap(Map<String, Object> data) {
		TreeMap<String, Object> sortMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				//升序，前边加负号变为降序
				return o1.compareTo(o2);
			}
		});
		sortMap.putAll(data);
		return sortMap;
	}
}
