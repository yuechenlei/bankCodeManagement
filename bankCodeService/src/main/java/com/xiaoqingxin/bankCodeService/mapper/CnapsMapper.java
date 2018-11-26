package com.xiaoqingxin.bankCodeService.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xiaoqingxin.bankCodeService.model.Cnaps;

@Repository
public interface CnapsMapper {
	
	void insert(Cnaps cnaps);

	void delete(String code);

	void update(Cnaps cnaps);

	Cnaps get(String code);

	Cnaps find(String name);
	
	List<Cnaps> cnapsCombQuery(@Param("cnaps")Cnaps cnaps);
	
	void batchInsert(List<Cnaps> list);

}
