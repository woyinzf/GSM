package gov.csc.ems.util;

import gov.csc.ems.gsm.sendservice.ModemService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class EmsConfigLoader extends HttpServlet {
	Logger loger = Logger.getLogger(EmsConfigLoader.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String ems = config.getInitParameter("ems");
		String realPathRoot = config.getServletContext().getRealPath("/");
		loger.info("开始加载系统配置文件ems.properties");
		SysUtils.initProperties(realPathRoot);
		//ModemTest.test();
		ModemService.initialize();
	}
}
