package com.xiaoqingxin.bankCodeService.model;

import java.io.Serializable;

/**
* 乐富地区码匹配银联地区码
* @ClassName: LefuCodeToUnionPayCode
* @Description: 乐富地区码匹配银联地区码
* @author Administrator
* @date 2018年9月20日
*
 */
public class LefuCodeToUnionPayCode implements Serializable{
	
	private static final long serialVersionUID = -4646308074991627359L;
	
	/** 乐富地区码 */
	private String lefuCode;
	/** 银联地区码 */
	private String unionPayCode;
	
	public String getLefuCode() {
		return lefuCode;
	}
	public void setLefuCode(String lefuCode) {
		this.lefuCode = lefuCode;
	}
	public String getUnionPayCode() {
		return unionPayCode;
	}
	public void setUnionPayCode(String unionPayCode) {
		this.unionPayCode = unionPayCode;
	}
	
}
