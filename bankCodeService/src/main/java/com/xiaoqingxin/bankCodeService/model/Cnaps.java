package com.xiaoqingxin.bankCodeService.model;

import java.io.Serializable;
import java.util.Date;


/**
* 联行号
* @ClassName: Cnaps
* @Description: China National Advanced Payment System
* @author Administrator
* @date 2018年9月20日
*
 */
public class Cnaps implements Serializable {
	private static final long serialVersionUID = -1668777873637582117L;
	
	/** 联行号 */
	private String code;
	/** 银行名称 */
	private String name;
	/** 清算行号 */
	private String clearingBankCode;
	/** 清算行级别 */
	private int clearingBankLevel;
	/** 接口提供方编号 (银行编码  例如： ICBC) */
	private String providerCode;
	/** 行政区划码administrativeDivisionCode */
	private String adCode;
	/** 创建日期 */
	private Date createDate;
	/** 最后修改日期 */
	private Date lastModifyDate;
	/** 版本号，记录修改次数,首次插入为0 */
	private int vision = 0;
	
	/** 创建时间起始`` */
	private String createDateStart;
	/** 创建时间结束`` */
	private String createDateEnd;
	/** 最后修改时间起始  */
	private String lastModifyDateStart;
	/** 最后修改时间结束  */
	private String lastModifyDateEnd;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClearingBankCode() {
		return clearingBankCode;
	}

	public void setClearingBankCode(String clearingBankCode) {
		this.clearingBankCode = clearingBankCode;
	}

	public String getAdCode() {
		return adCode;
	}

	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getClearingBankLevel() {
		return clearingBankLevel;
	}

	public void setClearingBankLevel(int clearingBankLevel) {
		this.clearingBankLevel = clearingBankLevel;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public int getVision() {
		return vision;
	}

	public void setVision(int vision) {
		this.vision = vision;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	
	public String getLastModifyDateStart() {
		return lastModifyDateStart;
	}

	public void setLastModifyDateStart(String lastModifyDateStart) {
		this.lastModifyDateStart = lastModifyDateStart;
	}

	public String getLastModifyDateEnd() {
		return lastModifyDateEnd;
	}

	public void setLastModifyDateEnd(String lastModifyDateEnd) {
		this.lastModifyDateEnd = lastModifyDateEnd;
	}

	@Override
	public String toString() {
		return "Cnaps [code=" + code + ", name=" + name + ", clearingBankCode=" + clearingBankCode
				+ ", clearingBankLevel=" + clearingBankLevel + ", providerCode=" + providerCode + ", adCode=" + adCode
				+ ", createDate=" + createDate + ", lastModifyDate=" + lastModifyDate + ", vision=" + vision
				+ ", createDateStart=" + createDateStart + ", createDateEnd=" + createDateEnd + ", lastModifyDateStart="
				+ lastModifyDateStart + ", lastModifyDateEnd=" + lastModifyDateEnd + "]";
	}

}
