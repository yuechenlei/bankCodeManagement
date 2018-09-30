package com.xiaoqingxin.bankCodeService.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.xiaoqingxin.bankCodeService.model.Cnaps;
import com.xiaoqingxin.bankCodeService.service.AdministrativeDivisionService;
import com.xiaoqingxin.bankCodeService.service.CnapsService;
import com.xiaoqingxin.bankCodeService.utils.Page;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

/**
 * @ClassName: CnapsController
 * @Description: 联行号访问控制器
 * @author Administrator
 * @date 2018年9月21日
 *
 */
@Controller
@RequestMapping("/cnaps")
public class CnapsController {
	private static final Logger logger = LoggerFactory.getLogger(CnapsController.class);
	@Resource
	private CnapsService cnapsService;
	@Resource
	private AdministrativeDivisionService administrativeDivisionService;
	
	/** 组合查询页面跳转 */
	@RequestMapping("/cnapsExhibitionQuery/index")
	public ModelAndView toCnapsExhibitionQuery(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		if (request.getSession().getAttribute("user") == null) {
			mv.setViewName("redirect:/toLogin.htm");
			return mv;
		}
		String user = request.getSession().getAttribute("user").toString();
		logger.info("method=toCnapsExhibitionQuery(),user={}", user);
		mv.addObject("user", user);
		mv.setViewName("cnaps/cnapsExhibitionQuery");
		return mv;
	}

	/** 根据联行号查询 */
	@RequestMapping("searchByCncode")
	@ResponseBody
	public Map<String, Object> searchByCncode(HttpServletResponse response,
			@RequestParam(value = "cncode", required = false) String cncode) {
		String code = cncode.trim();
		Map<String, Object> map = null;
		try {
			Cnaps cs = cnapsService.searchByCncode(code);
			map = new HashMap<String, Object>();
			map.put("cnaps", cs);
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}


	/** 组合查询 */
	@RequestMapping("/cnapsExhibitionQuery")
	public ModelAndView cnapsExhibitionQuery(HttpServletRequest request, @ModelAttribute("cnaps") Cnaps cnaps) {
		ModelAndView mv = new ModelAndView();
		if (request.getSession().getAttribute("user") == null) {
			// mv.addObject("msg", "请重新登录!!!");
			// mv.setViewName("cnaps/cnapsExhibitionQueryResult");
			mv.setViewName("error/nopermit");
			return mv;
		}
		String user = request.getSession().getAttribute("user").toString();
		logger.info("method=cnapsExhibitionQuery(),cnaps={},user={}", cnaps, user);
		mv.addObject("user", user);

		List<Cnaps> cnapsl = new ArrayList<Cnaps>();
		// 如果查询条件为空...
		if (cnaps.getCode().trim() == "" && cnaps.getName().trim() == "" && cnaps.getClearingBankLevel() == 0
				&& cnaps.getProviderCode().trim() == "" && cnaps.getAdCode().trim() == "") {
			mv.addObject("cnapsl", cnapsl);
			mv.setViewName("cnaps/cnapsExhibitionQueryResult");
			return mv;
		}

		try {
			if (StringUtils.notBlank(cnaps.getCode())) {
				String cncode = cnaps.getCode().trim();
				if (cncode.matches("^\\d{12}$")) {
					Cnaps cs = cnapsService.searchByCncode(cncode);
					if (cs != null) {
						cnapsl.add(cs);
					}
					mv.addObject("cnapsl", cnapsl);
				}
				mv.setViewName("cnaps/cnapsExhibitionQueryResult");
				return mv;

			}

			Page<List<Cnaps>> page = new Page<List<Cnaps>>();
			int currentPage = request.getParameter("currentPage") == null ? 1
					: Integer.parseInt(request.getParameter("currentPage"));
			page.setCurrentPage(currentPage);
			page.setShowCount(100);
			// 组合查询
			page = cnapsService.cnapsCombQuery(page, cnaps);
			logger.info("method:cnapsCombQuery(),page={}, cnaps={}", page, cnaps);

			mv.addObject("page", page);
			mv.addObject("cnapsl", page.getObject());
			mv.setViewName("cnaps/cnapsExhibitionQueryResult");
		} catch (Exception e) {
			logger.error("", e);
			mv.addObject("error", "系统异常，请稍后查询");
		}

		return mv;

	}
	
	/**
	 * 联行号维护(添加或修改)
	 * 
	 * @param separator
	 *            各字段之间的分隔符
	 * @param txt
	 *            文本文件
	 * @param text
	 *            文本框
	 * @return
	 */
	@RequestMapping("add")
	public String add(@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "cnapsTxt", required = false) MultipartFile txt,
			@RequestParam(value = "cnapsText", required = false) String text) {
		List<String> errorMsg = new ArrayList<String>();
		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			rows.addAll(Arrays.asList(text.split("\r\n")));
			if (txt != null) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()),
						1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}
			List<Cnaps> cnapses = textToCnaps(separator, rows, errorMsg);
			cnapsService.add(cnapses);
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		ModelAndView result = new ModelAndView();
		result.addObject("errorMsg", errorMsg);
		return "/cnaps/add";
	}

	/** 导出 */
	@RequestMapping("/cnapsExport")
	public ModelAndView cnapsExport(HttpServletRequest request, @ModelAttribute("cnaps") Cnaps cnaps) {
		logger.info("method:cnapsExport(),CnapsParam={}", cnaps);
		ModelAndView mv = new ModelAndView();
		List<Cnaps> cnapsl = new ArrayList<Cnaps>();

		// 如果查询条件为空...
		if (cnaps.getCode().trim() == "" && cnaps.getName().trim() == "" && cnaps.getClearingBankLevel() == 0
				&& cnaps.getProviderCode().trim() == "" && cnaps.getAdCode().trim() == "") {
			mv.addObject("cnapsl", cnapsl);
			mv.setViewName("cnaps/cnapsExportResult");
			return mv;
		}

		try {
			if (!StringUtils.isBlank(cnaps.getCode())) {
				String cncode = cnaps.getCode().trim();
				if (cncode.matches("^\\d{12}$")) {
					Cnaps cs = cnapsService.searchByCncode(cncode);
					if (cs != null) {
						cnapsl.add(cs);
					}
					mv.addObject("cnapsl", cnapsl);
				}

				mv.setViewName("cnaps/cnapsExportResult");
				return mv;

			}

			// 组合查询
			Page<List<Cnaps>> page = new Page<List<Cnaps>>();
			page.setCurrentPage(1);
			// 每页显示数量
			page.setShowCount(Integer.MAX_VALUE);
			// 组合查询
			page = cnapsService.cnapsCombQuery(page, cnaps);

			logger.info("method:cnapsExport(),cnaps={}, fields={}", cnaps);

			mv.addObject("cnapsl", page.getObject());
			mv.setViewName("cnaps/cnapsExportResult");
		} catch (Exception e) {
			logger.error("{}", e);
		}
		return mv;
	}

	/** 确认修改 (修改了联行号，执行新增) */
	@RequestMapping("confirmModify")
	@ResponseBody
	public Map<String, Object> confirmModify(Cnaps cnaps) {
		logger.info("method=confirmModify(),Cnaps={}", cnaps);
		Map<String, Object> msg = new HashMap<String, Object>();
		List<Cnaps> cnapsl = new ArrayList<Cnaps>();
		try {
			String str = cnapsValidate(cnaps);
			if (str != null) {
				msg.put("result", "fail");
				return msg;
			}
			cnapsl.add(cnaps);
			cnapsService.add(cnapsl);
			msg.put("result", "success");

		} catch (Exception e) {
			logger.info("{}", e);
			msg.put("result", "fail");
		}
		return msg;

	}

	/** 修改  */
	@RequestMapping("modify")
	@ResponseBody
	public Map<String, Object> modify(Cnaps cnaps) {
		logger.info("method=modify(),Cnaps={}", cnaps);
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			String str = cnapsValidate(cnaps);
			if (str != null) {
				msg.put("result", "fail");
				return msg;
			}
			boolean result = cnapsService.modify(cnaps);
			if (result) {
				msg.put("result", "success");
			} else {
				msg.put("result", "fail");
			}

		} catch (Exception e) {
			logger.info("{}", e);
			msg.put("result", "fail");
		}
		return msg;

	}

	/** 新增联行号 */
	@RequestMapping("cnapsIncrease")
	@ResponseBody
	public Map<String, Object> cnapsIncrease(HttpServletResponse response,
			@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "cnapsTxt", required = false) MultipartFile txt,
			@RequestParam(value = "cnapsText", required = false) String text,
			@RequestParam(value = "cnapsExcel", required = false) MultipartFile cnapsExcel) {
		logger.info("method=cnapsIncrease(),separator={},txt={},text={},cnapsExcel={}", separator,
				txt.getOriginalFilename(), text, cnapsExcel.getOriginalFilename());
		List<String> errorMsg = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) {
				rows.addAll(Arrays.asList(text.split("\r\n")));
			}
			if (txt != null) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()),
						1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (cnapsExcel != null && !"".equals(cnapsExcel.getOriginalFilename())) {
				readExcel(cnapsExcel, rows, map, errorMsg);
				if (!map.isEmpty())
					return map;
			}

			if (rows.isEmpty()) {
				map.put("result", "empty");
				return map;
			}
			if (rows.size() > 1000) {
				map.put("result", "overstep");
				return map;
			}
			List<Cnaps> cnapses = textToCnapsAndValidate(separator, rows, errorMsg);
			cnapsService.add(cnapses);
			map.put("result", "success");
		} catch (RuntimeException e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
			map.put("result", "success");
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
			map.put("result", "error");
		}
		map.put("errorMsg", errorMsg);
		return map;

	}

	/** 删除 */
	@RequestMapping("cnapsDel")
	@ResponseBody
	public Map<String, Object> deleteCnaps(Cnaps cnaps, String newCode) {
		logger.info("method=deleteCnaps(),Cnaps={},newCode={}", cnaps, newCode);
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			if (StringUtils.notBlank(newCode)) {
				Cnaps cnap = cnapsService.searchByCncode(newCode);
				if (cnap != null && StringUtils.notBlank(cnap.getCode())) {
					msg.put("result", "exist");
					return msg;
				}
			}
			cnapsService.del(cnaps);
			msg.put("result", "success");

		} catch (Exception e) {
			msg.put("result", "fail");
			logger.info("", e);
		}

		return msg;
	}

	/** 全部导出 (poi方式) */
	@RequestMapping("/cnapsTotalExport")
	public void cnapsTotalExport(HttpServletResponse response) {
		logger.info("method:cnapsTotalExport()");
		try {
			List<Cnaps> cnapsl = cnapsService.cnapsTotalQuery();
			logger.info("method=cnapsTotalExport(),List<Cnaps> size={}", cnapsl.size());

			// 创建07版的excel工作簿
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			// 创建工作表
			Sheet sheet = workbook.createSheet("cnaps");
			// 设置列宽
			sheet.setColumnWidth(0, 32 * 150);
			sheet.setColumnWidth(1, 32 * 500);
			sheet.setColumnWidth(2, 32 * 150);
			sheet.setColumnWidth(3, 32 * 100);
			sheet.setColumnWidth(4, 32 * 100);
			sheet.setColumnWidth(5, 32 * 100);

			// 加粗居中样式（表头用）
			Font font = workbook.createFont();
			font.setFontName("宋体");
			// font.setFontHeightInPoints((short) 10);
			font.setBold(true);
			CellStyle biStyle = workbook.createCellStyle();
			biStyle.setFont(font);
			biStyle.setAlignment(HorizontalAlignment.CENTER);
			biStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			// 制作表头
			Row header = sheet.createRow(0);
			Cell cnapsId = header.createCell(0);
			cnapsId.setCellValue("联行号");
			cnapsId.setCellStyle(biStyle);
			Cell bankName = header.createCell(1);
			bankName.setCellValue("银行名称");
			bankName.setCellStyle(biStyle);
			Cell clearingBankCode = header.createCell(2);
			clearingBankCode.setCellValue("清分行号");
			clearingBankCode.setCellStyle(biStyle);
			Cell clearingBankLevel = header.createCell(3);
			clearingBankLevel.setCellValue("清算级别");
			clearingBankLevel.setCellStyle(biStyle);
			Cell bankCode = header.createCell(4);
			bankCode.setCellValue("银行编码");
			bankCode.setCellStyle(biStyle);
			Cell adCode = header.createCell(5);
			adCode.setCellValue("地区编码");
			adCode.setCellStyle(biStyle);

			// 填充数据
			int i = 1;
			for (Cnaps cs : cnapsl) {
				Row generalRow = sheet.createRow(i);
				Cell id = generalRow.createCell(0);
				id.setCellValue(cs.getCode());
				Cell name = generalRow.createCell(1);
				name.setCellValue(cs.getName());
				Cell clearingBankCodes = generalRow.createCell(2);
				clearingBankCodes.setCellValue(cs.getClearingBankCode());
				Cell clearingBankLevels = generalRow.createCell(3);
				clearingBankLevels.setCellValue(cs.getClearingBankLevel());
				Cell providerCodes = generalRow.createCell(4);
				providerCodes.setCellValue(cs.getProviderCode());
				Cell adCodes = generalRow.createCell(5);
				adCodes.setCellValue(cs.getAdCode());
				i++;
			}

			// 导出文件
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + java.net.URLEncoder.encode("联行号信息", "utf8") + ".xlsx");
			OutputStream out = null;
			try {
				out = response.getOutputStream();
				workbook.write(out);
			} catch (Exception e) {
				logger.info("", e);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						logger.info("", e);
					}

				}
				workbook.close();
			}

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/** 新增数据检查 */
	private List<Cnaps> textToCnapsAndValidate(String separator, List<String> rows, List<String> errorMsg) {
		List<Cnaps> cnapses = new ArrayList<>();
		try {
			for (String row : rows) {
				String[] cnapsStrArray = row.split(separator);
				if (cnapsStrArray.length != 6) {
					errorMsg.add("长度必须为6,默认以英文逗号分割：" + row + " ---- ");
					continue;
				}
				if (!cnapsStrArray[0].trim().matches("^\\d{12}$")) {
					errorMsg.add("联行号必须为12位数字：" + row + " ---- ");
					continue;
				}
				if (!cnapsStrArray[2].trim().matches("^\\d{12}$")) {
					errorMsg.add("清分行号必须为12位数字：" + row + " ---- ");
					continue;
				}
				if (!cnapsStrArray[3].trim().matches("^-?\\d{1}$")) {
					errorMsg.add("清算级别必须为1位数字：" + row + " ---- ");
					continue;
				}
				if (!cnapsStrArray[4].trim().matches("^[A-Za-z]+$")) {
					errorMsg.add("银行编码必须为字母：" + row + " ---- ");
					continue;
				}
				if (!cnapsStrArray[5].trim().matches("^\\d{4}$")) {
					errorMsg.add("行政区划码必须为4位数字：" + row + " ---- ");
					continue;
				}
				Cnaps cnaps = new Cnaps();
				cnaps.setCode(cnapsStrArray[0].trim());
				cnaps.setName(cnapsStrArray[1].trim());
				cnaps.setClearingBankCode(cnapsStrArray[2].trim());
				cnaps.setClearingBankLevel(Integer.parseInt(cnapsStrArray[3].trim()));
				cnaps.setProviderCode(cnapsStrArray[4].trim().toUpperCase());
				cnaps.setAdCode(cnapsStrArray[5].trim());
				cnapses.add(cnaps);
			}
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		return cnapses;
	}

	public String cnapsValidate(Cnaps cnaps) {
		if (!cnaps.getCode().trim().matches("^\\d{12}$")) {
			return "errorParam";
		}
		if (!cnaps.getClearingBankCode().trim().matches("^\\d{12}$")) {
			return "errorParam";
		}
		if (StringUtils.isBlank(cnaps.getName()) || StringUtils.isBlank(cnaps.getProviderCode())
				|| StringUtils.isBlank(cnaps.getAdCode())) {
			return "errorParam";
		}
		if (cnaps.getClearingBankLevel() != 1 && cnaps.getClearingBankLevel() != 2
				&& cnaps.getClearingBankLevel() != -1) {
			return "errorParam";
		}
		return null;
	}

	/** 下载模版 */
	@RequestMapping("/downloadTemplate")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("exportData") String exportData) {
		logger.info("method=downloadTemplate(),exportData={}", exportData);
		String fileName = null;
		if (exportData.equals("cnapsTemplate")) {
			fileName = "cnapsTemplate.xlsx";
		}
		exportTemplate(request, response, fileName);
	}

	private void exportTemplate(HttpServletRequest request, HttpServletResponse response, String fileName) {
		if (fileName == null)
			return;
		// 清空response
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		String nowPath = request.getSession().getServletContext().getRealPath("/") + "/" + "resources" + "/" + "template"
				+ "/" + fileName;
		logger.info("method=exportTemplate(),fileName= " + fileName + ", nowPath= " + nowPath);
		File file = new File(nowPath);
		try {
			// 设置response的Header
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1"));
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

	/** 读取excel数据 */
	private void readExcel(MultipartFile excel, List<String> rows, Map<String, Object> msg, List<String> errorMsg) {
		logger.info("method=readExcel(),excel={},rows={},msg={},errorMsg={}", excel.getName(), rows, msg, errorMsg);
		try {
			// 效验文件格式
			String fileName = excel.getOriginalFilename();
			if (fileName == null || "".equals(fileName))
				return;
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (!("xls".equals(suffix) || "xlsx".equals(suffix))) {
				msg.put("result", "fail");
				msg.put("errorMsg", "文件类型不正确,当前类型：【" + suffix + "】");
				return;
			}

			// 获取文件内容
			Workbook wb = null;
			try {
				if (fileName.toLowerCase().endsWith(".xls"))
					wb = new HSSFWorkbook(excel.getInputStream());
				else if (fileName.toLowerCase().endsWith(".xlsx"))
					wb = new XSSFWorkbook(excel.getInputStream());
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

				String code = getStringValue(row, 0);
				String name = getStringValue(row, 1);
				String clearingBankCode = getStringValue(row, 2);
				String clearingBankLevel = getStringValue(row, 3);
				String providerCode = getStringValue(row, 4);
				String adCode = getStringValue(row, 5);

				s = code + "," + name + "," + clearingBankCode + "," + clearingBankLevel + "," + providerCode + ","
						+ adCode;
				rows.add(s);
				logger.info("method=readExcel(),read data = " + s);

			}
		} catch (Exception e) {
			logger.info("", e);
			errorMsg.add(e.getMessage());
		}

	}

	/** 获取数据，及数据检查 */
	private String getStringValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		// 存储数据
		String s = "";
		if (cell == null)
			return s;
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

	/** 批量修改联行号信息 */
	@RequestMapping("batchModification")
	@ResponseBody
	public Map<String, Object> batchModification(HttpServletResponse response,
			@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "cnapsTxt", required = false) MultipartFile txt,
			@RequestParam(value = "cnapsText", required = false) String text,
			@RequestParam(value = "cnapsExcel", required = false) MultipartFile cnapsExcel) {
		logger.info("method=batchModification(),separator={},txt={},text={},cnapsExcel={}", separator,
				txt.getOriginalFilename(), text, cnapsExcel.getOriginalFilename());
		List<String> errorMsg = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) {
				rows.addAll(Arrays.asList(text.split("\r\n")));
			}
			if (txt != null) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()),
						1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (cnapsExcel != null && !"".equals(cnapsExcel.getOriginalFilename())) {
				readExcel(cnapsExcel, rows, map, errorMsg);
				if (!map.isEmpty())
					return map;
			}

			if (rows.isEmpty()) {
				map.put("result", "empty");
				return map;
			}
			if (rows.size() > 1000) {
				map.put("result", "overstep");
				return map;
			}
			List<Cnaps> cnapses = textToCnapsAndValidate(separator, rows, errorMsg);
			int totalCount = cnapses.size();
			int failCount = 0;
			for (Cnaps cs : cnapses) {
				Map<String, Object> result = modify(cs);
				if (result.get("result").toString().equals("fail")) {
					failCount++;
				}
			}
			map.put("result", "success");
			map.put("totalCount", totalCount);
			map.put("failCount", failCount);
		} catch (RuntimeException e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
			map.put("result", "re");
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
			map.put("result", "fail");
		}
		map.put("errorMsg", errorMsg);
		return map;

	}

	

	/**
	 * 从文本形式转为Cnaps集合
	 * 
	 * @param separator
	 *            分隔符
	 * @param text
	 *            文本内容
	 * @param errorMsg
	 *            解析错误的信息
	 * @return Cnaps集合
	 */
	private List<Cnaps> textToCnaps(String separator, List<String> rows, List<String> errorMsg) {
		List<Cnaps> cnapses = new ArrayList<>();
		try {
			for (String row : rows) {
				String[] cnapsStrArray = row.split(separator);
				if (cnapsStrArray.length != 6) {
					errorMsg.add("长度必须为6,默认以英文逗号分割：" + row);
					continue;
				}
				Cnaps cnaps = new Cnaps();
				cnaps.setCode(cnapsStrArray[0]);
				cnaps.setName(cnapsStrArray[1]);
				cnaps.setClearingBankCode(cnapsStrArray[2]);
				cnaps.setClearingBankLevel(Integer.parseInt(cnapsStrArray[3]));
				cnaps.setProviderCode(cnapsStrArray[4]);
				cnaps.setAdCode(cnapsStrArray[5]);
				cnapses.add(cnaps);
			}
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		return cnapses;
	}
	

}
