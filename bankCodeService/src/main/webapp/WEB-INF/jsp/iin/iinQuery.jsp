<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<%@include file="../include/commons-include.jsp"%>
<%@include file="../include/Dialog.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/bankCodeService/resources/js/iin.js"></script>
<title>卡识别管理</title>
<style type="text/css">
.total_panel .batch_1{float:left; margin:0px 0px 0px 20px;}
.total_panel .add_1{float:left; margin:0px 0px 0px 20px;}
</style>
<script type="text/javascript">

</script>
</head>
<body style="overflow:auto;">
	<div class="search_tit" >
		<h2>卡识别</h2>
	</div>
	<br>
	<form:form id="iinForm" action="${pageContext.request.contextPath}/iin/iinQuery.htm" method="post"
	target="queryResult" modelAttribute="cardNo" style="font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif;">
		<div  style="height:30px"></div>
		<span style="margin-left:35%">银行卡号：</span><input type="text" id="cardNo" name="cardNo" size="25" style="font-size:16px;" /> &nbsp;&nbsp;
		<input type="button" value="识  别" onclick="identify();" class="global_btn">
		<br>
	</form:form>
	<br>
	<div class="total_panel">
		<c:if test="${user == 'admin' }" >
		<div class="add_1" onclick="javascript:alertIinWindow();return false;" style="cursor:pointer" >
				<a href="javascript:alertIinWindow();" class="onHover" >新  增</a>
		</div>
		<span class="add_1">|</span>
		<div class="batch_1" onclick="javascript:alertBatchWindow();return false;" style="cursor:pointer" >
				<a href="javascript:alertBatchWindow();" class="onHover" >批量修改</a>
		</div>
		<a href="javascript:totalExport();" class="export onHover" >全部导出</a>
		<span class="export">|</span>
		<a href="javascript:exportIin();" class="export onHover" >导 出</a>
		</c:if>
	</div>
	<iframe name="queryResult" src="" style="width:100%;height:auto;min-height:800px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>
	<iframe name="exportResult" src="" style="width:100%;height:auto;min-height:800px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>

	<c:if test="${user == 'admin' }" >
	<div id="dialog_html" title="">
	<div id="increaseIin" style="display:none">
	<form id="addIinForm" enctype="multipart/form-data" style="margin:10px 5% auto;font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif">
		<div>
		<dl>
			<br/>
			<dd>
				<input name="separator">&nbsp;&nbsp;<span>分隔符(不写默认为英文逗号“,”)</span>
			</dd>
		</dl>
		<br>
		<dl>
			<dd>1、文本方式：发行者识别信息(按以下顺序：发卡行名称,机构代码,卡名,卡号长度,识别码长度,识别码,卡种,银行编码)</dd>
			<br>
			<dd>
				<textarea name="iinText" rows="15" cols="120" style="font-size:16px;" autofocus="autofocus" ></textarea>
			</dd>
		</dl>
		<br><br>
		<dl>
			<dd>2、txt文件方式：发行者识别信息(按以下顺序：发卡行名称,机构代码,卡名,卡号长度,识别码长度,识别码,卡种,银行编码)</dd>
			<br>
			<dd>
				<input type="file" name="iinTxt" />
			</dd>
		</dl>
		</div>
		<div>
			<input type="button" class="global_btn" value="新  增" onclick="javascript:addIin()" style="margin-left:40%" id="addButton" />
			<input type="reset" class="global_btn" value="重  置" />
			<span id="loadingDiv" style="margin:0px 0px 0px 10px;display:none;">
				<img alt="请稍等" src="/bankCodeService/resources/image/loading_2.gif" >
			</span>
		</div>
		<br>
		<span id="errorMsg" style="color:red;"></span>
	</form>
	</div>
	</div>
	</c:if>

	<div id="dialog-message-1" title="提示信息">
	<br/>
	<span id="message-show-1" style="color:blue;"></span>
	</div>

</body>
</html>