<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="fhd.sys.portal.news"/></title>
</head>
<body>
    <div>
        <marquee height="100px" direction="up" scrollamount="2" scrolldelay="10" onmouseover="this.stop()" onmouseout="this.start()">
            <ol id="selectable">
           		<c:forEach items="${newsList}" var="v">
           			<li><a href="${ctx}/sys/portal/newsInfo.do?id=${v.id}" target="_blank">${v.title}</a></li>
           		</c:forEach>
            </ol>
        </marquee>
    </div>
</body>
</html>
