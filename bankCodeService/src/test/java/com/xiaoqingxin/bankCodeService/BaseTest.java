package com.xiaoqingxin.bankCodeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.xiaoqingxin.bankCodeService.context.SpringRootConfig;


/**
* @ClassName: BaseTest
* @Description: 测试类的父类，自动加载spring配置文件且运行于事务之下，测试完毕之后不回滚
* @author Administrator
* @date 2018年9月30日
*
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringRootConfig.class })
@Rollback(false)
public class BaseTest {

	@Test
	public void testConfig() {

	}

}
