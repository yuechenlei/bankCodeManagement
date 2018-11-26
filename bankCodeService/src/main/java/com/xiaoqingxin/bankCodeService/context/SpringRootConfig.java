package com.xiaoqingxin.bankCodeService.context;


import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;

/**
* @ClassName: SpringRootConfig
* @Description: Spring上下文配置
* @author Administrator
* @date 2018年9月21日
*
 */
@Configuration
@ImportResource({"classpath:/spring-dataSource.xml","classpath:/spring-mvc.xml"})
@Import({ RedisConfig.class})
@ComponentScan(basePackages = "com.xiaoqingxin.bankCodeService", excludeFilters = { @Filter(Controller.class), @Filter(Configuration.class) })
public class SpringRootConfig {
//	private static final Logger logger = LoggerFactory.getLogger(SpringRootConfig.class);
    
	// 让spring能够解析properties文件
	@Bean(name="propertySourcesPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//		MutablePropertySources ps = new MutablePropertySources();
//	    try {
//			ps.addFirst(new ResourcePropertySource("classpath:/mariadb.properties"));
//			ps.addFirst(new ResourcePropertySource("classpath:/redis.properties"));
//		} catch (IOException e) {
//			logger.error("", e);
//		}
	    PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
//		pspc.setIgnoreResourceNotFound(false);
//		pspc.setPropertySources(ps);
		return pspc;
	}

	@Bean(name="validator")
	public Validator validator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
	
//	@Bean(name="mariadbProperties")
//	public ResourcePropertySource mariadbProperties() throws IOException {
//		return new ResourcePropertySource("classpath:/mariadb.properties");
//
//	}
	

//	@Bean
//	public ResourcePropertySource serverHostProperties() throws IOException {
//		return new ResourcePropertySource("classpath:/serverHost.properties");
//
//	}

}
