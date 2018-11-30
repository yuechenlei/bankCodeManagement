package com.xiaoqingxin.bankCodeService.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class RedPacket implements Serializable {

	private static final long serialVersionUID = 8677917941320150620L;

	private Long id;
	private Long userId;
	private Double amount;
	private Timestamp sendDate;
	private Integer total;
	private Double unitAmount;
	private Integer stock;
	private Integer version;
	private String note;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Double getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(Double unitAmount) {
		this.unitAmount = unitAmount;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "RedPacket [id=" + id + ", userId=" + userId + ", amount=" + amount + ", sendDate=" + sendDate
				+ ", total=" + total + ", unitAmount=" + unitAmount + ", stock=" + stock + ", version=" + version
				+ ", note=" + note + "]";
	}

}
