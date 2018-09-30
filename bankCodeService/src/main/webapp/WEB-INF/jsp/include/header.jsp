<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
try{if(window.console&&window.console.log){console.log("%cHerzlich Willkommen","color:green");}}catch(e){};
</script>
<style type="text/css">
.onHover:hover{
	text-decoration:underline;
	color: rgb(2,120,210);
	}
</style>
<%
	String user = (String)session.getAttribute("user");
	// 请求路径
	String requestUrl = request.getRequestURL().toString();
	if (user == null || "".equals(user)) {
		String url = request.getContextPath() + "/toLogin.htm";
		response.sendRedirect(url);
	}
	if (requestUrl.contains("add") && !"admin".equals(user)) {
		String url = request.getContextPath() + "/error/toPermission.htm?type=A";
		response.sendRedirect(url);
	}
%>
</head>

<body>
<div style="margin-left: 91%; margin-top: 7px;font-size:16px;">
	<a href="${pageContext.request.contextPath}/toGuide.htm" class="onHover">回到主页</a>
	|
	<a href="${pageContext.request.contextPath}/toLogout.htm" class="onHover">退出</a>
</div>
</body>
</html>