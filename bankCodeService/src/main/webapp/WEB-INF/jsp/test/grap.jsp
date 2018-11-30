<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@include file="../include/header.jsp"%>
<%@include file="../include/commons-include.jsp"%>
<%@include file="../include/Dialog.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模拟异步请求，进行并发测试</title>
<!-- 加载Query文件-->
<script type="text/javascript" src="<spring:url value="/resources/js/jquery/jquery-3.3.1.js" />"></script>
<style type="text/css">
.adHover:hover{
		color:red;
		cursor:pointer;
	}
	
/* table */
table.global_table_new { width:100%; border:#e7e7e7 1px solid;}
table.global_table_new th { padding-top:10px; padding-bottom:10px; padding-right:10px;padding-left:100px;border:#e7e7e7 1px solid; background:#f8f8f8; font-size:16px;font-weight:normal;text-align:right}
table.global_table_new td { padding-top:10px; padding-bottom:10px; padding-left:10px; border:#e7e7e7 1px solid; background:#FFF; font-size:16px;}
</style>
<script type="text/javascript">
	function asynRequestTest() {
		//模拟异步请求 进行并发
		var max = $("#requestCount").val();
		if(max<=0 || max >30000){
			alert("测试值须在1-30000之间");
			return;
		}
		var type = $("input[type='radio'][name='mark']:checked").val();
		var urls = "${pageContext.request.contextPath}/userRedPacket/"+type+".htm";
		// var suCount = 0;
		// var erCount = 0;
		var start=new Date();
		for (var i = 1; i <= max; i++) {
			$.post({
				//请求抢id为1的红包
				//根据自己请求修改对应的url和大红包编号redPacketId
				url : urls+"?redPacketId=1&userId="+ i,
				async: true,  // 异步请求
				//成功后的方法
				success : function(result) {
				},
				error:function(){
				}
			});
		}
		var end = new Date();
		var time = (end.getTime() - start.getTime())/1000;
		// showPromptMsg("页面执行完毕，总数："+max+"，循环用时："+time+"秒");
	}
	
	//提示信息
	function showPromptMsg(msg){
		$("#dialog:ui-dialog").dialog( "destroy" );
		$("#message-show-1").html(msg);
		$("#dialog-message-1").dialog({
			position:[500,500],
			resizable: false,
			height:220,
			modal: true,
			buttons: {"关闭": function() { $( this ).dialog( "close" );}}
		});
	}
</script>
</head>

<body style="overflow: auto;">

	<div class="search_tit">
		<h2>并发测试</h2>
	</div>
	<br>
	
		<table cellpadding="0" cellspacing="1" align="center" class="global_table_new">
			<tr>
				<th>异步请求数</th>
				<td><input type="text" id="requestCount" name="requestCount" value=""></td>
			</tr>
			<tr>
			<th>并发类型</th>
				<td>
				    <input type="radio" name="mark" id="general" value="grapRedPacket" checked="checked"/><label for="general" class="adHover">MariaDB普通&nbsp;&nbsp;&nbsp;&nbsp;</label>
			        <input type="radio" name="mark" id="forUpdate" value="grapRedPacketForUpdate" /><label for="forUpdate" class="adHover">MariaDB悲观锁&nbsp;&nbsp;&nbsp;&nbsp;</label>
			        <input type="radio" name="mark" id="forVersion" value="grapRedPacketForVersion" /><label for="forVersion" class="adHover">MariaDB乐观锁&nbsp;&nbsp;&nbsp;&nbsp;</label>
			        <input type="radio" name="mark" id="forVersionTimeRe" value="grapRedPacketForVersionTimeRe" /><label for="forVersionTimeRe" class="adHover">MariaDB乐观锁(时间戳重入)&nbsp;&nbsp;&nbsp;&nbsp;</label>
			        <input type="radio" name="mark" id="forVersionCountRe" value="grapRedPacketForVersionCountRe" /><label for="forVersionCountRe" class="adHover">MariaDB乐观锁(次数重入)&nbsp;&nbsp;&nbsp;&nbsp;</label>
			        <input type="radio" name="mark" id="redisTest" value="grapRedPacketByRedis" /><label for="redisTest" class="adHover">Redis缓存&nbsp;&nbsp;&nbsp;&nbsp;</label>
			    </td>
			</tr>
		</table>
		<br />
		<div>
		    <input type="reset" class="global_btn" style="margin-left: 45%" value="重  置" />
			<input type="button" class="global_btn" style="margin: 0px 20px auto" value="发起请求" onclick="javascript:asynRequestTest()" /> 
		</div>
	
	
	<div class="total_panel">
	</div>
	
	<iframe name="queryResult" src="" style="width:100%;height:auto;min-height:2500px;max-height:none;"
	scrolling="auto" frameborder="0"></iframe>
	
	<div id="dialog-message-1" title="提示信息" style="display:none">
		<br/>
		<div>
		<span id="message-show-1" style="color:red;"></span>
		</div>
	</div>

</body>
</html>