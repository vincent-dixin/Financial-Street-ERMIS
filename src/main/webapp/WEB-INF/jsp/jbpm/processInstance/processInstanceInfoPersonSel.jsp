<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<script type="text/javascript">
	function save(){
		if(document.getElementById("editPerson").value==''){
			Ext.MessageBox.alert('提示','承办人不能为空');
			return;
		}
		var url="${ctx}/jbpm/processInstance/savePerson.do";
		var msgTip = FHD.opWait();
		var options={
				url:url,
				type:'POST',
				success:function(data){
					msgTip.hide();
					parentWindow().reloadStore();//重新加载数据
					closeWindow();
				},
				error:function(){
					msgTip.hide();
				}
			};
			$('#perForm').ajaxSubmit(options);
	}
	function cancel() {
		closeWindow();
	}
</script>
<body style="overflow: hidden;">
	<form:form commandName="perForm" enctype="multipart/form-data">
		<table  width="100%" height="100%" cellpadding="0" cellspacing="0" border="0" class="fhd_form_table" align="center">	
			<tr>
				<th>任务名称：</th>
				<td>${activityName}
					<input type="hidden" id="processInstanceId" name="processInstanceId" value="${processInstanceId}"/>
					<input type="hidden" value="${id}" name="id"/>				
				</td>
			</tr>		
			<tr>
				<th>状&nbsp;&nbsp;&nbsp;&nbsp;态：</th>
				<td>${dbversion}
				</td>
			</tr>
			<tr>
				<th>审批意见：</th>
				<td><textarea readonly="readonly" rows="8" cols="1" id="edesc" name="edesc"  style="width:300px;height: 135px;">${ea_Contents}</textarea> </td>
			</tr>
			<tr>
				<th>到达时间：</th>
				<td>${startStr}
				</td>
			</tr>
			<tr>
				<th>承办单位：</th>
				<td>${assigneeCompanyName}
				</td>
			</tr>	
			<tr>
				<th>承&nbsp;&nbsp;办&nbsp;&nbsp;人：</th>
				<td>
					<fhd:empSelector attributeName="editPerson"  multiple="false" title="选择人员" checkNode="true" checkModel="sinagle" defaultOrg="true" choosedEmps="${empCode}" onlyLeafCheckable="true"></fhd:empSelector>
					<font style="color:red">*</font>
				</td>			
			</tr>
			
			<tr>
				<th colspan="2">
					<input type="button" id="submits" value="保存" class="fhd_btn" onclick="save()"/>
					&nbsp;&nbsp;
					<input type="button" id="colse" value="关闭" onclick="cancel()" class="fhd_btn"/>
				</th>
			</tr>
		</table>
	</form:form>
</body>
</html>