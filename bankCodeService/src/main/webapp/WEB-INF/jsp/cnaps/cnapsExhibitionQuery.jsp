<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@include file="../include/header.jsp"%>
<%@include file="../include/commons-include.jsp"%>
<%@include file="../include/Dialog.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/bankCodeService/resources/js/cachecenter.js"></script>
<style type="text/css">
.total_panel .batch_1{float:left; margin:0px 0px 0px 20px;}
.total_panel .add_1{float:left; margin:0px 0px 0px 20px;}
</style>
<title>联行号管理</title>
</head>
<body style="overflow:auto;">

	<div class="search_tit">
		<h2>联行号管理</h2>
	</div>
	<br>
	<form:form id="form" action="${pageContext.request.contextPath}/cnaps/cnapsExhibitionQuery.htm" method="post"
	target="queryResult" modelAttribute="cnaps">
		<table cellpadding="0" cellspacing="1" align="center"
			class="global_table">
			<tr>
				<th>联&nbsp;&nbsp;行&nbsp;&nbsp;号</th>
				<td><input type="text" id="cncode" name="code" value=""></td>
				<th>银行名称</th>
				<td><input type="text" id="bankName" name="name" value="" onchange="javascript:autoGrowth()"></td>
				<th>清分行号</th>
				<td><input type="text" id="clearingBankCode"
					name="clearingBankCode" value="" disabled="disabled"></td>
			</tr>
			<tr>
				<th>清算级别</th>
				<td>
					<select name="clearingBankLevel" id="clearingBankLevel" >
					<option value="0" ></option>
					<option value="1" >总行</option>
					<option value="2" >分行</option>
					<option value="-1" >支行</option>
					</select>
				</td>
				<th>银行编码</th>
				<td><input type="text" id="proCode" name="providerCode"  value=""></td>
				<th>地区编码</th>
				<td><input type="text" id="areaCode" name="adCode"  value=""></td>
			</tr>

		</table>
		<br/>
		<div>
		<input type="submit" class="global_btn" style="margin-left:45%" value="查  询" />
		<input type="reset" class="global_btn" style="margin:0px 20px auto" value="重  置" />
		</div>
	</form:form>
	<div class="total_panel">
		<c:if test="${user == 'admin' }" >
		<div class="add_1" onclick="javascript:alertWindow();return false;" style="cursor:pointer" >
				<a href="javascript:alertWindow();" class="onHover" >新  增</a>
		</div>
		<span class="add_1">|</span>
		<div class="batch_1" onclick="javascript:alertBatchWindow();return false;" style="cursor:pointer" >
				<a href="javascript:alertBatchWindow();" class="onHover" >批量修改</a>
		</div>
		<a href="javascript:cnapsTotalExport();" class="export onHover" >全部导出</a>
		<span class="export">|</span>
		<a href="javascript:exportResult();" class="export onHover" >导 出</a>
		</c:if>
	</div>

	<div id="dialog-message-1" title="提示信息" style="display:none">
		<br/>
		<div>
		<span id="message-show-1" style="color:red;"></span>
		</div>
	</div>

	<c:if test="${user == 'admin' }" >
	<div id="dialog_html" title="">
	<div id="increaseCnaps" style="display:none">
	<form id="addCnapsForm" enctype="multipart/form-data" style="margin:0px 5% auto; font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif">
		<br>
		<dl>
			<dd>1、文本形式：联行号信息(按以下顺序：联行号,银行名称,清算行号,清算行级别,银行编码,行政区划码。中间以英文逗号分割)</dd>
			<br>
			<dd>
				<textarea id="cnapsText" name="cnapsText" rows="12" cols="130" style="font-size:16px;" autofocus="autofocus" ></textarea>
			</dd>
		</dl>
		<br><br>
		<dl>
			<dd>2、txt文件形式：联行号信息(按以下顺序：联行号,银行名称,清算行号,清算行级别,银行编码,行政区划码。中间以英文逗号分割)</dd>
			<br>
			<dd>
				<input type="file" name="cnapsTxt" id="cnapsTxt" />
			</dd>
		</dl>
		<br><br>
		<dl>
			<dd>3、excel文件形式&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:downloadTemplate('cnapsTemplate');" class="onHover">下载模版</a></dd>
			<br>
			<dd>
				<input type="file" name="cnapsExcel" id="cnapsExcel" />
			</dd>
		</dl>
		<br>
		<center>
			<input type="button" class="global_btn" value="新  增" onclick="javascript:addCnaps()" id="addButton"/>
			<input type="reset" class="global_btn" value="重  置" />
			<span id="loadingDiv" style="margin:0px 0px 0px 10px;display:none;">
				<img alt="请稍等" src="/bankCodeService/resources/image/loading_2.gif" >
			</span>
		</center>
		<br>
		<span id="errorMsg" style="color:red;"></span>
	</form>

	</div>
	</div>
	</c:if>
	<div>
	<form action="${pageContext.request.contextPath}/cnaps/downloadTemplate.htm" style="display:none;" target="exportResult" method="post" id="downloadForm">
		<input type="hidden" name="exportData" value="" id="exportData"/>
	</form>
	</div>
	<iframe name="queryResult" src="" style="width:100%;height:auto;min-height:2500px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>
	<iframe name="exportResult" src="" style="width:100%;height:auto;min-height:2500px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>
</body>
</html>