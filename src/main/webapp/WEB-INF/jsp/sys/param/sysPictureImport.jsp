<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div style="height:6px; background:#f9f9f9;"></div>
	<div id="prop-grid"></div>
	<div id="button-container"></div>
	<form:form id="variableGatherForm" enctype="multipart/form-data">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" align="center">
			<tr align="left">
				<td height="40">
				</td>			
			</tr>
			<tr align="center">
				<td height="40">
				    <input type='file' id='file' name='uploadFile' value="" style="width: 300px;" onkeydown="'this.blur();'"/>
				 	<input type="hidden" name="name" value="${name }"/>
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
				var index=file.lastIndexOf('.');
				var suffix=file.slice(index);
				if(suffix!='.jpg'&&suffix!='.gif'&&suffix!='.png')
				{
					Ext.MessageBox.alert('提示','文件格式不正确!')
					return;
				}				
		        msgTip = FHD.opWait("","","正在上传文件，请稍候...");
				var options = {
					url:'${ctx}/sys/param/systemPictureUpload.do',
					type:'POST',
					success:function(data) {
		     			msgTip.hide();
		         		if("true" == data){
							parent.Ext.MessageBox.alert('提示','上传成功!');
							if('reloadWin' in parentWindow()){
								parentWindow().reloadWin();
							}
		         			closeWindow();
		         		}else{
		         			parent.Ext.MessageBox.alert('提示', '上传失败!');
		         			closeWindow();
		         		}
		     		}
				};
				$('#variableGatherForm').ajaxSubmit(options);
		});
		
		$('#close').click(function(){
			closeWindow();//关闭窗口
		});
	</script>
</body>
</html>