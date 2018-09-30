<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<%@ include file="../include/commons-include.jsp" %>
	<%
	response.setHeader("Pragma ", "public");
	response.setHeader("Cache-Control ", "must-revalidate, post-check=0, pre-check=0 ");
	response.addHeader("Cache-Control ", "public ");
	response.addHeader("Content-Disposition", "attachment; filename="+java.net.URLEncoder.encode("银联地区码", "utf8")+".xls");
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
	<table cellpadding="0" cellspacing="1" align="center"  class="global_tableresult" style="width: 40%;font-size:14px; font-family:宋体, Arial;">
			<tr>
				<th width="20%">名称</th>
				<th width="20%">银联地区码</th>
			</tr>

			<c:forEach items="${adl}" var="proMap" >
			<tr>
				<th>${proMap['proName']}</th>
				<th class="format">${proMap['proCode']}</th>
			</tr>
			<c:forEach items="${proMap['cityList'] }" var="cityMap">
			<tr>
				<td style="font-weight:bold;">${cityMap['cityName'] }</td>
				<td class="format" style="font-weight:bold;">${cityMap['cityCode'] }</td>
			</tr>
			<c:forEach items="${cityMap['couMap'] }" var="cm">
			<tr>
				<td>${cm.value }</td>
				<td class="format">${cm.key }</td>
			</tr>
			</c:forEach>
			</c:forEach>
			</c:forEach>

		</table>
</body>
</html>
