<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.auth.authority.editauthority"/></title>
</head>
<body>
<form:form commandName="sysAuthorityForm" action="${ctx}/sys/auth/authority/edit.do" method="post">
	<form:hidden path="id" />
	<form:hidden path="seqNo" />
	<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
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
			<th><spring:message code="fhd.common.sn" />：</th>
			<td><form:input path="sn" cssClass="required"/></td>
		</tr>
		<tr>
			<th><spring:message code="fhd.sys.auth.authority.authorityLevel"/>：</th>
			<td><form:input path="rank" disabled="true"/></td>
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
		<tr>
			<th colspan="2" align="center">
				<input type="submit" value="<spring:message code="fhd.common.save"/>" class="fhd_btn" />
				&nbsp;&nbsp;
				<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn" />
			</th>
		</tr>
	</table>
</form:form>
<script type="text/javascript">

	$(document).ready(function(){
		$("#sysAuthorityForm").validate({
			errorElement: "em",
			success: function(label) {
				label.text("ok!").addClass("success");
			},
			rules: {
				authorityCode:{
					maxlength:32
				},
				authorityname:{
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
				},
				etype:{
					required:true
				}
			},
			submitHandler:function(form){
				form.submit();
				//parent.parent.parentNodeLoad();
	        }
		});

		$("#reset").click(function() {
	        validator.resetForm();
	    });
	});
</script>
</body>
</html>