package com.xiaoqingxin.bankCodeService.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xiaoqingxin.bankCodeService.model.RedPacket;

@Repository
public interface RedPacketDao {

	/** 查询红包信息 */
	public RedPacket getRedPacket(Long id);

	/** 扣减红包库存 */
	public int decreaseRedPacket(Long id);

	/** 悲观锁 */
	public RedPacket getRedPacketForUpdate(Long id);

	/** 扣减红包库存(乐观锁) */
	public int decreaseRedPacketForVersion(@Param("id") Long id, @Param("version") Integer version);

}
