/**
 * leijid
 *
 * SimpleMailSender.java
 *
 * 2014��9��28��
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
	 * ���ı���ʽ�����ʼ�
	 * 
	 * @param mailInfo
	 *            �����͵��ʼ�����Ϣ
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		// �ж��Ƿ���Ҫ�����֤
		Properties pro = mailInfo.getProperties();
		MyAuthenticator authenticator = isNeadAuth(mailInfo);
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session
		Session sendMailSession = Session
		        .getDefaultInstance(pro, authenticator);
		try {
			// ����session����һ���ʼ���Ϣ
			Message mailMessage = new MimeMessage(sendMailSession);
			// �����ʼ������ߵ�ַ
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// �����ʼ���Ϣ�ķ�����
			mailMessage.setFrom(from);
			setAliasName(mailInfo, mailMessage);
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// �����ʼ���Ϣ������
			mailMessage.setSubject(mailInfo.getSubject());
			// �����ʼ���Ϣ���͵�ʱ��
			mailMessage.setSentDate(new Date());
			// �����ʼ���Ϣ����Ҫ����
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// �����ʼ�
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
	 * ��HTML��ʽ�����ʼ�
	 * 
	 * @param mailInfo
	 *            �����͵��ʼ���Ϣ
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// �ж��Ƿ���Ҫ�����֤
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		authenticator = isNeadAuth(mailInfo);
		// �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session
		Session sendMailSession = Session
		        .getDefaultInstance(pro, authenticator);
		try {
			// ����session����һ���ʼ���Ϣ
			Message mailMessage = new MimeMessage(sendMailSession);
			// �����ʼ������ߵ�ַ
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// �����ʼ���Ϣ�ķ�����
			mailMessage.setFrom(from);
			setAliasName(mailInfo, mailMessage);
			// �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO���Ա�ʾ�����ߵ�����ΪTO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// �����ʼ���Ϣ������
			mailMessage.setSubject(mailInfo.getSubject());
			// �����ʼ���Ϣ���͵�ʱ��
			mailMessage.setSentDate(new Date());
			// MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���
			Multipart mainPart = new MimeMultipart();
			// ����һ������HTML���ݵ�MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// ����HTML����
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// ��MiniMultipart��������Ϊ�ʼ�����
			mailMessage.setContent(mainPart);
			// �����ʼ�
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
			// �����Ҫ�����֤���򴴽�һ��������֤��
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
			        mailInfo.getPassword());
		}
		return authenticator;
	}

	public static void main(String[] args) {
		MailSenderInfo info = new MailSenderInfo();
		info.setAliasName("����");
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