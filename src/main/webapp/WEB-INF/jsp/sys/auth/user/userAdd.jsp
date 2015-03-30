<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.user.user"/><spring:message code="fhd.common.add"/></title>
	<base target="_self">
</head>
<body>
	<form:form commandName="sysUserForm" action="${ctx}/sys/auth/userSave.do" method="post">
		<input type='hidden' name='callback' value='opReload'/>
		<input type='hidden' name='windowId' value='${param.windowId}'/>
		<table id="showTable" width="100%"  border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.auth.user.username"/>：</th>
				<td><form:input path="username" /><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.auth.role.realName"/>：</th>
				<td><form:input path="realname" cssClass="required"/><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.auth.user.password"/>：</th>
				<td><form:password path="password" cssClass="required" cssStyle="border-bottom: #b5b8c8 1px solid; border-left: #b5b8c8 1px solid; line-height: 22px; width: 200px; border-top: #b5b8c8 1px solid; border-right: #b5b8c8 1px solid;"/><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.regdate"/>：</th>
				<td><form:input path="regdate" onclick="WdatePicker();" cssClass="Wdate" /></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.abatedate"/>：</th>
				<td><form:input path="expiryDate" onclick="WdatePicker();" cssClass="Wdate" /></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.credentialsexpiryDate"/>：</th>
				<td><form:input path="credentialsexpiryDate" onclick="WdatePicker();" cssClass="Wdate" /></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.status"/>：</th>
			<td>
				<form:select path="userStatus" name="userStatus" cssClass="required">
					<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
					<option value="1"<c:if test="${sysUserForm.userStatus eq '1' }">selected="selected"</c:if>><spring:message code="fhd.common.normal"/></option>
					<option value="0"<c:if test="${sysUserForm.userStatus eq '0' }">selected="selected"</c:if>><spring:message code="fhd.common.cancellation"/></option>
				</form:select><font color="red">*</font>
			</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.lockState"/>：</th>
				<td>
					<form:select path="lockstate" cssClass="required">
						<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
						<option value="0"><spring:message code="fhd.common.notLocked"/></option>
						<option value="1"><spring:message code="fhd.common.lock"/></option>
					</form:select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.enable"/>：</th>
				<td>
					<form:select path="enable" cssClass="required">
						<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
						<option value="1"><spring:message code="fhd.common.true"/></option>
						<option value="0"><spring:message code="fhd.common.false"/></option>
					</form:select>
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th>MAC地址：</th>
				<td><form:input path="mac"/></td>
			</tr>
			<tr align="center">
				<th colspan="4">
					<input type="button" id="submits" value="<spring:message code="fhd.common.submit"/>" class="fhd_btn"/>
					&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn"  />
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function(){
			var validator = $("#sysUserForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					username:{
						maxlength:30,
						required:true,
						remote: {
					        type:"POST",
					        url:"${ctx}/sys/orgstructure/emp/queryUserByUsername.do", 
					        data:{
					        	username: function() {
						            return $("#username").val();
						        },
						        operateType:3
					        }
						}
					},
					realname:{
						maxlength:30
					},
					password:{
						maxlength:20
					}
				}
			});

			$('#submits').click(function(){
				var mac = document.getElementById("mac").value;
				if(mac != null && mac != ''){
					if(!checkMAC(mac)){
						return false;
					}
				}
				if(true == validator.form()){
		            var msgTip = FHD.opWait();
					var options = {
						url:'${ctx}/sys/auth/userSave.do',
						type:'POST',
						success:function(data) {
			     			msgTip.hide();
			         		if(data){
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateSuccess'/>");
			         			parentWindow().mv_grid.grid.store.reload();
			         			closeWindow();
			         		}else{
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code='fhd.common.operateFailure'/>");
			         		}
			     		}
					};
					$('#sysUserForm').ajaxSubmit(options);
					return false;
				}else{
					return false;
				}
		    });
	
			$("#reset").click(function() {
		        validator.resetForm();
		    });
		});

		//mac地址正则表达式
		function checkMAC(mac){
			var reg=/[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}/; 
			if(!reg.test(mac)){ 
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","mac地址格式不正确!mac地址格式为:00-24-21-19-BD-E4");
				document.getElementById("mac").value="";
				document.getElementById("mac").focus(); 
				return false; 
			} 
			return true; 
		}
	</script>
</body>
</html>