<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="WEB-INF/jsp/include/commons-include.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>银行编码服务</title>
<%
	String url = request.getContextPath() + "/toLogin.htm";
	response.sendRedirect(url);
%>

</head>

<body>
<div></div><br>
<h2>银行编码服务</h2>
</body>
</html>