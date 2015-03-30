<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.group.group" /></title>
</head>
<body>
	<form:form commandName="groupForm" action="" method="post">
		<input type="hidden" id="id" name="id" value="${param.id}"/>
		<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.group.groupCode" />：</th>
				<td><%-- readOnly="true" --%>
					<form:input path="groupCode" cssStyle="width:200px;" cssClass="required"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.group.groupName" />：</th>
				<td>
					<form:input path="groupName" cssStyle="width:200px;" cssClass="required"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.group.groupLevel" />：</th>
				<td>
					<form:input path="groupLevel" cssStyle="width:200px;" cssClass="required"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.startDate"/>：</th>
				<td>
					<form:input path="startDate" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:200px" onchange="islegal();" onkeydown="return false"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.common.endDate"/>：</th>
				<td>
					<form:input path="endDate" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:200px" onchange="islegal();" onkeydown="return false"/>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.group.groupDesc" />：</th>
				<td>
					<textarea rows="5" cols="1" id="groupDesc" name="groupDesc" style="width:200px;">${groupForm.groupDesc}</textarea>
				</td>
			</tr>
			<tr>
				<th colspan="2">
					<input type="button" id="submits" value="<spring:message code="fhd.common.save"/>" class="fhd_btn" />&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn" />
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function(){
			var validator = $("#groupForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				rules: {
					groupLevel:{number:2}
				}
			});
			$('#submits').click(function(){
				var startDate= document.getElementById("startDate").value;
				var endDate=document.getElementById("endDate").value;			
				if(startDate!=null && startDate!="" && endDate!=null && endDate!=""){
					if(!islegal()){
						return false;
					}
				}
				if(true == validator.form()){
		            var msgTip = opWait();
					var options = {
						url:'${ctx}/sys/group/save.do',
						type:'POST',
						success:function(data) {
			     			msgTip.hide();
			     			if(data=='true'){
								//工作组树弹出添加刷新
			         			if('nodeReload' in parentWindow()){
					         		parentWindow().nodeReload();
				         		}else{
			         				//如果是从下级工作组列表页面进入此页面，则要进行如下的父页面刷新操作；
			         				if('reloadGrid' in parentWindow()){
						         		parentWindow().reloadGrid();
			         				}
			         				parentWindow().parent.parent.nodeReload();
			         			}
			         			parent.window.top.Ext.ux.Toast.msg('提示', '操作成功!');
			         			FHD.closeWindow();
			         		}else{
			         			parent.window.top.Ext.ux.Toast.msg('提示', '操作失败!');
			         		}
			     		}
					};
					$('#groupForm').ajaxSubmit(options);
					return false;
				}else{
					return false;
				}
		    });
			$("#reset").click(function() {
		        validator.resetForm();
		    });
		});

		//检查日期
		function islegal(){
			var startDate= document.getElementById("startDate").value;
			var endDate=document.getElementById("endDate").value;			
			if(startDate!=null && startDate!="" && endDate!=null && endDate!=""){
				if(!checkDate(startDate,endDate)){
					Ext.MessageBox.alert("提示", "请选择生效日期小于失效日期！");
					return false;			
				}
			}
		}

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
	</script>
</body>
</html>