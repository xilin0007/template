package com.fxl.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GlobalViewExceptionHandle implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest reqeust,
	                                     HttpServletResponse response, Object arg2, Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		String stackTrace = ExceptionUtils.getStackTrace(ex);
		model.put("ex", stackTrace);
		log.info("统一异常类出错stackTrace:{}", stackTrace);
//		LogUtil.debug(MyExceptionHandle.class.getName(), "统一异常类出错："+ex,"weixin_excpetion_handle");
//		LogUtil.error("统一异常类出错msg:",ex);
		log.error("统一异常类出错msg:", ex);
		// 根据不同错误转向不同页面
		if (ex instanceof Exception) {
			return new ModelAndView("/error/code_error", model);//跳转统一公共报错jsp页面
		} else {
			return new ModelAndView("/error/code_error", model);
		}

	}

}
