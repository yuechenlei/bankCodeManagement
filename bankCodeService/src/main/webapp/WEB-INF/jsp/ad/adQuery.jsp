<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@include file="../include/header.jsp"%>
<%@include file="../include/commons-include.jsp"%>
<%@include file="../include/Dialog.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>银联地区码管理</title>
<script type="text/javascript" src="/bankCodeService/resources/js/administrative.js"></script>
<script type="text/javascript">
// 绑定enter事件
document.onkeydown=function() {
	if (event.keyCode == 13) {
		var btn = document.getElementById("adQueryButton");
		btn.focus();
		btn.click();
		}
}
</script>
<style type="text/css">
.adHover:hover{
		color:red;
		cursor:pointer;
	}
</style>

</head>
<body style="overflow:auto;">

	<div class="search_tit">
		<h2>银联地区码管理</h2>
	</div>
	<br>
	<form:form id="adForm" action="${pageContext.request.contextPath}/ad/adQuery.htm" method="post"
	target="queryResult" >
		<table cellpadding="0" cellspacing="1" align="center" class="global_table">
			<tr>
				<th style="font-size:16px; font-family:宋体, Arial;" >地区代码</th>
				<td><input type="text" id="adCode" name="adCode" style="font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif;" ></td>
				<th style="font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif;" >地区名称</th>
				<td><input type="text" id="adName" name="adName" style="font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif;" ></td>
			</tr>
		</table>
		<br>
		<div style="font-size:16px; font-family:宋体, Arial;margin-left:10.5%;color:blue" >
			<span>请选择：</span>
			<input type="radio" name="mark" id="county" value="higher" checked="checked"/><label for="county" class="adHover">上级&nbsp;&nbsp;</label>
			<input type="radio" name="mark" id="province" value="lower" /><label for="province" class="adHover">下级&nbsp;&nbsp;</label>
		</div>
		<br/>
		<div>
		<c:if test="${user == 'admin' }" >
		<input type="button" id="newIncrease" value="新增" style="margin-left:50px; width:55px; height:25px;cursor:pointer;" onclick="javascript:alertWindow()"/>
		</c:if>
		<c:if test="${user != 'admin' }" >
		<input type="button" style="margin: auto 50px auto; width:55px; height:25px;visibility:hidden;"/>
		</c:if>
		<input type="button" class="global_btn" style="margin-left:33.5%" value="查  询" onclick="javascript:adQuery();" id="adQueryButton" />
		<input type="reset" class="global_btn" style="margin:0px 20px auto" value="重  置" />
		</div>
	</form:form>
	<br/>

	<div class="total_panel">
	<c:if test="${user == 'admin' }" >
		<a href="javascript:adTotalExport();" class="export onHover" >全部导出</a>
		<span class="export">|</span>
		<a href="javascript:adExport();" class="export onHover" >导出</a>
		</c:if>
	</div>

	<div id="dialog-message-1" title="提示信息" style="display:none">
		<br/>
		<div>
		<span id="message-show-1" style="color:blue;" autofocus="autofocus" ></span>
		</div>
	</div>

	<c:if test="${user == 'admin' }" >
	<div id="dialog_html" title="新增银联地区码">
	<div id="increaseAd" style="display:none">
	<form id="addCouForm" enctype="multipart/form-data" style="margin:10px 5% auto;font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif">
		<dl>
		<dd>
		<span style="font-size:18px;color:blue;" >1、添加县</span>
		</dd>
		</dl>
		<br>
		<dl>
			<dd>(1)文字形式：县信息(按以下顺序：县名称,县地区码,市地区码。以英文逗号分割)</dd>
			<br>
			<dd>
				<textarea name="countyText" rows="5" cols="120" style="font-size:16px;" autofocus="autofocus" ></textarea>
			</dd>
		</dl>
		<br>
		<dl>
			<dd>(2)txt文件形式：县信息(按以下顺序：县名称,县地区码,市地区码。以英文逗号分割)</dd>
			<br>
			<dd>
				<input type="file" name="countyTxt" />
			</dd>
		</dl>
		<br>
		<dl>
			<dd>(3)excel文件形式：县信息(按以下顺序：县名称,县地区码,市地区码) &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:pointer;font-size:14px;text-decoration: underline;" onclick="javascript:downloadTemplate('countyTemplate');">下载模版</span></dd>
			<br>
			<dd>
				<input type="file" name="countyExcel" />
			</dd>
		</dl>
		<br>
		<center>
			<input type="button" class="global_btn" value="新  增" onclick="javascript:addCountys()"/>
			<input type="reset" class="global_btn" value="重 置" />
		</center>
		<br>
		<span id="errorMsg_cou" style="color:red;"></span>
	</form>

	<form id="addCityForm" enctype="multipart/form-data" style="margin:10px 5% auto;font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif">
		<dl>
		<dd>
		<span style="font-size:18px;color:blue;" >2、添加市</span>
		</dd>
		</dl>
		<br>
		<dl>
			<dd>(1)文字形式：市信息(按以下顺序：市名称,市地区码,省地区码。以英文逗号分割)</dd>
			<br>
			<dd>
				<textarea name="cityText" rows="5" cols="120" style="font-size:16px;" ></textarea>
			</dd>
		</dl>
		<br>
		<dl>
			<dd>(2)txt文件形式：市信息(按以下顺序：市名称,市地区码,省地区码。以英文逗号分割)</dd>
			<br>
			<dd>
				<input type="file" name="cityTxt" />
			</dd>
		</dl>
		<br>
		<dl>
			<dd>(3)excel文件形式：市信息(按以下顺序：市名称,市地区码,省地区码) &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:pointer;font-size:14px;text-decoration: underline;" onclick="javascript:downloadTemplate('cityTemplate');">下载模版</span></dd>
			<br>
			<dd>
				<input type="file" name="cityExcel" />
			</dd>
		</dl>
		<br>
		<center>
			<input type="button" class="global_btn" value="新  增" onclick="javascript:addCitys()"/>
			<input type="reset" class="global_btn" value="重 置" />
		</center>
		<br>
		<span id="errorMsg_city" style="color:red;"></span>
	</form>

	<form id="addProForm" enctype="multipart/form-data" style="margin:10px 5% auto;font-size:16px; font-family:宋体, Arial, Helvetica, sans-serif">
		<dl>
		<dd>
		<span style="font-size:18px;color:blue;" >3、添加省</span>
		</dd>
		</dl>
		<br>
		<dl>
			<dd>(1)文字形式：省信息(按以下顺序：省名称,省地区码。以英文逗号分割)</dd>
			<br>
			<dd>
				<textarea name="provinceText" rows="5" cols="120" style="font-size:16px;" ></textarea>
			</dd>
		</dl>
		<br>
		<dl>
			<dd>(2)txt文件形式：省信息(按以下顺序：省名称,省地区码。以英文逗号分割)</dd>
			<br>
			<dd>
				<input type="file" name="provinceTxt" />
			</dd>
		</dl>
		<br>
		<dl>
			<dd>(3)excel文件形式：省信息(按以下顺序：省名称,省地区码) &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:pointer;font-size:14px;text-decoration: underline;" onclick="javascript:downloadTemplate('provinceTemplate');">下载模版</span></dd>
			<br>
			<dd>
				<input type="file" name="provinceExcel" />
			</dd>
		</dl>
		<br>
		<center>
			<input type="button" class="global_btn" value="新  增" onclick="javascript:addProvinces();" />
			<input type="reset" class="global_btn" value="重 置" />
		</center>
		<br>
		<span id="errorMsg_pro" style="color:red;"></span>
	</form>
	</div>
	</div>
	</c:if>

	<div>
	<form action="${pageContext.request.contextPath}/ad/downloadTemplate.htm" style="display:none;" target="queryResult" method="post" id="downloadForm">
		<input type="hidden" name="exportData" value="" id="exportData"/>
	</form>
	</div>
	<iframe name="queryResult" src="" style="width:100%;height:auto;min-height:1000px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>
	<iframe name="exportResult" src="" style="width:100%;height:auto;min-height:1000px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>
</body>
</html>