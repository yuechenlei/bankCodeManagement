package com.xiaoqingxin.bankCodeService.service;

import java.util.List;

import com.xiaoqingxin.bankCodeService.model.IIN;


/**
* @ClassName: IINService
* @Description: 发行者识别号码业务接口
* @author Administrator
* @date 2018年9月20日
*
 */
public interface IINService {
	/**
	 * 银行卡识别
	 * @param panNo 联行号列表
	 * @param fields 返回参数列表
	 * @return IIN
	 */
	IIN recognition(String panNo, String[] fields);

	/**
	 * 批量新增银行卡识别信息
	 * @param cnapses 银行卡识别列表
	 */
	void add(List<IIN> iins);

	/** 修改 */
	boolean modify(IIN iin);

	/** 删除 */
	void del(String id);

	/** 全部导出 */
	List<IIN> totalExport(String[] fields);

}
