<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.org.editorg" /></title>
</head>
<body>
	<form:form commandName="orgForm" action="" method="post">
		<form:hidden path="id" />
		<form:hidden path="parentOrg.id" />
		<form:hidden path="orgseq" />
		<form:hidden path="orgLevel" />
		<form:hidden path="posiid" />
		<form:hidden path="isLeaf" />
		<form:hidden path="empid" />
		<input type="hidden" id="parentId" name="parentId" value="${param.id}"/>
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th nowrap="nowrap"><spring:message code="fhd.sys.orgstructure.org.orgcode" />：</th>
				<td><form:input path="orgcode" cssClass="required"></form:input><font color="red">*</font></td>
				<th><spring:message code="fhd.sys.orgstructure.org.orgName" />：</th>
				<td><form:input path="orgname" cssClass="required"></form:input><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.org.parentorgname" />：</th>
				<td><form:input path="parentOrg.orgname" disabled="true"></form:input>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.org.orgtype" />：</th>
				<td><div style="float:left"><d:select name="orgType" style="width:100px" dicTypeId="0orgtype" property="${orgForm.orgType}"/></div><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.sn" />：</th>
				<td><form:input path="sn" cssClass="required"></form:input><font color="red">*</font></td>
				<th><spring:message code="fhd.sys.orgstructure.org.orgstatus" />：</th>
				<td>
				<form:select path="orgStatus" name="orgStatus">
					<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
					<option value="1"><spring:message code="fhd.common.normal"/></option>
					<option value="0"><spring:message code="fhd.common.cancellation"/></option>
				</form:select><font color="red">*</font>
			</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.org.forum" />：</th>
				<td><div style="float:left"><d:select name="forum" style="width:100px" dicTypeId="0forum" property="${orgForm.forum}"/></div><font color="red">*</font></td>
				<th><spring:message code="fhd.sys.orgstructure.org.region" />：</th>
				<td><div style="float:left"><d:select name="region" style="width:100px" dicTypeId="0district" property="${orgForm.region}"/></div><font color="red">*</font></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.org.orgaddress" />：</th>
				<td><form:input path="address"></form:input></td>
				<th><spring:message code="fhd.sys.orgstructure.org.orgzipcode" />：</th>
				<td><form:input path="zipcode"></form:input></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.org.linkMan" />：</th>
				<td><form:input path="linkMan"></form:input></td>
				<th><spring:message code="fhd.sys.orgstructure.org.linktel" />：</th>
				<td><form:input path="linkTel"></form:input></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.org.email" />：</th>
				<td><form:input path="email"></form:input></td>
				<th><spring:message code="fhd.sys.orgstructure.org.weburl" />：</th>
				<td><form:input path="weburl"></form:input></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.effectdate" />：</th>
				<td><form:input path="startDate" onclick="WdatePicker();"
					cssClass="Wdate"></form:input></td>
				<th><spring:message code="fhd.common.abatedate" />：</th>
				<td><form:input path="endDate" onclick="WdatePicker();"
					cssClass="Wdate"></form:input></td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.remark" />：</th>
				<td colspan="3"><form:textarea path="remark" rows="5" cssStyle="width:612;"></form:textarea>
				</td>
			</tr>
			<tr>
				<th colspan="4" align="center">
					<input type="button" name="submits" id="submits" value="<spring:message code="fhd.common.save"/>" class="fhd_btn" />&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn" />
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function(){
			var validator = $("#orgForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					zipcode:{
						maxlength:6
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
						url:'${ctx}/sys/orgstructure/org/add.do',
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
			         			closeWindow();//关闭窗口
			         		}else if("false" == data){
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
			         			closeWindow();
			         		}else{
			         			parent.window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", data);
			         			closeWindow();
			         		}
			     		}
					};
					$('#orgForm').ajaxSubmit(options);
					return false;
				}else{
					return false;
				}
		    });
			 	
		});
		/*
		$(document).ready(function(){
			var validator = $("#orgForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					zipcode:{
						maxlength:6
					}
				},
				submitHandler:function(form){
					form.submit();
					window.returnValue='refresh';
					window.close();
		        }
			});
			
			$("#reset").click(function() {
		        validator.resetForm();
		    });
		});
		*/
	</script>
</body>
</html>