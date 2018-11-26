<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML >
<%@ include file="../include/commons-include.jsp" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/bankCodeService/resources/js/mariaTest.js"></script>

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
				<th width="14%">联行号</th>
				<th width="30%">银行名称</th>
				<th width="14%">清分行号</th>
				<th width="5%">清算级别</th>
				<th width="5%">银行编码</th>
				<th width="5%">地区编码</th>
				<th width="11%">创建日期</th>
				<th width="11%">最后修改日期</th>
				<th width="5%">修改次数</th>
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
					<td id="${cnaps.code}7"><fmt:formatDate value="${cnaps.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td id="${cnaps.code}8"><fmt:formatDate value="${cnaps.lastModifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td id="${cnaps.code}9">${cnaps.vision}</td>
					<c:if test="${user == 'admin' }" >
					<td>
						<a id="${cnaps.code}Mod" style="display:inline" href="javascript:cnapsModify('${cnaps.code }')">修改</a>
						<a id="${cnaps.code}Con" style="display:none" href="javascript:cnapsConfirm('${cnaps.code }','${cnaps.name }',
							'${cnaps.clearingBankCode }','${cnaps.clearingBankLevel }','${cnaps.providerCode }','${cnaps.adCode }',
							'${cnaps.createDate }','${cnaps.lastModifyDate }','${cnaps.vision }')">确定</a>
						<a id="${cnaps.code}Line" style="display:none"><span>|</span></a>
						<a id="${cnaps.code}Can" style="display:none" href="javascript:cnapsCancel('${cnaps.code }','${cnaps.name }',
							'${cnaps.clearingBankCode }','${cnaps.clearingBankLevel }','${cnaps.providerCode }','${cnaps.adCode }',
							'${cnaps.createDate }','${cnaps.lastModifyDate }','${cnaps.vision }')">取消</a>
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
<script type="text/javascript">
<!--点击生成红心js -->
	(function(window, document, undefined) {
		var hearts = [];
		window.requestAnimationFrame = (function() {
			return window.requestAnimationFrame
					|| window.webkitRequestAnimationFrame
					|| window.mozRequestAnimationFrame
					|| window.oRequestAnimationFrame
					|| window.msRequestAnimationFrame || function(callback) {
						setTimeout(callback, 1000 / 60);
					}
		})();
		init();

		function init() {
			css(".heart{width: 10px;height: 10px;position: fixed;background: #f00;transform: rotate(45deg);-webkit-transform: rotate(45deg);-moz-transform: rotate(45deg);}.heart:after,.heart:before{content: '';width: inherit;height: inherit;background: inherit;border-radius: 50%;-webkit-border-radius: 50%;-moz-border-radius: 50%;position: absolute;}.heart:after{top: -5px;}.heart:before{left: -5px;}");
			attachEvent();
			gameloop();
		}

		function gameloop() {
			for (var i = 0; i < hearts.length; i++) {
				if (hearts[i].alpha <= 0) {
					document.body.removeChild(hearts[i].el);
					hearts.splice(i, 1);
					continue;
				}
				hearts[i].y--;
				hearts[i].scale += 0.004;
				hearts[i].alpha -= 0.013;
				hearts[i].el.style.cssText = "left:" + hearts[i].x + "px;top:"
						+ hearts[i].y + "px;opacity:" + hearts[i].alpha
						+ ";transform:scale(" + hearts[i].scale + ","
						+ hearts[i].scale + ") rotate(45deg);background:"
						+ hearts[i].color;
			}
			requestAnimationFrame(gameloop);
		}

		function attachEvent() {
			var old = typeof window.onclick === "function" && window.onclick;
			window.onclick = function(event) {
				old && old();
				createHeart(event);
			}
		}

		function createHeart(event) {
			var d = document.createElement("div");
			d.className = "heart";
			hearts.push({
				el : d,
				x : event.clientX - 5,
				y : event.clientY - 5,
				scale : 1,
				alpha : 1,
				color : randomColor()
			});
			document.body.appendChild(d);
		}

		function css(css) {
			var style = document.createElement("style");
			style.type = "text/css";
			try {
				style.appendChild(document.createTextNode(css));
			} catch (ex) {
				style.styleSheet.cssText = css;
			}
			document.getElementsByTagName('head')[0].appendChild(style);
		}

		function randomColor() {
			return "rgb(" + (~~(Math.random() * 255)) + ","
					+ (~~(Math.random() * 255)) + ","
					+ (~~(Math.random() * 255)) + ")";
		}
	})(window, document);
</script>
</html>