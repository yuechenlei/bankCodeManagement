package com.xiaoqingxin.bankCodeService.redisService;

import java.util.List;

import com.xiaoqingxin.bankCodeService.model.Cnaps;
import com.xiaoqingxin.bankCodeService.utils.Page;


/**
* @ClassName: CnapsService
* @Description: 联行号业务接口
* @author Administrator
* @date 2018年9月20日
*
 */
public interface CnapsService {
	/**
	 * 批量新增联行号信息
	 * @param cnapses 联行号列表
	 */
	void add(List<Cnaps> cnapses);


	/**
	 * 根据联行号查询相关信息
	 * @Description 一句话描述方法用法
	 * @param code
	 * @return
	 * @see 需要参考的类或方法
	 */
	Cnaps searchByCncode(String code);

	/**
	 * 组合查询
	 * @Description 一句话描述方法用法
	 * @param cnaps
	 * @param fields
	 * @return
	 * @see 需要参考的类或方法
	 */
	Page<List<Cnaps>> cnapsCombQuery(Page<List<Cnaps>> page, Cnaps cnaps);

	void del(Cnaps cnaps);

	List<Cnaps> cnapsTotalQuery();

	/** 修改 */
	boolean modify(Cnaps cnaps);
	
	
}
