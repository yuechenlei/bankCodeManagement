package com.xiaoqingxin.bankCodeService.redisService;

import java.util.List;
import java.util.Map;

import com.xiaoqingxin.bankCodeService.model.LefuCodeToUnionPayCode;

/**
* @ClassName: AdministrativeDivisionService
* @Description: 行政规划码业务接口
* @author Administrator
* @date 2018年9月20日
*
 */
public interface AdministrativeDivisionService {

	/**
	 * 根据乐富地区码查询
	 * @param lefuCode 乐富地区编码
	 * @return 乐富地区码匹配银联地区码
	 */
	LefuCodeToUnionPayCode searchLefuCodeToUnionPayCode(String lefuCode);

	/** 添加省 */
	String addProvince(String proCode, String proName);

	/** 添加市 */
	String addCity(String cityCode, String cityName, String proCode);

	/** 添加县 */
	String addCounty(String countyCode, String countyName, String cityCode);

	/** 通过code查询银联地区码信息 */
	Map<String, Object> queryByCode(String adCode, String mark);

	/** 通过name模糊查询银联地区码信息 */
	Map<String, Object> queryByName(String adName, String mark);

	/** 县升级为省辖市 (不代管) */
	void upgradeCounty(String countyCode, String cityCode);

	/** 省辖市(非代管)降级为县 */
	void degradeCity(String countyCode, String cityCode);

	/** 修改 name,code */
	void adModify(String oldCode, String oldName, String newCode, String newName);

	/** 全部导出 */
	List<Object> adTotalQuery();

	/** 更改县所属的市 */
	void modifyCounty(String countyCode, String cityCode);

	/** 更改市所属的省 */
	void modifyCity(String cityCode, String provinceCode);

	String queryAdname(String code);

}
