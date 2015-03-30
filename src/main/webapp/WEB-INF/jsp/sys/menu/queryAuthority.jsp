<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="function"/><spring:message code="list"/></title>
<base target="_self">
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<script type="text/javascript">
	function selectRecord(){
		var rets = new Object();
		var s = document.getElementsByName("id");
	    for(var i=0;i <s.length;i++){            
	        if(s[i].checked){
	        	//根据选择的id获得需要的authorityName和authorityCode值
	        	rets.code = s[i].parentNode.nextSibling.innerText;
	        	rets.code = s[i].value;
	        	rets.name = s[i].parentNode.nextSibling.nextSibling.innerText;
	        	//document.getElementById(authorityName)
		        //parentWindow().document.getElementById("authorityName").value=s[i].parentNode.nextSibling.nextSibling.innerText;
		        //parentWindow().document.getElementById("authority_id").value=s[i].value;
		        break;
	        }                                    
	    }                         
		//closeWindow();//关闭窗口
	    window.returnValue = rets;
        window.close();
        
	}

	function unSelectRecord(){
		window.returnValue = 'cancel';
        window.close();
	}
	
	function closeWindows(){
		window.close();
	}
	
	$(document).ready(function(){
		$("#select").attr("disabled","disabled");
	});
	
	function changeButton(tag){
		onClickRaidoColor(tag);
		$("#select").removeAttr("disabled");                  
	    //获取radio对象 
	    //var radioObj = $("[name='id'][@checked]"); 
	    //获取当前checked的value值 
		//var radio = radioObj.get(0).value; 
	}
</script>
</head>
<body onkeydown="if(event.keyCode==116){reload.click();}"> 
<a id="reload" href="${ctx}/sys/auth/functionList.do" style="display:none">reload...</a> 
<form id="sysAuthorityForm" name="sysAuthorityForm" action="${ctx}/sys/auth/functionByCondition.do" method="post">
	<table width="750px" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr><td class="fhd_nav_title">&nbsp;<img src="${ctx}/images/panel/arrownav.jpg" width="15" height="15" /><spring:message code="sysmanage"/><spring:message code="auth"/><spring:message code="function"/></td></tr>
	</table>
	<table width="750px" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="hideTable(this)" width="15" height="15" /><spring:message code="querycondition"></spring:message></td></tr>
	</table>
	<table id="showTable" width="750px" border="0" cellpadding="0" cellspacing="0"
		class="fhd_query_table" style="display:block;">
		<tr>  
			<th><spring:message code="functionCode"/>：</th>
			<td><input type="text" name="authorityCode" id="authorityCode" value="${param.authorityCode}"/></td>                                             
			<th><spring:message code="functionName"/>：</th>
			<td><input type="text" name="authorityName" id="authorityName" value="${param.authorityName}"/></td>
		</tr>
		<tr>
			<td align="center" colspan="4">
				<input type="submit" value="<spring:message code="search_btn" />" class="fhd_btn" />
			</td>
		</tr>
	</table>
	<table width="750px" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F5F5" class="fhd_border_table"><tr><td>
	<display:table name="authorityList" id="o" requestURI="${ctx}/sys/auth/functionList.do" pagesize="10" class="fhd_result">
		<display:column titleKey="select">
			<input type="radio" name="id" id="id" value="${o.id}" onclick="changeButton(this);"/>
		</display:column>
		<display:column property="authorityCode" sortable="true" titleKey="functionCode"></display:column>
		<display:column property="authorityName" sortable="true" titleKey="functionName"></display:column>
		<display:column sortable="true" titleKey="functionGroupName">
			<fhd:authorityLabel path="${o.parentAuthority.id }"/>
		</display:column>
	</display:table>
	<span id="exportlinks" class="exportlinks"></span>
	<span id="fhd_button" class="fhd_button">
		<c:if test="${not empty authorityList}">
			<input id="select" type="button" class="fhd_btn" onclick="javascript:selectRecord();" value="<spring:message code="select" />" />
		</c:if>
		<input type="button" class="fhd_btn" onclick="javascript:unSelectRecord();" value="<spring:message code="unSelect" />" />
		<input type="button" class="fhd_btn" onclick="javascript:closeWindows();" value="<spring:message code="close" />" />
	</span>
	</td></tr></table>
</form>
<c:if test="${not empty success }">
<script>
<c:choose>
<c:when test="${success eq '1'}">
	alert("<spring:message code="success" />");
</c:when>
<c:when test="${success eq '0'}">
	alert("<spring:message code="delfailure" />");
</c:when>
</c:choose>
</script>
</c:if>
</body>
</html>