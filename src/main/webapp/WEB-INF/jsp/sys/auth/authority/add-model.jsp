<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.authority.authority"/><spring:message code="fhd.common.add"/></title>
</head>
<body>
<form:form commandName="sysAuthorityForm" action="${ctx}/sys/auth/authority/add.do" method="post">
	<form:hidden path="id" />
	<form:hidden path="parentAuthority.id" />
	<form:hidden path="seqNo" />
	<input type="hidden" id="parentId" name="parentId" value="${param.id}"/>
	<table id="showTable" width="100%"  border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
		<tr>
			<th><spring:message code="fhd.sys.auth.authority.authorityCode"/>：</th>
			<td><form:input path="authorityCode" cssClass="required"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.sys.auth.authority.authorityName"/>：</th>
			<td><form:input path="authorityName" cssClass="required"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.sys.auth.authority.parentAuthorityName" />：</th>
			<td><form:input path="parentAuthority.authorityName" disabled="true"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.common.sn"/>：</th>
			<td><form:input path="sn" cssClass="required"/></td>
		</tr>
		<tr>		
			<th><spring:message code="fhd.common.url"/>：</th>
			<td><form:input path="url"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.common.etype"/>：</th>
			<td><select id="etype" name="etype">
				<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
				<option value="M"><spring:message code="fhd.common.menu"/></option>
				<option value="B"><spring:message code="fhd.common.buttons"/></option>
				<option value="T"><spring:message code="fhd.common.tabs"/></option>
				</select>
		</tr>
		<tr>
			<th><spring:message code="fhd.common.iconurl"/>：</th>
			<td><form:input path="icon"/></td>
		</tr>
		<tr align="center">
			<th colspan="2">
				<input type="button" id="submits"  value="<spring:message code="fhd.common.submit"/>" class="fhd_btn"/>
				&nbsp;&nbsp;
				<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn"  />
			</th>
		</tr>
	</table>
</form:form>
<script type="text/javascript">
$(document).ready(function(){
	var validator = $("#sysAuthorityForm").validate({
		errorElement: "em",
		success: function(label) {
			label.text("ok!").addClass("success");
		},
		rules: {
			authorityCode:{
				maxlength:127
			},
			authorityname:{
				maxlength:127
			},
			sn:{
				maxlength:7
			},
			isMenu:{
				required:true
			},
			etype:{
				required:true
			}
		}
	});

	$("#reset").click(function() {
        validator.resetForm();
    });
    
    $('#submits').click(function(){
		if(true == validator.form()){
            var msgTip = FHD.opWait( {
				title : "<spring:message code='fhd.common.prompt'/>",
				width : 250,
				msg : "<spring:message code='fhd.common.opWait'/>",
				progress:true,
				wait:true
			});
			
			var options = {
				url:'${ctx}/sys/auth/authority/add.do',
				type:'POST',
				success:function(data) {
	     			msgTip.hide();
	         		if("true" == data){
	         			if("${operation}"=="page"){//来自列表页面的操作
		         			parentWindow().mv_grid.grid.store.reload();
		         			parentWindow().parent.parent.selectNodeReload();
		         			
	         			}else{//来自tree的操作
	         				parentWindow().selectNodeReload();
	         				
	         			}
	         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
	         			closeWindow();
	         		}else
	         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
	     		}
			};
			$('#sysAuthorityForm').ajaxSubmit(options);
			return false;
		}else{
			return false;
		}
    });
	 	
});


</script>
</body>
</html>