<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="${ctx}/scripts/kindeditor-4.1.1/plugins/code/prettify.css" />
<script charset="utf-8" src="${ctx}/scripts/kindeditor-4.1.1/plugins/code/prettify.js"></script>
</head>
<body style="overflow-y:hidden">
	<form:form id="helpTopicForm" enctype="multipart/form-data" method="post" commandName="helpTopicForm">
		<form:hidden path="id"/>
		<form:hidden path="helpCatalog.id"/>
		<table  width="100%"  border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>	
				<th>编号：</th>
				<td>
					<form:input path="topicCode"/>
				</td>
				<th>名称：</th>
				<td>
					<form:input path="topicName"/>
				</td>
			</tr>
			<tr>
				<th>类型：</th>
				<td>
					<form:radiobuttons path="type" items="${helpTopicForm.types}" />
				</td>
				<th>排序：</th>
				<td>
					<form:input path="sort"/>
				</td>
			</tr>
			<tr>
				<th>所属分类：</th>
				<td>
					<fhd:catalogSelector cmWidth="200" multiple="false" fullPath="true" attributeName="riskId" title="选择" checkNode="true" catalogClass="false" onlyLeafCheckable="true" checkModel="single"  choosedCatalog="${riskId==null?param.riskId:riskId}" cssClass="required"></fhd:catalogSelector>
				    <font style="color:red">*</font>
				</td>
			
				<th>其他分类：</th>
				<td>
					<fhd:catalogSelector cmWidth="200" multiple="false" fullPath="true" attributeName="riskId" title="选择" checkNode="true" catalogClass="false" onlyLeafCheckable="true" checkModel="single"  choosedCatalog="${riskId==null?param.riskId:riskId}" cssClass="required"></fhd:catalogSelector>
				    <font style="color:red">*</font>
				</td>
			</tr>
			<tr>
				<th>内容：</th>
				<td colspan="3">
				<textarea id="content" name="content" cols="100" rows="8" style="width:100%;height:300px;visibility:hidden;">
				${helpTopicForm.content} 
				</textarea>
				</td>
			</tr>
			<tr>
				<th>附件：</th>
				<td colspan="3">
				<div style="height:60px;overflow-y: scroll; width: 800px;">
					<fhd:fileUpload width="300" multiple="false" delFun="delf" choosedFileIds="${helpTopicForm.fileUpload.id}" fileCount="1"></fhd:fileUpload>
				         （允许上传附件如下：*.jpg,*.jpeg,*.gif,*.txt,*.doc,*.mp3,*.wma,*.m4a,*.rar*.zip,*.xls,*.docx,*.xlsx,*.ftl,*.properties,*.pdf）
				</div>
				</td>
			</tr>
			
			<tr>
				<th colspan="4" align="center">
					<input type="button" id="submits" value="保存" class="fhd_btn" />
					&nbsp;&nbsp;
					<input type="reset" value="重置"  class="fhd_btn" />
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
	/* 删除附件 */
	function delf(fileUploadId) {
		var flag=false;
		jQuery.ajax({
			type:"post",
			url:'${ctx}/sys/file/fileUploadDel.do?ids='+fileUploadId,
			data:{fileUploadId:fileUploadId},
			async:false,
			success:function(data){
				window.top.Ext.ux.Toast.msg('提示','操作成功');
				flag=true;
			},
			error:function(){
				window.top.Ext.ux.Toast.msg('提示','操作失败');
			}
		});
		return flag;
	}
	jQuery(function(){
		var validator = $("#helpTopicForm").validate({
			errorElement: "em",
			success: function(label) {
				label.text("ok!").addClass("success");
			},
			rules:{
				sort:{
					required:true,
					maxlength:3
				},
				topicName:{
					required:true
				},
				topicCode:{
					required:true
				},
				file:{
					fileType:["jpg","jpeg","gif","txt","doc","mp3","wma","m4a","rar","zip","xls","docx","xlsx","ftl","properties","pdf"]
				}
			},
			submitHandler:function(form){
				form.submit();
				window.returnValue = 'refresh';
				window.close();
	        },
	        errorPlacement:function(error, element) {
	        	if (element.is(':file')) {
	        		error.insertAfter(element.parent());
	        		element.parent().css("float","");
        		} else { 
	        		error.insertAfter(element);
        		}
    		},
    		messages:{
    			file:{
    				fileType:"请允许范围内的文件"
    			}
    		}
		});
	
		$("#submits").click(function() {
			if(true == validator.form()){
				//var msgTip = FHD.opWait();
				var options = {
					url:'${ctx}/sys/helponline/addOrEditHelpTopic.do',
					type:'POST',
					success:function(response) {
		     			//msgTip.hide();
		     			parentWindow().reload();
		     			closeWindow();
						
		     		}
				};
				$("#content").attr("value",editor1.html());
				$('#helpTopicForm').ajaxSubmit(options);
			}
	    });
	});
	function getEditorHTMLContents(EditorName) { 
		var oEditor = FCKeditorAPI.GetInstance(EditorName); 
		return(oEditor.GetXHTML(true)); 
	}
	
	var editor1 = KindEditor.create('textarea[name="content"]', {
		cssPath : '${ctx}/scripts/kindeditor-4.1.1/plugins/code/prettify.css',
		uploadJson : '${ctx}/scripts/kindeditor-4.1.1/jsp/upload_json.jsp',
		fileManagerJson : '${ctx}/scripts/kindeditor-4.1.1/jsp/file_manager_json.jsp',
		allowFileManager : true
	});
	prettyPrint();
	</script>
</body>
</html>