//查询
function adQuery(){
	var code = $.trim($("#adCode").val());
	var name = $("#adName").val();
	if(code != "" && name != ""){
		$("#adName").val("");
	}
	if(code == "" && name != "" && name.length <= 1){
		$("#dialog:ui-dialog").dialog( "destroy" );
		$("#message-show-1").html("请至少输入两个字。");
		$("#dialog-message-1").dialog({
			position:[550,100],
			resizable: false,
			height:220,
			modal: true,
			buttons: {
				"确定": function() {
				$( this ).dialog( "close" );
			}
		}
		});
		return;
	}
	$("#adForm").submit();
}

// 弹出新增窗口
function alertWindow(){
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#increaseAd").show();
	$("#dialog_html").dialog({
		position:[130,10],
		resizable: true,
		width: 1100,
		height: 1210,
		modal: true,
		buttons: {"关闭": function() {
			$( this ).dialog( "close" );
			}},
		close: function(){
			clearText();
			$("#addCouForm")[0].reset();
			$("#addCityForm")[0].reset();
			$("#addProForm")[0].reset();
			$( this ).dialog( "close" );
			}
	});
}

// 清空文本
function clearText(){
	$("#errorMsg_cou").html("");
	$("#errorMsg_city").html("");
	$("#errorMsg_pro").html("");
}

// 添加县
function addCountys(){
	clearText();
	var fd = new FormData($("#addCouForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/ad/addCounty.json",
		data: fd,
		dataType:"json",
		cache: false,
		processData: false,
		contentType: false,
		success:function(msg){
			if(msg.result == "success"){
				if(msg.errorMsg!="" && msg.errorMsg != null){
					alert("操作成功，但是有错误，详见框体下方。");
					$("#errorMsg_cou").html(msg.errorMsg);
				}else{
					alert("添加成功");
				}
			}else if(msg.result == "fail"){
				alert("添加失败，"+ msg.errorMsg);
			}else{
				alert("系统异常，请稍后再试");
			}
		},
		error:function(){
			alert("系统异常，请稍后重试");
		}
	});

}

//添加市
function addCitys(){
	clearText();
	var fd = new FormData($("#addCityForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/ad/addCity.json",
		data: fd,
		dataType:"json",
		cache: false,
		processData: false,
		contentType: false,
		success:function(msg){
			if(msg.result == "success"){
				if(msg.errorMsg!="" && msg.errorMsg != null){
					alert("操作成功，但是有错误，详见框体下方。");
					$("#errorMsg_city").html(msg.errorMsg);
				}else{
					alert("添加成功");
				}
			}else if(msg.result == "fail"){
				alert("添加失败，"+msg.errorMsg);
			}else{
				alert("系统异常，请稍后再试");
			}
		},
		error:function(){
			alert("系统异常，请稍后重试");
		}
	});

}

//添加省
function addProvinces(){
	clearText();
	var fd = new FormData($("#addProForm")[0]);

	$.ajax({
		type:"POST",
		url: contextPath+"/ad/addProvince.json",
		data: fd,
		dataType:"json",
		cache: false,
		processData: false,
		contentType: false,
		success:function(msg){
			if(msg.result == "success"){
				if(msg.errorMsg!="" && msg.errorMsg != null){
					alert("操作成功，但是有错误，详见框体下方。");
					$("#errorMsg_pro").html(msg.errorMsg);
				}else{
					alert("添加成功");
				}
			}else if(msg.result == "fail"){
				alert("添加失败，"+msg.errorMsg);
			}else{
				alert("系统异常，请稍后重试");
			}
		},
		error:function(){
			alert("系统异常，请稍后重试");
		}
	});

}

// 修改name、code
function adModify(adCode,higherCode){
	$("#"+adCode+"Mod").css("display","none");
	$("#"+adCode+"Line").css("display","inline");
	$("#"+adCode+"Con").css("display","inline");
	$("#"+adCode+"Can").css("display","inline");
	if($("#"+adCode+"upCounty")){
		$("#"+adCode+"Line2").css("display","none");
		$("#"+adCode+"upCounty").css("display","none");
		$("#"+adCode+"Line3").css("display","none");
		$("#"+adCode+"deCity").css("display","none");
	}


	$("#"+adCode+"1").html('<input type="text" size="20" id='+adCode+'a'+' value='+$("#"+adCode+"1").html()+' />');
	$("#"+adCode+"2").html('<input type="text" size="20" id='+adCode+'b'+' value='+adCode+' />');
	if(higherCode != null && higherCode != ""){
		$("#"+higherCode).html('<input type="text" size="20" id='+higherCode+'a'+' value='+$("#"+higherCode).html()+' />');
	}
}

// 确认修改
function adConfirm(adCode,adName,higherCode){
	var newCode = $.trim($("#"+adCode+"b").val());
	var newName = $.trim($("#"+adCode+"a").val());
	var newHigherCode = "";
	if(higherCode != null && higherCode != ""){
		newHigherCode = $.trim($("#"+higherCode+"a").val());
		if(newHigherCode != higherCode){
			var genre = $("#genre").val();
			if(genre == "county"){
				var flag = modifyCounty(adCode,higherCode,newHigherCode);
				if(!flag){
					adCancel(adCode,adName,higherCode);
					return;
				}
			}else if(genre == "city"){
				var flag = modifyCity(adCode,higherCode,newHigherCode);
				if(!flag){
					adCancel(adCode,adName,higherCode);
					return;
				}
			}
		}
	}

	// 信息验证
	var flag = notEmptyValidate(newCode,newName);
	if(flag) return;

	$.ajax({
		type:"POST",
		url: contextPath+"/ad/adModify.json",
		data:{"oldCode":adCode,"oldName":adName,"newCode":newCode,"newName":newName},
		dataType:"json",
		success:function(msg){
			if(msg.result == "success"){
				$("#"+adCode+"1").html(newName);
				if(newHigherCode != ""){  //判断是否存在上级code
					$("#"+higherCode).html(newHigherCode);
					$("#"+higherCode).attr("id",newHigherCode);

				}

				if(adCode != newCode){
					$("#"+adCode+"2").html(newCode);

					$("#"+adCode).attr("id",newCode);
					$("#"+adCode+"Mod").attr("id",newCode+"Mod");
					$("#"+adCode+"Con").attr("id",newCode+"Con");
					$("#"+adCode+"Line").attr("id",newCode+"Line");
					$("#"+adCode+"Can").attr("id",newCode+"Can");

					if(newHigherCode != ""){
						$("#"+newCode+"Mod").attr("href","javascript:adModify("+newCode+","+"'"+newHigherCode+"'"+")");
						$("#"+newCode+"Con").attr("href","javascript:adConfirm("+newCode+","+"'"+newName+"'"+","+"'"+newHigherCode+"'"+")");
						$("#"+newCode+"Can").attr("href","javascript:adCancel("+newCode+","+"'"+newName+"'"+","+"'"+newHigherCode+"'"+")");
					}else{
						$("#"+newCode+"Mod").attr("href","javascript:adModify("+newCode+","+"'"+"'"+")");
						$("#"+newCode+"Con").attr("href","javascript:adConfirm("+newCode+","+"'"+newName+"'"+","+"'"+"'"+")");
						$("#"+newCode+"Can").attr("href","javascript:adCancel("+newCode+","+"'"+newName+"'"+","+"'"+"'"+")");
					}

					$("#"+adCode+"1").attr("id",newCode+"1");
					$("#"+adCode+"2").attr("id",newCode+"2");

					$("#"+newCode).css("color","blue");
					$("#"+newCode+"Mod").css("display","inline");
					$("#"+newCode+"Line").css("display","none");
					$("#"+newCode+"Con").css("display","none");
					$("#"+newCode+"Can").css("display","none");
					if($("#"+adCode+"upCounty").length > 0){
						$("#"+adCode+"Line2").css("display","inline");
						$("#"+adCode+"upCounty").css("display","inline");
						$("#"+adCode+"Line3").css("display","inline");
						$("#"+adCode+"deCity").css("display","inline");

						$("#"+adCode+"Line2").attr("id",newCode+"Line2");
						$("#"+adCode+"upCounty").attr("id",newCode+"upCounty");
						$("#"+adCode+"Line3").attr("id",newCode+"Line3");
						$("#"+adCode+"deCity").attr("id",newCode+"deCity");
					}
				}else{
					$("#"+adCode+"2").html(adCode);
					if(newHigherCode != ""){
						$("#"+adCode+"Mod").attr("href","javascript:adModify("+adCode+","+"'"+newHigherCode+"'"+")");
						$("#"+adCode+"Con").attr("href","javascript:adConfirm("+adCode+","+"'"+newName+"'"+","+"'"+newHigherCode+"'"+")");
						$("#"+adCode+"Can").attr("href","javascript:adCancel("+adCode+","+"'"+newName+"'"+","+"'"+newHigherCode+"'"+")");
					}else{
						$("#"+adCode+"Mod").attr("href","javascript:adModify("+adCode+","+"'"+"'"+")");
						$("#"+adCode+"Con").attr("href","javascript:adConfirm("+adCode+","+"'"+newName+"'"+","+"'"+"'"+")");
						$("#"+adCode+"Can").attr("href","javascript:adCancel("+adCode+","+"'"+newName+"'"+","+"'"+"'"+")");
					}

					$("#"+adCode).css("color","blue");
					$("#"+adCode+"Mod").css("display","inline");
					$("#"+adCode+"Line").css("display","none");
					$("#"+adCode+"Con").css("display","none");
					$("#"+adCode+"Can").css("display","none");
					if($("#"+adCode+"upCounty").length > 0){
						$("#"+adCode+"Line2").css("display","inline");
						$("#"+adCode+"upCounty").css("display","inline");
						$("#"+adCode+"Line3").css("display","inline");
						$("#"+adCode+"deCity").css("display","inline");
					}

				}

			}else if(msg.result == "fail"){
				showPromptMsg("修改失败，"+msg.errorMsg);
			}

		},
		error:function(){
			showPromptMsg("系统异常，请稍后重试");
		}
	});
}

//取消修改
function adCancel(adCode,adName,higherCode){
	$("#"+adCode+"Mod").css("display","inline");
	$("#"+adCode+"Line").css("display","none");
	$("#"+adCode+"Con").css("display","none");
	$("#"+adCode+"Can").css("display","none");

	$("#"+adCode+"1").html(adName);
	$("#"+adCode+"2").html(adCode);
	if(higherCode !="" && higherCode !=null ){
		$("#"+higherCode).html(higherCode);
	}
	if($("#"+adCode+"upCounty").length > 0){
		$("#"+adCode+"Line2").css("display","inline");
		$("#"+adCode+"upCounty").css("display","inline");
		$("#"+adCode+"Line3").css("display","inline");
		$("#"+adCode+"deCity").css("display","inline");
	}

}


// 信息验证
function notEmptyValidate(code,name){
	var reg = /^\s*$/; //空白
	if(reg.test(code)){
		showPromptMsg("银联地区码为空");
		return true;
	}
	var regu = /^\d{4}$/;  //4位数字
	if(!regu.test(code)){
		showPromptMsg("银联地区码必须为4位数字");
		return true;
	}
	if(reg.test(name)){
		showPromptMsg("名称为空");
		return true;
	}
	return false;
}
//提示信息
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

//导出
function adExport(){
	$("#adForm").attr("action", contextPath+"/ad/adExport.htm");
	$("#adForm").attr("target", "exportResult");
	$("#adForm").submit();

	$("#adForm").attr("action", contextPath+"/ad/adQuery.htm");
	$("#adForm").attr("target", "queryResult");
}

//全部导出
function adTotalExport(){
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
			$("#adForm").attr("action", contextPath+"/ad/adTotalExport.htm");
			$("#adForm").attr("target", "exportResult");
			$("#adForm").submit();

			$("#adForm").attr("action", contextPath+"/ad/adQuery.htm");
			$("#adForm").attr("target", "queryResult");
		},
		"取消": function() {
			$( this ).dialog( "close" );
		}

	}
	});

}


//县升级为省辖市
function upgradeCounty(countyCode,higherCode,countyName,higherName){
	$("#dynamicInfo1").css("color","green");
	$("#dynamicInfo2").css("color","green");
	$("#countyCode_up").val(countyCode);
	$("#cityCode_up").val(higherCode);
	$("#dynamicInfo1").html(countyName);
	$("#dynamicInfo2").html(higherName);
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#dialog_upgradeCounty").show();
	$("#dialog_upgradeCounty").dialog({
		position:[500,80],
		resizable: false,
		width: 400,
		height: 200,
		modal: true,
		buttons: {
			"确定": function() {
				if(!confirm("确认无误吗？")) return;
				var countyCode = document.getElementById("countyCode_up").value;
				var cityCode = document.getElementById("cityCode_up").value;
				var regu = /^\d{4}$/;  //4位数字
				if(!regu.test(countyCode)){
					alert("银联地区码必须为4位数字");
					return;
				}
				if(!regu.test(cityCode)){
					alert("银联地区码必须为4位数字");
					return;
				}
				$( this ).dialog( "close" );
				$.ajax({
					type:"POST",
					url: contextPath+"/ad/upgradeCounty.json",
					data:{"countyCode":countyCode,"cityCode":cityCode},
					dataType:"json",
					success:function(msg){
						if(msg.result == "success"){
							alert("升级成功");
						}else if(msg.result == "fail"){
							alert("操作失败，"+ msg.errorMsg);
						}else{
							alert("系统异常，请稍候再试");
						}

					},
					error:function(){
						alert("系统异常，请稍候再试");
						}
				});
			},
			"取消": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function(){
			$("#upgradeCountyForm")[0].reset();
			$( this ).dialog( "close" );
			}
	});
}


// 省辖市降级为县
function degradeCity(code,name){
	$("#dynamicInfo3").css("color","green");
	$("#dynamicInfo4").css("color","green");
	$("#countyCode_de").val(code);
	$("#dynamicInfo3").html(name);
	$("#dynamicInfo4").html("");
	$("#dialog:ui-dialog").dialog( "destroy" );
	$("#dialog_degradeCity").show();
	$("#dialog_degradeCity").dialog({
		position:[520,80],
		resizable: false,
		width: 380,
		height: 200,
		modal: true,
		buttons: {
			"确定": function() {
				if(!confirm("确认无误吗？")) return;
				var countyCode = document.getElementById("countyCode_de").value;
				var cityCode = document.getElementById("cityCode_de").value;
				var regu = /^\d{4}$/;  //4位数字
				if(!regu.test(countyCode)){
					alert("银联地区码必须为4位数字");
					return;
				}
				if(!regu.test(cityCode)){
					alert("银联地区码必须为4位数字");
					return;
				}
				if(countyCode == cityCode){
					alert("两个地区码不能相同");
					return;
				}
				$( this ).dialog( "close" );
				$.ajax({
					type:"POST",
					url: contextPath+"/ad/degradeCity.json",
					data:{"countyCode":countyCode,"cityCode":cityCode},
					dataType:"json",
					success:function(msg){
						if(msg.result == "success"){
							alert("降级成功");
						}else if(msg.result == "fail"){
							alert("操作失败，"+ msg.errorMsg);
						}else{
							alert("系统异常，请稍候再试");
						}

					},
					error:function(){
						alert("系统异常，请稍候再试");
						}
				});
			},
			"取消": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function(){
			$("#degradeCityForm")[0].reset();
			$( this ).dialog( "close" );
			}
	});
}

// 修改县所在市
function modifyCounty(countyCode,cityCode,newCityCode){
	var flag = false;
	$.ajax({
		type:"POST",
		async:false,
		url: contextPath+"/ad/modifyCounty.json",
		data:{"countyCode":countyCode,"oldCityCode":cityCode,"newCityCode":newCityCode},
		dataType:"json",
		success:function(msg){
			if(msg.result == "success"){
				flag =true;

			}else if(msg.result == "fail"){
				showPromptMsg("修改所在市失败，"+msg.errorMsg);
			}else{
				showPromptMsg("修改所在市失败，系统异常，请稍候再试");
			}
		},
		error:function(){
			showPromptMsg("修改所在市失败，系统异常，请稍候再试");
		}
	});
	return flag;
}
// 修改市所在省
function modifyCity(cityCode,proCode,newProCode){
	var flag = false;
	$.ajax({
		type:"POST",
		async:false,
		url: contextPath+"/ad/modifyCity.json",
		data:{"cityCode":cityCode,"oldProCode":proCode,"newProCode":newProCode},
		dataType:"json",
		success:function(msg){
			if(msg.result == "success"){
				flag = true;
			}else if(msg.result == "fail"){
				showPromptMsg("修改所在省失败，"+msg.errorMsg);
			}else{
				showPromptMsg("修改所在省失败，系统异常，请稍候再试");
			}
		},
		error:function(){
			showPromptMsg("修改所在省失败，系统异常，请稍候再试");
		}
	});
	return flag;
}

//下载模版
function downloadTemplate(name){
	$("#exportData").val(name);
	$("#downloadForm").submit();
}

// 异步查询
function queryAdName(id,infoId){
	var code = $("#"+id).val();
	if(code == null || code == "") return;
	$.ajax({
		url: contextPath + "/ad/queryAdName.json",
		type: "post",
		data: {"code":code},
		dataType: "json",
		async : false,
		success : function(msg){
			if(msg.result == "success"){
				$("#"+infoId).css("color","green");
				$("#"+infoId).html(msg.name);
			}else{
				$("#"+infoId).css("color","red");
				$("#"+infoId).html("系统不存在");
			}
		},
		error : function(){
			$("#"+infoId).css("color","red");
			$("#"+infoId).html("系统异常...");
		}
	});
}

