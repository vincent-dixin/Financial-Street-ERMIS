<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div style="height:6px; background:#f9f9f9;"></div>
	<form:form id="variableGatherForm" enctype="multipart/form-data">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
			
			<c:if test="${hasTemplate eq 'true'}">
				<tr align="left">
					<td height="40">
						&nbsp;&nbsp;<a href="${ctx}/sys/file/downloadFileTemplate.do?fileName=${templateFile }" style="color:red">点击这里下载导入模板</a>
					</td>			
				</tr>
			</c:if>
			<tr align="center">
				<td height="40">
				    <input type='file' id='file' name='uploadFile' value="" style="width: 300px;" onkeydown="'this.blur();'"/>
				</td>
			</tr>
			<tr align="center">
				<td height="40">
					<input type="hidden" value="${id }">
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
			var file = document.getElementById("file").value;
				if(file.length<2){
					window.top.Ext.Msg.alert('提示','要上传的文件不正确！');
					return false;
				}
				
		        msgTip = FHD.opWait("","","正在上传文件，请稍候...");
				var options = {
					url:'${ctx}/components/sys/saveUploadFile.do',
					type:'POST',
					success:function(data) {
		     			msgTip.hide();
		         		if("false" == data){
		         			Ext.MessageBox.alert('提示', '操作失败.');
		         			closeWindow();
		         		}else if(data.length==32){
		         			parentWindow().${param.callBack}(data);
		         			closeWindow();
		         		}else{
		         			parentWindow().Ext.MessageBox.alert('上传文件错误提示', data);
		         			closeWindow();
		         		}
		     		}
				};
				$('#variableGatherForm').ajaxSubmit(options);
		});
		
		$('#close').click(function(){
			closeWindow();//关闭窗口
		});
		function closeTip(){
			msgTip.hide();
		}
	</script>
</body>
</html>