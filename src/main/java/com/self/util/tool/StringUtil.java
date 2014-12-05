/**
 * leijid
 *
 * StringUtil.java
 *
 * 2014Äê9ÔÂ12ÈÕ
 */
package com.self.util.tool;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.self.util.common.Common;

/**
 * @author leijid
 * 
 */
public class StringUtil {

	private static final Logger logger = Logger.getLogger(StringUtil.class);

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static <T extends Object> boolean isEmpty(Collection<T> list) {
		return list == null || list.isEmpty();
	}

	public static <T extends Object> boolean isNotEmpty(Collection<T> list) {
		return !isEmpty(list);
	}

	public static <K extends Object, V extends Object> boolean isEmpty(
	        Map<K, V> map) {
		return map == null || map.isEmpty();
	}

	public static <K extends Object, V extends Object> boolean isNotEmpty(
	        Map<K, V> map) {
		return !isEmpty(map);
	}

	public static String trim(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.trim();
	}

	public static String upperString(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.toUpperCase();
	}

	public static String lowerString(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.toLowerCase();
	}

	public static String upperFirstChar(String str) {
		if (isEmpty(str)) {
			return str;
		}
		str = lowerString(str);
		str = str
		        .replace(str.substring(0, 1), upperString(str.substring(0, 1)));
		return str;
	}

	public static String lowerFirstChar(String str) {
		if (isEmpty(str)) {
			return str;
		}
		str = upperString(str);
		str = str
		        .replace(str.substring(0, 1), lowerString(str.substring(0, 1)));
		return str;
	}

	public static String byteToString(byte[] bytes) {
		String str = null;
		try {
			str = new String(bytes, Common.ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return str;
	}

	public static byte[] stringToByte(String str) {
		if (isEmpty(str)) {
			return null;
		}
		byte[] bytes = null;
		try {
			bytes = str.getBytes(Common.ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return bytes;
	}

	public static String[] splitString(String str, String delim) {
		if (isEmpty(str)) {
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(str, delim);
		String[] strs = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreElements()) {
			strs[i] = tokenizer.nextToken();
			i++;
		}
		return strs;
	}

	public static String appendString(String str1, String str2) {
		StringBuffer sb = new StringBuffer();
		sb.append(str1).append(str2);
		return sb.toString();
	}

	public static String convertToUTF8(String str, String encoding) {
		if (isEmpty(str)) {
			return str;
		}
		return convertEncoding(str, encoding, Common.ENCODING);
	}

	public static String convertFromUTF8(String str, String encoding) {
		if (isEmpty(str)) {
			return str;
		}
		return convertEncoding(str, Common.ENCODING, encoding);
	}

	private static String convertEncoding(String str, String encoding1,
	        String encoding2) {
		String outStr = null;
		try {
			outStr = new String(str.getBytes(encoding1), encoding2);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return outStr;
	}

}
