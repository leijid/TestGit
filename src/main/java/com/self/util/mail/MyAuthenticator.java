/**
 * leijid
 *
 * MyAuthenticator.java
 *
 * 2014Äê9ÔÂ28ÈÕ
 */
package com.self.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author leijid
 * 
 */
public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}