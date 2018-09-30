package com.xiaoqingxin.bankCodeService.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.xiaoqingxin.bankCodeService.utils.StringUtils;



/**
* @ClassName: SimpleForwardHttpRequestHandlerForToHTM
* @Description: 通用URL转向jsp处理(针对toXXX.htm)
* @author Administrator
* @date 2018年9月21日
*
 */
public class SimpleForwardHttpRequestHandlerForToHTM extends ResourceHttpRequestHandler {
	private static final Logger logger = LoggerFactory.getLogger(SimpleForwardHttpRequestHandlerForToHTM.class);
	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		logger.info("通用URL转向jsp处理(针对toXXX.htm),servletPath="+servletPath);
		int lastIndex = servletPath.lastIndexOf("/");
		String name = servletPath.substring(lastIndex + 1);
		if (name.startsWith("to") && name.endsWith(".htm")) {
			String forwardURL = StringUtils.concatToSB("/WEB-INF/jsp", servletPath.substring(0, lastIndex + 1),
					servletPath.substring(lastIndex + 3, lastIndex + 4).toLowerCase(), servletPath.substring(lastIndex + 4, servletPath.length() - 4), ".jsp")
					.toString();
			logger.info("forwardURL: "+forwardURL);
			if (logger.isDebugEnabled()) logger.debug("forward to {}", forwardURL);
			request.getRequestDispatcher(forwardURL).forward(request, response);
			return;
		}
		super.handleRequest(request, response);
	}
	
	
}
