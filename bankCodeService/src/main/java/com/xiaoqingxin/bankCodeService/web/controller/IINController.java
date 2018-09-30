package com.xiaoqingxin.bankCodeService.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.xiaoqingxin.bankCodeService.model.IIN;
import com.xiaoqingxin.bankCodeService.service.IINService;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

/**
* @ClassName: IINController
* @Description: 发行者识别号码访问控制器
* @author Administrator
* @date 2018年9月21日
*
 */
@Controller
@RequestMapping("/iin")
public class IINController {
	private static final Logger logger = LoggerFactory.getLogger(IINController.class);
	@Resource
	private IINService iinService;

	private static final String[] FIELDS = new String[] { "agencyName", "agencyCode", "cardName", "panLength", "length", "code", "cardType", "providerCode" };

	/** 卡识别页面跳转 */
	@RequestMapping("/iinQuery/index")
	public ModelAndView toIinQuery(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		if (request.getSession().getAttribute("user") == null) {
			mv.setViewName("redirect:/toLogin.htm");
			return mv;
		}
		String user = request.getSession().getAttribute("user").toString();
		logger.info("method=toIinQuery(),user={}", user);
		mv.addObject("user", user);
		mv.setViewName("iin/iinQuery");
		return mv;
	}

	/** @Description 卡识别 */
	@RequestMapping("/iinQuery")
	public ModelAndView iinQuery(HttpServletRequest request, @RequestParam("cardNo") String cardNo) {
		ModelAndView mv = new ModelAndView();
		if (request.getSession().getAttribute("user") == null) {
			mv.setViewName("error/nopermit");
			return mv;
		}
		String user = request.getSession().getAttribute("user").toString();
		logger.info("method=iinQuery(),cardNo={},user={}", cardNo, user);
		mv.addObject("user", user);
		try {
			List<IIN> iins = new ArrayList<IIN>();
			String cardNum = cardNo.trim();
			if (cardNum.equals("") || cardNum.length() < 6) {
				mv.addObject("iins", iins);
				mv.setViewName("iin/iinQueryResult");
				return mv;
			}

			IIN iin = iinService.recognition(cardNum, FIELDS);
			if (iin != null && iin.getLength() != 0 && iin.getPanLength() != 0) {
				iins.add(iin);
			}
			mv.addObject("iins", iins);
			mv.setViewName("iin/iinQueryResult");
			return mv;
		} catch (Exception e) {
			logger.error("", e);
			mv.addObject("error", e.getMessage());
			mv.setViewName("iin/iinQueryResult");
			return mv;
		}
	}

	/** @Description 新增卡识别 */
	@RequestMapping("/iinIncrease")
	@ResponseBody
	public Map<String, Object> iinIncrease(@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "iinTxt", required = false) MultipartFile txt, @RequestParam(value = "iinText", required = false) String text) {
		logger.info("method=iinIncrease(),separator={},txt={},text={}", separator, txt, text);
		List<String> errorMsg = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) {
				rows.addAll(Arrays.asList(text.split("\r\n")));
			}
			if (txt != null) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()), 1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (rows.isEmpty()) {
				map.put("result", "empty");
				return map;
			}

			if (rows.size() > 3000) {
				map.put("result", "overStep");
				return map;
			}

			List<IIN> iins = textToCnaps(separator, rows, errorMsg);
			iinService.add(iins);
			map.put("result", "success");
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
			map.put("result", "error");
		}
		map.put("errorMsg", errorMsg);
		return map;
	}

	/** 导出卡bin */
	@RequestMapping("/iinExport")
	public ModelAndView iinExport(String cardNo) {
		logger.info("method:cnapsExport(),cardNo={}", cardNo);
		ModelAndView mv = new ModelAndView();
		List<IIN> iins = new ArrayList<IIN>();
		String cardNum = cardNo.trim();
		// 如果查询条件为空...
		if (cardNum.equals("") || cardNum.length() < 6) {
			mv.addObject("iins", iins);
			mv.setViewName("iin/iinExportResult");
			return mv;
		}

		try {
			IIN iin = iinService.recognition(cardNum, FIELDS);
			if (iin != null && iin.getLength() != 0 && iin.getPanLength() != 0) {
				iins.add(iin);
			}
			mv.addObject("iins", iins);
			mv.setViewName("iin/iinExportResult");
			return mv;

		} catch (Exception e) {
			logger.error("{}", e);
		}
		return mv;
	}

	/** 修改 */
	@RequestMapping("/iinModify")
	@ResponseBody
	public Map<String, Object> modify(IIN iin) {
		logger.info("method=modify(),iin={}", iin);
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			iinService.modify(iin);
			msg.put("result", "success");
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}
		return msg;
	}

	/** 删除 */
	@RequestMapping("/iinDel")
	@ResponseBody
	public Map<String, Object> delete(IIN iin) {
		logger.info("method=delete(),iin={}", iin);
		String id = "" + iin.getCode() + "." + iin.getLength() + "." + iin.getPanLength();
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			iinService.del(id);
			msg.put("result", "success");
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}
		return msg;
	}

	/** 添加 */
	@RequestMapping("/iinAdd")
	@ResponseBody
	public Map<String, Object> add(IIN iin) {
		logger.info("method=add(),iin={}", iin);
		if (iin == null) return null;
		Map<String, Object> msg = new HashMap<String, Object>();
		List<IIN> iins = new ArrayList<>();
		iins.add(iin);
		try {
			iinService.add(iins);
			msg.put("result", "success");
		} catch (Exception e) {
			logger.error("", e);
			msg.put("result", "fail");
			msg.put("errorMsg", e.getMessage());
		}
		return msg;
	}

	/** 全部导出 */
	@RequestMapping("/totalExport")
	public ModelAndView totalExport() {
		logger.info("method:totalExport(),start...");
		ModelAndView mv = new ModelAndView();
		try {
			List<IIN> iins = iinService.totalExport(FIELDS);
			mv.addObject("iins", iins);
			mv.setViewName("iin/iinExportResult");
			return mv;
		} catch (Exception e) {
			logger.error("{}", e);
			return null;
		}
	}

	/** 批量修改 */
	@RequestMapping("/batchModification")
	@ResponseBody
	public Map<String, Object> batchModification(@RequestParam(value = "separator", defaultValue = ",") String separator,
			@RequestParam(value = "iinTxt", required = false) MultipartFile txt, @RequestParam(value = "iinText", required = false) String text) {
		logger.info("method=batchModification(),separator={},txt={},text={}", separator, txt.getOriginalFilename(), text);
		List<String> errorMsg = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> rows = new ArrayList<>();
			text = StringUtils.safeValue(text);
			if (!text.equals("")) rows.addAll(Arrays.asList(text.split("\r\n")));
			if (txt != null) {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(txt.getInputStream()), 1024 * 2048)) {
					for (String row = reader.readLine(); row != null; row = reader.readLine()) {
						rows.add(row);
					}
				}
			}

			if (rows.isEmpty()) {
				map.put("result", "empty");
				return map;
			}
			if (rows.size() > 3000) {
				map.put("result", "overStep");
				return map;
			}

			List<IIN> iins = textToCnaps(separator, rows, errorMsg);
			int totalCount = iins.size();
			int failCount = 0;
			for (IIN iin : iins) {
				boolean result = iinService.modify(iin);
				if (!result) failCount++;
			}
			map.put("result", "success");
			map.put("totalCount", totalCount);
			map.put("failCount", failCount);
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
			map.put("result", "fail");
		}
		map.put("errorMsg", errorMsg);
		return map;
	}
	
	/**
	 * 从文本形式转为IIN集合
	 * @param separator 分隔符
	 * @param text 文本内容
	 * @param errorMsg 解析错误的信息
	 * @return IIN集合
	 */
	private List<IIN> textToCnaps(String separator, List<String> rows, List<String> errorMsg) {
		List<IIN> iins = new ArrayList<>();
		try {
			for (String row : rows) {
				String[] cnapsStrArray = row.split(separator);
				if (cnapsStrArray.length != 8) {
					errorMsg.add("长度必须为8,默认以英文逗号分割：" + row);
					continue;
				}
				IIN iin = new IIN();
				iin.setAgencyCode(cnapsStrArray[1].trim());
				iin.setAgencyName(cnapsStrArray[0].trim());
				iin.setCardName(cnapsStrArray[2].trim());
				iin.setCardType(cnapsStrArray[6].trim());
				iin.setCode(cnapsStrArray[5].trim());
				iin.setLength(Integer.parseInt(cnapsStrArray[4].trim()));
				iin.setPanLength(Integer.parseInt(cnapsStrArray[3].trim()));
				iin.setProviderCode(cnapsStrArray[7].trim());
				iins.add(iin);
			}
		} catch (Exception e) {
			logger.error("", e);
			errorMsg.add(e.getMessage());
		}
		return iins;
	}
}
