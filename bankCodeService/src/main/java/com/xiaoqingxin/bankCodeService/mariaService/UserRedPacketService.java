package com.xiaoqingxin.bankCodeService.mariaService;

public interface UserRedPacketService {

	/**
	 * @description: 保存抢红包信息
	 * @date 2018年11月28日上午10:46:24
	 * @param redPacketId 红包编号
	 * @param userId      抢红包用户编号
	 * @return 影响记录数
	 */
	public int grapRedPacket(Long redPacketId, Long userId);

	/** 悲观锁 */
	int grapRedPacketForUpdate(Long redPacketId, Long userId);

	/** 乐观锁(无重入) */
	public int grapRedPacketForVersion(Long redPacketId, Long userId);

	/** 乐观锁(按时间戳重入) */
	int grapRedPacketForVersionTimeRe(Long redPacketId, Long userId);

	/** 乐观锁(按次数重入) */
	int grapRedPacketForVersionCountRe(Long redPacketId, Long userId);

	/**
	 * @description 通过Redis实现抢红包
	 * @param redPacketId --红包编号
	 * @param userId      -- 用户编号
	 * @return 0-没有库存，失败 1--成功，且不是最后一个红包 2--成功，且是最后一个红包
	 */
	public Long grapRedPacketByRedis(Long redPacketId, Long userId);

}
