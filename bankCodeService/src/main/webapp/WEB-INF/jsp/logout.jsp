<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include/header.jsp"%>
<%@include file="include/commons-include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3CUTF-8//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Bank code management</title>
<%
	session.removeAttribute("user");
	String url = request.getContextPath();
	response.sendRedirect(url);
%>
</head>

<body>
</body>
</html>