<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>面板</title>
	<script type="text/javascript">
		function isNotNull(val,message){
			if(val==null||val==""){
				Ext.MessageBox.alert('提示', message);
				return false;
			}else{
				return true;
			}
		}
		function riTitle(){
			var title=jQuery("#title").val();
			return isNotNull(title,"标题不能为空");
		}
		function riHeight(){
			var height=jQuery("#height").val();
			return isNotNull(height,"高度不能为空");
		}
		function riUrl(){
			var url=jQuery("#url").val();
			return isNotNull(url,"URL不能为空");
		}
		function update(){
			var validator = $("#form").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
				submitHandler:function(form){
		        }
			});
			var id=jQuery("#id").val();
			var title=jQuery("#title").val();
			var height=jQuery("#height").val();
			var url=jQuery("#url").val();
			if(true == validator.form()){
				Ext.Ajax.request({
					type: "POST",
					url: "${ctx}/sys/portlet/update.do",
					params:"id="+id+"&title="+title+"&height="+height+"&url="+url,
					success: function(response) {
						parentWindow().hisDs.reload();
						if(id==""){
							parentWindow().Ext.Msg.alert('成功', "新增成功");
						}else{
							parentWindow().Ext.Msg.alert('成功', "修改成功");
						}
						cancel();
					},
					failure: function(response) {
						if(id==""){
							Ext.Msg.alert('失败', "新增失败");
						}else{
							Ext.Msg.alert('失败', "修改失败");
						}
					}
				});
			}
		}
		function goBack(){
			cancel();
		}
		//关闭窗口
		function cancel() {
			closeWindow();
		}
	</script>
</head>
<body>
	<input id="id" name="id" type="hidden" value="${portlet.id }">
	<form id="form">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="fhd_form_table">
		  <tr>
		  	<th style="width: 30%">标题：</th>
		  	<td style="width: 70%"><input id="title" name="title" value="${portlet.title }" class="required" maxlength="20"/>
		  	<font style="color:red">*</font></td>
		  </tr>
		  <tr>	
		  	<th>高度：</th>
		  	<td>
		  		<input id="height" name="height" value="${portlet.height }" maxlength="3" onkeyup="this.value=this.value.replace(/\D/g,'')" class="required" onchange="this.value=this.value.replace(/\D/g,'')"/>
		  		<font style="color:red">*</font>
		  	</td>
		  </tr>
		  <tr>
		  	<th>URL：</th>
		  	<td>
		  		<input id="url" name="url" value="${portlet.url }" class="required" maxlength="256">
		  		<font style="color:red">*</font>
		  	</td>
		  </tr>
		  <tr>
			<td align="center" colspan="3">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td width="50%" align="center">
							<c:if test="${portlet!=null}">
								<input type="button" id="update1" name="update1" value="更新" class="fhd_btn" onclick="update();"/>  
							</c:if>
							<c:if test="${portlet==null}">
								<input type="button" id="new" value="新建" class="fhd_btn" onclick="update();"/>  
							</c:if>
						</td>
						<td width="50%" align="center"><input type="button" id="back" value="放弃" class="fhd_btn" onclick="goBack();"/>
						</td>
					</tr>
				</table>
		</td>
	  </tr>	
	</table>
	</form>
</body>
</html>