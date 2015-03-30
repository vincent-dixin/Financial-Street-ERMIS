<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>修改密码</title>
</head>
<body>
	<form:form commandName="sysUserForm"  method="post">
		<input type="hidden" id="userid" name="userid" value="${sysUserForm.id}"/>
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th>用&nbsp;户&nbsp;名：</th>
				<td><form:input path="username" disabled="true" cssStyle="width:200px"/></td>
			</tr>
			<tr>
				<th>真实姓名：</th>
				<td><form:input path="realname" cssClass="required" cssStyle="width:200px"/></td>
			</tr>
			<tr>
				<th>原始密码：</th>
				<td><input type="password" id="oldpwd" name="oldpwd" class="required" style="width:200px"/></td>
			</tr>
			<tr>
				<th>新&nbsp;密&nbsp;码：</th>
				<td><input type="password" id="password" name="password" class="required" style="width:200px"/></td>
			</tr>
			<tr>
				<th>重复密码：</th>
				<td><input id="confirm_password" name="confirm_password" type="password" class="required" style="width:200px"/></td>
			</tr>
			<tr align="right">
				<th colspan="4">
					<input type="button" id="submits" value="<spring:message code="fhd.common.submit"/>" class="fhd_btn"/>
					&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn"/>
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
				messages:{
					oldpwd:"原始密码有误!"
				},
				rules: {
					oldpwd:{
						maxlength:30,
						required:true,
						remote: {
					        type:"POST",
					        url:"${ctx}/sys/auth/queryUserByUsername.do", 
					        data:{
					        	oldpwd: function() {
					        		
						            return document.getElementById("oldpwd").value;
						        },
						        uid: function() {
							        return document.getElementById("userid").value;
						        }
					        }
						}
					},
					confirm_password:{
						required:true,
						maxlength:30,
						equalTo:"#password"
					},
					realname:{
						maxlength:30
					},
					password:{
						maxlength:30
					}
				}
			});
			$('#submits').click(function(){
				if(true == validator.form()){
		            var msgTip = FHD.opWait();
					
					var options = {
						url:'${ctx}/sys/auth/userUpdatePwd.do',
						type:'POST',
						success:function(data) {
			     			msgTip.hide();
			         		if("true" == data){
			         			//parentWindow().opReload();
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
			         			closeWindow();
			         		}else
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
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
	</script>
</body>
</html>