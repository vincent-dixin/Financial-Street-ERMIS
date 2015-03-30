<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<div style="height:6px; background:#f9f9f9;"></div>
<form:form commandName="processUploadForm" id="processUploadForm" enctype="multipart/form-data">
    <input type="hidden" id="id" name="id" value="${processUploadForm.id}"/>
	<table width="400px" cellpadding="0" cellspacing="0" border="0" align="center">
		<tr align="center">
			<th height="40" align="right">
				名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：
			</th>
			<td align="left">
				<input class="textfiled"  id="fileName" name="fileName" value="${processUploadForm.fileName}" style="width:300px;"/>
				<font style="color:red">*</font>
			</td>			
		</tr>
		<tr>
			<th>类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型：</th>
			<td>
				<form:select path="processDefineID">
					<option value="风险、风险事件">风险、风险事件</option>
					<option value="历史事件">历史事件</option>
					<option value="应对">应对</option>
					<option value="问卷">问卷</option>
					<option value="指标">kpi</option>
					<option value="辨识任务">辨识任务</option>
					<option value="风险控制计划">风险控制计划</option>
					<option value="风险控制措施">风险控制措施</option>
					<option value="内控评价计划">内控评价计划</option>
					<option value="内控整改计划">内控整改计划</option>
					<option value="内控建设计划">内控建设计划</option>
				</form:select>
			</td>
		</tr>
		<%--<tr>
			<th>业务查看路径：</th>
			<td>
				<input class="textfiled"  id="url" name="url" value="${processUploadForm.url}" style="width:300px;"/>
				<font style="color:red">*</font>
			</td>
		</tr>--%>
		<tr>
			<th>业务查看路径：</th>
			<td>
				<input class="textfiled"  id="url" name="url" value="${processUploadForm.url}" style="width:300px;"/>
				<%-- <font style="color:red">*</font> --%>
			</td>
		</tr>
		<tr align="center">
			<th height="40" align="right">
				描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述：
			</th>
			<td align="left">
				<textarea id="description" name="description" rows="2" cols="1" style="width:300px;">${processUploadForm.description}</textarea>
			</td>			
		</tr>
		<tr align="center">
			<th height="40" align="right">
			          选择文件：
			</th>
			<td align="left">
				<input type='file' id='file' name='uploadFile' value="" style="width: 300px;" onkeydown="'this.blur();'"/>
				<font style="color:red">*</font>
			</td>
		</tr>
		<tr align="center">
			<td height="40" colspan="2">
				<input type="button" id="submits" value="确定" class="fhd_btn"/>
				&nbsp;&nbsp;
				<input type="button" id="close" value="取消" class="fhd_btn"/>
				 
			</td>
		</tr>
	</table>
</form:form>
<script>
var msgTip;
$('#submits').click(function(){
	var id = document.getElementById("id").value;
	var file = document.getElementById("file").value;
	var fileName = document.getElementById("fileName").value;	
		if(file.length<2 && id == null){
			window.top.Ext.Msg.alert('数据验证','请选择要导入的文件!');
			return false;
		}
		
       msgTip = FHD.opWait("","","正在上传文件，请稍候...");
		var options = {
			url:'${ctx}/jbpm/uploadProcess.do',
			type:'POST',
			success:function(data) {
     			msgTip.hide();
         		if("false" == data){
         			parent.Ext.MessageBox.alert('提示', '操作失败.');
         		}else{  
         			parentWindow().reloadStore1();
         			closeWindow();//关闭窗口
         		}
     		}
		};
		$('#processUploadForm').ajaxSubmit(options);
});

$('#close').click(function(){
	closeWindow();//关闭窗口
});
</script>
</body>
</html>