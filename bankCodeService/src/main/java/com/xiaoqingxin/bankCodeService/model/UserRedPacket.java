package com.xiaoqingxin.bankCodeService.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserRedPacket implements Serializable {

	private static final long serialVersionUID = -8844164217123472638L;

	private Long id;
	private Long redPacketId;
	private Long userId;
	private Double amount;
	private Timestamp grabTime;
	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(Long redPacketId) {
		this.redPacketId = redPacketId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Timestamp getGrabTime() {
		return grabTime;
	}

	public void setGrabTime(Timestamp grabTime) {
		this.grabTime = grabTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "UserRedPacket [id=" + id + ", redPacketId=" + redPacketId + ", userId=" + userId + ", amount=" + amount
				+ ", grabTime=" + grabTime + ", note=" + note + "]";
	}

}
