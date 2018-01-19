package gov.csc.ems.gsm.service;

import java.util.List;

/**
 * 
 * @author Qiaohz 2011-7-9 下午07:41:16
 */
public interface ISmsService {
	
	/**
	 * 利用阻塞队列一次性发送多条短信，并返回发送结果，发送成功的手机号在前，失败的手机号在后，以‘；’号隔开
	 * @param sendTel
	 * @param text
	 * @return
	 */
	public String Sms_Sendbatch_web(String telNum, String smsText);

	/**
	 * 利用阻塞队列一次性发送多条短信，无返回结果
	 * @param sendTel
	 * @param text
	 * @return
	 */
	public String Sms_Sendbatch_web_NoResult(String telNum, String smsText);
	
	
	/**
	 * 返回短信猫内所有短信内容,读取后便清空短信猫，防止sim卡满了收不到短信
	 * @return
	 */
	
	public String Sms_Getbatch_web_All();
	
	/**
	 * 返回指定类型的短信
	 * @param type 短信类型(0：未读短信；1：已读短信；2：全部短信)
	 * @return
	 */
	public String Sms_Getbatch_web_ByType(int type);
	
	/**
	 * 删除指定类型的短信
	 * @param type 短信类型(0：未读短信；1：已读短信；2：全部短信)
	 * @return
	 */
	public String Sms_DeleteAll_web_ByType(int type);
}
