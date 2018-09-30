package com.xiaoqingxin.bankCodeService.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.xiaoqingxin.bankCodeService.C;
import com.xiaoqingxin.bankCodeService.model.Cnaps;
import com.xiaoqingxin.bankCodeService.service.CnapsService;
import com.xiaoqingxin.bankCodeService.utils.Page;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @ClassName: CnapsServiceImpl
 * @Description: 联行号业务实现
 * @author Administrator
 * @date 2018年9月21日
 *
 */
@Service("cnapsService")
public class CnapsServiceImpl implements CnapsService {
	private static final Logger logger = LoggerFactory.getLogger(CnapsServiceImpl.class);
	// @Resource
	// private JedisSentinelPool jedisPool;
	// @Resource
	// private JedisPool pool;
	@SuppressWarnings("rawtypes")
	@Resource
	private RedisTemplate redisTemplate;

	private static final String[] FIELDS = new String[] { "name", "clearingBankCode", "clearingBankLevel",
			"providerCode", "adCode" };

	// 添加
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void add(List<Cnaps> cnapses) {
		if (cnapses == null || cnapses.size() == 0 || cnapses.size() > 1000)
			return;
		try {
			List<Cnaps> cnapsesl = new ArrayList<>();
			for (int i = 0; i < cnapses.size(); i++) {
				Cnaps cnap = searchByCncode(cnapses.get(i).getCode());
				if (cnap != null) {
					cnapsesl.add(cnap);
					cnapses.remove(i);
					i--;
				}
			}
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					for (Cnaps cnaps : cnapses) {
						Map<String, String> map = new HashMap<>();
						map.put("name", cnaps.getName());
						map.put("clearingBankCode", cnaps.getClearingBankCode());
						map.put("clearingBankLevel", Integer.toString(cnaps.getClearingBankLevel()));
						map.put("providerCode", cnaps.getProviderCode());
						map.put("adCode", cnaps.getAdCode());
						ops.opsForHash().putAll(C.CNAPS_KEY_PREFIX + cnaps.getCode(), map);
						// 以下存储为了查询
						// 去掉无意义字词
						String name = cnaps.getName().trim().replaceAll(C.MEANINGLESS_WORD_REGEX, "");
						// 按词的长度做为权重
						String[] words = StringUtils.splitByWord(name, false);
						int weight = 1;
						if (cnaps.getClearingBankLevel() == 1)
							weight = 50;
						else if (cnaps.getClearingBankLevel() == 2)
							weight = 20;
						ops.opsForZSet().add(C.CNAPS_SUGGEST_KEY_PREFIX + name, cnaps.getCode(), 1000);
						for (int i = 0; i < words.length; i++) {
							ops.opsForZSet().add(C.CNAPS_SUGGEST_KEY_PREFIX + words[i], cnaps.getCode(),
									(100 - name.length()) + (words[i].length() >= 2 ? 2 : words[i].length()) * weight);
						}
						ops.opsForZSet().add(C.CNAPS_PROVIDER_KEY_PREFIX + cnaps.getProviderCode(),
								cnaps.getCode(), C.CNAPS_PROVIDER_SCORE);
						ops.opsForZSet().add(C.CNAPS_CLEARINGBANK_KEY_PREFIX + cnaps.getProviderCode() + "."
								+ cnaps.getClearingBankLevel(), cnaps.getCode(), C.CNAPS_CLEARINGBANK_SCORE);
						ops.opsForZSet().add(C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + cnaps.getClearingBankLevel(),
								cnaps.getCode(), C.CNAPS_CLEARBANKLEVEL_KEY_SCORE);
						ops.opsForZSet().add(C.CNAPS_ADCODE_KEY_PREFIX + cnaps.getAdCode(), cnaps.getCode(),
								C.CNAPS_ADCODE_KEY_SCORE);
					}
					return ops.exec();
				}

			};
			redisTemplate.executePipelined(callBack);
			if (!cnapsesl.isEmpty()) {
				throw new RuntimeException("系统已存在该联行号信息" + cnapsesl.toString());
			}
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}
	}

	// 根据联行号查询
	@SuppressWarnings("unchecked")
	@Override
	public Cnaps searchByCncode(String code) {
		// 非空检查
		if (StringUtils.isBlank(code))
			return null;
		try {
			List<String> fieldValues = redisTemplate.opsForHash().multiGet(C.CNAPS_KEY_PREFIX + code,
					Arrays.asList(FIELDS));
			Cnaps cnaps = new Cnaps();
			if (!fieldValues.isEmpty() && fieldValues.get(0) != null) {
				for (String value : fieldValues) {
					if ("name".equals(FIELDS[fieldValues.indexOf(value)])) {
						cnaps.setName(value);
					} else if ("clearingBankCode".equals(FIELDS[fieldValues.indexOf(value)])) {
						cnaps.setClearingBankCode(value);
					} else if ("clearingBankLevel".equals(FIELDS[fieldValues.indexOf(value)])) {
						cnaps.setClearingBankLevel(Integer.valueOf(value));
					} else if ("providerCode".equals(FIELDS[fieldValues.indexOf(value)])) {
						cnaps.setProviderCode(value);
					} else if ("adCode".equals(FIELDS[fieldValues.indexOf(value)])) {
						cnaps.setAdCode(value);
					}
				}
				cnaps.setCode(code);
				return cnaps;
			}
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}
		return null;
	}

	/* 组合查询 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page<List<Cnaps>> cnapsCombQuery(Page<List<Cnaps>> page, Cnaps cnaps) {
		logger.info("into cnapsCombQuery_service(),Page={},cnaps={},fields={}", page, cnaps, FIELDS);
		List<Cnaps> result = new ArrayList<Cnaps>();

		String fullName = null;
		List<String> splitNameList = new ArrayList<>();
		List<String> otherList = new ArrayList<>();
		// generalForTemplate(cnaps, splitNameList, fullName);
		String[] words = null;
		if (StringUtils.notBlank(cnaps.getName())) {
			// 去掉无意义字词
			fullName = cnaps.getName().trim().replaceAll(C.MEANINGLESS_WORD_REGEX, "");
			// 非智能分词拆分银行名
			words = StringUtils.splitByWord(fullName, false);
			// 把每一个拆分结果，加上key前缀
			for (int i = 0; i < words.length; i++) {
				words[i] = C.CNAPS_SUGGEST_KEY_PREFIX + words[i];
			}
			fullName = C.CNAPS_SUGGEST_KEY_PREFIX + fullName;
		}
		if (words != null) {
			splitNameList.addAll(Arrays.asList(words));
		}

		// 清分行级别
		if (cnaps.getClearingBankLevel() != 0) {
			otherList.add(C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + cnaps.getClearingBankLevel());
		}

		// 银行编码
		if (StringUtils.notBlank(cnaps.getProviderCode())) {
			otherList.add(C.CNAPS_PROVIDER_KEY_PREFIX + cnaps.getProviderCode().trim().toUpperCase());
		}
		// 行政区划码
		if (StringUtils.notBlank(cnaps.getAdCode())) {
			otherList.add(C.CNAPS_ADCODE_KEY_PREFIX + cnaps.getAdCode().trim());
		}

		try {
			if (StringUtils.notBlank(cnaps.getName())) {
				List<Object> resultList = redisTemplate.executePipelined(new SessionCallback() {
					String fullName = C.CNAPS_SUGGEST_KEY_PREFIX
							+ cnaps.getName().trim().replaceAll(C.MEANINGLESS_WORD_REGEX, "");
					@Override
					public Object execute(RedisOperations ops) throws DataAccessException {
						ops.opsForZSet().intersectAndStore(splitNameList.get(0), splitNameList,
								C.CNAPS_SUGGEST_TEMP_KEY + "split");
						ops.opsForZSet().unionAndStore(fullName, C.CNAPS_SUGGEST_TEMP_KEY + "split",
								C.CNAPS_SUGGEST_TEMP_KEY + "union");
						if (!otherList.isEmpty()) {
							ops.opsForZSet().intersectAndStore(C.CNAPS_SUGGEST_TEMP_KEY + "union", otherList,
									C.CNAPS_SUGGEST_TEMP_KEY + "total");
							return ops.opsForZSet().reverseRange(C.CNAPS_SUGGEST_TEMP_KEY + "total", 0, 499);
						} else {
							return ops.opsForZSet().reverseRange(C.CNAPS_SUGGEST_TEMP_KEY + "union", 0, 499);
						}

					}

				});
				redisTemplate.expire(C.CNAPS_SUGGEST_TEMP_KEY + "split", 60, TimeUnit.SECONDS);
				redisTemplate.expire(C.CNAPS_SUGGEST_TEMP_KEY + "union", 60, TimeUnit.SECONDS);
				redisTemplate.expire(C.CNAPS_SUGGEST_TEMP_KEY + "total", 60, TimeUnit.SECONDS);
				Set<String> resultCodes = (Set<String>) resultList.get(resultList.size() - 1);
				// 分页,设置总数
				page.setTotalResult(resultCodes.size());
				//根据联行号查询
				SessionCallback session = new SessionCallback() {
					@Override
					public Object execute(RedisOperations ops) throws DataAccessException {
						ops.multi();
						for (String code : resultCodes) {
							ops.opsForHash().multiGet(C.CNAPS_KEY_PREFIX + code, Arrays.asList(FIELDS));
						}
						return ops.exec();
					}

				};
				List<Object> cnapsL = redisTemplate.executePipelined(session);
				List<Object> cnapsT = (List<Object>) cnapsL.get(cnapsL.size() - 1);
				int i = -1;
				// 遍历联行号集合，取出需要的字段值fields
				for (String code : resultCodes) {
					i++;
					Cnaps cnap = new Cnaps();
					List<String> fieldValues = (List<String>) cnapsT.get(i);
					if (!fieldValues.isEmpty() && fieldValues.get(0) != null) {
						for (String value : fieldValues) {
							if ("name".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setName(value);
							} else if ("clearingBankCode".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setClearingBankCode(value);
							} else if ("clearingBankLevel".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setClearingBankLevel(Integer.valueOf(value));
							} else if ("providerCode".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setProviderCode(value);
							} else if ("adCode".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setAdCode(value);
							}
						}
						cnap.setCode(code);
						result.add(cnap);
					}

				}
				page.setObject(result);

			} else {
				SessionCallback callBack = new SessionCallback() {
					@Override
					public Object execute(RedisOperations ops) throws DataAccessException {
						ops.opsForZSet().intersectAndStore(otherList.get(0), otherList, C.CNAPS_SUGGEST_TEMP_KEY);
						return ops.opsForZSet().reverseRange(C.CNAPS_SUGGEST_TEMP_KEY, 0, 499);
					}

				};
				// 获取联行号集合
				List<Object> resultList = redisTemplate.executePipelined(callBack);
				redisTemplate.expire(C.CNAPS_SUGGEST_TEMP_KEY, 60, TimeUnit.SECONDS);
				Set<String> resultCodes = (Set<String>) resultList.get(resultList.size() - 1);

				// 分页,设置总数
				page.setTotalResult(resultCodes.size());
				SessionCallback session = new SessionCallback() {
					@Override
					public Object execute(RedisOperations ops) throws DataAccessException {
						ops.multi();
						for (String code : resultCodes) {
							ops.opsForHash().multiGet(C.CNAPS_KEY_PREFIX + code, Arrays.asList(FIELDS));
						}
						return ops.exec();
					}

				};
				List<Object> cnapsL = redisTemplate.executePipelined(session);
				List<Object> cnapsT = (List<Object>) cnapsL.get(cnapsL.size() - 1);
				int i = -1;
				// 遍历联行号集合，取出需要的字段值fields
				for (String code : resultCodes) {
					i++;
					Cnaps cnap = new Cnaps();
					List<String> fieldValues = (List<String>) cnapsT.get(i);
					if (!fieldValues.isEmpty() && fieldValues.get(0) != null) {
						for (String value : fieldValues) {
							if ("name".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setName(value);
							} else if ("clearingBankCode".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setClearingBankCode(value);
							} else if ("clearingBankLevel".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setClearingBankLevel(Integer.valueOf(value));
							} else if ("providerCode".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setProviderCode(value);
							} else if ("adCode".equals(FIELDS[fieldValues.indexOf(value)])) {
								cnap.setAdCode(value);
							}
						}
						cnap.setCode(code);
						result.add(cnap);
					}

				}
				page.setObject(result);
			}
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}

		return page;
	}

	/** 删除 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void del(Cnaps cnaps) {
		String fullName = cnaps.getName().trim().replaceAll(C.MEANINGLESS_WORD_REGEX, "");
		String[] words = StringUtils.splitByWord(fullName, false);
		try {
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.delete(C.CNAPS_KEY_PREFIX + cnaps.getCode());
					// 删除名称及关联
					ops.opsForZSet().remove(C.CNAPS_SUGGEST_KEY_PREFIX + fullName, cnaps.getCode());
					for (int i = 0; i < words.length; i++) {
						ops.opsForZSet().remove(C.CNAPS_SUGGEST_KEY_PREFIX + words[i], cnaps.getCode());
					}
					// 删除银行编码匹配
					ops.opsForZSet().remove(C.CNAPS_PROVIDER_KEY_PREFIX + cnaps.getProviderCode(), cnaps.getCode());
					// 删除清分行匹配
					ops.opsForZSet().remove(C.CNAPS_CLEARINGBANK_KEY_PREFIX + cnaps.getProviderCode() + "."
							+ cnaps.getClearingBankLevel(), cnaps.getCode());
					// 删除清算级别匹配
					ops.opsForZSet().remove(C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + cnaps.getClearingBankLevel(),
							cnaps.getCode());
					// 删除行政编码匹配
					ops.opsForZSet().remove(C.CNAPS_ADCODE_KEY_PREFIX + cnaps.getAdCode(), cnaps.getCode());
					return ops.exec();
				}

			};
			redisTemplate.executePipelined(callBack);

			// 检查空值key,并设置过期时间
			if (redisTemplate.boundZSetOps(C.CNAPS_SUGGEST_KEY_PREFIX + fullName).zCard() == 0) {
				redisTemplate.expire(C.CNAPS_SUGGEST_KEY_PREFIX + fullName, 10, TimeUnit.SECONDS);
			}
			
			for (int i = 0; i < words.length; i++) {
				if (redisTemplate.opsForZSet().zCard(C.CNAPS_SUGGEST_KEY_PREFIX + words[i]) == 0) {
					redisTemplate.expire(C.CNAPS_SUGGEST_KEY_PREFIX + words[i], 10, TimeUnit.SECONDS);
				}
			}
			if (redisTemplate.opsForZSet().zCard(C.CNAPS_PROVIDER_KEY_PREFIX + cnaps.getProviderCode()) == 0) {
				redisTemplate.expire(C.CNAPS_PROVIDER_KEY_PREFIX + cnaps.getProviderCode(), 10, TimeUnit.SECONDS);
			}
			if (redisTemplate.opsForZSet().zCard(C.CNAPS_CLEARINGBANK_KEY_PREFIX + cnaps.getProviderCode() + "."
					+ cnaps.getClearingBankLevel()) == 0) {
				redisTemplate.expire(
						C.CNAPS_CLEARINGBANK_KEY_PREFIX + cnaps.getProviderCode() + "." + cnaps.getClearingBankLevel(),
						10, TimeUnit.SECONDS);
			}
			if (redisTemplate.opsForZSet()
					.zCard(C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + cnaps.getClearingBankLevel()) == 0) {
				redisTemplate.expire(C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + cnaps.getClearingBankLevel(), 10,
						TimeUnit.SECONDS);
			}
			if (redisTemplate.opsForZSet().zCard(C.CNAPS_ADCODE_KEY_PREFIX + cnaps.getAdCode()) == 0) {
				redisTemplate.expire(C.CNAPS_ADCODE_KEY_PREFIX + cnaps.getAdCode(), 10, TimeUnit.SECONDS);
			}

		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}

	}

	/** 全部导出 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Cnaps> cnapsTotalQuery() {
		List<Cnaps> result = new ArrayList<Cnaps>();

		// 清分行级别
		String clearBankLevelKey1 = C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + 1;
		String clearBankLevelKey2 = C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + 2;
		String clearBankLevelKey3 = C.CNAPS_CLEARBANKLEVEL_KEY_PREFIX + -1;
		// 查找的多个key
		String[] unionTempKey = new String[] { clearBankLevelKey2, clearBankLevelKey3 };
		// 并集集合key
		String fullTempKey = C.CNAPS_SUGGEST_TEMP_KEY + ".FULL";
		try {
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.opsForZSet().unionAndStore(clearBankLevelKey1, Arrays.asList(unionTempKey), fullTempKey);
					return ops.opsForZSet().reverseRange(fullTempKey, 0, -1);
				}
			};
			List<Object> resultList = redisTemplate.executePipelined(callBack);
			redisTemplate.expire(fullTempKey, 30, TimeUnit.SECONDS);
			Set<String> cnapsCodes = (Set<String>) resultList.get(resultList.size() - 1);
			SessionCallback session = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					for (String code : cnapsCodes) {
						ops.opsForHash().multiGet(C.CNAPS_KEY_PREFIX + code, Arrays.asList(FIELDS));
					}
					return ops.exec();
				}

			};
			List<Object> cnapsL = redisTemplate.executePipelined(session);
			List<Object> cnapsT = (List<Object>) cnapsL.get(cnapsL.size() - 1);
			Iterator<String> codes = cnapsCodes.iterator();
			// 遍历联行号集合canpsl，注入bean
			for (int i = 0; i < cnapsT.size(); i++) {
				String code = codes.next();
				Cnaps cnap = new Cnaps();
				cnap.setCode(code);
				List<String> fieldValues = (List<String>) cnapsT.get(i);
				if (!fieldValues.isEmpty() && fieldValues.get(0) != null) {
					for (String value : fieldValues) {
						if ("name".equals(FIELDS[fieldValues.indexOf(value)])) {
							cnap.setName(value);
						} else if ("clearingBankCode".equals(FIELDS[fieldValues.indexOf(value)])) {
							cnap.setClearingBankCode(value);
						} else if ("clearingBankLevel".equals(FIELDS[fieldValues.indexOf(value)])) {
							cnap.setClearingBankLevel(Integer.valueOf(value));
						} else if ("providerCode".equals(FIELDS[fieldValues.indexOf(value)])) {
							cnap.setProviderCode(value);
						} else if ("adCode".equals(FIELDS[fieldValues.indexOf(value)])) {
							cnap.setAdCode(value);
						}
					}
				}
				result.add(cnap);
			}
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	/** 修改 */
	@Override
	public boolean modify(Cnaps cnaps) {
		Cnaps oldCnaps = searchByCncode(cnaps.getCode().trim());
		// 不存在的不能修改
		if (oldCnaps == null)
			return false;
		String code = oldCnaps.getCode();
		String oldname = oldCnaps.getName();
		if (StringUtils.isBlank(code) || StringUtils.isBlank(oldname))
			return false;
		try {
			del(oldCnaps);
			List<Cnaps> cnapses = new ArrayList<>();
			cnapses.add(cnaps);
			add(cnapses);
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		}
		return true;
	}

	

}
