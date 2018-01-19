package gov.csc.ems.gsm.service.impl;

import gov.csc.ems.gsm.sendservice.ModemService;
import gov.csc.ems.gsm.sendservice.impl.OutboundNotification;
import gov.csc.ems.gsm.service.ISmsService;
import gov.csc.ems.util.DateUtil;
import gov.csc.ems.util.IConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.sendsms.GatewayException;
import cn.sendsms.InboundMessage;
import cn.sendsms.OutboundMessage;
import cn.sendsms.SendSMSException;
import cn.sendsms.Service;
import cn.sendsms.TimeoutException;
import cn.sendsms.InboundMessage.MessageClasses;

/**
 * @author liuy 2013-7-15
 */
public class SmsServiceImpl implements ISmsService {
	private static Logger loger = Logger.getLogger(SmsServiceImpl.class);

	private OutboundNotification on = null;

	public String Sms_Sendbatch_web(String telNum, String smsText) {

		Service serv = ModemService.getService();

		String result = "";

		on = new OutboundNotification();

		serv.setOutboundMessageNotification(on);

		List<OutboundMessage> outboundMessageList = new ArrayList<OutboundMessage>();

		if (StringUtils.isNotEmpty(telNum)) {
			String[] telArray = telNum.split(",");
			int len = telArray.length;

			for (int i = 0; i < len; i++) {
				String tel = telArray[i];
				if (StringUtils.isEmpty(tel)) {
					continue;
				}
				OutboundMessage msg = new OutboundMessage(tel, smsText);
				msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
				msg.setStatusReport(true);
				outboundMessageList.add(msg);

			}

			try {
				// 利用并发阻塞队列机制发送短信
				serv.queueMessages(outboundMessageList);
				// 所有短信发送完成时，才继续往下执行
				while (true) {
					if (len == on.getTelCount().intValue())
						break;
				}
				// 存入发送成功的手机号
				if (StringUtils.isNotBlank(on.getSuccessNum()))
					result += new String(on.getSuccessNum().substring(0,
							on.getSuccessNum().length() - 1));
				// 存入发送失败的手机号
				if (StringUtils.isNotBlank(on.getFailNum()))
					result += ";"
							+ new String(on.getFailNum().substring(0,
									on.getFailNum().length() - 1));
			} catch (Exception e) {
				e.printStackTrace();
				return IConstants.FAILED;
			}

		}
		return result;
	}

	public String Sms_Sendbatch_web_NoResult(String telNum, String smsText) {

		Service serv = ModemService.getService();

		String result = "";

		on = new OutboundNotification();

		serv.setOutboundMessageNotification(on);

		List<OutboundMessage> outboundMessageList = new ArrayList<OutboundMessage>();

		if (StringUtils.isNotEmpty(telNum)) {
			String[] telArray = telNum.split(",");
			int len = telArray.length;

			for (int i = 0; i < len; i++) {
				String tel = telArray[i];
				if (StringUtils.isEmpty(tel)) {
					continue;
				}
				OutboundMessage msg = new OutboundMessage(tel, smsText);
				msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
				msg.setStatusReport(true);
				outboundMessageList.add(msg);

			}
			try {
				// 利用并发阻塞队列机制发送短信
				serv.queueMessages(outboundMessageList);
			} catch (Exception e) {
				e.printStackTrace();
				return IConstants.FAILED;
			}

		}
		return IConstants.SUCCESS;
	}

	public String Sms_Getbatch_web_All() {
		Service serv = ModemService.getService();

		List<InboundMessage> msgList = new ArrayList<InboundMessage>();
		// 用于拼接短信内容
		StringBuilder buffer = new StringBuilder();

		// 读取所有短信到msgList
		try {
			serv.readMessages(msgList, MessageClasses.ALL);
			if (msgList != null) {
				for (int i = 0; i < msgList.size(); i++) {
					InboundMessage readBean = (InboundMessage) msgList.get(i);
					buffer.append(readBean.getMemIndex());
					buffer.append("#");
					buffer.append(readBean.getOriginator());
					buffer.append("#");
					buffer.append(readBean.getText());
					buffer.append("#");
					buffer.append(DateUtil.transferFromDateToString(readBean
							.getDate()));
					if (i != msgList.size() - 1) {
						buffer.append("|");
					}
					serv.deleteMessage(readBean);
				}
			}

		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String Sms_Getbatch_web_ByType(int type) {

		Service serv = ModemService.getService();

		List<InboundMessage> msgList = new ArrayList<InboundMessage>();
		// 用于拼接短信内容
		StringBuilder buffer = new StringBuilder();
		try {
			if (type == IConstants.SMS_TYPE_ALL) {
				serv.readMessages(msgList, MessageClasses.ALL);
			} else if (type == IConstants.SMS_TYPE_ALREAD) {
				serv.readMessages(msgList, MessageClasses.READ);
			} else if (type == IConstants.SMS_TYPE_UNREAD) {
				//此方法执行有问题，如果想取得未读的短信，需要在此方法中启动serv.startService()，但这样在工程初始化时就不能启动短信猫服务了，
				//这势必会大大影响方法执行速度，所以不建议使用此方法
				serv.readMessages(msgList, MessageClasses.UNREAD);
			}
			if (msgList != null) {
				for (int i = 0; i < msgList.size(); i++) {
					InboundMessage readBean = (InboundMessage) msgList.get(i);
					buffer.append(readBean.getMemIndex());
					buffer.append("#");
					buffer.append(readBean.getOriginator());
					buffer.append("#");
					buffer.append(readBean.getText());
					buffer.append("#");
					buffer.append(DateUtil.transferFromDateToString(readBean
							.getDate()));
					if (i != msgList.size() - 1) {
						buffer.append("|");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(buffer.toString());
		return buffer.toString();

	}

	public String Sms_DeleteAll_web_ByType(int type) {

		Service serv = ModemService.getService();

		List<InboundMessage> msgList = new ArrayList<InboundMessage>();

		try {
			if (type == IConstants.SMS_TYPE_ALL) {
				serv.readMessages(msgList, MessageClasses.ALL);
			} else if (type == IConstants.SMS_TYPE_ALREAD) {
				serv.readMessages(msgList, MessageClasses.READ);
			} else if (type == IConstants.SMS_TYPE_UNREAD) {
				//此方法执行有问题，如果想取得未读的短信，需要在此方法中启动serv.startService()，但这样在工程初始化时就不能启动短信猫服务了，
				//这势必会大大影响方法执行速度，所以不建议使用此方法
				serv.readMessages(msgList, MessageClasses.UNREAD);
			}

			if (msgList != null) {
				for (int i = 0; i < msgList.size(); i++) {
					InboundMessage readBean = (InboundMessage) msgList.get(i);
					serv.deleteMessage(readBean);
				}
			}

		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IConstants.SUCCESS;
	}

}
