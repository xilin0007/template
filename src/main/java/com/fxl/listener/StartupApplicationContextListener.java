package com.fxl.listener;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.fxl.frame.util.ConfigProUtils;
import com.fxl.frame.util.constant.Consts;

/**
 * 常量初始化赋值
 * @Description TODO
 * @author fangxilin
 * @date 2016-12-9
 * @Copyright: Copyright (c) 2016 Shenzhen Angelsound Technology Co., Ltd. Inc. 
 *             All rights reserved.
 */
public class StartupApplicationContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			Properties config = new Properties();
			
			ServletContext servletContext = servletContextEvent.getServletContext();
			config.load(servletContext.getResourceAsStream("/WEB-INF/classes/conf/config.properties"));
//			servletContext.setAttribute("CHAT_URL", Consts.CHAT_URL);
			
			//获取环境变量的路径
//			String resource = System.getenv("WEBAPP_TEMPLATE_CONF");
//			resource = resource.replace("\\", "/");
//			config.load(new FileInputStream(new File(resource+"/conf/config.properties")));
			Consts.BASE_URL = config.getProperty("BASE_URL");
			Consts.BASE_FILE_URL = config.getProperty("BASE_FILE_URL");
			Consts.COMMON_URL = config.getProperty("COMMON_URL");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
