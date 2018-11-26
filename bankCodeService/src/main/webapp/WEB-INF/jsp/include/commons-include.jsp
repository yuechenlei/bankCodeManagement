<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link type="text/css" rel="stylesheet" href="<spring:url value="/resources/js/jquery/themes/blitzer/jquery-ui.min.css" />">
<link type="text/css" rel="stylesheet" href="<spring:url value="/resources/js/jquery/themes/blitzer/jquery.ui.theme.css" />">
<link href="<spring:url value="/resources/css/common.css" />" rel="stylesheet" type="text/css" />
<link href="<spring:url value="/resources/css/style.css" />" rel="stylesheet" type="text/css" />
<link href="<spring:url value="/resources/css/page.css" />" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<spring:url value="/resources/js/jquery/jquery-1.10.2.min.js" />"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/jquery/ui/jquery-ui.min.js" />"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/jquery/ui/jquery.ui.datepicker.default.js" />"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/jquery/ui/i18n/jquery.ui.datepicker-zh-CN.min.js" />"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/page/page.js" />"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/common.js" />"></script>

<script type="text/javascript">
	$(function() {
		$("form").submit(function() {
			$(":input").each(function() {
				if ($(this).attr("type") != "file") {
					$(this).val($.trim($(this).val()));
				}
			});
		});
	});

	// 全局变量 存放着当前应用名 contextPath 
	var contextPath = "${pageContext.request.contextPath}";
	//var staticFilesHost = "${staticFilesHost}";
</script>