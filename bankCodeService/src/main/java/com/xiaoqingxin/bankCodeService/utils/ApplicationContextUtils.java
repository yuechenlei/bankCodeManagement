package com.xiaoqingxin.bankCodeService.utils;

import org.springframework.context.ApplicationContext;

/**
* @ClassName: ApplicationContextUtils
* @Description: 存放Spring上下文
* @author Administrator
* @date 2018年9月21日
*
 */
public class ApplicationContextUtils {
	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtils.applicationContext = applicationContext;
	}

}
