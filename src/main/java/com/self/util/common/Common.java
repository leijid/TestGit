/**
 * leijid
 *
 * Common.java
 *
 * 2014年9月16日
 */
package com.self.util.common;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author leijid
 * 
 */
public class Common {

	private static final Logger logger = Logger.getLogger(Common.class);

	private static final String CONFIG_FILE_NAME = "config.properties";

	static {
		Properties pro = parseConfigFile();
		ENCODING = pro.getProperty("encoding");
	}

	public static Properties parseConfigFile() {
		Properties pro = new Properties();
		try {
			pro.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME));
		} catch (IOException e) {
			logger.error("解析config.properties文件失败: " + e);
			return null;
		}
		return pro;
	}

	public static final String ENCODING;

}
