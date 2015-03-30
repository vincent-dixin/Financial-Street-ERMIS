<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.emp.emplist"/></title>
	<script type="text/javascript">
		function saveEmpPosi(){
	
			var s = document.getElementsByName("id");
		    var check=0;                             
		    for(var i=1;i <s.length;i++){        
		        if(s[i].checked){
		       		check=1;                       
		        }                                    
		    }                          
		    if(check == 0){                          
		       alert("<spring:message code="fhd.common.addTip"/>");          
		       return false;                         
		    }
		    document.forms[0].action = "${ctx}/sys/orgstructure/emp/addEmpPosi.do";
		    $('#empForm').submit();
		    window.returnValue='refresh';
		    window.close();
			return true;
		}
	
		function changeButton(tag){
			onClickColor(tag);
			
			var s = document.getElementsByName("id");
		    var check=0;                             
		    for(var i=1;i <s.length;i++){        
		        if(s[i].checked){
		       		check=1;                    
		        }                                    
		    }                          
		    if(check == 0){                          
		    	$("#adds").attr("disabled","disabled");
		      	return false;                         
		    }else{
		    	$("#adds").removeAttr("disabled");
		    	return true;
		    }
		}
	
		$(document).ready(function(){
			$("#adds").attr("disabled","disabled");
		});
	</script>
</head>
<body>
<form id="empForm" name="empForm" action="${ctx}/sys/orgstructure/emp/queryEmpByPosiIdCondition.do" method="post">
	<input type="hidden" id="id" name="id" value="${param.id}" />
	<table width="750px" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td class="fhd_query_title">&nbsp;<img
				src="${ctx}/images/plus.gif" onclick="hideTable(this)" width="15"
				height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
		</tr>
	</table>
	<table id="showTable" width="750px" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display: block;">
		<tr>
			<th><spring:message code="fhd.sys.orgstructure.emp.empcode" />：</th>
			<td><input type="text" name="filter_LIKES_empcode" id="empcode"
				value="${param.filter_LIKES_empcode}" /></td>
			<th><spring:message code="fhd.sys.orgstructure.emp.empname" />：</th>
			<td><input type="text" name="filter_LIKES_empname" id="empname"
				value="${param.filter_LIKES_empname}" /></td>
		</tr>
		<tr>
			<th colspan="4" align="center"><input id="submits" type="submit"
				class="fhd_btn" value="<spring:message code="fhd.common.search" />" /></th>
		</tr>
	</table>
	<table width="750px" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F5F5" class="fhd_border_table"><tr><td>
		<display:table id="o" name="emps" requestURI="${ctx}/sys/orgstructure/emp/queryEmployeeByOrg.do" pagesize="10" class="fhd_result">
			<display:column titleKey="select" style="width:10px">
				<input type="checkbox" id="id" name="id" value="${o.id}" onclick="javascript:changeButton(this);"/>
			</display:column>
			<display:column property="empcode" sortable="true" titleKey="empcode"></display:column>
			<display:column property="empname" sortable="true" titleKey="empname"></display:column>
			<display:column property="sysOrganization.orgname" sortable="true" titleKey="sysOrganization.orgname"></display:column>
			<display:column property="username" sortable="true" titleKey="username"></display:column>
			<display:column property="mobikeno" sortable="true" titleKey="mobikeno"></display:column>
			<display:column property="regdate" sortable="true" titleKey="regdate"></display:column>
			<display:column sortable="true"  titleKey="status">
				<fhd:dictEntryLabel path="${o.empStatus}"/>
			</display:column>
		</display:table>
		<span id="exportlinks" class="exportlinks"></span>
		<span id="fhd_button" class="fhd_button">
			<input id="adds" type="button" class="fhd_btn" onclick="javascript:saveEmpPosi();" value="<spring:message code="fhd.common.add" />" />
		</span>
	</td></tr></table>
</form>
<c:if test="${empty emps}">
	<script type="text/javascript">
		$("#submits").attr("disabled","disabled");
	</script>
</c:if>
</body>
</html>