package com.xiaoqingxin.bankCodeService.mariaService;

import com.xiaoqingxin.bankCodeService.model.RedPacket;

public interface RedPacketService {

	/**
	 * @description: 获取红包
	 * @date 2018年11月28日上午10:43:25
	 * @param id 编号
	 * @return 红包信息
	 */
	public RedPacket getRedPacket(Long id);

	/**
	 * @description: 扣减红包
	 * @date 2018年11月28日上午10:44:16
	 * @param id
	 * @return 影响条数
	 */
	public int decreaseRedPacket(Long id);

	/** 加锁查询 */
	public RedPacket getRedPacketForUpdate(Long id);

}
