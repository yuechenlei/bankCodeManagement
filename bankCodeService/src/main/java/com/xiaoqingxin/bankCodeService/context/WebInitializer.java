package com.xiaoqingxin.bankCodeService.context;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.xiaoqingxin.bankCodeService.utils.ApplicationContextUtils;



/**
* @ClassName: WebInitializer
* @Description: Web应用初始化
* @author Administrator
* @date 2018年9月21日
*
 */
public class WebInitializer implements WebApplicationInitializer {
	private static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
//		ContextLoaderListener cll = new ContextLoaderListener();
		logger.info("Web应用初始化");
		// 创建Spring上下文
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(SpringRootConfig.class);
		ConfigurableEnvironment environment = rootContext.getEnvironment();
		try {
			environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:/mariadb.properties"));
			environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:/environment.properties"));
//			environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:/serverHost.properties"));
		} catch (IOException e) {
			logger.error("", e);
		}
//		rootContext.setEnvironment(environment);
		// 管理Spring上下文的生命周期，ContextLoaderListener初始化Spring IoC容器
		servletContext.addListener(new ContextLoaderListener(rootContext));
		// 将Spring上下文放入工具类
		ApplicationContextUtils.setApplicationContext(rootContext);

//		servletContext.setAttribute("staticFilesHost", environment.getProperty("static.files.host"));

		// 字符编码过滤器
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", encodingFilter);
		characterEncodingFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

		String filePath = "F:/uploads";
		// 5mb
		Long singleMax = (long)(5*Math.pow(2, 20));
		// 10mb
		Long totalMax = (long)(10*Math.pow(2, 20));
		
		
		// 创建SpringMVC上下文
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(SpringMVCConfig.class);
//		dispatcherContext.refresh();
		// 注册SpringMVC分发器
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		// 上传文件配置
		dispatcher.setMultipartConfig(new MultipartConfigElement(filePath,singleMax,totalMax,0));

	}
}