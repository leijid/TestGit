//package com.self.util.sms;
//
//import org.smslib.AGateway;
//import org.smslib.IOutboundMessageNotification;
//import org.smslib.OutboundMessage;
//
//public class OutboundNotification implements IOutboundMessageNotification {
//
//	public void process(AGateway gateWay, OutboundMessage msg) {
//		System.out.println("Outbound handler called from Gateway: "
//		        + gateWay.getGatewayId());
//		System.out.println(msg);
//	}
//
//}