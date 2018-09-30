<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML >
<%@ include file="../include/commons-include.jsp" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/bankCodeService/resources/js/administrative.js"></script>
</head>
<body style="overflow:auto;">
	<div class="pop_tit"><h2>查询结果</h2></div>

		<table id="queryResult" cellpadding="1" cellspacing="1" align="center" class="global_tableresult" style="width:100%;margin-left:auto;margin-right:auto;">
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
				<c:if test="${user == 'admin' }" >
				<th width="20%">操作</th>
				</c:if>
			</tr>
			<c:if test="${adl['lowerMap'] == null && adl['mark'] == 'higher' }">
			<tr id="${adl['code']}">
				<td id="${adl['code']}1">${adl['name']}</td>
				<td id="${adl['code']}2">${adl['code']}</td>
				<td>${adl['higherName']}</td>
				<td id="${adl['higherCode']}">${adl['higherCode']}</td>
				<input type="hidden"  value="${adl['genre']}" id="genre" />
				<c:if test="${user == 'admin' }" >
				<td>
					<a id="${adl['code']}Mod" style="display:inline" href="javascript:adModify('${adl['code']}','${adl['higherCode']}')">修改</a>
					<a id="${adl['code']}Con" style="display:none" href="javascript:adConfirm('${adl['code']}','${adl['name']}','${adl['higherCode']}')">确定</a>
					<a id="${adl['code']}Line" style="display:none"><span>|</span></a>
					<a id="${adl['code']}Can" style="display:none" href="javascript:adCancel('${adl['code']}','${adl['name']}','${adl['higherCode']}')">取消</a>
					<a id="${adl['code']}Line2" ><span>|</span></a>
					<a id="${adl['code']}upCounty" href="javascript:upgradeCounty('${adl['code']}','${adl['higherCode']}','${adl['name']}','${adl['higherName']}' );" >县升级为省辖市</a>
					<a id="${adl['code']}Line3" ><span>|</span></a>
					<a id="${adl['code']}deCity" href="javascript:degradeCity('${adl['code']}','${adl['name']}');" >省辖市降级为县</a>
				</td>
				</c:if>
			</tr>
			</c:if>
			<c:if test="${adl['lowerMap'] != null && fn:length(adl['lowerMap']) > 0 }">
			<c:forEach items="${adl['lowerMap']}" var="lowerMap">
			<tr id="${adl['code']}">
				<td id="${adl['code']}1">${adl['name']}</td>
				<td id="${adl['code']}2">${adl['code']}</td>
				<td>${lowerMap.value}</td>
				<td>${lowerMap.key}</td>
				<c:if test="${user == 'admin' }" >
				<td>
					<a id="${adl['code']}Mod" style="display:inline" href="javascript:adModify('${adl['code']}','')">修改</a>
					<a id="${adl['code']}Con" style="display:none" href="javascript:adConfirm('${adl['code']}','${adl['name']}','')">确定</a>
					<a id="${adl['code']}Line" style="display:none"><span>|</span></a>
					<a id="${adl['code']}Can" style="display:none" href="javascript:adCancel('${adl['code']}','${adl['name']}','')">取消</a>
				</td>
				</c:if>
			</tr>
			</c:forEach>
			</c:if>
			<c:if test="${(adl['lowerMap'] == null || fn:length(adl['lowerMap']) < 1) && adl['mark'] == 'lower' }">
			<tr id="${adl['code']}">
				<td id="${adl['code']}1">${adl['name']}</td>
				<td id="${adl['code']}2">${adl['code']}</td>
				<td></td>
				<td></td>
				<c:if test="${user == 'admin' }" >
				<td>
					<a id="${adl['code']}Mod" style="display:inline" href="javascript:adModify('${adl['code']}','')">修改</a>
					<a id="${adl['code']}Con" style="display:none" href="javascript:adConfirm('${adl['code']}','${adl['name']}','')">确定</a>
					<a id="${adl['code']}Line" style="display:none"><span>|</span></a>
					<a id="${adl['code']}Can" style="display:none" href="javascript:adCancel('${adl['code']}','${adl['name']}','')">取消</a>
				</td>
				</c:if>
			</tr>
			</c:if>
			</c:if>
		</table>
	<c:if test="${msg !=null }">
	<br>
	<span style="color:red;">${msg }</span>
	</c:if>
	<c:if test="${(adl== null || fn:length(adl) < 1) && msg==null }">
		无符合条件的记录
	</c:if>
	<c:if test="${error != null && error !=''}">
		---${error}
	</c:if>

	<div id="dialog-message-2" title="提示信息" style="display:none;">
	<br/>
	<span id="message-show-2" style="color:red;"></span>
	</div>

	<c:if test="${user == 'admin' }" >
	<div id="dialog_upgradeCounty" title="县升级为省辖市(非代管)" style="display:none">
		<form id="upgradeCountyForm">
		<br/>
		<div>
		<span>县&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;&nbsp;区&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
		<input type="text" id="countyCode_up" name="countyCode" onblur="javascript:queryAdName('countyCode_up','dynamicInfo1');" />&nbsp;&nbsp;<span id="dynamicInfo1" style="color:green" ></span>
		<br/><br/>
		<span>县原来的市地区码：</span>
		<input type="text" id="cityCode_up" name="cityCode"  onblur="javascript:queryAdName('cityCode_up','dynamicInfo2');" />&nbsp;&nbsp;<span id="dynamicInfo2" style="color:green" ></span>
		</div>
		</form>
	</div>

	<div id="dialog_degradeCity" title="省辖市(非代管)降级为县" style="display:none">
		<form id="degradeCityForm">
		<br/>
		<div>
		<span>省辖市地区码：</span>
		<input type="text" id="countyCode_de" name="countyCode"  onblur="javascript:queryAdName('countyCode_de','dynamicInfo3');" />&nbsp;&nbsp;<span id="dynamicInfo3" style="color:green" ></span>
		<br/><br/>
		<span>分配市地区码：</span>
		<input type="text" id="cityCode_de" name="cityCode" autofocus="autofocus"  onblur="javascript:queryAdName('cityCode_de','dynamicInfo4');" />&nbsp;&nbsp;<span id="dynamicInfo4" style="color:green" ></span>
		</div>
		</form>
	</div>
	</c:if>
</body>
</html>