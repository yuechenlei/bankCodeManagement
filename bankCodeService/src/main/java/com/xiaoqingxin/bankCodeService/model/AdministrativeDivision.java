package com.xiaoqingxin.bankCodeService.model;

import java.io.Serializable;

/**
 * 行政区划
* @ClassName: AdministrativeDivision
* @Description: 行政区划
* @author Administrator
* @date 2018年9月20日
*
 */
public class AdministrativeDivision implements Serializable {
	
	private static final long serialVersionUID = -9101722097639428385L;
	
	/** 行政区划码 */
	private String adCode;
	/** 行政区划名称 */
	private String adName;

	public String getAdCode() {
		return adCode;
	}

	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public AdministrativeDivision(String adCode, String adName) {
		super();
		this.adCode = adCode;
		this.adName = adName;
	}

	public AdministrativeDivision() {}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdministrativeDivision [adCode=");
		builder.append(adCode);
		builder.append(", adName=");
		builder.append(adName);
		builder.append("]");
		return builder.toString();
	}


}
