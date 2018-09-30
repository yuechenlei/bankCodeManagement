<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<%@ include file="../include/commons-include.jsp" %>
	<%
	response.setHeader("Pragma ", "public");
	response.setHeader("Cache-Control ", "must-revalidate, post-check=0, pre-check=0 ");
	response.addHeader("Cache-Control ", "public ");
	response.addHeader("Content-Disposition", "attachment; filename="+java.net.URLEncoder.encode("银联地区码表", "utf8")+".xls");
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

	<table cellpadding="0" cellspacing="1" align="center"  class="global_tableresult" style="width: 80%;font-size:14px; font-family:宋体, Arial;">
			<c:if test="${adl!= null && fn:length(adl) > 0}">
			<tr>
				<th width="20%">名称</th>
				<th width="20%">银联地区码</th>
				<c:if test="${adl['mark'] == 'higher'}">
				<th width="20%">上级名称</th>
				<th width="20%">上级地区码</th>
				</c:if>
				<c:if test="${adl['mark'] == 'lower'}">
				<th width="20%">下级名称</th>
				<th width="20%">下级地区码</th>
				</c:if>
			</tr>
			<c:if test="${adl['lowerMap'] == null && adl['mark'] == 'higher'}">
			<tr>
				<td>${adl['name']}</td>
				<td class="format">${adl['code']}</td>
				<td>${adl['higherName']}</td>
				<td class="format">${adl['higherCode']}</td>
			</tr>
			</c:if>
			<c:if test="${adl['lowerMap'] != null  && fn:length(adl['lowerMap']) > 0 }">
			<c:forEach items="${adl['lowerMap']}" var="lowerMap">
			<tr>
				<td>${adl['name']}</td>
				<td class="format">${adl['code']}</td>
				<td>${lowerMap.value}</td>
				<td class="format">${lowerMap.key}</td>
			</tr>
			</c:forEach>
			</c:if>
			<c:if test="${(adl['lowerMap'] == null || fn:length(adl['lowerMap']) < 1) && adl['mark'] == 'lower' }">
			<tr>
				<td>${adl['name']}</td>
				<td  class="format">${adl['code']}</td>
				<td></td>
				<td></td>
			</tr>
			</c:if>
			</c:if>

			<c:if test="${adMap['adList']!= null && fn:length(adMap['adList']) > 0}">
			<tr>
				<th width="20%">名称</th>
				<th width="10%">银联地区码</th>
				<c:if test="${adMap['mark'] == 'higher'}">
				<th width="20%">上级名称</th>
				<th width="10%">上级地区码</th>
				</c:if>
				<c:if test="${adMap['mark'] == 'lower'}">
				<th width="20%">下级名称</th>
				<th width="10%">下级地区码</th>
				</c:if>
			</tr>
			<c:if test="${adMap['adList']!= null && fn:length(adMap['adList']) > 0}">
			<c:forEach  items="${adMap['adList']}" var="adl">
			<c:if test="${adl['lowerMap'] == null && adMap['mark'] == 'higher' }">
			<tr id="${adl['code']}">
				<td id="${adl['code']}1">${adl['name']}</td>
				<td id="${adl['code']}2">${adl['code']}</td>
				<td>${adl['higherName']}</td>
				<td id="${adl['higherCode']}">${adl['higherCode']}</td>
			</tr>
			</c:if>
			<c:if test="${adl['lowerMap'] != null && fn:length(adl['lowerMap']) > 0 }">
			<c:forEach items="${adl['lowerMap']}" var="lowerMap">
			<tr id="${adl['code']}">
				<td id="${adl['code']}1">${adl['name']}</td>
				<td id="${adl['code']}2">${adl['code']}</td>
				<td>${lowerMap.value}</td>
				<td>${lowerMap.key}</td>
			</tr>
			</c:forEach>
			</c:if>
			<c:if test="${(adl['lowerMap'] == null || fn:length(adl['lowerMap']) < 1 ) && adMap['mark'] == 'lower' }">
			<tr id="${adl['code']}">
				<td id="${adl['code']}1">${adl['name']}</td>
				<td id="${adl['code']}2">${adl['code']}</td>
				<td></td>
				<td></td>
			</tr>
			</c:if>
			</c:forEach>
			</c:if>
			</c:if>
		</table>
</body>
</html>
