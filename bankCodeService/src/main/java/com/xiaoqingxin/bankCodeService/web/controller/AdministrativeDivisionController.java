package com.xiaoqingxin.bankCodeService.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.xiaoqingxin.bankCodeService.redisService.AdministrativeDivisionService;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;


/**
* @ClassName: AdministrativeDivisionController
* @Description: 银联地区码访问控制器
* @author Administrator
* @date 2018年9月21日
*
 */
@Controller
@RequestMapping("/ad")
public class AdministrativeDivisionController {
	private static final Logger logger = LoggerFactory.getLogger(AdministrativeDivisionController.class);
	@Resource
	private AdministrativeDivisionService administrativeDivisionService;

	/** 银联地区码查询页面跳转 */
	@RequestMapping("/adQuery/index")
	public ModelAndView toAdQuery(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		if (request.getSession().getAttribute("user") == null) {
			mv.setViewName("redirect:/toLogin.htm");
			return mv;
		}
		String user = request.getSession().getAttribute("user").toString();
		logger.info("method=toAdQuery(),user={}", user);
		mv.addObject("user", user);
		mv.setViewName("ad/adQuery");
		return mv;
	}

	/** 银联地区码查询 */
	@RequestMapping("adQuery")
	public ModelAndView adQuery(HttpServletRequest request, @RequestParam("adCode") String adCode, @RequestParam("adName") String adName,
			@RequestParam("mark") String mark) {
		ModelAndView mv = new ModelAndView();
		if (request.getSession().getAttribute("user") == null) {
			mv.setViewName("error/nopermit");
			return mv;
		}
		String user = request.getSession().getAttribute("user").toString();
		logger.info("method=adQuery(),adCode={},adName={},mark={},user={}", adCode, adName, mark, user);
		mv.addObject("user", user);
		mv.setViewName("ad/adQueryResult");

		adCode = adCode.trim();
		adName = adName.trim();
		mark = mark.trim();
		try {
			if (StringUtils.notBlank(adCode) && StringUtils.notBlank(mark)) {
				Map<String, Object> adl = administrativeDivisionService.queryByCode(adCode, mark);
				if (!adl.isEmpty()) {
					mv.addObject("adl", adl);
					return mv;
				}
			} else if (StringUtils.notBlank(adName) && StringUtils.notBlank(mark)) {
				Map<String, Object> adMap = administrativeDivisionService.queryByName(adName, mark);
				mv.setViewName("ad/adQueryResultForName");
				mv.addObject("adMap", adMap);
				return mv;
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		mv.addObject("adl", null);
		return mv;

	}

	/** 添加省 */
	@RequestMapping("addProvince")
	@ResponseBody
	public Map<String, Object> addProvince(@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "provinceTxt", required = false) MultipartFile txt, @RequestParam(value = "provinceText", required = false) String text,
			@RequestParam(value = "provinceExcel", required = false) MultipartFile provinceExcel) {
		logger.info("method=addProvince(),text={},txt={},provinceExcel={}", text, txt.getOriginalFilename(), provinceExcel.getOriginalFilename());
		Map<String, Object> msg = new HashMap<String, Object>();
		List<String> errorMsg = new ArrayList<>();

		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) {
				rows.addAll(Arrays.asList(text.split("\r\n")));
			}
			if (txt != null && !"".equals(txt.getOriginalFilename())) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()), 1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (provinceExcel != null && !"".equals(provinceExcel.getOriginalFilename())) {
				readExcel(provinceExcel, rows, msg, errorMsg);
				if (!msg.isEmpty()) return msg;
			}

			if (rows.isEmpty()) {
				msg.put("result", "fail");
				msg.put("errorMsg", "没有输入任何有效数据");
				return msg;
			}

			if (rows.size() > 50) {
				msg.put("result", "fail");
				msg.put("errorMsg", "一次添加量不能超过50");
				return msg;
			}

			for (String row : rows) {
				String[] proStrArray = row.split(separator);
				if (proStrArray.length != 2) {
					errorMsg.add("长度为2,且中间须以英文逗号分割:" + row + " ---- ");
					continue;
				}

				String proName = proStrArray[0].trim();
				String proCode = proStrArray[1].trim();
				if (!proCode.matches("^\\d{4}$")) {
					errorMsg.add("区划码必须为4位数字:" + proStrArray[1] + " ---- ");
					continue;
				}
				if (StringUtils.isBlank(proName)) {
					errorMsg.add("名称不能为空:" + proStrArray[0] + "," + proStrArray[1] + " ---- ");
					continue;
				}
				String result = administrativeDivisionService.addProvince(proCode, proName);
				if (result != null) {
					errorMsg.add(result);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		msg.put("result", "success");
		msg.put("errorMsg", errorMsg);
		return msg;
	}

	/** 添加市 */
	@RequestMapping("addCity")
	@ResponseBody
	public Map<String, Object> addCity(@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "cityTxt", required = false) MultipartFile txt, @RequestParam(value = "cityText", required = false) String text,
			@RequestParam(value = "cityExcel", required = false) MultipartFile cityExcel) {
		logger.info("method=addCity(),text={},txt={},cityExcel={}", text, txt.getOriginalFilename(), cityExcel.getOriginalFilename());
		Map<String, Object> msg = new HashMap<String, Object>();
		List<String> errorMsg = new ArrayList<String>();

		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) {
				rows.addAll(Arrays.asList(text.split("\r\n")));
			}
			if (txt != null && !"".equals(txt.getOriginalFilename())) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()), 1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (cityExcel != null && !"".equals(cityExcel.getOriginalFilename())) {
				readExcel(cityExcel, rows, msg, errorMsg);
				if (!msg.isEmpty()) return msg;
			}

			if (rows.isEmpty()) {
				msg.put("result", "fail");
				msg.put("errorMsg", "没有输入任何有效数据");
				return msg;
			}

			if (rows.size() > 500) {
				msg.put("result", "fail");
				msg.put("errorMsg", "一次添加量不能超过500");
				return msg;
			}

			for (String row : rows) {
				String[] cityStrArray = row.split(separator);
				if (cityStrArray.length != 3) {
					errorMsg.add("长度为3,且中间须以英文逗号分割:" + row + " ---- ");
					continue;
				}

				String cityName = cityStrArray[0].trim();
				String cityCode = cityStrArray[1].trim();
				String proCode = cityStrArray[2].trim();
				if (!cityCode.matches("^\\d{4}$")) {
					errorMsg.add("区划码必须为4位数字:" + cityStrArray[1] + " ---- ");
					continue;
				}
				if (!proCode.matches("^\\d{4}$")) {
					errorMsg.add("区划码必须为4位数字:" + cityStrArray[2] + " ---- ");
					continue;
				}
				if (cityCode.equals(proCode)) {
					errorMsg.add("市区划码与省区划码不能相同:" + cityStrArray[1] + "," + cityStrArray[2] + " ---- ");
					continue;
				}
				if (StringUtils.isBlank(cityName)) {
					errorMsg.add("名称不能为空:" + cityStrArray[0] + "," + cityStrArray[1] + "," + cityStrArray[2] + " ---- ");
					continue;
				}
				String result = administrativeDivisionService.addCity(cityCode, cityName, proCode);
				if (result != null) {
					errorMsg.add(result);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		msg.put("result", "success");
		msg.put("errorMsg", errorMsg);
		return msg;
	}

	/** 添加县 */
	@RequestMapping("addCounty")
	@ResponseBody
	public Map<String, Object> addCounty(@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "countyTxt", required = false) MultipartFile txt, @RequestParam(value = "countyText", required = false) String text,
			@RequestParam(value = "countyExcel", required = false) MultipartFile countyExcel) {
		logger.info("method=addCounty(),text={},txt={},countyExcel={}", text, txt.getOriginalFilename(), countyExcel.getOriginalFilename());
		Map<String, Object> msg = new HashMap<String, Object>();
		List<String> errorMsg = new ArrayList<String>();

		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) {
				rows.addAll(Arrays.asList(text.split("\r\n")));
			}
			if (txt != null && !"".equals(txt.getOriginalFilename())) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()), 1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (countyExcel != null && !"".equals(countyExcel.getOriginalFilename())) {
				readExcel(countyExcel, rows, msg, errorMsg);
				if (!msg.isEmpty()) return msg;
			}

			if (rows.isEmpty()) {
				msg.put("result", "fail");
				msg.put("errorMsg", "没有输入任何有效数据");
				return msg;
			}

			if (rows.size() > 500) {
				msg.put("result", "fail");
				msg.put("errorMsg", "一次添加量不能超过500");
				return msg;
			}

			for (String row : rows) {
				String[] countyStrArray = row.split(separator);
				if (countyStrArray.length != 3) {
					errorMsg.add("长度为3,且中间须以英文逗号分割:" + row + " ---- ");
					continue;
				}

				String countyName = countyStrArray[0].trim();
				String countyCode = countyStrArray[1].trim();
				String cityCode = countyStrArray[2].trim();
				if (!cityCode.matches("^\\d{4}$")) {
					errorMsg.add("区划码必须为4位数字:" + countyStrArray[2] + " ---- ");
					continue;
				}
				if (!countyCode.matches("^\\d{4}$")) {
					errorMsg.add("区划码必须为4位数字:" + countyStrArray[1] + " ---- ");
					continue;
				}
				if (cityCode.equals(countyCode)) {
					errorMsg.add("县区划码与市区划码不能相同:" + countyStrArray[1] + "," + countyStrArray[2] + " ---- ");
					continue;
				}
				if (StringUtils.isBlank(countyName)) {
					errorMsg.add("名称不能为空:" + countyStrArray[0] + "," + countyStrArray[1] + "," + countyStrArray[2] + " ---- ");
					continue;
				}
				String result = administrativeDivisionService.addCounty(countyCode, countyName, cityCode);
				if (result != null) {
					errorMsg.add(result);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		msg.put("result", "success");
		msg.put("errorMsg", errorMsg);
		return msg;
	}

	/** 读取excel数据 */
	private void readExcel(MultipartFile excel, List<String> rows, Map<String, Object> msg, List<String> errorMsg) {
		logger.info("method=readExcel(),excel={},rows={},msg={},errorMsg={}", excel.getName(), rows, msg, errorMsg);
		try {
			// 效验文件格式
			String fileName = excel.getOriginalFilename();
			if (fileName == null || "".equals(fileName)) return;
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (!("xls".equals(suffix) || "xlsx".equals(suffix))) {
				msg.put("result", "fail");
				msg.put("errorMsg", "文件类型不正确,当前类型：【" + suffix + "】");
				return;
			}

			// 获取文件内容
			Workbook wb = null;
			try {
				if (fileName.toLowerCase().endsWith(".xls")) wb = new HSSFWorkbook(excel.getInputStream());
				else if (fileName.toLowerCase().endsWith(".xlsx")) wb = new XSSFWorkbook(excel.getInputStream());
			} catch (Exception e) {
				logger.error("", e);
				msg.put("result", "fail");
				msg.put("errorMsg", "文件为空或类型不正确,当前文件：【" + fileName + "】");
				return;
			}

			// 检查表格数据是否为空
			Sheet sheet = wb.getSheetAt(0);
			String s = null;
			if (sheet == null || sheet.getLastRowNum() < 1) {
				msg.put("result", "fail");
				msg.put("errorMsg", "解析到文件【" + fileName + "】无数据");
				return;
			}

			// 循环读取每行 每列 数据
			for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					logger.info("method = readExcel(), row no data, row num = {}", i);
					errorMsg.add("第" + (i + 1) + "行无数据" + " ---- ");
					continue;
				}
				String s4 = getStringValue(row, 3);
				if (!s4.equals("")) {
					errorMsg.add("第" + (i + 1) + "行数据过长，不符合要求" + " ---- ");
					continue;
				}

				String s1 = getStringValue(row, 0);
				String s2 = getStringValue(row, 1);
				String s3 = getStringValue(row, 2);

				if (s3.equals("")) {
					s = s1 + "," + s2;
					rows.add(s);
				} else {
					s = s1 + "," + s2 + "," + s3;
					rows.add(s);
				}
				logger.info("method=readExcel(),read data =" + s);

			}
		} catch (Exception e) {
			logger.info("", e);
			errorMsg.add(e.getMessage());
		}

	}

	/** 修改 */
	@RequestMapping("adModify")
	@ResponseBody
	public Map<String, Object> adModify(@RequestParam(value = "oldCode") String oldCode, @RequestParam(value = "oldName") String oldName,
			@RequestParam(value = "newCode") String newCode, @RequestParam(value = "newName") String newName) {
		logger.info("method=adModify(),oldCode={},oldName={},newCode={},newName={}", oldCode, oldName, newCode, newName);
		if (StringUtils.isBlank(oldCode) || StringUtils.isBlank(oldName) || StringUtils.isBlank(newCode) || StringUtils.isBlank(newName)) return null;
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			if (oldCode.equals(newCode) && oldName.equals(newName)) {
				msg.put("result", "success");
				return msg;
			}
			administrativeDivisionService.adModify(oldCode.trim(), oldName.trim(), newCode.trim(), newName.trim());
			msg.put("result", "success");
		} catch (RuntimeException re) {
			logger.error("", re);
			msg.put("result", "fail");
			msg.put("errorMsg", re.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}
		return msg;

	}

	/** 导出 */
	@RequestMapping("/adExport")
	public ModelAndView adExport(@RequestParam("adCode") String adCode, @RequestParam("adName") String adName, @RequestParam("mark") String mark) {
		logger.info("method=adExport(),adCode={},adName={},mark={}", adCode, adName, mark);
		ModelAndView mv = new ModelAndView();
		Map<String, Object> adl = new LinkedHashMap<String, Object>();
		try {
			if (StringUtils.notBlank(adCode) && StringUtils.notBlank(mark)) {
				adl = administrativeDivisionService.queryByCode(adCode.trim(), mark.trim());
				if (!adl.isEmpty()) {
					mv.addObject("adl", adl);
				}
			}
			if (adl.isEmpty()) {
				if (StringUtils.notBlank(adName) && StringUtils.notBlank(mark)) {
					adl = administrativeDivisionService.queryByName(adName.trim(), mark.trim());
					mv.addObject("adMap", adl);
				}
			}
		} catch (Exception e) {
			logger.error("{}", e);
		}
		mv.setViewName("ad/adExportResult");
		return mv;
	}

	/** 全部导出 */
	@RequestMapping("/adTotalExport")
	public ModelAndView adTotalExport() {
		logger.info("method:adTotalExport()");
		ModelAndView mv = new ModelAndView("ad/adTotalExportResult");
		try {
			List<Object> adl = administrativeDivisionService.adTotalQuery();
			mv.addObject("adl", adl);
		} catch (Exception e) {
			logger.error("", e);
		}
		return mv;
	}

	/**
	 * @Description 县升级为省辖市 (非代管)
	 * @param countyCode
	 * @param cityCode
	 * @return
	 */
	@RequestMapping("/upgradeCounty")
	@ResponseBody
	public Map<String, Object> upgradeCounty(@RequestParam("countyCode") String countyCode, @RequestParam("cityCode") String cityCode) {
		logger.info("method=upgradeCounty(),countyCode={},cityCode={}", countyCode, cityCode);
		if (StringUtils.isBlank(countyCode) || StringUtils.isBlank(cityCode)) return null;

		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			administrativeDivisionService.upgradeCounty(countyCode, cityCode);
			msg.put("result", "success");
		} catch (RuntimeException re) {
			logger.error("", re);
			msg.put("result", "fail");
			msg.put("errorMsg", re.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}
		return msg;

	}

	/**
	 * @Description 省辖市(非代管)降级为县
	 * @param countyCode 降级为县
	 * @param cityCode 把它归到的市
	 */
	@RequestMapping("/degradeCity")
	@ResponseBody
	public Map<String, Object> degradeCity(@RequestParam("countyCode") String countyCode, @RequestParam("cityCode") String cityCode) {
		logger.info("method=degradeCity(),countyCode={},cityCode={}", countyCode, cityCode);
		if (StringUtils.isBlank(countyCode) || StringUtils.isBlank(cityCode)) return null;
		Map<String, Object> msg = new HashMap<String, Object>();
		if (countyCode.equals(cityCode)) {
			msg.put("result", "fail");
			msg.put("errorMsg", "两个区划码不能相同");
			return msg;
		}
		try {
			administrativeDivisionService.degradeCity(countyCode, cityCode);
			msg.put("result", "success");
		} catch (RuntimeException re) {
			logger.error("", re);
			msg.put("result", "fail");
			msg.put("errorMsg", re.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}

		return msg;
	}

	/**
	 * @Description 修改县所属的市
	 * @param countyCode
	 * @param oldCityCode
	 * @param newCityCode
	 */
	@RequestMapping("/modifyCounty")
	@ResponseBody
	public Map<String, Object> modifyCounty(@RequestParam("countyCode") String countyCode, @RequestParam("oldCityCode") String oldCityCode,
			@RequestParam("newCityCode") String newCityCode) {
		logger.info("method=modifyCounty(),countyCode={},oldCityCode={},newCityCode={}", countyCode, oldCityCode, newCityCode);
		if (StringUtils.isBlank(countyCode) || StringUtils.isBlank(oldCityCode) || StringUtils.isBlank(newCityCode)) return null;
		Map<String, Object> msg = new HashMap<String, Object>();
		oldCityCode = oldCityCode.trim();
		newCityCode = newCityCode.trim();
		if (oldCityCode.equals(newCityCode)) {
			msg.put("result", "fail");
			msg.put("errorMsg", "要更改的市与原来的市不能相同");
			return msg;
		}
		try {
			administrativeDivisionService.modifyCounty(countyCode, newCityCode);
			msg.put("result", "success");
		} catch (RuntimeException re) {
			logger.error("", re);
			msg.put("result", "fail");
			msg.put("errorMsg", re.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}

		return msg;
	}

	/**
	 * @Description 更改市所属的省
	 * @param cityCode
	 * @param oldProCode
	 * @param newProCode
	 */
	@RequestMapping("/modifyCity")
	@ResponseBody
	public Map<String, Object> modifyCity(@RequestParam("cityCode") String cityCode, @RequestParam("oldProCode") String oldProCode,
			@RequestParam("newProCode") String newProCode) {
		logger.info("method=modifyCity(),cityCode={},oldProCode={},newProCode={}", cityCode, oldProCode, newProCode);
		if (StringUtils.isBlank(cityCode) || StringUtils.isBlank(oldProCode) || StringUtils.isBlank(newProCode)) return null;
		Map<String, Object> msg = new HashMap<String, Object>();
		oldProCode = oldProCode.trim();
		newProCode = newProCode.trim();
		if (oldProCode.equals(newProCode)) {
			msg.put("result", "fail");
			msg.put("errorMsg", "要更改的省与原来的省不能相同");
			return msg;
		}
		try {
			administrativeDivisionService.modifyCity(cityCode, newProCode);
			msg.put("result", "success");
		} catch (RuntimeException re) {
			logger.error("", re);
			msg.put("result", "fail");
			msg.put("errorMsg", re.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}

		return msg;
	}

	// 下载模版
	@RequestMapping("/downloadTemplate")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response, @RequestParam("exportData") String exportData) {
		logger.info("method=downloadTemplate(),exportData={}", exportData);
		String fileName = null;
		if (exportData.equals("countyTemplate")) {
			fileName = "countyTemplate.xlsx";
		} else if (exportData.equals("cityTemplate")) {
			fileName = "cityTemplate.xlsx";
		} else if (exportData.equals("provinceTemplate")) {
			fileName = "provinceTemplate.xlsx";
		}
		exportTemplate(request, response, fileName);
	}

	private void exportTemplate(HttpServletRequest request, HttpServletResponse response, String fileName) {
		if (fileName == null) return;
		response.setContentType("application/vnd.ms-excel");
		String nowPath = request.getSession().getServletContext().getRealPath("/") + "/" + "resources" + "/" + "template" + "/" + fileName;
		logger.info("method=exportTemplate(),fileName= " + fileName + ", nowPath= " + nowPath);
		File file = new File(nowPath);
		// 清空response
		response.reset();
		try {
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			// 以流的形式下载文件
			InputStream fis = new BufferedInputStream(new FileInputStream(nowPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			logger.error("downLoad Template error .. ", e);
		}
	}

	/** 获取数据，及数据检查 */
	private String getStringValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		// 存储数据
		String s = "";
		if (cell == null) return s;
		try {
			if (cell.getCellType() == CellType.STRING) {
				s = cell.getStringCellValue().replaceAll("\u00A0|\\s*|\r|\t|\n", "");
			} else if (cell.getCellType() == CellType.NUMERIC) {
				s = String.valueOf((int) cell.getNumericCellValue());
			}

		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException("第" + (cellIndex + 1) + "列，数据解析失败;");

		}
		return s;
	}

	/** 查询名称 */
	@RequestMapping("/queryAdName")
	@ResponseBody
	public Map<String, Object> queryAdName(@RequestParam("code") String code) {
		logger.info("method=queryAdName(),code={}", code);
		Map<String, Object> msg = new HashMap<String, Object>();
		String name = administrativeDivisionService.queryAdname(code);
		if (name != null) {
			msg.put("result", "success");
			msg.put("name", name);
		} else {
			msg.put("result", "fail");
		}

		return msg;

	}


}
