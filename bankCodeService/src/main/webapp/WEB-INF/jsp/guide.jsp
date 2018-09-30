<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include/commons-include.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Bank code management</title>
<style type="text/css">
	div{
		margin-top: 10px;
	}
	.onHover:hover{
	text-decoration:underline;
	color: rgb(2,120,210);
	}
	.oldPage{
	display:none;
	}
</style>
<script type="text/javascript">
function toOldPage(){
	$(".oldPage").attr("class","newPage");
	$("#oldView").hide();
	$("#newView").show();
	$(".newPage2").hide();
	//$("#position").attr("style","width: 970px; margin: 100px auto;");
}
function toNewPage(){
	$(".newPage").attr("class","oldPage");
	$("#oldView").show();
	$("#newView").hide();
	$(".newPage2").show();
	//$("#position").attr("style","width: 970px; margin: 160px auto;");
}
</script>
</head>

<body>
<div style="margin-left: 96%; margin-top: 7px;font-size:16px;">
    <!-- 
	<c:if test="${user == 'admin' }" >
	<a href="javascript:void(0);" class="onHover" onclick="javascript:toOldPage();return false;" id="oldView">回到老页面</a>
	<a href="javascript:void(0);" class="onHover" onclick="javascript:toNewPage();return false;" style="display:none;" id="newView">回到新页面</a>
	|
	</c:if>
	<c:if test="${user != 'admin' }" >
	<span style="visibility:hidden;" >回到老页面</span>
	</c:if>
	 -->
	<a href="${pageContext.request.contextPath}/toLogout.htm" class="onHover">退出</a>
</div>
<div style="text-align:center; font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif">
	<div style="width: 970px; margin: 160px auto;" id="position" >
		<div>
			<a href="${pageContext.request.contextPath}/cnaps/cnapsExhibitionQuery/index.htm" class="onHover">联行号管理</a>
		</div>
		<br class="newPage2">
		<div>
			<a href="${pageContext.request.contextPath}/iin/iinQuery/index.htm" class="onHover">卡识别管理</a>
		</div>
		<br class="newPage2">
		<div>
			<a href="${pageContext.request.contextPath}/ad/adQuery/index.htm" class="onHover">银联地区码管理</a>
		</div>
	</div>
</div>
</body>
</html>