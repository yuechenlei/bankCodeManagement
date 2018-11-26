package com.xiaoqingxin.bankCodeService;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoqingxin.bankCodeService.mapper.CnapsMapper;
import com.xiaoqingxin.bankCodeService.model.Cnaps;

public class MybatisTest {
	private static Logger logger = LoggerFactory.getLogger(MybatisTest.class);
	// 单例模式
	private final static Class<MybatisTest> LOCK = MybatisTest.class;
	private static SqlSessionFactory sqlSessionFactory = null;

	public MybatisTest() {
	}

	// xml文件生成 SqlSessionFactory
	public static SqlSessionFactory getSqlSessionFactory() {
		synchronized (LOCK) {
			if (sqlSessionFactory != null) {
				return sqlSessionFactory;
			}
			String resource = "mybatis-config.xml";
			InputStream inputStream;
			try {
				inputStream = Resources.getResourceAsStream(resource);
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return sqlSessionFactory;
		}
	}

	// 代码生成SqlSessionFactory
	public static SqlSessionFactory getSqlSessionFactory2() {
		synchronized (LOCK) {
			// 数据库连接池信息
			PooledDataSource dataSource = new PooledDataSource();
			dataSource.setDriver("com.mysql.jdbc.Driver");
			dataSource.setUsername("root");
			dataSource.setPassword("123456");
			dataSource.setUrl("jdbc:mysql://localhost:3306/chapter3");
			dataSource.setDefaultAutoCommit(false);
			// 采用MyBatis的JDBC事务方式
			TransactionFactory transactionFactory = new JdbcTransactionFactory();
			Environment environment = new Environment("development", transactionFactory, dataSource);
			// 创建Configuration对象
			Configuration configuration = new Configuration(environment);
			// 注册一个MyBatis上下文别名
			// configuration.getTypeAliasRegistry().registerAlias("role",
			// Role.class);
			// 加入一个映射器
			// configuration.addMapper(RoleMapper.class);
			// configuration.addMapper(RoleMapper2.class);
			// 使用SqlSessionFactoryBuilder构建SqlSessionFactory
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
			return sqlSessionFactory;
		}
	}

	// 生成SqlSession
	public static SqlSession openSqlSession() {
		if (sqlSessionFactory == null) {
			getSqlSessionFactory();
		}
		return sqlSessionFactory.openSession();
	}

	@Test
	public  void test(){
		sqlSessionFactory = getSqlSessionFactory();
		logger.info("xml文件生成 SqlSessionFactory = "+sqlSessionFactory);
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			logger.info("xml文件生成 SqlSession = "+sqlSession);
			
//			Cnaps cnaps = sqlSession.selectOne("get","102100000030");
//			logger.info("查询结果sqlSession.selectOne(),cnaps={}",cnaps);
			
			CnapsMapper cm = sqlSession.getMapper(CnapsMapper.class);
			Cnaps cnaps = cm.get("102100000030");
			logger.info("查询结果CnapsMapper.get(),cnaps={}",cnaps);
			
			sqlSession.commit();
		} catch (Exception e) {
			logger.error("Exception e = "+e);
			sqlSession.rollback();
		}finally{
			if(sqlSession!=null) sqlSession.close();
		}
		
	}

}
