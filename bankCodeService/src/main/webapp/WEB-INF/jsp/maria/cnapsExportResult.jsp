<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<%@ include file="../include/commons-include.jsp" %>
	<%
	response.setHeader("Pragma ", "public");
	response.setHeader("Cache-Control ", "must-revalidate, post-check=0, pre-check=0 ");
	response.addHeader("Cache-Control ", "public ");
	response.addHeader("Content-Disposition", "attachment; filename="+java.net.URLEncoder.encode("联行号-总行清分行号", "utf8")+".xls");
	response.setContentType("application/vnd.ms-excel.numberformat:@;charset=UTF-8");
	%>
<html>
<head>
<meta http-equiv="Content-Type" content="charset=utf-8" />
<style type="text/css">
table {
	border: thin solid #333;
	border-collapse: collapse;
	border-spacing: 0px;
}

th {
	background-color: #ccc;
	border: thin solid #333;
}

td {
	border: thin solid #000;
}

.format{
mso-number-format:'\@';
}
</style>

</head>
<body>
	<table cellpadding="0" cellspacing="1" align="center"  class="global_tableresult" style="width: 100%">
			<tr>
				<th width="15%">联行号</th>
				<th width="30%">银行名称</th>
				<th width="15%">清分行号</th>
				<th width="10%">清算级别</th>
				<th width="15%">银行编码</th>
				<th width="15%">地区编码</th>
			</tr>
			<c:forEach items="${cnapsl}" var="cnaps">
				<tr>
					<td class="format">${cnaps.code}</td>
					<td>${cnaps.name}</td>
					<td class="format">${cnaps.clearingBankCode}</td>
					<td>${cnaps.clearingBankLevel}</td>
					<td>${cnaps.providerCode}</td>
					<td>${cnaps.adCode}</td>
				</tr>
			</c:forEach>
		</table>
</body>
</html>
