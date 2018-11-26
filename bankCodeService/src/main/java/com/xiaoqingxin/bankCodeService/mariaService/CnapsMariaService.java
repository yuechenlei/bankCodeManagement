package com.xiaoqingxin.bankCodeService.mariaService;

import java.util.List;

import com.xiaoqingxin.bankCodeService.model.Cnaps;
import com.xiaoqingxin.bankCodeService.utils.Page;

/**
* @ClassName: CnapsMaiaService
* @Description: mariaDB服务接口
* @author Administrator
* @date 2018年10月7日
*
*/

public interface CnapsMariaService {
	
	void insert(Cnaps cnaps);

	void delete(String code);

	boolean update(Cnaps cnaps);

	/** 通过联行号查询  */
	Cnaps get(String code);

	/** 根据名字模糊查询  */
	Cnaps find(String name);
	
	/** 组合查询 */
	Page<List<Cnaps>> cnapsCombQuery(Page<List<Cnaps>> page, Cnaps cnaps);
	
	/** 批量新增 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException */
	void add(List<Cnaps> cnapses);

	/** 批量插入 */
	void batchInsert(List<Cnaps> cnapses);
}
