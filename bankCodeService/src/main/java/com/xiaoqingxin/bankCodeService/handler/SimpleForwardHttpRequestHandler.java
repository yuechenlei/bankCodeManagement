package com.xiaoqingxin.bankCodeService.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
* @ClassName: SimpleForwardHttpRequestHandler
* @Description: 通用URL转向jsp处理
* @author Administrator
* @date 2018年9月21日
*
 */
public class SimpleForwardHttpRequestHandler extends ResourceHttpRequestHandler {
	private static final Logger logger = LoggerFactory.getLogger(SimpleForwardHttpRequestHandler.class);

	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getServletPath().endsWith(".htm")) {
			String forwardURL = "/WEB-INF/jsp" + request.getServletPath().substring(0, request.getServletPath().length() - 4) + ".jsp";
			logger.debug("forward to {}", forwardURL);
			request.getRequestDispatcher(forwardURL).forward(request, response);
			return;
		}
		super.handleRequest(request, response);
	}

}
