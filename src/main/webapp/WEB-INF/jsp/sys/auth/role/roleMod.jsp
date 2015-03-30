<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.role.role"/><spring:message code="fhd.common.edit"/></title>
</head>
<body>
	<form:form commandName="sysRoleForm" action="" method="post">
		<table id="showTable" width="100%"  border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.auth.role.roleCode"/>：</th>
				<td><form:input path="roleCode" cssClass="required"/><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.auth.role.roleName"/>：</th>
				<td><form:input path="roleName" cssClass="required"/><font color="red">*</font></td>
			</tr>
			<tr align="right">
				<th colspan="2">
					<input type="button" id="submits" value="<spring:message code="fhd.common.submit"/>" class="fhd_btn"/>
					&nbsp;&nbsp;
					<input type="reset"  value="<spring:message code="fhd.common.reset"/>" class="fhd_btn"/>
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function(){
			var validator = $("#sysRoleForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					roleCode:{
						maxlength:30
					},
					roleName:{
						maxlength:30
					}
				}
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
						url:'${ctx}/sys/auth/roleUpdate.do',
						type:'POST',
						success:function(data) {
			     			msgTip.hide();
			         		if("true" == data){
			         			parentWindow().mv_grid.grid.store.reload();
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
			         			closeWindow();//关闭窗口
			         		}else if("false" == data){
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
			         		}else{
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", data);
			         		}
			     		}
					};
					$('#sysRoleForm').ajaxSubmit(options);
					return false;
				}else{
					return false;
				}
		    });
		});
	</script>
</body>
</html>