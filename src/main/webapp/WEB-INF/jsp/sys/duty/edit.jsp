<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.orgstructure.posi.editposi" /></title>
</head>
<body>
	<form:form commandName="dutyForm" action="" method="post">
		<input type="hidden" id="orgId" name="orgId" value="${orgId}" />
		<input type="hidden" id="id" name="id" value="${param.id}" />
		<table id="showTable" width="100%" border="0" cellpadding="0"
			cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.duty.dutycompany" />：</th>
				<td><input type="text" id="dutyCompany" name="dutyCompany"
					value="${org.orgname}" readOnly="true" /> <font style="color: red">*</font>
				</td>
				<th><spring:message code="fhd.sys.duty.dutyname" />：</th>
				<td><form:input path="dutyName" cssClass="required" /> <font
					style="color: red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.duty.dutyusable" />：</th>
				<td><form:select path="dutyStatus" name="dutyStatus">
					<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
					<option value="1" <c:if test="${dutyForm.status eq '1'}">selected="selected"</c:if>><spring:message code="fhd.common.true"/></option>
					<option value="0" <c:if test="${dutyForm.status eq '0'}">selected="selected"</c:if>><spring:message code="fhd.common.false"/></option>
				</form:select><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.sys.duty.dutyweight" />：</th>
				<td><form:input path="weight" cssClass="required" /> <font
					style="color: red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.duty.dutyremark" />：</th>
				<td colspan="3"><form:textarea path="remark" rows="5"></form:textarea>
			</tr>
			<tr>
				<th colspan="4" align="center"><input type="button"
					id="submits" name="submits"
					value="<spring:message code="fhd.common.save"/>" class="fhd_btn" />&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>"
					class="fhd_btn" /></th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		<c:if test="${afterAdd != null}">
			window.close();
		</c:if>
	
		$(document).ready(function(){
			var validator = $("#dutyForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					dutyName:{
						maxlength:100
					},
					dutyStatus:{
						isNeed:true
					},
					weight:{
						number:2
					},
					remark:{
						maxlength:100
					}
				}
			});
			
			$("#reset").click(function() {
		        validator.resetForm();
		    });
		    
		    $('#submits').click(function(){
				if(true == validator.form()){
		            var msgTip = FHD.opWait( {
						title : '提示',
						width : 250,
						msg : '操作进行中,请稍后......',
						progress:true,
						wait:true
					});
					
					var options = {
						url:'${ctx}/sys/duty/edit.do',
						type:'POST',
						success:function(data) {
			     			msgTip.hide();
			         		if("true" == data){
			         			if("${operation}"=="page"){//来自列表页面的操作
				         			parentWindow().mv_grid.grid.store.reload();
				         		//	parentWindow().parent.parent.selectNodeReload();
			         			}
			         			//	parentWindow().selectNodeReload();
			         			
			         			window.top.Ext.ux.Toast.msg('提示', '操作成功!');
			         			closeWindow();
			         			//关闭窗口
			         		}else{
			         			window.top.Ext.ux.Toast.msg('提示', '操作失败!');
			         		}
			     		}
					};
					$('#dutyForm').ajaxSubmit(options);
					return false;
				}else{
					return false;
				}
		    });
		});
	</script>
</body>
</html>