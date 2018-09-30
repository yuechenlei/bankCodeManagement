// 卡识别
function identify(){
	var cardNo = $("#cardNo").val();
	var reg = /^\s*$/; //空白
	if(reg.test(cardNo)){
		showPromptMsg("银行卡号不能为空");
		return;
	}
	if(cardNo.length<6){
		showPromptMsg("银行卡号不能小于6位");
		return;
	}
	//var regu = /^\d{6,}$/;
	//if(!regu.test(cardNo)){
	//	showPromptMsg("银行卡号必须是大于5位的数字");
	//	return;
	//}

	$("#iinForm").submit();

}

//提示信息
function showPromptMsg(msg){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#message-show-1").html(msg);
	$("#dialog-message-1").dialog({
		position:[550,180],
		resizable: false,
		height:220,
		modal: true,
		buttons: {"关闭": function() { $( this ).dialog( "close" );}}
	});
}

function showPromptMsg2(msg){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#message-show-2").html(msg);
	$("#dialog-message-2").dialog({
		position:[550,100],
		resizable: false,
		height:220,
		modal: true,
		buttons: {"关闭": function() { $( this ).dialog( "close" );}}
	});
}

//弹出新增窗口
function alertIinWindow(){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#addButton").attr("value","新 增");
	$("#addButton").attr("onclick","javascript:addIin()");

	$("#increaseIin").show();
	$("#dialog_html").dialog({
		title : "新增",
		position:[130,10],
		resizable: true,
		width: 1100,
		height: 630,
		modal: true,
		buttons: {"关闭": function() {
			$( this ).dialog( "close" );
			}},
		close: function(){
			$("#errorMsg").html("");
			$("#addIinForm")[0].reset();
			$( this ).dialog( "close" );
			}

	});
}

//新增卡识别
function addIin(){
	$("#errorMsg").html("");
	$("#loadingDiv").show();
	var fd = new FormData($("#addIinForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/iin/iinIncrease.json",
		data: fd,
		dataType:"json",
		cache: false,  // 不用缓存
		processData: false,  // 不处理发送的数据
		contentType: false,  // 不设置Content-Type请求头
		success:function(msg){
			$("#loadingDiv").hide();
			if(msg.result == "success"){
				if(msg.errorMsg!=""){
					alert("操作成功，但是有错误，详见框体最下方。");
					$("#errorMsg").html(msg.errorMsg);
				}else{
					alert("添加成功");
				}
			}else if(msg.result == "error"){
				alert("添加失败");
				$("#errorMsg").html(msg.errorMsg);
			}else if(msg.result == "empty"){
				alert("没有输入任何有效信息");
			}else if(msg.result == "overStep"){
				alert("一次添加量不能超过1000");
			}
		},
		error:function(){
			$("#loadingDiv").hide();
			alert("系统异常，请稍后重试");
		}


	});

}

//导出
function exportIin(){
	$("#iinForm").attr("action", contextPath+"/iin/iinExport.htm");
	$("#iinForm").attr("target", "exportResult");
	$("#iinForm").submit();

	$("#iinForm").attr("action", contextPath+"/iin/iinQuery.htm");
	$("#iinForm").attr("target", "queryResult");
}

//全部导出
function totalExport(){
	$("#iinForm").attr("action", contextPath+"/iin/totalExport.htm");
	$("#iinForm").attr("target", "exportResult");
	$("#iinForm").submit();

	$("#iinForm").attr("action", contextPath+"/iin/iinQuery.htm");
	$("#iinForm").attr("target", "queryResult");
}

//修改
function modify(agencyName,agencyCode,cardName,panLength,length,code,cardType,providerCode){
	var suffix = ""+code+length+panLength;
	$("#modify"+suffix).attr("style","display:none;");
	$("#confirm"+suffix).attr("style","display:inline;");
	$("#line"+suffix).attr("style","display:inline;");
	$("#cancle"+suffix).attr("style","display:inline;");

	$("#agencyName"+suffix).html("<input id='agencyName1"+suffix+ "' value="+agencyName+" size='25' >");
	$("#agencyCode"+suffix).html("<input id='agencyCode2"+suffix+"' value="+agencyCode+" size='10' >");
	$("#cardName"+suffix).html("<input id='cardName3"+suffix+"' value="+cardName+" size='30' >");
	$("#panLength"+suffix).html("<input id='panLength4"+suffix+"' value="+panLength+" size='10' >");
	$("#length"+suffix).html("<input id='length5"+suffix+"' value="+length+" size='10' >");
	$("#code"+suffix).html("<input id='code6"+suffix+"' value="+code+" size='10' >");
	$("#cardType"+suffix).html("<input id='cardType7"+suffix+"' value="+cardType+" size='10' >");
	$("#providerCode"+suffix).html("<input id='providerCode8"+suffix+"' value="+providerCode+" size='10' >");
}

// 确认修改
function confirmModify(agencyName,agencyCode,cardName,panLength,length,code,cardType,providerCode){
	var suffix = ""+code+length+panLength;
	var new_agencyName = $.trim($("#agencyName1"+suffix).val());
	var new_agencyCode = $.trim($("#agencyCode2"+suffix).val());
	var new_cardName = $.trim($("#cardName3"+suffix).val());
	var new_panLength = $.trim($("#panLength4"+suffix).val());
	var new_length = $.trim($("#length5"+suffix).val());
	var new_code = $.trim($("#code6"+suffix).val());
	var new_cardType = $.trim($("#cardType7"+suffix).val());
	var new_providerCode = $.trim($("#providerCode8"+suffix).val());

	var flag = notEmpty(new_agencyName,new_agencyCode,new_cardName,new_panLength,new_length,new_code,new_cardType,new_providerCode);
	if(flag) return;
	if( code != new_code || length != new_length || panLength != new_panLength ){
		if(!confirm("id修改，执行添加，原来的数据将自动删除！")) return ;

		// 删除
		$.ajax({
			type: "POST",
			url: contextPath+"/iin/iinDel.json",
			data: {"panLength":panLength,"length":length,"code":code},
			dataType: "json",
			success:function(msg){
				if(msg.result == "success"){

				}else if(msg.result == "fail"){
					showPromptMsg2("删除失败，"+msg.errorMsg);
					flag = true;
					return;
				}else{
					showPromptMsg2("删除失败，系统异常，请稍候重试");
					flag = true;
					return;
				}

			},
			error:function(){
				showPromptMsg2("删除失败，系统异常，请稍候重试");
				flag = true;
				return;
			}
		});

		if(flag) return;

		// 添加
		$.ajax({
			type:"POST",
			url: contextPath+"/iin/iinAdd.json",
			data:{"agencyName":new_agencyName,"agencyCode":new_agencyCode,"cardName":new_cardName,"panLength":new_panLength,"length":new_length,"code":new_code,"cardType":new_cardType,"providerCode":new_providerCode },
			dataType:"json",
			success:function(msg){
				if(msg.result == "success"){
					$("#modify"+suffix).attr("style","display:inline;");
					$("#confirm"+suffix).attr("style","display:none;");
					$("#line"+suffix).attr("style","display:none;");
					$("#cancle"+suffix).attr("style","display:none;");

					$("#modify"+suffix).attr("onclick","javascript:modify('"+new_agencyName+"','"+new_agencyCode+"','"+new_cardName+"','"+new_panLength+"','"+new_length+"','"+new_code+"','"+new_cardType+"','"+new_providerCode+"');return false;");
					$("#confirm"+suffix).attr("onclick","javascript:confirmModify('"+new_agencyName+"','"+new_agencyCode+"','"+new_cardName+"','"+new_panLength+"','"+new_length+"','"+new_code+"','"+new_cardType+"','"+new_providerCode+"');return false;");
					$("#cancle"+suffix).attr("onclick","javascript:cancleModify('"+new_agencyName+"','"+new_agencyCode+"','"+new_cardName+"','"+new_panLength+"','"+new_length+"','"+new_code+"','"+new_cardType+"','"+new_providerCode+"');return false;");

					$("#agencyName"+suffix).html(new_agencyName);
					$("#agencyCode"+suffix).html(new_agencyCode);
					$("#cardName"+suffix).html(new_cardName);
					$("#panLength"+suffix).html(new_panLength);
					$("#length"+suffix).html(new_length);
					$("#code"+suffix).html(new_code);
					$("#cardType"+suffix).html(new_cardType);
					$("#providerCode"+suffix).html(new_providerCode);

					var newSuffix = "" + new_code + new_length + new_panLength;

					$("#modify"+suffix).attr("id","modify"+newSuffix);
					$("#confirm"+suffix).attr("id","confirm"+newSuffix);
					$("#line"+suffix).attr("id","line"+newSuffix);
					$("#cancle"+suffix).attr("id","cancle"+newSuffix);

					$("#agencyName"+suffix).attr("id","agencyName"+newSuffix);
					$("#agencyCode"+suffix).attr("id","agencyCode"+newSuffix);
					$("#cardName"+suffix).attr("id","cardName"+newSuffix);
					$("#panLength"+suffix).attr("id","panLength"+newSuffix);
					$("#length"+suffix).attr("id","length"+newSuffix);
					$("#code"+suffix).attr("id","code"+newSuffix);
					$("#cardType"+suffix).attr("id","cardType"+newSuffix);
					$("#providerCode"+suffix).attr("id","providerCode"+newSuffix);

					$("#"+suffix).attr("id",newSuffix);
					$("#"+newSuffix).attr("style","color:blue;");

				}else if(msg.result == "fail"){
					showPromptMsg2("添加失败，请前去新增！"+msg.errorMsg);
				}else{
					showPromptMsg2("添加失败，请前去新增！");
				}

			},
			error:function(){
				showPromptMsg2("添加失败，请前去新增！");
			}
		});

	}else{
		$.ajax({
			type:"POST",
			url: contextPath+"/iin/iinModify.json",
			data:{"agencyName":new_agencyName,"agencyCode":new_agencyCode,"cardName":new_cardName,"panLength":panLength,"length":length,"code":code,"cardType":new_cardType,"providerCode":new_providerCode },
			dataType:"json",
			success:function(msg){
				if(msg.result == "success"){
					$("#modify"+suffix).removeAttr("style");
					$("#confirm"+suffix).hide();
					$("#line"+suffix).hide();
					$("#cancle"+suffix).hide();

					$("#modify"+suffix).attr("onclick","javascript:modify('"+new_agencyName+"','"+new_agencyCode+"','"+new_cardName+"','"+panLength+"','"+length+"','"+code+"','"+new_cardType+"','"+new_providerCode+"');return false;");
					$("#confirm"+suffix).attr("onclick","javascript:confirmModify('"+new_agencyName+"','"+new_agencyCode+"','"+new_cardName+"','"+panLength+"','"+length+"','"+code+"','"+new_cardType+"','"+new_providerCode+"');return false;");
					$("#cancle"+suffix).attr("onclick","javascript:cancleModify('"+new_agencyName+"','"+new_agencyCode+"','"+new_cardName+"','"+panLength+"','"+length+"','"+code+"','"+new_cardType+"','"+new_providerCode+"');return false;");

					$("#agencyName"+suffix).html(new_agencyName);
					$("#agencyCode"+suffix).html(new_agencyCode);
					$("#cardName"+suffix).html(new_cardName);
					$("#panLength"+suffix).html(panLength);
					$("#length"+suffix).html(length);
					$("#code"+suffix).html(code);
					$("#cardType"+suffix).html(new_cardType);
					$("#providerCode"+suffix).html(new_providerCode);

					$("#"+suffix).attr("style","color:blue");

				}else if(msg.result == "fail"){
					showPromptMsg2("修改失败，"+msg.errorMsg);
				}else{
					showPromptMsg2("修改失败，系统异常，请稍候重试");
				}

			},
			error:function(){
				showPromptMsg2("修改失败，系统异常，请稍候重试");
			}
		});
	}
}

// 取消修改
function cancleModify(agencyName,agencyCode,cardName,panLength,length,code,cardType,providerCode){
	var suffix = ""+code+length+panLength;
	$("#modify"+suffix).attr("style","display:inline;");
	$("#confirm"+suffix).attr("style","display:none;");
	$("#line"+suffix).attr("style","display:none;");
	$("#cancle"+suffix).attr("style","display:none;");

	$("#agencyName"+suffix).html(agencyName);
	$("#agencyCode"+suffix).html(agencyCode);
	$("#cardName"+suffix).html(cardName);
	$("#panLength"+suffix).html(panLength);
	$("#length"+suffix).html(length);
	$("#code"+suffix).html(code);
	$("#cardType"+suffix).html(cardType);
	$("#providerCode"+suffix).html(providerCode);

}


//非空验证
function notEmpty(agencyName,agencyCode,cardName,panLength,length,code,cardType,providerCode){
	var reg = /^\s*$/; //空白
	var regu = /^\d{1,}$/;  //数字
	if(reg.test(agencyName)){
		showPromptMsg2("发卡行名称为空");
		return true;
	}
	if(reg.test(agencyCode)){
		showPromptMsg2("机构代码为空");
		return true;
	}
	if(reg.test(cardName)){
		showPromptMsg2("卡名为空");
		return true;
	}
	if(!regu.test(panLength)){
		showPromptMsg2("卡号长度必须为数字");
		return true;
	}
	if(!regu.test(length)){
		showPromptMsg2("识别码长度必须为数字");
		return true;
	}
	if(reg.test(code)){
		showPromptMsg2("识别码为空");
		return true;
	}
	if(reg.test(cardType)){
		showPromptMsg2("卡种为空");
		return true;
	}
	if(reg.test(providerCode)){
		showPromptMsg2("银行编号为空");
		return true;
	}

	return false;

}


//弹出批量修改窗口
function alertBatchWindow(){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#errorMsg").html("注意：此处不会执行添加。");
	$("#addButton").attr("value","批量修改");
	$("#addButton").attr("onclick","javascript:batchModify()");

	$("#increaseIin").show();
	$("#dialog_html").dialog({
		title : "批量修改",
		position:[130,10],
		resizable: true,
		width: 1100,
		height: 630,
		modal: true,
		buttons: {"关闭": function() {
			$( this ).dialog( "close" );
			}},
		close: function(){
			$("#errorMsg").html("");
			$("#addIinForm")[0].reset();
			$( this ).dialog( "close" );
			}
	});
}

//批量修改
function batchModify(){
	$("#errorMsg").html("");
	$("#loadingDiv").show();
	var fd = new FormData($("#addIinForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/iin/batchModification.json",
		data: fd,
		dataType:"json",
		cache: false,  // 不用缓存
		processData: false,  // 不处理发送的数据
		contentType: false,  // 不设置Content-Type请求头
		success:function(msg){
			$("#loadingDiv").hide();
			if(msg.result == "success"){
				if(msg.errorMsg!=""){
					alert("操作成功，但是有错误，详见框体最下方。");
					$("#errorMsg").html(msg.errorMsg);
				}else{
					alert("修改成功，共有"+msg.totalCount+"笔，失败"+msg.failCount+"笔。");
				}
			}else if(msg.result == "fail"){
				alert("修改失败");
				$("#errorMsg").html(msg.errorMsg);
			}else if(msg.result == "empty"){
				alert("没有输入任何有效信息");
			}else if(msg.result == "overStep"){
				alert("一次修改量不能超过1000");
			}
		},
		error:function(){
			$("#loadingDiv").hide();
			alert("系统异常，请稍后重试");
		}
	});

}


