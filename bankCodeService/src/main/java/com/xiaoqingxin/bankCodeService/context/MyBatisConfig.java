package com.xiaoqingxin.bankCodeService.context;


import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

//@Configuration
//@MapperScan("com.xiaoqingxin.bankCodeService.mapper")
public class MyBatisConfig {
	private static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);
	
//	@javax.annotation.Resource
	DataSource dataSource;
	
	/***
	 * 配置SqlSessionFactoryBean
	 * @return SqlSessionFactoryBean
	 */
//	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean initSqlSessionFactory() {
		logger.info("配置SqlSessionFactoryBean......,dataSource="+dataSource);
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		//配置MyBatis配置文件
		Resource resource = new ClassPathResource("mybatis-config.xml");
		sqlSessionFactory.setConfigLocation(resource);
		return sqlSessionFactory;
	}
	
	/***
	 * 通过自动扫描，发现MyBatis Mapper接口,必须使用@Repository
	 * @return Mapper扫描器
	 */
//	@Bean 
	public MapperScannerConfigurer initMapperScannerConfigurer() {
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("com.xiaoqingxin.bankCodeService.mapper");
		msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
		msc.setAnnotationClass(Repository.class);
		return msc;
	}

	
	

}
