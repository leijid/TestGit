//package com.self.util.sms;
//
//import org.smslib.Message.MessageEncodings;
//import org.smslib.OutboundMessage;
//import org.smslib.Service;
//import org.smslib.modem.SerialModemGateway;
//
//import com.self.util.tool.StringUtil;
//
///**
// * ���ŷ��Ͳ�����
// * 
// * @author leijid
// * 
// */
//public class SMSUtil {
//
//	public static void sendSMS(String mobilePhones, String content) {
//		SerialModemGateway gateway = new SerialModemGateway("modem.com1",
//		        "COM1", 115200, "wavecom", "9600");
//		gateway.setInbound(true);
//		gateway.setOutbound(true);
//		gateway.setSimPin("0000");
//		OutboundNotification outboundNotification = new OutboundNotification();
//		Service srv = Service.getInstance();
//		srv.setOutboundMessageNotification(outboundNotification);
//		OutboundMessage msg;
//		try {
//			srv.addGateway(gateway);
//			System.out.println("��ʼ���ɹ���׼����������");
//			srv.startService();
//			System.out.println("���������ɹ�");
//			String[] phones = StringUtil.splitString(mobilePhones, ",");
//			for (int i = 0; i < phones.length; i++) {
//				msg = new OutboundMessage(phones[i], content);
//				msg.setEncoding(MessageEncodings.ENCUCS2);
//				srv.sendMessage(msg);
//				System.out.println(phones[i] + " == " + content);
//			}
//			srv.stopService();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
//		SMSUtil.sendSMS("18651874535", "This is a test sms!");
//	}
//}