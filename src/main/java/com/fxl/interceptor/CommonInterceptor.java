package com.fxl.interceptor;

import java.util.Enumeration;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fxl.template.user.service.UserInfoService;

/**
 * 公共拦截器，主要拦截参数
 * @Description TODO
 * @author fangxilin
 * @date 2016-11-29
 * @Copyright: Copyright (c) 2016 Shenzhen Angelsound Technology Co., Ltd. Inc. 
 *             All rights reserved.
 */
public class CommonInterceptor implements HandlerInterceptor {
	
	private final static Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * ThreadLocal用法测试
	 */
	private static ThreadLocal<String> limitFlag = new ThreadLocal<String>();


	/**
     * 该方法在目标方法之前被调用.
     * 若返回值为 true, 则继续调用后续的拦截器和目标方法. 
     * 若返回值为 false, 则不会再调用后续的拦截器和目标方法. 
     * 可以考虑做权限. 日志, 事务等. 
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//解决session跨域失效的问题
		//response.setHeader("P3P","CP=CAO PSA OUR");
		//请求参数
		String requestParam = "";
		//以拼接形式获取地址参数
		Enumeration<String> keys = request.getParameterNames(); 
		while(keys.hasMoreElements()) { 
		    String k = keys.nextElement(); 
		    requestParam += k + " = " + request.getParameter(k)+",";
		}
		String uri =  request.getRequestURI();
		requestParam = (StringUtils.isNotEmpty(requestParam))?requestParam.substring(0, requestParam.lastIndexOf(",")):"";
		logger.info("请求方式："+request.getMethod()+",访问路径："+uri+",输出参数==>"+requestParam);
		//获取自定义头部信息
		String keyvalue = request.getHeader("key");
		//192.168.1.235
		String host = request.getHeader("Host");
		//http://192.168.1.235:8080/template
		String basePath = request.getHeader("origin") + request.getContextPath();
		
		//获取body参数
		ServletInputStream bodyStream = request.getInputStream();
        int size = request.getContentLength();
        if (size > 1) {
            byte[] bytes = new byte[size];
            bodyStream.read(bytes, 0, size);
            String body = new String(bytes, "UTF-8");
            logger.info("body参数==>" + body);
        }

		String preValue = limitFlag.get();
        logger.info("preHandle，ThreadLocal设置之前的值：{}", preValue);
		String threadName = Thread.currentThread().getName();
		limitFlag.set(threadName);
		return true;
	}
	
	/**
     * 调用目标方法之后, 但渲染视图之前. 
     * 可以对请求域中的属性或视图做出修改. 
     */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
     * 渲染视图之后被调用. 释放资源
     */
	@Override
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)	throws Exception {
		//获取spring中HandlerMethod对象
	    HandlerMethod method = (HandlerMethod) handler;
	    if (method.hasMethodAnnotation(LogRecordInfo.class)) {
	    	String content = response.getHeader("logContent");
	    	LogRecordInfo logri = method.getMethodAnnotation(LogRecordInfo.class);
	    	logger.info("动作：" +  logri.operate() + "，对象：" + logri.module() + "，对象内容：" + logri.content());
	    	logger.info("header日志对象内容：" + content);
	    	//可进行数据库日志的录入
		}

		String afterValue = limitFlag.get();
		logger.info("afterCompletion，ThreadLocal设置之后的值：{}", afterValue);
		limitFlag.remove();
	}
}
