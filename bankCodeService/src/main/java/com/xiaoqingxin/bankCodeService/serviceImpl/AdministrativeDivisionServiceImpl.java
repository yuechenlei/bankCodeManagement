package com.xiaoqingxin.bankCodeService.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xiaoqingxin.bankCodeService.C;
import com.xiaoqingxin.bankCodeService.model.LefuCodeToUnionPayCode;
import com.xiaoqingxin.bankCodeService.service.AdministrativeDivisionService;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @ClassName: AdministrativeDivisionServiceImpl
 * @Description: 银联地区码业务实现
 * @author Administrator
 * @date 2018年9月20日
 *
 */
@Service("administrativeDivisionService")
public class AdministrativeDivisionServiceImpl implements AdministrativeDivisionService {

	private static final Logger logger = LoggerFactory.getLogger(AdministrativeDivisionServiceImpl.class);
	// @Resource
	// private JedisSentinelPool jedisPool;
	@Resource
	private JedisPool pool;

	@SuppressWarnings("unchecked")
	@Override
	public LefuCodeToUnionPayCode searchLefuCodeToUnionPayCode(String lefuCode) {
		LefuCodeToUnionPayCode lp = new LefuCodeToUnionPayCode();
		Jedis jedis = pool.getResource();
		try {
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
//			Transaction transaction = jedis.multi();
			pipe.hgetAll(C.LEFUCODETOUNIONPAYCODE_KEY_PREFIX + lefuCode);
			pipe.exec();
			List<Object> execResult = pipe.syncAndReturnAll();
			Map<String, String> fieldValues = (Map<String, String>) execResult.get(execResult.size()-1);
			lp.setLefuCode(fieldValues.get("lefuCode"));
			lp.setUnionPayCode(fieldValues.get("unionPayCode"));
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return lp;
	}

	/*添加省*/
	@Override
	public String addProvince(String proCode, String proName) {
		if (proCode.equals("") || proName.equals("") || proCode == null || proName == null)
			return null;
		Jedis jedis = pool.getResource();
		try {
			Set<String> oldCodes = jedis.smembers(C.AD_NAME_CODE_KEY_PREFIX + proName);
			String cityName = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + proCode);
			if (StringUtils.isNotBlank(cityName)) {
				return "已存在市，" + cityName + "," + proCode + " ---- ";
			}
			String countyName = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + proCode);
			if (StringUtils.isNotBlank(countyName)) {
				return "已存在县，" + countyName + "," + proCode + " ---- ";
			}
			String oldName = jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + proCode);
			if (StringUtils.isNotBlank(oldName)) {
				if (!oldName.equals(proName)) {
					return "省code已存在，请执行修改，" + proName + "," + proCode + " ---- ";
				}
				for (String oldCode : oldCodes) {
					if (proCode.equals(oldCode) && proName.equals(oldName)) {
						return "系统已存在该省，" + proName + "," + proCode + " ---- ";
					}
				}
			}
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
//			Transaction transaction = jedis.multi();
			pipe.set(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + proCode, proName);
			pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + proName, proCode);
			pipe.exec();
			pipe.sync();
			return null;
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	/*添加市*/
	@Override
	public String addCity(String cityCode, String cityName, String proCode) {
		if (StringUtils.isEmpty(cityCode) || StringUtils.isEmpty(cityName) || StringUtils.isEmpty(proCode))
			return null;
		Jedis jedis = pool.getResource();
		try {
			Set<String> oldCodes = jedis.smembers(C.AD_NAME_CODE_KEY_PREFIX + cityName);
			String countyName = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + cityCode);
			if (StringUtils.isNotBlank(countyName)) {
				return "已存在县，" + countyName + "," + cityCode + " ---- ";
			}
			String provinceName = jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + cityCode);
			if (StringUtils.isNotBlank(provinceName)) {
				return "已存在省，" + provinceName + "," + cityCode + " ---- ";
			}
			String oldName = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode);
			if (StringUtils.isNotBlank(oldName)) {
				if (!cityName.equals(oldName)) {
					return "市code已存在，请执行修改，" + cityName + "," + cityCode + "," + proCode + " ---- ";
				}
				for (String oldCode : oldCodes) {
					if (cityCode.equals(oldCode) && cityName.equals(oldName)) {
						return "系统已存在该市，" + cityName + "," + cityCode + " ---- ";
					}
				}
			}
			String proName = jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + proCode);
			if (StringUtils.isBlank(proName)) {
				return "系统不存在该省，code=" + proCode + " ---- ";
			}
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
//			Transaction transaction = jedis.multi();
			pipe.set(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode, cityName);
			pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + cityName, cityCode);
			pipe.set(C.AD_CITY_PROVINCE_KEY_PREFIX + cityCode, proCode);
			pipe.zadd(C.AD_PROVINCE_CITY_KEY_PREFIX + proCode, C.ADCODE_KEY_SCORE, cityCode);
			pipe.exec();
			pipe.sync();
			return null;
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/*添加县*/
	@Override
	public String addCounty(String countyCode, String countyName, String cityCode) {
		if (StringUtils.isEmpty(countyCode) || StringUtils.isEmpty(countyName) || StringUtils.isEmpty(cityCode))
			return null;
		Jedis jedis = pool.getResource();
		try {
			Set<String> oldCodes = jedis.smembers(C.AD_NAME_CODE_KEY_PREFIX + countyName);
			String city_Name = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + countyCode);
			if (StringUtils.isNotBlank(city_Name)) {
				return "已存在市，" + city_Name + "," + countyCode + " ---- ";
			}
			String proName = jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + countyCode);
			if (StringUtils.isNotBlank(proName)) {
				return "已存在省，" + proName + "," + countyCode + " ---- ";
			}
			String oldName = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode);

			if (StringUtils.isNotBlank(oldName)) {
				if (!countyName.equals(oldName)) {
					return "县code已存在，请执行修改，" + countyName + "," + countyCode + "," + cityCode + " ---- ";
				}
				for (String oldCode : oldCodes) {
					if (countyCode.equals(oldCode) && countyName.equals(oldName)) {
						return "系统已存在该县，" + countyName + "," + countyCode + " ---- ";
					}
				}

			}
			String cityName = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode);
			if (StringUtils.isBlank(cityName)) {
				return "系统不存在该市，code=" + cityCode + " ---- ";
			}

			Pipeline pipe = jedis.pipelined();
			pipe.multi();
//			Transaction transaction = jedis.multi();
			pipe.set(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode, countyName);
			pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + countyName, countyCode);
			pipe.set(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode, cityCode);
			pipe.zadd(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, C.ADCODE_KEY_SCORE, countyCode);
			pipe.exec();
			pipe.sync();
			return null;
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/** 通过code查询银联地区码信息 */
	@Override
	public Map<String, Object> queryByCode(String adCode, String mark) {
		if (StringUtils.isEmpty(adCode) || StringUtils.isEmpty(mark))
			return null;
		Map<String, Object> adMap = new LinkedHashMap<String, Object>();
		Jedis jedis = pool.getResource();
		try {
			String keyPrefix = discerns(adCode);
			if (keyPrefix == null)
				return adMap;

			adMap.put("code", adCode);
			adMap.put("mark", mark);
			// 如果是一个县
			if (keyPrefix.equals(C.AD_COUNTY_CODE_NAME_KEY_PREFIX)) {
				adMap.put("name", jedis.get(keyPrefix + adCode));
				adMap.put("genre", "county");
				if (mark.equals("higher")) {
					String higherCode = jedis.get(C.AD_COUNTY_CITY_KEY_PREFIX + adCode);
					adMap.put("higherCode", higherCode);
					adMap.put("higherName", jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + higherCode));
				}
			} else if (keyPrefix.equals(C.AD_CITY_CODE_NAME_KEY_PREFIX)) { // 如果是一个市(地区市或省辖)
				adMap.put("name", jedis.get(keyPrefix + adCode));
				adMap.put("genre", "city");
				if (mark.equals("higher")) {
					String higherCode = jedis.get(C.AD_CITY_PROVINCE_KEY_PREFIX + adCode);
					adMap.put("higherCode", higherCode);
					adMap.put("higherName", jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + higherCode));
				} else {
					Set<String> lowerCodeSet = jedis.zrange(C.AD_CITY_COUNTY_KEY_PREFIX + adCode, 0, -1);
					Map<String, Object> lowerMap = new LinkedHashMap<String, Object>();
					for (String lowerCode : lowerCodeSet) {
						String lowerName = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + lowerCode);
						lowerMap.put(lowerCode, lowerName);
					}
					adMap.put("lowerMap", lowerMap);
				}
			} else if (keyPrefix.equals(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX)) { // 如果是一个省或(省级)直辖市
				adMap.put("name", jedis.get(keyPrefix + adCode));
				if (mark.equals("lower")) {
					Set<String> lowerCodeSet = jedis.zrange(C.AD_PROVINCE_CITY_KEY_PREFIX + adCode, 0, -1);
					Map<String, Object> lowerMap = new LinkedHashMap<String, Object>();
					for (String lowerCode : lowerCodeSet) {
						String lowerName = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + lowerCode);
						lowerMap.put(lowerCode, lowerName);
					}
					adMap.put("lowerMap", lowerMap);
				}
			}

		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return adMap;
	}

	/** 通过name模糊查询银联地区码信息 */
	@Override
	public Map<String, Object> queryByName(String adName, String mark) {
		if (StringUtils.isEmpty(adName))
			return null;
		Map<String, Object> result = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		result.put("mark", mark);
		Jedis jedis = pool.getResource();
		try {
			Set<String> keySet = jedis.keys(C.AD_NAME_CODE_KEY_PREFIX + adName + "*");
			if (!keySet.isEmpty()) {
				for (String key : keySet) {
					Set<String> codes = jedis.smembers(key);
					for (String code : codes) {
						list.add(queryByCode(code, mark));
					}
				}
			}
			result.put("adList", list);
		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return result;
	}

	/*
	 * 县升级为省辖市(非代管)
	 */
	@Override
	public void upgradeCounty(String countyCode, String cityCode) {
		Jedis jedis = pool.getResource();
		try {
			if (jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode) == null) {
				throw new RuntimeException("不存在该县,code=" + countyCode);
			} else if (jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode) == null) {
				throw new RuntimeException("不存在该市,code=" + cityCode);
			}
			String oldCityCode = jedis.get(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode);
			if (!cityCode.equals(oldCityCode)) {
				throw new RuntimeException(countyCode + "与" + cityCode + "不存在县市关系");
			}

			String provinceCode = jedis.get(C.AD_CITY_PROVINCE_KEY_PREFIX + cityCode);
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
//			Transaction transaction = jedis.multi();
			// 删除县市关系
			pipe.del(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode);
			pipe.zrem(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, countyCode);
			// 添加省市关系
			pipe.set(C.AD_CITY_PROVINCE_KEY_PREFIX + countyCode, provinceCode);
			pipe.zadd(C.AD_PROVINCE_CITY_KEY_PREFIX + provinceCode, C.ADCODE_KEY_SCORE, countyCode);
			// 修改KEY
			pipe.rename(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode,
					C.AD_CITY_CODE_NAME_KEY_PREFIX + countyCode);
			pipe.exec();
			pipe.sync();
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/*
	 * 省辖市(非代管)降级为县
	 * 
	 * @param countyCode 降级为县
	 * 
	 * @param cityCode 把它归到的市
	 */
	@Override
	public void degradeCity(String countyCode, String cityCode) {
		Jedis jedis = pool.getResource();
		try {
			if (jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + countyCode) == null) {
				throw new RuntimeException("不存在该市，code=" + countyCode);
			}
			if (jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode) == null) {
				throw new RuntimeException("不存在该市，code=" + cityCode);
			}
			String count = jedis.zcard(C.AD_CITY_COUNTY_KEY_PREFIX + countyCode).toString();
			if (!count.equals("0")) {
				throw new RuntimeException("该省辖市(" + countyCode + ")有下属县，请先处理其下属县的归属");
			}
			String provinceCode = jedis.get(C.AD_CITY_PROVINCE_KEY_PREFIX + cityCode);
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
//			Transaction transaction = jedis.multi();
			// 删除省市关系
			pipe.del(C.AD_CITY_PROVINCE_KEY_PREFIX + countyCode);
			pipe.zrem(C.AD_PROVINCE_CITY_KEY_PREFIX + provinceCode, countyCode);
			// 添加县市关系
			pipe.set(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode, cityCode);
			pipe.zadd(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, C.ADCODE_KEY_SCORE, countyCode);
			// 修改KEY
			pipe.rename(C.AD_CITY_CODE_NAME_KEY_PREFIX + countyCode,
					C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode);
			pipe.exec();
			pipe.sync();

		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/* 修改name,code*/
	@Override
	public void adModify(String oldCode, String oldName, String newCode, String newName) {
		Jedis jedis = pool.getResource();
		try {
			String keyPrefix = discerns(oldCode);
			if (keyPrefix == null)
				return;
			boolean modifyName = !oldName.equals(newName);
			// 如果修改的是name
			if (modifyName) {
				long count = jedis.scard(C.AD_NAME_CODE_KEY_PREFIX + oldName);
				boolean existNewName = jedis.exists(C.AD_NAME_CODE_KEY_PREFIX + newName);
				Pipeline pipe = jedis.pipelined();
				pipe.multi();
//				Transaction transaction = jedis.multi();
				if (count == 1) {
					if (existNewName) {
						pipe.del(C.AD_NAME_CODE_KEY_PREFIX + oldName);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + newName, oldCode);
					} else {
						pipe.rename(C.AD_NAME_CODE_KEY_PREFIX + oldName, C.AD_NAME_CODE_KEY_PREFIX + newName);
					}
				} else if (count > 1) {
					pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + oldName, oldCode);
					pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + newName, oldCode);
				}
				pipe.set(keyPrefix + oldCode, newName);
				pipe.exec();
				pipe.sync();
			}

			// 如果修改的是code
			if (!oldCode.equals(newCode)) {
				// 如果是县
				if (keyPrefix.equals(C.AD_COUNTY_CODE_NAME_KEY_PREFIX)) {
					isExist(newCode);
					String cityCode = jedis.get(C.AD_COUNTY_CITY_KEY_PREFIX + oldCode);
					Pipeline pipe = jedis.pipelined();
					pipe.multi();
//					Transaction transaction = jedis.multi();
					pipe.rename(keyPrefix + oldCode, keyPrefix + newCode);
					if (modifyName) {
						pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + newName, oldCode);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + newName, newCode);
					} else {
						pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + oldName, oldCode);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + oldName, newCode);
					}

					pipe.zrem(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, oldCode);
					pipe.zadd(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, C.ADCODE_KEY_SCORE, newCode);
					pipe.rename(C.AD_COUNTY_CITY_KEY_PREFIX + oldCode, C.AD_COUNTY_CITY_KEY_PREFIX + newCode);
					pipe.exec();
					pipe.sync();
				} else if (keyPrefix.equals(C.AD_CITY_CODE_NAME_KEY_PREFIX)) {// 如果是市
					isExist(newCode);
					String provinceCode = jedis.get(C.AD_CITY_PROVINCE_KEY_PREFIX + oldCode);
					Set<String> countyCodes = jedis.zrange(C.AD_CITY_COUNTY_KEY_PREFIX + oldCode, 0, -1);
					Pipeline pipe = jedis.pipelined();
					pipe.multi();
//					Transaction transaction = jedis.multi();
					pipe.rename(keyPrefix + oldCode, keyPrefix + newCode);
					if (modifyName) {
						pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + newName, oldCode);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + newName, newCode);
					} else {
						pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + oldName, oldCode);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + oldName, newCode);
					}

					pipe.zrem(C.AD_PROVINCE_CITY_KEY_PREFIX + provinceCode, oldCode);
					pipe.zadd(C.AD_PROVINCE_CITY_KEY_PREFIX + provinceCode, C.ADCODE_KEY_SCORE, newCode);
					pipe.rename(C.AD_CITY_PROVINCE_KEY_PREFIX + oldCode,
							C.AD_CITY_PROVINCE_KEY_PREFIX + newCode);

					for (String countyCode : countyCodes) {
						pipe.set(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode, newCode);
					}
					pipe.rename(C.AD_CITY_COUNTY_KEY_PREFIX + oldCode, C.AD_CITY_COUNTY_KEY_PREFIX + newCode);
					pipe.exec();
					pipe.sync();
				} else if (keyPrefix.equals(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX)) { // 如果是省
					isExist(newCode);
					Set<String> cityCodes = jedis.zrange(C.AD_PROVINCE_CITY_KEY_PREFIX + oldCode, 0, -1);
					Pipeline pipe = jedis.pipelined();
					pipe.multi();
//					Transaction transaction = jedis.multi();
					pipe.rename(keyPrefix + oldCode, keyPrefix + newCode);
					if (modifyName) {
						pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + newName, oldCode);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + newName, newCode);
					} else {
						pipe.srem(C.AD_NAME_CODE_KEY_PREFIX + oldName, oldCode);
						pipe.sadd(C.AD_NAME_CODE_KEY_PREFIX + oldName, newCode);
					}

					for (String cityCode : cityCodes) {
						pipe.set(C.AD_CITY_PROVINCE_KEY_PREFIX + cityCode, newCode);
					}
					pipe.rename(C.AD_PROVINCE_CITY_KEY_PREFIX + oldCode,
							C.AD_PROVINCE_CITY_KEY_PREFIX + newCode);
					pipe.exec();
					pipe.sync();
				}

			}

		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/*全部导出*/
	@Override
	public List<Object> adTotalQuery() {
		Jedis jedis = pool.getResource();
		List<Object> proList = new ArrayList<Object>();
		try {
			Set<String> proKeySet = jedis.keys(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + "*");
			List<String> proCodes = new ArrayList<String>();
			for (String proKey : proKeySet) {
				String proCode = proKey.substring(proKey.indexOf(":") + 1, proKey.length());
				if (!proCode.equals("")) {
					proCodes.add(proCode);
				}
			}
			for (String proCode : proCodes) {
				String proName = jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + proCode);
				Map<String, Object> proMap = new LinkedHashMap<String, Object>();
				proMap.put("proName", proName);
				proMap.put("proCode", proCode);
				proList.add(proMap);

				List<Object> cityList = new ArrayList<Object>();
				Set<String> cityCodes = jedis.zrange(C.AD_PROVINCE_CITY_KEY_PREFIX + proCode, 0, -1);
				for (String cityCode : cityCodes) {
					String cityName = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode);
					Map<String, Object> cityMap = new LinkedHashMap<String, Object>();
					cityMap.put("cityName", cityName);
					cityMap.put("cityCode", cityCode);
					cityList.add(cityMap);

					Set<String> countyCodes = jedis.zrange(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, 0, -1);
					Map<String, String> couMap = new LinkedHashMap<String, String>();
					for (String countyCode : countyCodes) {
						String countyName = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode);
						couMap.put(countyCode, countyName);

					}
					proMap.put("cityList", cityList);
					cityMap.put("couMap", couMap);

				}
			}

		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return proList;
	}

	/** 辨别属于县/市/省... */
	public String discerns(String code) {
		Jedis jedis = pool.getResource();
		try {
			if (jedis.exists(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + code)) {
				return C.AD_COUNTY_CODE_NAME_KEY_PREFIX;
			}
			if (jedis.exists(C.AD_CITY_CODE_NAME_KEY_PREFIX + code)) {
				return C.AD_CITY_CODE_NAME_KEY_PREFIX;
			}
			if (jedis.exists(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + code)) {
				return C.AD_PROVINCE_CODE_NAME_KEY_PREFIX;
			}

		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/** 是否已经存在 */
	public void isExist(String newCode) {
		Jedis jedis = pool.getResource();
		try {
			if (jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + newCode) != null) {
				throw new RuntimeException("已存在县code=" + newCode);
			} else if (jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + newCode) != null) {
				throw new RuntimeException("已存在市code=" + newCode);
			} else if (jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + newCode) != null) {
				throw new RuntimeException("已存在省code=" + newCode);
			}

		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public String getProvinceCode(String code) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.get(C.AD_CITY_PROVINCE_KEY_PREFIX + code);
		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	public String getCityCode(String code) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.get(C.AD_COUNTY_CITY_KEY_PREFIX + code);
		} catch (JedisConnectionException e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

	/*更改县所属的市*/
	@Override
	public void modifyCounty(String countyCode, String cityCode) {
		Jedis jedis = pool.getResource();
		try {
			if (jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + countyCode) == null) {
				throw new RuntimeException("不存在该县，code=" + countyCode);
			}
			if (jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode) == null) {
				throw new RuntimeException("不存在该市，code=" + cityCode);
			}
			String oldCityCode = jedis.get(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode);
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
			pipe.zrem(C.AD_CITY_COUNTY_KEY_PREFIX + oldCityCode, countyCode);
			pipe.zadd(C.AD_CITY_COUNTY_KEY_PREFIX + cityCode, C.ADCODE_KEY_SCORE, countyCode);
			pipe.set(C.AD_COUNTY_CITY_KEY_PREFIX + countyCode, cityCode);
			pipe.exec();
			pipe.sync();
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/*更改市所属的省*/
	@Override
	public void modifyCity(String cityCode, String provinceCode) {
		Jedis jedis = pool.getResource();
		try {
			if (jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + cityCode) == null) {
				throw new RuntimeException("不存在该市，code=" + cityCode);
			}
			if (jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + provinceCode) == null) {
				throw new RuntimeException("不存在该省，code=" + provinceCode);
			}

			String oldProCode = jedis.get(C.AD_CITY_PROVINCE_KEY_PREFIX + cityCode);
			Pipeline pipe = jedis.pipelined();
			pipe.multi();
			pipe.zrem(C.AD_PROVINCE_CITY_KEY_PREFIX + oldProCode, cityCode);
			pipe.zadd(C.AD_PROVINCE_CITY_KEY_PREFIX + provinceCode, C.ADCODE_KEY_SCORE, cityCode);
			pipe.set(C.AD_CITY_PROVINCE_KEY_PREFIX + cityCode, provinceCode);
			pipe.exec();
			pipe.sync();
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/** 查询名称 */
	@Override
	public String queryAdname(String code) {
		Jedis jedis = pool.getResource();
		String name = null;
		try {
			name = jedis.get(C.AD_COUNTY_CODE_NAME_KEY_PREFIX + code);
			if (name == null) {
				name = jedis.get(C.AD_CITY_CODE_NAME_KEY_PREFIX + code);
			}
			if (name == null) {
				name = jedis.get(C.AD_PROVINCE_CODE_NAME_KEY_PREFIX + code);
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return name;
	}

}
