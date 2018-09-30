<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML >
<%@ include file="../include/commons-include.jsp" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/bankCodeService/resources/js/iin.js"></script>
	<script type="text/javascript">
		$(function(){
			initPage('${page.currentPage}','${page.totalResult}','${page.totalPage}','${page.entityOrField}',parent.document.getElementById("iinForm"));
		});
	</script>

</head>
<body style="overflow:auto;">
	<div class="pop_tit"><h2>查询结果</h2></div>
	<c:if test="${iins!= null && fn:length(iins) > 0}">
		<table id="queryResult" cellpadding="1" cellspacing="1" align="center" class="global_tableresult" style="width:100%;margin-left:auto;margin-right:auto;">
			<tr>
				<th width="20%">发卡行名称</th>
				<th width="10%">机构代码</th>
				<th width="20%">卡  名</th>
				<th width="10%">卡号长度</th>
				<th width="10%">识别码长度</th>
				<th width="10%">识别码</th>
				<th width="10%">卡种</th>
				<th width="10%">银行编号</th>
				<c:if test="${user == 'admin' }" >
				<th width="10%">操作</th>
				</c:if>
			</tr>
			<c:forEach items="${iins}" var="iin">
				<tr id="${iin.code}${iin.length}${iin.panLength}" >
					<td id="agencyName${iin.code}${iin.length}${iin.panLength}" >${iin.agencyName}</td>
					<td id="agencyCode${iin.code}${iin.length}${iin.panLength}" >${iin.agencyCode}</td>
					<td id="cardName${iin.code}${iin.length}${iin.panLength}" >${iin.cardName}</td>
					<td id="panLength${iin.code}${iin.length}${iin.panLength}" >${iin.panLength}</td>
					<td id="length${iin.code}${iin.length}${iin.panLength}" >${iin.length}</td>
					<td id="code${iin.code}${iin.length}${iin.panLength}" >${iin.code}</td>
					<td id="cardType${iin.code}${iin.length}${iin.panLength}" >${iin.cardType}</td>
					<td id="providerCode${iin.code}${iin.length}${iin.panLength}" >${iin.providerCode}</td>
					<c:if test="${user == 'admin' }" >
					<td style="font-size:14px;" >
					<a onclick="javascript:modify('${iin.agencyName}','${iin.agencyCode}','${iin.cardName}',
					'${iin.panLength}','${iin.length}','${iin.code}','${iin.cardType}','${iin.providerCode}');return false;"
					id="modify${iin.code}${iin.length}${iin.panLength}" href="javascript:void(0);">修改</a>
					<a onclick="javascript:confirmModify('${iin.agencyName}','${iin.agencyCode}','${iin.cardName}',
					'${iin.panLength}','${iin.length}','${iin.code}','${iin.cardType}','${iin.providerCode}');return false;"
					id="confirm${iin.code}${iin.length}${iin.panLength}" href="javascript:void(0);" style="display:none;">确定</a>
					<span id="line${iin.code}${iin.length}${iin.panLength}" style="display:none;">|</span>
					<a onclick="javascript:cancleModify('${iin.agencyName}','${iin.agencyCode}','${iin.cardName}',
					'${iin.panLength}','${iin.length}','${iin.code}','${iin.cardType}','${iin.providerCode}');return false;"
					id="cancle${iin.code}${iin.length}${iin.panLength}" href="javascript:void(0);" style="display:none;">取消</a>
					</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		<br>
	</c:if>
	<c:if test="${msg !=null }">
	<br>
	<span style="color:red;">${msg }</span>
	</c:if>
	<c:if test="${fn:length(iins)== 0 && msg == null}">
		无符合条件的记录
	</c:if>
	<c:if test="${error != null && error !=''}">
		---${error}
	</c:if>

	<div id="dialog-message-2" title="提示信息" style="display:none;" >
	<br/>
	<span id="message-show-2" ></span>
	</div>

</body>
</html>