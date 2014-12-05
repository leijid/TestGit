/**
 * leijid
 *
 * CollectionUtil.java
 *
 * 2014Äê9ÔÂ19ÈÕ
 */
package com.self.util.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leijid
 * 
 */
public class CollectionUtil {

	public static <T extends Object> List<List<T>> splitList(List<T> list,
	        int splitSize) {
		List<List<T>> subLists = new ArrayList<List<T>>();
		int count = list.size() / splitSize + 1;
		for (int i = 0; i < count; i++) {
			int fromInex = i * splitSize;
			int toIndex = (i + 1) * splitSize;
			if (toIndex > list.size()) {
				toIndex = list.size();
			}
			List<T> subList = list.subList(fromInex, toIndex);
			subLists.add(subList);
		}
		return subLists;
	}

}
