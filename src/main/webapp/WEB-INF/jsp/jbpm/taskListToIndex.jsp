<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="news"/></title>
 	<script type="text/javascript">
    
    function reloadStore(){
    	//alert("ddd");
    	window.location.reload();
    }
    </script>
</head>
<body>
        <marquee height="100%" style="margin:0px;padding:0px" direction="up" scrollamount="2" scrolldelay="10" onmouseover="this.stop()" onmouseout="this.start()">
            <ol id="selectable">
           		<c:forEach items="${taskList}" var="v">
           			<li>
           				<table width="100%" >
           					<tr>
           						<td>
           						<font size="+0">${v[0]}</font>
           						</td>
           						<td width="50">
           						<font size="+0">${v[1]}</font>
           						</td>
           					</tr>
           				</table>
           			</li>
           		</c:forEach>
            </ol>
        </marquee>
</body>
</html>