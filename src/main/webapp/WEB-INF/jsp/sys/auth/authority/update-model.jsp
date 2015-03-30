<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.auth.authority.authority"/><spring:message code="fhd.common.edit"/></title>
<base target="_self">
</head>
<body>
<form:form commandName="sysAuthorityForm" action="${ctx}/sys/auth/authority/update.do" method="post">
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
			<th><spring:message code="fhd.sys.auth.authority.parentAuthorityName"/>：</th>
			<td>
				<fhd:authoritySelect path="${sysAuthorityForm.parentAuthority.id}" value="${sysAuthorityForm.parentAuthority.id}" name="parentId" id="parentId" cssClass="required"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="fhd.common.sn"/>:</th>
			<td><form:input path="sn" cssClass="required"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.sys.auth.authority.authorityLevel"/>：</th>
			<td><form:input path="rank" disabled="true" value="${sysAuthorityForm.parentAuthority.rank+1 }"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.common.isLeaf" />：</th>
			<td>
				<select id="isLeaf" name="isLeaf">
					<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
					<option value="true" <c:if test="${sysAuthorityForm.isLeaf eq 'true'}">selected="selected"</c:if>><spring:message code="fhd.common.true"/></option>
					<option value="false" <c:if test="${sysAuthorityForm.isLeaf eq 'false'}">selected="selected"</c:if>><spring:message code="fhd.common.false"/></option>
				</select>
			</td>
		</tr>
		<tr>		
			<th><spring:message code="fhd.common.url"/>：</th>
			<td><form:input path="url"/></td>
		</tr>
				<tr>
			<th><spring:message code="fhd.common.etype"/>：</th>
			<td><select id="etype" name="etype">
				<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
				<option value="M" <c:if test="${sysAuthorityForm.etype eq 'M' }">selected="selected"</c:if>><spring:message code="fhd.common.menu"/></option>
				<option value="B" <c:if test="${sysAuthorityForm.etype eq 'B' }">selected="selected"</c:if>><spring:message code="fhd.common.buttons"/></option>
				<option value="T" <c:if test="${sysAuthorityForm.etype eq 'T' }">selected="selected"</c:if>><spring:message code="fhd.common.tabs"/></option>
				</select>
		</tr>
		<tr>
			<th><spring:message code="fhd.common.iconurl"/>：</th>
			<td><form:input path="icon"/></td>
		</tr>
		<tr align="right">
			<th colspan="2">
				<input type="button" id="submits" value="<spring:message code="fhd.common.submit"/>" class="fhd_btn"/>
				&nbsp;&nbsp;
				<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn"/>
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
			authorityName:{
				maxlength:127
			},
			sn:{
				maxlength:7
			},
			rank:{
				required:true
			},
			isLeaf:{
				required:true
			}
		},
		submitHandler:function(form){
			form.submit();
			window.returnValue = 'refresh';
			window.close();
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
				url:'${ctx}/sys/auth/authority/update.do',
				type:'POST',
				success:function(data) {
	     			msgTip.hide();
	         		if("true" == data){
	         			parentWindow().mv_grid.grid.store.reload();
	         			parentWindow().parent.parent.selectNodeReload2();
	     					
	         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
	         			closeWindow();//关闭窗口
	         		}else{
	         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>\n"+data);
	         			closeWindow();
	         		}
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