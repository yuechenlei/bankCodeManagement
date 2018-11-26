package com.xiaoqingxin.bankCodeService.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
* @ClassName: ExceptionLogInterceptor
* @Description: 异常日志打印拦截器
* @author Administrator
* @date 2018年9月21日
*
 */
public class ExceptionLogInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LoggerFactory.getLogger(ExceptionLogInterceptor.class);

	/** 无论是否产生异常都会在渲染视图后执行  */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (ex != null) {
			logger.error("Uncatched Exception", ex);
			String exceptionClassName = ex.getClass().getName();
			if (exceptionClassName.startsWith("com.xiaoqingxin")) {
				super.afterCompletion(request, response, handler, ex);
			} else {
				super.afterCompletion(request, response, handler, new Exception("系统异常，请稍候再试。"));
			}
		}
	}

}
