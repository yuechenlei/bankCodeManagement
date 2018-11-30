package com.xiaoqingxin.bankCodeService.mapper;

import org.springframework.stereotype.Repository;

import com.xiaoqingxin.bankCodeService.model.UserRedPacket;

@Repository
public interface UserRedPacketDao {

	/**
	 * @description: 插入抢红包信息
	 * @param userRedPacket 抢红包信息
	 * @date 2018年11月28日上午10:26:44
	 * @return 影响记录数
	 *
	 */
	public int grapRedPacket(UserRedPacket userRedPacket);

}
