package gov.csc.ems.util;

import gov.csc.ems.gsm.service.ISmsService;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

/**
 * @author liuy 
 *  2013-7-16
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String serviceUrl = "http://localhost:8080/GSM/xfire/services/ISmsService";
		Service serviceModel = new ObjectServiceFactory().create(
				ISmsService.class, null,
				"http://localhost:8080/GSM/xfire/services/ISmsServiceISmsService?wsdl", null);
		XFireProxyFactory serviceFactory = new XFireProxyFactory();
		try {
			ISmsService service = (ISmsService) serviceFactory.create(
					serviceModel, serviceUrl);
			//String result = service.Sms_Getbatch_web_ByType(1);
			service.Sms_Sendbatch_web_NoResult("18210274012", "尹兆发通过程序：发送的短信测试。");
			//String result = service.Sms_DeleteAll_web_ByType(2);
			//System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
