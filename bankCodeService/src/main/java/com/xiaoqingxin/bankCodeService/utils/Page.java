package com.xiaoqingxin.bankCodeService.utils;

import java.io.Serializable;

/**
* @ClassName: Page
* @Description: 分页信息
* @author Administrator
* @date 2018年9月21日
*
* @param <T>
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = -4221387216744433913L;
	private int showCount = 10; // 每页显示记录数
	private int totalPage; // 总页数
	private int totalResult; // 总记录数
	private int currentPage; // 当前页
	private int currentResult; // 当前记录起始索引
	private boolean entityOrField; // true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	private T object;// 查询结果

	public int getTotalPage() {
		if (totalResult % showCount == 0) totalPage = totalResult / showCount;
		else totalPage = totalResult / showCount + 1;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	public int getCurrentPage() {
		if (currentPage <= 0) currentPage = 1;
		if (currentPage > getTotalPage()) currentPage = getTotalPage();
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}

	public int getCurrentResult() {
		currentResult = (getCurrentPage() - 1) * getShowCount();
		if (currentResult < 0) currentResult = 0;
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		if (currentResult < 0) {
			currentResult = 0;
		}
		this.currentResult = currentResult;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [showCount=");
		builder.append(showCount);
		builder.append(", totalPage=");
		builder.append(totalPage);
		builder.append(", totalResult=");
		builder.append(totalResult);
		builder.append(", currentPage=");
		builder.append(currentPage);
		builder.append(", currentResult=");
		builder.append(currentResult);
		builder.append(", entityOrField=");
		builder.append(entityOrField);
		builder.append("]");
		return builder.toString();
	}

}
