<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="chooseOperate" /></title>
<base target="_self">
</head>
<body>
<form:form commandName="chooseForm" action="" method="post">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F5F5" class="fhd_border_table"><tr><td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table">
		<tr>
			<td colspan="2"><spring:message code="chooseOperateValue"/>：</td>
		</tr>
		<tr>
			<td>
				<input type="radio" id="move" name="radio" value="move"/><spring:message code="move"/>
				<input type="radio" id="cancel" name="radio" value="cancel"/><spring:message code="cancel"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="<spring:message code="confirm"/>" class="fhd_btn" />
			</td>
		</tr>
	</table>
	</td></tr></table>
</form:form>
<script type="text/javascript">
$(document).ready(function(){
	$("#chooseForm").validate({
		errorElement: "em",
		success: function(label) {
			label.text("ok!").addClass("success");
		},
		submitHandler:function(form){
			var ss = document.getElementsByName("radio");
			var operate = "cancel";
			for(var i=0;i <ss.length;i++){            
		        if(ss[i].checked){
			        operate = ss[i].value;
		        }
			}
			form.submit();
			window.returnValue = operate;
			window.close();
        }
	});
});
</script>
</body>
</html>