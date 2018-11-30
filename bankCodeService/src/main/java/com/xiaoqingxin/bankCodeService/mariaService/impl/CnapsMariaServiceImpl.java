package com.xiaoqingxin.bankCodeService.mariaService.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoqingxin.bankCodeService.mapper.CnapsMapper;
import com.xiaoqingxin.bankCodeService.mariaService.CnapsMariaService;
import com.xiaoqingxin.bankCodeService.model.Cnaps;
import com.xiaoqingxin.bankCodeService.utils.Page;

@Service("cnapsMariaService")
public class CnapsMariaServiceImpl implements CnapsMariaService{
	private static Logger logger = LoggerFactory.getLogger(CnapsMariaServiceImpl.class);
	@Resource
	private CnapsMapper cnapsMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CachePut(value = "redisCacheManager", key = "'MARIADB_CNAPS:'+#cnaps.code") // 缓存的数据类型为字符串
	public Cnaps insert(Cnaps cnaps) {
		logger.info("method=insert(),cnaps={}",cnaps);
		cnaps.setCreateDate(new Date());
		cnaps.setLastModifyDate(new Date());
		cnaps.setVision(0);
		cnapsMapper.insert(cnaps);
		return cnaps;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CacheEvict(value = "redisCacheManager", key = "'MARIADB_CNAPS:'+#code")
	public void delete(String code) {
		logger.info("method=delete(),code={}",code);
		cnapsMapper.delete(code);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	@CachePut(value = "redisCacheManager", key = "'MARIADB_CNAPS:'+#cnaps.code")
	public Cnaps update(Cnaps cnaps) {
		logger.info("method=update(),cnaps={}",cnaps);
		try {
			cnaps.setLastModifyDate(new Date());
			int vision = cnaps.getVision();
			cnaps.setVision(++vision);
			cnapsMapper.update(cnaps);
			return cnaps;
		} catch (Exception e) {
			logger.error("",e);
			// 让事务管理器捕捉异常
			throw new RuntimeException(e);
		}
	}

	@Override
	@Cacheable(value = "redisCacheManager", key = "'MARIADB_CNAPS:'+#code")
	public Cnaps get(String code) {
		logger.info("method=get(),code={}",code);
		return cnapsMapper.get(code);
	}

	@Override
	public Cnaps find(String name) {
		logger.info("method=find(),name={}",name);
		return cnapsMapper.find(name);
	}

	@Override
	public Page<List<Cnaps>> cnapsCombQuery(Page<List<Cnaps>> page, Cnaps cnaps) {
		logger.info("method=cnapsCombQuery(),page={},cnaps={}",page,cnaps);
		List<Cnaps> list = cnapsMapper.cnapsCombQuery(cnaps);
		for(Cnaps cs : list){
			logger.info("method=cnapsMapper.cnapsCombQuery(),Cnaps={}",cs);
		}
		page.setTotalResult(list.size());
		page.setObject(list);
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void add(List<Cnaps> cnapses) {
		logger.info("批量新增开始,time="+System.currentTimeMillis());
		for(Cnaps cnaps : cnapses){
			Cnaps cs = cnapsMapper.get(cnaps.getCode());
			if(cs != null) continue;
			cnaps.setCreateDate(new Date());
			cnaps.setLastModifyDate(new Date());
			cnaps.setVision(0);
			cnapsMapper.insert(cnaps);
		}
		logger.info("批量新增结束,time="+System.currentTimeMillis());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void batchInsert(List<Cnaps> cnapses) {
		logger.info("批量插入开始,time="+System.currentTimeMillis());
		for(int i=0;i<cnapses.size();i++){
			if(cnapsMapper.get(cnapses.get(i).getCode()) != null) {
				cnapses.remove(i);
				i--;
				continue;
			}
			cnapses.get(i).setCreateDate(new Date());
			cnapses.get(i).setLastModifyDate(new Date());
		}
		cnapsMapper.batchInsert(cnapses);
		logger.info("批量插入结束,time="+System.currentTimeMillis());
	}
	

}
