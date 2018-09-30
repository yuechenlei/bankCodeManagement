<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<%@ include file="../include/commons-include.jsp" %>
	<%
	response.setHeader("Pragma ", "public");
	response.setHeader("Cache-Control ", "must-revalidate, post-check=0, pre-check=0 ");
	response.addHeader("Cache-Control ", "public ");
	response.addHeader("Content-Disposition", "attachment; filename="+java.net.URLEncoder.encode("卡bin", "utf8")+".xls");
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
				<th width="20%">发卡行名称</th>
				<th width="10%">机构代码</th>
				<th width="20%">卡  名</th>
				<th width="10%">卡号长度</th>
				<th width="10%">识别码长度</th>
				<th width="10%">识别码</th>
				<th width="10%">卡种</th>
				<th width="10%">银行编号</th>
			</tr>
			<c:forEach items="${iins}" var="iin">
				<tr>
					<td>${iin.agencyName}</td>
					<td class="format">${iin.agencyCode}</td>
					<td>${iin.cardName}</td>
					<td>${iin.panLength}</td>
					<td>${iin.length}</td>
					<td class="format">${iin.code}</td>
					<td>${iin.cardType}</td>
					<td>${iin.providerCode}</td>
				</tr>
			</c:forEach>
		</table>
</body>
</html>
