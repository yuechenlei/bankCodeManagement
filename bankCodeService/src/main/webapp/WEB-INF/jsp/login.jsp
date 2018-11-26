<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include/commons-include.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Bank code management</title>
<link href="<spring:url value="/resources/css/test2.css" />" rel="stylesheet" type="text/css" />
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	if("guest".equals(username) && "guest".equals(password)){
		session.setAttribute("user", "guest");
		session.setMaxInactiveInterval(3600);
		String url = request.getContextPath() + "/toGuide.htm";
		response.sendRedirect(url);
	}
	if("admin".equals(username) && "lefu2015".equals(password)){
		session.setAttribute("user", "admin");
		session.setMaxInactiveInterval(3600);
		String url = request.getContextPath() + "/toGuide.htm";
		response.sendRedirect(url);
	}
%>

</head>

<body>
<div style="text-align: center; width:98%">
	<div style="margin: 250px auto">
		<form action="toLogin.htm" method="POST" >
			<div style="font-size:14px;font-family:宋体, Arial, Helvetica, sans-serif">
				用户：<input type="text" name="username" size="25" />
			</div>
			<br/>
			<div style="font-size:14px;font-family:宋体, Arial, Helvetica, sans-serif">
				密码：<input type="password" name="password" size="25" />
			</div>
			<br/>
			<div style="margin: 5px">
				<input type="submit" value="登  录" class="global_btn">
			</div>
		</form>
	</div>
</div>
</body>
</html>