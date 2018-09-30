package com.xiaoqingxin.bankCodeService.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.xiaoqingxin.bankCodeService.C;
import com.xiaoqingxin.bankCodeService.model.IIN;
import com.xiaoqingxin.bankCodeService.service.IINService;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @ClassName: IINServiceImpl
 * @Description: 发行者识别号码业务实现
 * @author Administrator
 * @date 2018年9月21日
 *
 */
@Service("iINService")
public class IINServiceImpl implements IINService {
	private static final Logger logger = LoggerFactory.getLogger(IINServiceImpl.class);
	// @Resource
	// private JedisSentinelPool jedisPool;
//	@Resource
//	private JedisPool pool;
	@SuppressWarnings("rawtypes")
	@Resource
	private RedisTemplate redisTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public IIN recognition(String cardNo, String[] fields) {
		if (cardNo.length() < 6) {
			return new IIN();
		}
		IIN iin = null;
		try {
			// 先按6位长度命中
			String iinLength6 = cardNo.substring(0, 6);
			if (!C.IIN_EXCLUDE_LENGTH6.contains(iinLength6)) {
				List<String> fieldValues = redisTemplate.opsForHash().multiGet(StringUtils
						.concatToSB(C.IIN_KEY_PREFIX, iinLength6, ".", "6", ".", String.valueOf(cardNo.length()))
						.toString(), Arrays.asList(fields));
				iin = buildIIN(fieldValues, fields);
			}
			if (iin != null && StringUtils.notBlank(iin.getProviderCode()))
				return iin;
			// 6位未命中的从10位开始到3位取出来
			for (int i = 10; i >= 3; i--) {
				if (cardNo.length() < i)
					break;
				List<String> fieldValues = redisTemplate.opsForHash().multiGet(StringUtils.concatToSB(C.IIN_KEY_PREFIX, cardNo.substring(0, i), ".",
						String.valueOf(i), ".", String.valueOf(cardNo.length())).toString(), Arrays.asList(fields));
				iin = buildIIN(fieldValues, fields);
				if (StringUtils.notBlank(iin.getProviderCode())) {
					break;
				}
			}
			return iin;
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		} 

	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(List<IIN> iins) {
		if (iins == null || iins.size() == 0)
			return;
		try {
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback() {
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					for (IIN iin : iins) {
						int index = iins.indexOf(iin);
						Map<String, String> map = new HashMap<>();
						map.put("code", iin.getCode());
						map.put("length", Integer.toString(iin.getLength()));
						map.put("panLength", Integer.toString(iin.getPanLength()));
						map.put("providerCode", iin.getProviderCode());
						map.put("cardType", iin.getCardType());
						map.put("cardName", iin.getCardName());
						map.put("agencyCode", iin.getAgencyCode());
						map.put("agencyName", iin.getAgencyName());
						ops.opsForHash().putAll(C.IIN_KEY_PREFIX + iin.getCode() + "." + iin.getLength() + "." + iin.getPanLength(),
								map);
						if ((index != 0 && index % 3000 == 0) || index == iins.size() - 1) {
							break;
						}
					}
					return ops.exec();
				}
			};
			redisTemplate.execute(callBack);
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		}catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
	}

	/*修改*/
	@SuppressWarnings("unchecked")
	@Override
	public boolean modify(IIN iin) {
		if (iin == null)
			return false;
		try {
			String id = iin.getCode() + "." + iin.getLength() + "." + iin.getPanLength();
			Object code =  redisTemplate.opsForHash().get(C.IIN_KEY_PREFIX + id, "code");
			if (code==null ||StringUtils.isBlank((String)code))
				return false;
			
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback(){
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.opsForHash().put(C.IIN_KEY_PREFIX + id, "providerCode", iin.getProviderCode());
					ops.opsForHash().put(C.IIN_KEY_PREFIX + id, "cardType", iin.getCardType());
					ops.opsForHash().put(C.IIN_KEY_PREFIX + id, "cardName", iin.getCardName());
					ops.opsForHash().put(C.IIN_KEY_PREFIX + id, "agencyCode", iin.getAgencyCode());
					ops.opsForHash().put(C.IIN_KEY_PREFIX + id, "agencyName", iin.getAgencyName());
					return ops.exec();
				}
				
			};
			redisTemplate.execute(callBack);
			return true;
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
	}

	/*删除 */
	@SuppressWarnings("unchecked")
	@Override
	public void del(String id) {
		if (StringUtils.isBlank(id))
			return;
		try {
			@SuppressWarnings("rawtypes")
			SessionCallback callBack = new SessionCallback(){
				@Override
				public Object execute(RedisOperations ops) throws DataAccessException {
					ops.multi();
					ops.opsForHash().delete(C.IIN_KEY_PREFIX + id);
					return ops.exec();
				}
			};
			redisTemplate.execute(callBack);
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IIN> totalExport(String[] fields) {
		List<IIN> iins = new ArrayList<>();
		try {
			Set<String> keys = redisTemplate.keys(C.IIN_KEY_PREFIX + "*");
			for (String key : keys) {
				List<String> fieldValues = redisTemplate.opsForHash().multiGet(key, Arrays.asList(fields));
				IIN iin = buildIIN(fieldValues, fields);
				iins.add(iin);
			}
		} catch (JedisConnectionException e) {
			logger.error("", e);
			throw e;
		} catch (Exception e) {
			logger.error("", e);
		} 
		return iins;
	}
	
	private IIN buildIIN(List<String> fieldValues, String[] fields) throws Exception {
		IIN iin = new IIN();
		if (fieldValues != null && fieldValues.size() > 0) {
			for (String value : fieldValues) {
				if (value == null) {
					continue;
				}
				String fidledName = fields[fieldValues.indexOf(value)];
				if ("length".equals(fidledName) || "panLength".equals(fidledName)) {
					PropertyUtils.setSimpleProperty(iin, fidledName, Integer.parseInt(value));
				} else {
					PropertyUtils.setSimpleProperty(iin, fidledName, value);
				}
			}
		}
		return iin;
	}

}