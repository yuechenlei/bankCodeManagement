package com.xiaoqingxin.bankCodeService.model;

import java.io.Serializable;

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
	/** 清算行号 */
	private String clearingBankCode;
	/** 行政区划码administrativeDivisionCode */
	private String adCode;
	/** 银行名称 */
	private String name;
	/** 清算行级别 */
	private int clearingBankLevel;
	/** 接口提供方编号 (银行编码) */
	private String providerCode;

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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cnaps [code=");
		builder.append(code);
		builder.append(", clearingBankCode=");
		builder.append(clearingBankCode);
		builder.append(", adCode=");
		builder.append(adCode);
		builder.append(", name=");
		builder.append(name);
		builder.append(", clearingBankLevel=");
		builder.append(clearingBankLevel);
		builder.append(", providerCode=");
		builder.append(providerCode);
		builder.append("]");
		return builder.toString();
	}

}
