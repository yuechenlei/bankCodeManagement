<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML >
<%@ include file="../include/commons-include.jsp" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/bankCodeService/resources/js/cachecenter.js"></script>

	<script type="text/javascript">
		$(function(){
			initPage('${page.currentPage}','${page.totalResult}','${page.totalPage}','${page.entityOrField}',parent.document.getElementById("form"));
		});
	</script>

</head>
<body style="overflow:auto;">
	<div class="pop_tit"><h2>查询结果</h2></div>
	<c:if test="${cnapsl!= null && fn:length(cnapsl) > 0}">
		<table id="queryResult" cellpadding="1" cellspacing="1" align="center" class="global_tableresult" style="width:100%;margin-left:auto;margin-right:auto;">
			<tr>
				<th width="15%">联行号</th>
				<th width="30%">银行名称</th>
				<th width="15%">清分行号</th>
				<th width="10%">清算级别</th>
				<th width="10%">银行编码</th>
				<th width="10%">地区编码</th>
				<c:if test="${user == 'admin' }" >
				<th width="10%">操作</th>
				</c:if>
			</tr>
			<c:forEach items="${cnapsl}" var="cnaps">
				<tr id="${cnaps.code}">
					<td id="${cnaps.code}1">${cnaps.code}</td>
					<td id="${cnaps.code}2">${cnaps.name}</td>
					<td id="${cnaps.code}3">${cnaps.clearingBankCode}</td>
					<td id="${cnaps.code}4">${cnaps.clearingBankLevel}</td>
					<td id="${cnaps.code}5">${cnaps.providerCode}</td>
					<td id="${cnaps.code}6">${cnaps.adCode}</td>
					<c:if test="${user == 'admin' }" >
					<td>
						<a id="${cnaps.code}Mod" style="display:inline" href="javascript:cnapsModify('${cnaps.code }')">修改</a>
						<a id="${cnaps.code}Con" style="display:none" href="javascript:cnapsConfirm('${cnaps.code }','${cnaps.name }',
							'${cnaps.clearingBankCode }','${cnaps.clearingBankLevel }','${cnaps.providerCode }','${cnaps.adCode }')">确定</a>
						<a id="${cnaps.code}Line" style="display:none"><span>|</span></a>
						<a id="${cnaps.code}Can" style="display:none" href="javascript:cnapsCancel('${cnaps.code }','${cnaps.name }',
							'${cnaps.clearingBankCode }','${cnaps.clearingBankLevel }','${cnaps.providerCode }','${cnaps.adCode }')">取消</a>
						<a id="${cnaps.code}Line2" style="display:inline"><span>|</span></a>
						<a id="${cnaps.code}Del" href="javascript:cnapsDel('${cnaps.code }')">删除</a>
					</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		<br>

		<%@ include file="../include/page.jsp" %>
	</c:if>
	<c:if test="${msg !=null }">
	<br>
	<span style="color:red;">${msg }</span>
	</c:if>
	<c:if test="${fn:length(cnapsl)== 0 && msg == null}">
		无符合条件的记录
	</c:if>
	<c:if test="${error != null && error !=''}">
		---${error}
	</c:if>

	<div id="dialog-message-2" title="提示信息" style="display:none;">
	<br/>
	<span id="message-show-2" style="color:red;" autofocus ></span>
	</div>

</body>
</html>