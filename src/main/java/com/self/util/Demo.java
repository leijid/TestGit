/**
 * leijid
 *
 * Demo.java
 *
 * 2014Äê10ÔÂ8ÈÕ
 */
package com.self.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author leijid
 * 
 */
public class Demo {

	public static void main(String[] args) throws Exception {
		new dn();
	}
}

class an {
	static {
		System.out.println(1);
	}

	public an() {
		System.out.println(2);
	}
}

class dn extends an {
	static {
		System.out.println(3);
	}

	public dn() {
		System.out.println(4);
	}
}
