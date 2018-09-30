package com.xiaoqingxin.bankCodeService.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额工具类
 * @ClassName: AmountUtils
 * @Description: BigDecimal
 * @author Administrator
 * @date 2018年9月29日
 */
public class AmountUtils {

	private AmountUtils() {
	}

	/**
	 * 提供精确的加法运算
	 * 
	 * @param oneAmount
	 *            被加数
	 * @param twoAmount
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double oneAmount, double twoAmount) {
		return BigDecimal.valueOf(oneAmount).add(BigDecimal.valueOf(twoAmount)).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * 
	 * @param oneAmount
	 *            被减数
	 * @param twoAmount
	 *            减数
	 * @return 两个参数的差
	 */
	public static double subtract(double oneAmount, double twoAmount) {
		return BigDecimal.valueOf(oneAmount).subtract(BigDecimal.valueOf(twoAmount)).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * 
	 * @param oneAmount
	 *            被乘数
	 * @param twoAmount
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double multiply(double oneAmount, double twoAmount) {
		return BigDecimal.valueOf(oneAmount).multiply(BigDecimal.valueOf(twoAmount)).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入
	 * 
	 * @param oneAmount
	 *            被除数
	 * @param twoAmount
	 *            除数
	 * @return 两个参数的商
	 */
	public static double divide(double oneAmount, double twoAmount) {
		return divide(oneAmount, twoAmount, 2);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
	 * 
	 * @param oneAmount
	 *            被除数
	 * @param twoAmount
	 *            除数
	 * @param scale
	 *            精度。
	 * @return 两个参数的商
	 */
	public static double divide(double oneAmount, double twoAmount, int scale) {
		return divide(oneAmount, twoAmount, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字按取舍模式进行取舍
	 * 
	 * @param oneAmount
	 *            被除数
	 * @param twoAmount
	 *            除数
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            取舍模式
	 * @return 两个参数的商
	 */
	public static double divide(double oneAmount, double twoAmount, int scale, RoundingMode roundingMode) {
		if (scale < 0)
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		return BigDecimal.valueOf(oneAmount).divide(BigDecimal.valueOf(twoAmount), scale, roundingMode).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param amount
	 *            需要四舍五入的数字
	 * @param scale
	 *            精度
	 * @return 四舍五入后的结果
	 */
	public static double round(double amount, int scale) {
		return round(amount, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供精确的小数位取舍处理
	 * 
	 * @param amount
	 *            需要四舍五入的数字
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            取舍模式
	 * @return 四舍五入后的结果
	 */
	public static double round(double amount, int scale, RoundingMode roundingMode) {
		if (scale < 0)
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		return BigDecimal.valueOf(amount).setScale(scale, roundingMode).doubleValue();
	}

	/**
	 * 比较两个数是否相等
	 * 
	 * @param onwAmount
	 *            第一个数
	 * @param twoAmount
	 *            第二个数
	 * @return 比较结果
	 */
	public static boolean eq(double onwAmount, double twoAmount) {
		return BigDecimal.valueOf(onwAmount).compareTo(BigDecimal.valueOf(twoAmount)) == 0;
	}

	/**
	 * 比较第一个数是否比第二个数小
	 * 
	 * @param onwAmount
	 *            第一个数
	 * @param twoAmount
	 *            第二个数
	 * @return 比较结果
	 */
	public static boolean less(double onwAmount, double twoAmount) {
		return BigDecimal.valueOf(onwAmount).compareTo(BigDecimal.valueOf(twoAmount)) < 0;
	}

	/**
	 * 比较第一个数是否比第二个数大
	 * 
	 * @param onwAmount
	 *            第一个数
	 * @param twoAmount
	 *            第二个数
	 * @return 比较结果
	 */
	public static boolean greater(double onwAmount, double twoAmount) {
		return BigDecimal.valueOf(onwAmount).compareTo(BigDecimal.valueOf(twoAmount)) > 0;
	}

	/**
	 * 比较第一个数是否比第二个数小或相等
	 * 
	 * @param onwAmount
	 *            第一个数
	 * @param twoAmount
	 *            第二个数
	 * @return 比较结果
	 */
	public static boolean leq(double onwAmount, double twoAmount) {
		return BigDecimal.valueOf(onwAmount).compareTo(BigDecimal.valueOf(twoAmount)) <= 0;
	}

	/**
	 * 比较第一个数是否比第二个数大或相等
	 * 
	 * @param onwAmount
	 *            第一个数
	 * @param twoAmount
	 *            第二个数
	 * @return 比较结果
	 */
	public static boolean geq(double onwAmount, double twoAmount) {
		return BigDecimal.valueOf(onwAmount).compareTo(BigDecimal.valueOf(twoAmount)) >= 0;
	}

	/**
	 * 求一个数值的相反数
	 * 
	 * @param amount
	 *            要计算的数值
	 * @return 数值的相反数
	 */
	public static double negate(double amount) {
		return BigDecimal.valueOf(amount).negate().doubleValue();
	}
}
