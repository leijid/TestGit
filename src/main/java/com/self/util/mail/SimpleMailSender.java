/**
 * leijid
 *
 * SimpleMailSender.java
 *
 * 2014年9月28日
 */
package com.self.util.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.self.util.tool.StringUtil;

/**
 * @author leijid
 * 
 */
public class SimpleMailSender {
	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		Properties pro = mailInfo.getProperties();
		MyAuthenticator authenticator = isNeadAuth(mailInfo);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
		        .getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			setAliasName(mailInfo, mailMessage);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		authenticator = isNeadAuth(mailInfo);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
		        .getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			setAliasName(mailInfo, mailMessage);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void setAliasName(MailSenderInfo mailInfo,
	        Message mailMessage) throws MessagingException, AddressException,
	        UnsupportedEncodingException {
		if (StringUtil.isNotEmpty(mailInfo.getAliasName())) {
			StringBuffer fromName = new StringBuffer();
			fromName.append(mailInfo.getAliasName()).append("<")
			        .append(mailInfo.getFromAddress()).append(">");
			mailMessage.setFrom(new InternetAddress(MimeUtility
			        .encodeText(fromName.toString())));
		}
	}

	private static MyAuthenticator isNeadAuth(MailSenderInfo mailInfo) {
		MyAuthenticator authenticator = null;
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
			        mailInfo.getPassword());
		}
		return authenticator;
	}

	public static void main(String[] args) {
		MailSenderInfo info = new MailSenderInfo();
		info.setAliasName("嵇磊");
		info.setContent("This is a test mail!");
		info.setFromAddress("jsjilei1986@163.com");
		info.setMailServerHost("smtp.163.com");
		info.setMailServerPort("25");
		info.setUserName("jsjilei1986@163.com");
		info.setPassword("");
		info.setSubject("test");
		info.setToAddress("370303352@qq.com");
		info.setValidate(true);
		SimpleMailSender.sendTextMail(info);
	}
}