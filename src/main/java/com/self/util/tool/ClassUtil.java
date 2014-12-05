/**
 * leijid
 *
 * ClassUtil.java
 *
 * 2014Äê9ÔÂ17ÈÕ
 */
package com.self.util.tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author leijid
 * 
 */
public class ClassUtil {

	public static String getFieldMethod(String name) {
		return StringUtil.appendString("get", StringUtil.upperFirstChar(name));
	}

	public static String setFieldMethod(String name) {
		return StringUtil.appendString("set", StringUtil.upperFirstChar(name));
	}

	public static <T extends Object> Method getMethod(T t, String getFieldName)
	        throws SecurityException, NoSuchMethodException {
		return t.getClass().getMethod(getFieldName, new Class[] {});
	}

	public static <T extends Object> Object invokeMethod(Method method, T t)
	        throws IllegalArgumentException, IllegalAccessException,
	        InvocationTargetException {
		return method.invoke(t, new Object[] {});
	}

}
