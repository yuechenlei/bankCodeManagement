package com.xiaoqingxin.bankCodeService.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.xiaoqingxin.bankCodeService.handler.SimpleForwardHttpRequestHandlerForToHTM;
import com.xiaoqingxin.bankCodeService.interceptor.ExceptionLogInterceptor;



/**
* @ClassName: SpringMVCConfig
* @Description: Spring MVC配置
* @author Administrator
* @date 2018年9月21日
*
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.xiaoqingxin.bankCodeService.web.controller", useDefaultFilters = false, includeFilters = @Filter(Controller.class))
public class SpringMVCConfig extends WebMvcConfigurerAdapter {

	/**
	 * 配置拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ExceptionLogInterceptor());
//		registry.addInterceptor(localeChangeInterceptor());
		super.addInterceptors(registry);
	}

	/** 文件上传  */
	@Bean
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	/**
	 * jsp视图解析器,查找jsp文件
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		//将视图解析为JstlView，因为要使用jstl标签
		resolver.setViewClass(JstlView.class);
		return resolver;
	}

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
		exceptionResolver.setDefaultErrorView("error/error");
		return exceptionResolver;
	}

	/**
	 * 配置异常处理器
	 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(simpleMappingExceptionResolver());
		super.configureHandlerExceptionResolvers(exceptionResolvers);
	}
	@Bean
	public SimpleForwardHttpRequestHandlerForToHTM simpleForwardHttpRequestHandlerForToHTM() {
		return new SimpleForwardHttpRequestHandlerForToHTM();
	}

	@Bean
	public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
		Properties mappings = new Properties();
		mappings.put("/**", simpleForwardHttpRequestHandlerForToHTM());
		simpleUrlHandlerMapping.setMappings(mappings);
		return simpleUrlHandlerMapping;
	}
	
	/** 静态资源处理器  */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		super.addResourceHandlers(registry);
	}
	
	/**
	 * 初始化RequestMappingHandlerAdapter，并加载Http的Json转换器
	 * @return  RequestMappingHandlerAdapter 对象
	 */
	@Bean(name="requestMappingHandlerAdapter") 
	public HandlerAdapter initRequestMappingHandlerAdapter() {
		//创建RequestMappingHandlerAdapter适配器
		RequestMappingHandlerAdapter rmhd = new RequestMappingHandlerAdapter();
		//HTTP JSON转换器
		MappingJackson2HttpMessageConverter  jsonConverter = new MappingJackson2HttpMessageConverter();
		//MappingJackson2HttpMessageConverter接收JSON类型消息的转换
		MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(mediaType);
		//加入转换器的支持类型
		jsonConverter.setSupportedMediaTypes(mediaTypes);
		//往适配器加入json转换器
		rmhd.getMessageConverters().add(jsonConverter);
		return rmhd;
	}
	
	// 国际化信息(配合msg.properties与spring的message标签)
//	@Bean(name="messageSource")
//	public MessageSource messageSource(){
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setDefaultEncoding("UTF-8");
//		messageSource.setBasename("msg");
//		return messageSource;
//	}
	
//	@Bean(name="localeResolver")
//	public LocaleResolver initSessionLocaleResolver() {
//		SessionLocaleResolver slr = new SessionLocaleResolver();
//		//默认使用简体中文
//		slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//		return slr;
//	}
	// 国际化拦截器
//	@Bean
//	public LocaleChangeInterceptor localeChangeInterceptor() {
//	LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
////	localeChangeInterceptor.setParamName("lang"); // 查询参数
//	return localeChangeInterceptor;
//	}
	
	
	// Tiles视图解析器，页面布局模版
//	@Bean
//	public TilesConfigurer tilesConfigurer(){
//		TilesConfigurer tiles = new TilesConfigurer();
//		tiles.setDefinitions(new String[]{"/WEB-INF/jsp/layout/tiles.xml"});
//		tiles.setCheckRefresh(true); // 刷新
//		
//		return tiles;
//	}
//	// Tiles ViewResolver
//	@Bean
//	public ViewResolver viewResolver(){
//		return new TilesViewResolver();
//	}
	
	

}
