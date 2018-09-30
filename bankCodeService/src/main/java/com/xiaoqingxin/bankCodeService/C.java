package com.xiaoqingxin.bankCodeService;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: C
* @Description: 全局常量
* @author Administrator
* @date 2018年9月20日
*
 */
public class C {

	public static final String APP_NAME = "cachecenter";

	/** 联行号key前缀 */
	public static final String CNAPS_KEY_PREFIX = "CNAPS:";
	/** 联行号分词匹配key前缀 */
	public static final String CNAPS_SUGGEST_KEY_PREFIX = "CNAPS.SUGGEST:";
	/** 联行号分词score默认值 */
	public static final double CNAPS_SUGGEST_DEFAULT_SCORE = 1;
	/** 联行号提供方匹配key前缀 */
	public static final String CNAPS_PROVIDER_KEY_PREFIX = "CNAPS.PROVIDER:";
	/** 联行号提供方score默认值 */
	public static final double CNAPS_PROVIDER_SCORE = 1;
	/** 清分行匹配key前缀 */
	public static final String CNAPS_CLEARINGBANK_KEY_PREFIX = "CNAPS.CLEARINGBANK:";
	/** 清分行score默认值 */
	public static final double CNAPS_CLEARINGBANK_SCORE = 1;
	/** 联行号地区匹配key前缀 */
	public static final String CNAPS_ADCODE_KEY_PREFIX = "CNAPS.ADCODE:";
	/** 联行号地区score默认值 */
	public static final double CNAPS_ADCODE_KEY_SCORE = 1;
	/** 联行号分词匹配临时key */
	public static final String CNAPS_SUGGEST_TEMP_KEY = "CNAPS.TEMP";
	/** 银行名称简称匹配key */
	public static final String CNAPS_NICKNAME_KEY = "CNAPS.NICKNAME";
	/** 联行号地区匹配key前缀 */
	public static final String CNAPS_CLEARBANKLEVEL_KEY_PREFIX = "CNAPS.CLEARBANKLEVEL:";
	/** 联行号地区score默认值 */
	public static final double CNAPS_CLEARBANKLEVEL_KEY_SCORE = 1;
	/** 常用无意义字词 */
	public static final String MEANINGLESS_WORD_REGEX = "省|市|区|县|股份有限公司|分行|支行";

	/** 行政区划匹配key前缀 */
	public static final String ADCODE_KEY_PREFIX = "ADCODE:";
	/** 行政区划score默认值 */
	public static final double ADCODE_KEY_SCORE = 1;

	/** 乐富地区码匹配银联地区码匹配key前缀 */
	public static final String LEFUCODETOUNIONPAYCODE_KEY_PREFIX = "LEFECODETOUNIONPAYCODE:";
	/** 乐富地区码匹配银联地区码score默认值 */
	public static final double LEFUCODETOUNIONPAYCODE_KEY_SCORE = 1;

	/** 发行者识别号码key前缀 */
	public static final String IIN_KEY_PREFIX = "IIN:";

	/** 商户信息key前缀 */
	public static final String PARTNER_KEY_PREFIX = "PARTNER:";

	/** 发行者识别号码按6位长度判断需排除的列表 */
	public static final List<String> IIN_EXCLUDE_LENGTH6 = new ArrayList<>();
	static {
		IIN_EXCLUDE_LENGTH6.add("685800");
		IIN_EXCLUDE_LENGTH6.add("622498");
		IIN_EXCLUDE_LENGTH6.add("940046");
		IIN_EXCLUDE_LENGTH6.add("900105");
		IIN_EXCLUDE_LENGTH6.add("900205");
		IIN_EXCLUDE_LENGTH6.add("622319");
		IIN_EXCLUDE_LENGTH6.add("620108");
	}

	/** 县key前缀 (code-->name) */
	public static final String AD_COUNTY_CODE_NAME_KEY_PREFIX = "AD.COUNTY.CODE.NAME:";

	/** 市key前缀 (code-->name) */
	public static final String AD_CITY_CODE_NAME_KEY_PREFIX = "AD.CITY.CODE.NAME:";

	/** 省key前缀 (code-->name) */
	public static final String AD_PROVINCE_CODE_NAME_KEY_PREFIX = "AD.PROVINCE.CODE.NAME:";

	/** name-->code前缀 */
	public static final String AD_NAME_CODE_KEY_PREFIX = "AD.NAME.CODE:";

	/** 县-->市 关系key前缀 */
	public static final String AD_COUNTY_CITY_KEY_PREFIX = "AD.COUNTY.CITY:";

	/** 市-->县 关系key前缀 */
	public static final String AD_CITY_COUNTY_KEY_PREFIX = "AD.CITY.COUNTY:";

	/** 市-->省 关系key前缀 */
	public static final String AD_CITY_PROVINCE_KEY_PREFIX = "AD.CITY.PROVINCE:";

	/** 省-->市 关系key前缀 */
	public static final String AD_PROVINCE_CITY_KEY_PREFIX = "AD.PROVINCE.CITY:";

	/** 乐富_省级地区码匹配key前缀 */
	public static final String LEFU_PRO_REGION_CODE_KEY_PREFIX = "LEFU.PRO.REGION.CODE:";

	/** 乐富_市级地区码匹配key前缀 */
	public static final String LEFU_CITY_REGION_CODE_KEY_PREFIX = "LEFU.CITY.REGION.CODE:";

	/** 乐富_省-市地区码关系key前缀 */
	public static final String LEFU_PRO_CITY_REGION_CODE_KEY_PREFIX = "LEFU.PRO.CITY.REGION.CODE:";

	/** 乐富_市-省地区码关系key前缀 */
	public static final String LEFU_CITY_PRO_REGION_CODE_KEY_PREFIX = "LEFU.CITY.PRO.REGION.CODE:";

}
