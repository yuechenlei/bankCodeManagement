<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../include/commons-include.jsp"%>
<%@ include file="../include/header.jsp"%>

<html>
	<head>
		<script>

			$(function() {

				<%  String type = request.getParameter("type");
					if("A".equals(type)){
						out.println("noPermit();");
					}else if("B".equals(type)){
						out.println("sessionExpired();");
					}else if("C".equals(type)){
						out.println("compelLogout();");
					}
				%>
			});

			function noPermit(){
				alter("无操作此功能的权限！");
				window.top.location.href = "${pageContext.request.contextPath}";
			}

			function sessionExpired(){
				alert("Session信息过期或未登录，请重新登录系统！");
				window.top.location.href = "${pageContext.request.contextPath}/toLogin.htm";
			}

			function compelLogout() {
				alert("用户名在其它客户端重新登录，您被强制登出！");
				window.top.location.href = "${pageContext.request.contextPath}/toLogin.htm";
			}
		</script>
	</head>
	<body>
	</body>
</html>