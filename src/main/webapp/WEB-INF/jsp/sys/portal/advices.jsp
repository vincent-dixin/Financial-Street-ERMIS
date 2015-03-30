<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="fhd.sys.portal.advices"/></title>
</head>
<body>
    <div>
        <ol id="selectable">
        	<c:forEach items="${advicesList}" var="v">
            	<li><a href="${ctx}/sys/portal/advicesInfo.do?id=${v.id}" target="_blank">● ${v.title}</a></li>
            </c:forEach>
        </ol>
    </div>
</body>
</html>