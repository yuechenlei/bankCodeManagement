package com.xiaoqingxin.bankCodeService.context;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
@Import({ RedisConfig.class })
@ComponentScan(basePackages = "com.xiaoqingxin.bankCodeService", excludeFilters = { @Filter(Controller.class), @Filter(Configuration.class) })
public class SpringRootConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public Validator validator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
	

//	@Bean
//	public ResourcePropertySource serverHostProperties() throws IOException {
//		return new ResourcePropertySource("classpath:/serverHost.properties");
//
//	}

}
