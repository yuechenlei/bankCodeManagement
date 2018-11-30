package com.xiaoqingxin.bankCodeService.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.xiaoqingxin.bankCodeService.mariaService.UserRedPacketService;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

@Controller
@RequestMapping("/userRedPacket")
public class UserRedPacketController {
	private static final Logger logger = LoggerFactory.getLogger(UserRedPacketController.class);

	@Autowired
	private UserRedPacketService userRedPacketService = null;

	/** 页面跳转 */
	@RequestMapping("/grapRedPacket/index")
	public ModelAndView toGrapRedPacket(@SessionAttribute(name = "user", required = false) String user) {
		logger.info("method=toGrapRedPacket(),user={}", user);
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(user)) {
			mv.setViewName("redirect:/toLogin.htm");
			return mv;
		}

		mv.addObject("user", user);
		mv.setViewName("test/grap");
		return mv;
	}

	/** 普通测试 */
	@RequestMapping(value = "/grapRedPacket")
	@ResponseBody
	public Map<String, Object> grapRedPacket(Long redPacketId, Long userId) {

		int result = 0;
		// 抢红包
		logger.info("进入普通MariaDB测试......");
		result = userRedPacketService.grapRedPacket(redPacketId, userId);

		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean flag = result > 0;
		retMap.put("success", flag);
		retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
		return retMap;
	}

	/** 悲观锁测试 */
	@RequestMapping(value = "/grapRedPacketForUpdate")
	@ResponseBody
	public Map<String, Object> grapRedPacketForUpdate(Long redPacketId, Long userId) {

		int result = 0;
		// 抢红包
		logger.info("进入MariaDB悲观锁测试......");
		result = userRedPacketService.grapRedPacketForUpdate(redPacketId, userId);

		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean flag = result > 0;
		retMap.put("success", flag);
		retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
		return retMap;
	}

	/** 乐观锁(无重入) */
	@RequestMapping(value = "/grapRedPacketForVersion")
	@ResponseBody
	public Map<String, Object> grapRedPacketForVersion(Long redPacketId, Long userId) {
		logger.info("进入MariaDB乐观锁无重入测试......");
		// 抢红包
		int result = userRedPacketService.grapRedPacketForVersion(redPacketId, userId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean flag = result > 0;
		retMap.put("success", flag);
		retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
		return retMap;
	}

	/** 乐观锁(时间戳重入) */
	@RequestMapping(value = "/grapRedPacketForVersionTimeRe")
	@ResponseBody
	public Map<String, Object> grapRedPacketForVersionTimeRe(Long redPacketId, Long userId) {
		logger.info("进入MariaDB乐观锁时间戳重入测试......");
		// 抢红包
		int result = userRedPacketService.grapRedPacketForVersionTimeRe(redPacketId, userId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean flag = result > 0;
		retMap.put("success", flag);
		retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
		return retMap;
	}

	/** 乐观锁(次数重入) */
	@RequestMapping(value = "/grapRedPacketForVersionCountRe")
	@ResponseBody
	public Map<String, Object> grapRedPacketForVersionCountRe(Long redPacketId, Long userId) {
		logger.info("进入MariaDB乐观锁次数重入测试......");
		// 抢红包
		int result = userRedPacketService.grapRedPacketForVersionCountRe(redPacketId, userId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean flag = result > 0;
		retMap.put("success", flag);
		retMap.put("message", flag ? "抢红包成功" : "抢红包失败");
		return retMap;
	}

	/** redis测试  本次测试要把格式化器都设为 StringRedisSerializer  */
	@RequestMapping(value = "/grapRedPacketByRedis")
	@ResponseBody
	public Map<String, Object> grapRedPacketByRedis(Long redPacketId, Long userId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long result = userRedPacketService.grapRedPacketByRedis(redPacketId, userId);
		boolean flag = result > 0;
		resultMap.put("result", flag);
		resultMap.put("message", flag ? "抢红包成功" : "抢红包失败");
		return resultMap;
	}

}
