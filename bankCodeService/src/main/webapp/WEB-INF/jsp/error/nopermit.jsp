<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include/commons-include.jsp"%>

<html>
	<head>
		<script>
			$(function() {
				window.top.location.href = "${pageContext.request.contextPath}/toLogin.htm";
			});
		</script>
	</head>
	<body>
	</body>
</html>