<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看当前流程图</title>
</head>
<body>
<!-- 1.获取到规则流程图 -->
<img style="position: absolute;top: 0px;left: 0px;" src="${basePath}/chqs/workflow/display?deploymentId=MeetingMeeting&imageName=2">

<!-- 2.根据当前活动的坐标，动态绘制DIV -->
<div style="position: absolute;border:1px solid red;top:${acs.y}px;left: ${acs.x}px;width: ${acs.width }px;height:${acs.height }px;   "></div></body>
</html>