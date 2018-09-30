
// 新增联行号
function addCnaps(){
	$("#errorMsg").html("");
	$("#loadingDiv").show();
	var fd = new FormData($("#addCnapsForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/cnaps/cnapsIncrease.json",
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
			}else if(msg.result == "overstep"){
				alert("一次添加量不能超过1000");
			}else if(msg.result == "fail"){
				alert("添加失败");
				$("#errorMsg").html(msg.errorMsg);
			}
		},
		error:function(){
			$("#loadingDiv").hide();
			alert("系统异常，请稍后重试");
		}


	});

}

//导出
function exportResult(){
	$("#form").attr("action", contextPath+"/cnaps/cnapsExport.htm");
	$("#form").attr("target", "exportResult");
	$("#form").submit();

	$("#form").attr("action", contextPath+"/cnaps/cnapsExhibitionQuery.htm");
	$("#form").attr("target", "queryResult");
}

// 全部导出
function cnapsTotalExport(){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#message-show-1").html("您确定要全部导出吗？(请耐心等待......)");
	$("#dialog-message-1").dialog({
		position:[550,100],
		resizable: false,
		height:220,
		modal: true,
		buttons: {
			"确定": function() {
			$( this ).dialog( "close" );
			$("#form").attr("action", contextPath+"/cnaps/cnapsTotalExport.htm");
			$("#form").attr("target", "exportResult");
			$("#form").submit();

			$("#form").attr("action", contextPath+"/cnaps/cnapsExhibitionQuery.htm");
			$("#form").attr("target", "queryResult");
		},
		"取消": function() {
			$( this ).dialog( "close" );
		}

	}
	});

}

// 自动增长
function autoGrowth(){
	var word_len = $("#bankName").val().length;
	if(word_len>=10){
		$("#bankName").attr("size",word_len*2+2);
	}else{
		$("#bankName").attr("size","20");
	}
}

// 修改
function cnapsModify(cncode){

	$("#"+cncode+"Mod").css("display","none");
	$("#"+cncode+"Del").css("display","none");
	$("#"+cncode+"Line2").css("display","none");
	$("#"+cncode+"Line").css("display","inline");
	$("#"+cncode+"Con").css("display","inline");
	$("#"+cncode+"Can").css("display","inline");

	$("#"+cncode+"1").html('<input type="text" size="15" id='+cncode+'a'+' value='+cncode+' />');
	$("#"+cncode+"2").html('<input type="text" size="56" id='+cncode+'b'+' value='+$("#"+cncode+"2").html()+' />');
	$("#"+cncode+"3").html('<input type="text" size="15" id='+cncode+'c'+' value='+$("#"+cncode+"3").html()+' />');
	$("#"+cncode+"4").html('<input type="text" size="7" id='+cncode+'d'+' value='+$("#"+cncode+"4").html()+' />');
	$("#"+cncode+"5").html('<input type="text" size="7" id='+cncode+'e'+' value='+$("#"+cncode+"5").html()+' />');
	$("#"+cncode+"6").html('<input type="text" size="7" id='+cncode+'f'+' value='+$("#"+cncode+"6").html()+' />');

}

// 确认修改
function cnapsConfirm(cncode,cnname,cnclearingBankCode,cnclearingBankLevel,cnproviderCode,cnadCode){
	var code = $.trim($("#"+cncode+"a").val());
	var name = $.trim($("#"+cncode+"b").val());
	var clearingBankCode = $.trim($("#"+cncode+"c").val());
	var clearingBankLevel = $.trim($("#"+cncode+"d").val());
	var providerCode = $.trim($("#"+cncode+"e").val());
	var adCode = $.trim($("#"+cncode+"f").val());

	// 非空验证
	var flag = notEmptyValidate(code,name,clearingBankCode,clearingBankLevel,providerCode,adCode);
	if(flag) return;
	// 如果修改了联行号
	if(code != cncode){
		if(!confirm("修改联行号，执行的是新增，原来的数据自动删除。")){
			return false;
		}
		// 先删除原来的数据
		$.ajax({
			type:"POST",
			async: false,
			url: contextPath+"/cnaps/cnapsDel.json",
			data:{"code":cncode,"name":cnname,"clearingBankCode":cnclearingBankCode,"clearingBankLevel":cnclearingBankLevel,"providerCode":cnproviderCode,"adCode":cnadCode,"newCode":code},
			dataType:"json",
			success:function(msg){
				if(msg.result == "success"){

				}else if(msg.result == "exist"){
					showPromptMsg("修改失败，已存在该联行号（"+code+"）信息");
					flag = true;
					return;
				}else{
					showPromptMsg("操作失败!");
					flag = true;
					return;
				}

			},
			error:function(){
				showPromptMsg("系统异常，请稍后重试");
				flag = true;
				return;
			}
		});

		if(flag) return;
		// 新增
		$.ajax({
			type:"POST",
			url: contextPath+"/cnaps/confirmModify.json",
			data:{"code":code,"name":name,"clearingBankCode":clearingBankCode,"clearingBankLevel":clearingBankLevel,"providerCode":providerCode,"adCode":adCode},
			dataType:"json",
			success:function(msg){
				if(msg.result == "success"){
					$("#"+cncode+"1").html(code);
					$("#"+cncode+"2").html(name);
					$("#"+cncode+"3").html(clearingBankCode);
					$("#"+cncode+"4").html(clearingBankLevel);
					$("#"+cncode+"5").html(providerCode);
					$("#"+cncode+"6").html(adCode);

					$("#"+cncode).attr("id",code);
					$("#"+cncode+"Mod").attr("id",code+"Mod");
					$("#"+cncode+"Con").attr("id",code+"Con");
					$("#"+cncode+"Line").attr("id",code+"Line");
					$("#"+cncode+"Line2").attr("id",code+"Line2");
					$("#"+cncode+"Can").attr("id",code+"Can");
					$("#"+cncode+"Del").attr("id",code+"Del");

					$("#"+code+"Mod").attr("href","javascript:cnapsModify("+code+")");
					$("#"+code+"Con").attr("href","javascript:cnapsConfirm("+code+","+"'"+name+"'"+","+clearingBankCode+","+clearingBankLevel+","+"'"+providerCode+"'"+","+adCode+")");
					$("#"+code+"Can").attr("href","javascript:cnapsCancel("+code+","+"'"+name+"'"+","+clearingBankCode+","+clearingBankLevel+","+"'"+providerCode+"'"+","+adCode+")");
					$("#"+code+"Del").attr("href","javascript:cnapsDel("+code+")");


					$("#"+cncode+"1").attr("id",code+"1");
					$("#"+cncode+"2").attr("id",code+"2");
					$("#"+cncode+"3").attr("id",code+"3");
					$("#"+cncode+"4").attr("id",code+"4");
					$("#"+cncode+"5").attr("id",code+"5");
					$("#"+cncode+"6").attr("id",code+"6");

					$("#"+code).css("color","blue");
					$("#"+code+"Mod").css("display","inline");
					$("#"+code+"Del").css("display","inline");
					$("#"+code+"Line2").css("display","inline");
					$("#"+code+"Line").css("display","none");
					$("#"+code+"Con").css("display","none");
					$("#"+code+"Can").css("display","none");

				}else{
					showPromptMsg("修改失败，数据异常");
				}

			},
			error:function(){
				showPromptMsg("系统异常，请稍后重试");
			}
		});
	}else{  // 修改
		$.ajax({
			type:"POST",
			url: contextPath+"/cnaps/modify.json",
			data:{"code":cncode,"name":name,"clearingBankCode":clearingBankCode,"clearingBankLevel":clearingBankLevel,"providerCode":providerCode,"adCode":adCode},
			dataType:"json",
			success:function(msg){
				if(msg.result == "success"){
					$("#"+cncode+"1").html(code);
					$("#"+cncode+"2").html(name);
					$("#"+cncode+"3").html(clearingBankCode);
					$("#"+cncode+"4").html(clearingBankLevel);
					$("#"+cncode+"5").html(providerCode);
					$("#"+cncode+"6").html(adCode);

					$("#"+cncode+"Con").attr("href","javascript:cnapsConfirm("+cncode+","+"'"+name+"'"+","+clearingBankCode+","+clearingBankLevel+","+"'"+providerCode+"'"+","+adCode+")");
					$("#"+cncode+"Can").attr("href","javascript:cnapsCancel("+cncode+","+"'"+name+"'"+","+clearingBankCode+","+clearingBankLevel+","+"'"+providerCode+"'"+","+adCode+")");

					$("#"+cncode).css("color","blue");
					$("#"+cncode+"Mod").css("display","inline");
					$("#"+cncode+"Del").css("display","inline");
					$("#"+cncode+"Line2").css("display","inline");
					$("#"+cncode+"Line").css("display","none");
					$("#"+cncode+"Con").css("display","none");
					$("#"+cncode+"Can").css("display","none");

				}else{
					showPromptMsg("修改失败，数据异常");
				}

			},
			error:function(){
				showPromptMsg("系统异常，请稍后重试");
			}
		});
	}

}

// 取消修改
function cnapsCancel(cncode,name,clearingBankCode,clearingBankLevel,providerCode,adCode){
	$("#"+cncode+"Mod").css("display","inline");
	$("#"+cncode+"Del").css("display","inline");
	$("#"+cncode+"Line2").css("display","inline");
	$("#"+cncode+"Line").css("display","none");
	$("#"+cncode+"Con").css("display","none");
	$("#"+cncode+"Can").css("display","none");

	$("#"+cncode+"1").html(cncode);
	$("#"+cncode+"2").html(name);
	$("#"+cncode+"3").html(clearingBankCode);
	$("#"+cncode+"4").html(clearingBankLevel);
	$("#"+cncode+"5").html(providerCode);
	$("#"+cncode+"6").html(adCode);

}

// 删除
function cnapsDel(cncode){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$( "#message-show-2").html("您确定要删除吗？"+"("+"联行号:"+cncode+")");
	$( "#dialog-message-2" ).dialog({
		hide: {
			effect: "explode",
			duration: 300
		},
		position:[550,100],
		resizable: false,
		height:220,
		modal: true,
		buttons: {
			"确定": function() {
			$( this ).dialog( "close" );
			var code = cncode;
			var name = $("#"+cncode+"2").html();
			var clearingBankCode = $("#"+cncode+"3").html();
			var clearingBankLevel = $("#"+cncode+"4").html();
			var providerCode = $("#"+cncode+"5").html();
			var adCode = $("#"+cncode+"6").html();

			$.ajax({
				type:"POST",
				url: contextPath+"/cnaps/cnapsDel.json",
				data:{"code":code,"name":name,"clearingBankCode":clearingBankCode,"clearingBankLevel":clearingBankLevel,"providerCode":providerCode,"adCode":adCode},
				dataType:"json",
				success:function(msg){
					if(msg.result == "success"){
						showPromptMsg("删除成功！")
						$("#"+cncode).remove();
					}else{
						showPromptMsg("数据异常，删除失败！");
					}

				},
				error:function(){
					showPromptMsg("系统异常，请稍后重试");
				}
			});


		},
		"取消": function() {
			$( this ).dialog( "close" );
		}

	}
	});
}

// 非空验证
function notEmptyValidate(code,name,clearingBankCode,clearingBankLevel,providerCode,adCode){
	var reg = /^\s*$/; //空白
	if(reg.test(code)){
		showPromptMsg("联行号为空");
		return true;
	}
	var regu = /^\d{12}$/;  //12位数字
	if(!regu.test(code)){
		showPromptMsg("联行号必须为12位数字");
		return true;
	}
	if(reg.test(name)){
		showPromptMsg("银行名为空");
		return true;
	}
	if(reg.test(clearingBankCode)){
		showPromptMsg("清分行号为空");
		return true;
	}
	if(!regu.test(clearingBankCode)){
		showPromptMsg("清分行号必须为12位数字");
		return true;
	}
	if(reg.test(clearingBankLevel)){
		showPromptMsg("清算级别为空");
		return true;
	}
	if(reg.test(providerCode)){
		showPromptMsg("银行号为空");
		return true;
	}
	if(reg.test(adCode)){
		showPromptMsg("行政编码为空");
		return true;
	}

	return false;

}



// 提示信息
function showPromptMsg(msg){
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

// 弹出新增窗口
function alertWindow(){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#addButton").attr("value","新  增");
	$("#addButton").attr("onclick","javascript:addCnaps()");
	$("#increaseCnaps").show();
	$("#dialog_html").dialog({
		title: "新增",
		position:[130,10],
		resizable: true,
		width: 1200,
		height: 630,
		modal: true,
		buttons: {"关闭": function() {
			$( this ).dialog( "close" );
			}},
		close: function(){
			$("#errorMsg").html("");
			$("#addCnapsForm")[0].reset();
			$( this ).dialog( "close" );
			}

	});
}

//弹出批量修改窗口
function alertBatchWindow(){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#addButton").attr("value","批量修改");
	$("#addButton").attr("onclick","javascript:batchModify()");
	$("#errorMsg").html("注意：此处不可以修改联行号，也不会执行新增。");

	$("#increaseCnaps").show();
	$("#dialog_html").dialog({
		title: "批量修改",
		position:[130,10],
		resizable: true,
		width: 1200,
		height: 630,
		modal: true,
		buttons: {"关闭": function() {
			$( this ).dialog( "close" );
			}},
		close: function(){
			$("#errorMsg").html("");
			$("#addCnapsForm")[0].reset();
			$( this ).dialog( "close" );
			}

	});
}

// 确认提示
function showConfirmMsg(msg){
	$("#dialog:ui-dialog").dialog("destroy");
	$("#message-show-2").html(msg);
	$("#dialog-message-2").dialog({
		position:[550,100],
		resizable: false,
		height:220,
		modal: true,
		buttons: {
			"确定": function() {
			$( this ).dialog( "close" );
		},
		"取消": function() {
			$( this ).dialog( "close" );
		}

	}
	});

}

// 下载模版
function downloadTemplate(name){
	$("#exportData").val(name);
	$("#downloadForm").submit();
}

// 批量修改
function batchModify(){
	$("#errorMsg").html("");
	$("#loadingDiv").show();
	var fd = new FormData($("#addCnapsForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/cnaps/batchModification.json",
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
			}else if(msg.result == "re"){
				alert("发现异常，详见框体最下方。");
				$("#errorMsg").html(msg.errorMsg);
			}else if(msg.result == "empty"){
				alert("没有输入任何有效信息");
			}else if(msg.result == "overstep"){
				alert("一次修改量不能超过1000");
			}else if(msg.result == "fail"){
				alert("修改失败!");
				$("#errorMsg").html(msg.errorMsg);
			}
		},
		error:function(){
			$("#loadingDiv").hide();
			alert("系统异常，请稍后重试");
		}


	});

}


