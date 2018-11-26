/**
 *
 */
package com.xiaoqingxin.bankCodeService.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoqingxin.bankCodeService.BaseTest;
import com.xiaoqingxin.bankCodeService.redisService.AdministrativeDivisionService;



/**
 * @Description: 行政区划码测试
 * @see: AdministrativeDivisionServiceImpl
 * @version 2016年6月22日 下午3:59:33
 * @author xiaowei.zhu
 */
public class AdministrativeDivisionServiceImplTest extends BaseTest {
	private static final Logger logger = LoggerFactory.getLogger(AdministrativeDivisionServiceImplTest.class);

	@Resource
	private AdministrativeDivisionService administrativeDivisionService;
//	@Resource
//	private JedisPool pool;

	@Test
	public void testAd() throws Exception {
		long start = System.currentTimeMillis();
		logger.info("TEST START -------  " + start);

//		Jedis jedis = pool.getResource();
//		try {
//			jedis.getDB();
			// 删除省
			// jedis.del(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + "4900");
			// jedis.del("AD.PROVINCE.NAME.CODE:" + "河南省");
			// Set<String> couKeySet = jedis.keys("AD.COUNTY.NAME.CODE:" + "*");
			// for (String couKey : couKeySet) {
			// logger.info("couKey=" + couKey);
			// jedis.del(couKey);
			// }
			// jedis.del("AD.PROVINCES:" + "proCodes");
			// 删除市
			// jedis.del(C.AD_CITY_CODE_NAME_KEY_PREFIX + "4910");
			// jedis.del(C.AD_CITY_NAME_CODE_KEY_PREFIX + "郑州市");
			// jedis.del(C.AD_CITY_PROVINCE_KEY_PREFIX + "4910");
			// jedis.zrem(C.AD_PROVINCE_CITY_KEY_PREFIX + "4900", "4910");
			// 删除县
			// jedis.del(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + "4913");
			// jedis.del(C.AD_NAME_CODE_KEY_PREFIX + "新郑市");
			// jedis.del(C.AD_COUNTY_CITY_KEY_PREFIX + "4913");
			// jedis.zrem(C.AD_CITY_COUNTY_KEY_PREFIX + "4910", "4913");

			// name模糊查询
			// 如果是一个县
			// Set<String> couKeySet = jedis.keys(C.AD_COUNTY_NAME_CODE_KEY_PREFIX + "新*");
			// for (String couKey : couKeySet) {
			// logger.info("县couKeySet:" + couKey);
			// }
			// // 如果是一个市
			// Set<String> cityKeySet = jedis.keys(C.AD_CITY_NAME_CODE_KEY_PREFIX + "郑*");
			// for (String cityKey : cityKeySet) {
			// logger.info("市couKeySet:" + cityKey);
			// }
			// // 如果是一个省
			// Set<String> proKeySet = jedis.keys(C.AD_PROVINCE_NAME_CODE_KEY_PREFIX + "河*");
			// for (String proKey : proKeySet) {
			// logger.info("省couKeySet:" + proKey);
			// }

			// code 查询
			// String name = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + 4910);
			// logger.info("name=" + name);
			// Object o = jedis.get("");
			// logger.info("o=");

			// Transaction transaction = jedis.multi();
			// Response<String> rs =
			// transaction.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + "4910");
			// transaction.exec();
			// String test = rs.get();
			// logger.info("test=" + test);
//
//		} catch (JedisConnectionException e) {
//			logger.error("", e);
//		} finally {
//			if (jedis != null) {
//				jedis.close();
//			}
//		}

		long end = System.currentTimeMillis();
		logger.info("TEST END -------  " + end);
		logger.info("PROCESSED -------  " + (end - start) + " ms");

	}

}
