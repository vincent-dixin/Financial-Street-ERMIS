<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form:form id="exportRisksForm" enctype="multipart/form-data">
		<table width="400px" cellpadding="0" cellspacing="0" border="0" align="center">
			<tr align="left">
				<td height="40" colspan="2">
					&nbsp;&nbsp;<a id='temphref' href="${ctx}/userfiles/newExcelTemplate/organiseTemplate.xls" style="color:red">点击这里下载导入模板</a>
				</td>			
			</tr>
			<tr align="center">
				<td height="40" align="right">
					导入类型：
				</td>
				<td align="left">
					<select id="dataType" style="width: 235px;">
						<option value="1">机构</option>
						<option value="2">岗位</option>
						<option value="3">职务</option>
						<option value="4">员工</option>
						<option value="5">工作组</option>
					</select>
				</td>			
			</tr>
			<tr align="center">
				<td height="40" align="right">
				          选择文件：
				</td>
				<td align="left">
					<input type='file' id='file' name='uploadFile' value="" style="width: 300px;" onkeydown="'this.blur();'"/>
				</td>
			</tr>
			<tr align="center">
				<td height="40" colspan="2">
					<input type="button" id="submits" value="确定" class="fhd_btn"/>
				</td>
			</tr>
			<tr align="center">
				<td height="40" align="right">
					<font color="red">导入说明：</font>
				</td>
				<td align="left">
					<font color="red">导入顺序请根据导入类型顺序依次导入!</font>
				</td>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		var msgTip;
		$('#submits').click(function(){
			var file = document.getElementById("file").value;
			var dataType = document.getElementById("dataType").value;	
				if(file.length<2){
					window.top.Ext.ux.Toast.msg('数据验证','请选择要导入的文件!<br>如果您不知道导入的文件格式，请下载系统提供的导入模板.');
					return false;
				}
				
		       msgTip = FHD.opWait("","","正在上传文件，请稍候...");
				var options = {
					url:'${ctx}/sys/orgstructure/org/uploadExcel.do',
					type:'POST',
					success:function(data) {
		     			msgTip.hide();
		         		if("false" == data){
		         			window.top.Ext.ux.Toast.msg('提示', '操作失败.');
		         		}else if(data.startWith("id:")){
		         			var id = data.substring(3,data.length);
		         			//如果导入的是机构，则先验证树根是否正确
		         			 if(1 == dataType){
		         				Ext.Ajax.request({
		         					url:'${ctx}/sys/orgstructure/org/importOrgCheckRoot.do',
		         					method:'post',
		         					params:{
		         						fileId: id
		         					},
		         					success:function(response){
		         						var ret = response.responseText;
		         						if('false'!=ret){
		         							window.top.Ext.ux.Toast.msg('提示', ret);
		         						}else{
		         							msgTip = FHD.opWait("","","正在对上传文件中的数据进行合法性和完整性扫描，请稍候...");
		         							//打开数据预览窗口
		    			         			dataPreview(id,dataType);
		         						}
		         					}				
		         				}); 
		         			}else{
		         				msgTip = FHD.opWait("","","正在对上传文件中的数据进行合法性和完整性扫描，请稍候...");
			         			//打开数据预览窗口
			         			dataPreview(id,dataType);
		         			}
		         		}else{
		         			window.top.Ext.ux.Toast.msg('上传文件错误提示', data);
		         		}
		     		}
				};
				$('#exportRisksForm').ajaxSubmit(options);
		});
		
		//查看预数据页面
		function dataPreview(fileId,dataType){
			openWindow('数据查看', 900, 400, '${ctx}/sys/orgstructure/org/importPreview.do?fileId='+fileId + '&importType='+dataType);
		}
		
		//关闭msgTip
		function closeTip(){
			msgTip.hide();
		}
		String.prototype.startWith=function(str){
			if(str==null||str==""||this.length==0||str.length>this.length)
			  return false;
			if(this.substr(0,str.length)==str)
			  return true;
			else
			  return false;
			return true;
			}
	</script>
</body>
</html>