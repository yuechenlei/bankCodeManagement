package com.xiaoqingxin.bankCodeService.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoqingxin.bankCodeService.BaseTest;
import com.xiaoqingxin.bankCodeService.service.CnapsService;

import redis.clients.jedis.JedisPool;



/**
 * 联行号业务实现测试
* @ClassName: CnapsServiceImplTest
* @Description: 联行号业务实现测试
* @author Administrator
* @date 2018年9月30日
*
 */
public class CnapsServiceImplTest extends BaseTest {
	private static final Logger logger = LoggerFactory.getLogger(CnapsServiceImplTest.class);
	private static final String[] FIELDS = new String[] { "name", "clearingBankCode", "clearingBankLevel", "providerCode", "adCode" };

	@Resource
	private CnapsService cnapsService;
//	@Resource
//	private JedisPool pool;

	public static void main(String[] args) {
//		Cnaps oldCnaps = null;
//		String code = oldCnaps.getCode();
//		String oldname = oldCnaps.getName();
//		if (oldCnaps == null || StringUtils.isBlank(code) || StringUtils.isBlank(oldname)) {
//			System.out.println("********************** is null");
//		}
	}

	/**
	 * Test method for {@link com.lefu.cachecenter.service.impl.CnapsServiceImpl#suggestSearch(int, java.lang.String[])}.
	 */
	// @Test
	// public void testTotalExport() {
	// List<Cnaps> result = new ArrayList<Cnaps>();
	// String clearBankLevelKey1 = C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + 1;
	// String clearBankLevelKey2 = C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + 2;
	// String clearBankLevelKey3 = C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + -1;
	//
	// // 查找的多个key
	// String[] unionTempKey = new String[] { clearBankLevelKey1, clearBankLevelKey2, clearBankLevelKey3 };
	// // 并集集合key
	// String fullTempKey = C.CNAPS_SUGGEST_TEMP_KEY + ".FULL";
	//
	// Jedis jedis = jedisPool.getResource();
	// long start = System.currentTimeMillis();
	// logger.info("TEST START -------  " + start);
	// try {
	// Pipeline pipe2 = jedis.pipelined();
	// pipe2.zunionstore(fullTempKey, unionTempKey);
	// pipe2.zrange(fullTempKey, 0, -1);
	// List<Object> execResult = pipe2.syncAndReturnAll();
	// @SuppressWarnings("unchecked")
	// Set<String> cnapsCodes = (Set<String>) execResult.get(1);
	// logger.info("cnapsCodes.size()=" + cnapsCodes.size());
	//
	// List<String> csCodes = new ArrayList<String>();
	// for (String code : cnapsCodes) {
	// csCodes.add(code);
	// }
	// Pipeline pipe = jedis.pipelined();
	// for (String code : csCodes) {
	// pipe.hmget(C.CNAPS_KEY_PREFIX + code, FIELDS);
	// }
	// List<Object> lists = pipe.syncAndReturnAll();
	// logger.info("lists.size=" + lists.size());
	//
	// long end1 = System.currentTimeMillis();
	// logger.info("pipe查询共用时 -------  " + (end1 - start) + " ms");
	//
	// for (int i = 0; i < lists.size(); i++) {
	// @SuppressWarnings("unchecked")
	// List<String> fieldValues = (List<String>) lists.get(i);
	// String code = csCodes.get(i);
	// Cnaps cnap = new Cnaps();
	// cnap.setCode(code);
	// if (!fieldValues.isEmpty() && fieldValues.get(0) != null) {
	// for (String value : fieldValues) {
	// if ("name".equals(FIELDS[fieldValues.indexOf(value)])) {
	// cnap.setName(value);
	// }
	// if ("clearingBankCode".equals(FIELDS[fieldValues.indexOf(value)])) {
	// cnap.setClearingBankCode(value);
	// }
	// if ("clearingBankLevel".equals(FIELDS[fieldValues.indexOf(value)])) {
	// cnap.setClearingBankLevel(Integer.valueOf(value));
	// }
	// if ("providerCode".equals(FIELDS[fieldValues.indexOf(value)])) {
	// cnap.setProviderCode(value);
	// }
	// if ("adCode".equals(FIELDS[fieldValues.indexOf(value)])) {
	// cnap.setAdCode(value);
	// }
	// }
	// }
	// result.add(cnap);
	// }
	//
	// long end2 = System.currentTimeMillis();
	// logger.info("填充数据共用时 -------  " + (end2 - start) + " ms");
	//
	// for (int i = 0; i < result.size(); i++) {
	// System.out.println("result(" + i + ")=" + result.get(i));
	// }
	//
	// } catch (Exception e) {
	// logger.info("", e);
	// } finally {
	// if (jedis != null) {
	// jedis.close();
	// }
	// }
	//
	// }

}
