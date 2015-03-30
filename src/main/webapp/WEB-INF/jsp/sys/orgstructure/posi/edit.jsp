<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.posi.editposi" /> </title>
</head>
<body>
	<form:form commandName="posiForm">
		<form:hidden path="sysOrganization.id"/>
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.orgstructure.posi.posicode"/>：</th>
				<td>
					<form:input path="posicode" cssClass="required"/><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.sys.orgstructure.posi.posiname"/>：</th>
				<td>
					<form:input path="posiname" cssClass="required"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.status"/>：</th>
				<td>
					<form:select path="posiStatus" name="posiStatus" cssClass="required">
						<option value=""><spring:message code="fhd.common.pleaseSelect"/></option>
						<option value="1"<c:if test="${posiForm.posiStatus eq '1' }">selected="selected"</c:if>><spring:message code="fhd.common.normal"/></option>
						<option value="0"<c:if test="${posiForm.posiStatus eq '0' }">selected="selected"</c:if>><spring:message code="fhd.common.cancellation"/></option>
					</form:select><font color="red">*</font>
				</td>
				<th><spring:message code="fhd.common.sn"/>：</th>
				<td>
					<form:input path="sn" cssClass="required"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.effectdate" />：</th>
				<td>
					<input type="text" name="startDate" id="startDate" onchange="islegal();" onfocus="WdatePicker();" class="Wdate" style="width:200px"/>
					<!--<form:input path="startDate" onclick="WdatePicker();" cssClass="Wdate"></form:input><font color="red">*</font>-->
				</td>
				<th><spring:message code="fhd.common.abatedate" />：</th>
				<td>
					<input type="text" name="endDate" id="endDate" onfocus="WdatePicker();" onchange="islegal();" class="Wdate"  style="width:200px"/>
					<!--<form:input path="endDate" onclick="WdatePicker();" cssClass="Wdate"></form:input><font color="red">*</font>-->
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.remark"/>：</th>
				<td colspan="3">
					<form:textarea path="remark" rows="5" cssStyle="width:608;"></form:textarea>
				</td>
			</tr>
			<tr>
				<th colspan="4" align="center">
					<input type="button" id="submits" value="<spring:message code="fhd.common.save"/>" class="fhd_btn" />
					&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn" />
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function(){
			var validator = $("#posiForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					
				}
			});

			$('#submits').click(function(){
				if(true == validator.form()){
		            var msgTip = parent.FHD.opWait( {
						title : "<spring:message code='fhd.common.prompt'/>",
						width : 250,
						msg : "<spring:message code='fhd.common.opWait'/>",
						progress:true,
						wait:true
					});
					
					var options = {
						url:'${ctx}/sys/orgstructure/posi/edit.do',
						type:'POST',
						success:function(data) {
			     			msgTip.hide();
			         		if("true" == data){
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
			         		}else if("false" == data){
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
			         		}else{
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", data);
			         		}
			     		}
					};
					$('#posiForm').ajaxSubmit(options);
					return false;
				}else{
					return false;
				}
		    });
		});

		//判断时间开始和结束
		function checkDate(startDate,endDate){
			var regS = new RegExp("-","gi");
			date1=startDate.replace(regS,"/");
			date2=endDate.replace(regS,"/");	
			var bd =new Date(Date.parse(date1));
			var ed =new Date(Date.parse(date2));
		    if(bd<ed){
				return true;
			}
			return false;
		}
		
		//检查日期
		function islegal(){
			var startDate= document.getElementById("startDate").value;
			var endDate=document.getElementById("endDate").value;			
			if(startDate!=null && startDate!="" && endDate!=null && endDate!=""){
				if(!checkDate(startDate,endDate)){
					window.top.Ext.ux.Toast.msg("${fhd.common.prompt}", "${fhd.common.alertDate}");
					return false;			
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
	</script>
</body>
</html>