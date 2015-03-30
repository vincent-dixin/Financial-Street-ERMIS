<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.file.file" /><spring:message code="fhd.common.add" /></title>
</head>
<body>
	<form:form commandName="fileUploadForm" method="post" enctype="multipart/form-data">
		<table id="showTable" width="100%" border="0" cellpadding="0"
			cellspacing="0" class="fhd_form_table">
			<tr>
				<th><spring:message code="fhd.sys.file.oldFileName" />：</th>
				<td>
					<fhd:fileUpload multiple="true" fileCount="3" choosedFileIds="${choosedFileIds}"></fhd:fileUpload>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.sys.file.chooseWay" />：</th>
				<td>
					<form:select path="chooseWay" cssClass="required" cssStyle="width: 300px">
						<option value=""><spring:message code="fhd.common.pleaseSelect" /></option>
						<option value="fileDir"><spring:message code="fhd.sys.file.uploadToDir" /></option>
						<option value="dataBase"><spring:message code="fhd.sys.file.uploadToDB" /></option>
					</form:select>
				</td>
			</tr>
			<tr>
				<th><spring:message code="fhd.commom.mark" />：</th>
				<td>
					<textarea name="notes" cols="1" rows="5" style="width: 300px"></textarea>
				</td>
			</tr>
			<tr align="center">
				<td colspan="2" class="fhd_form_bottom">
					<input type="button" id="submits" name="submits" value="<spring:message code="fhd.common.submit"/>" class="fhd_btn" />
					&nbsp;&nbsp;
					<input type="reset" value="<spring:message code="fhd.common.reset"/>" class="fhd_btn" />
				</td>
			</tr>
			<tr>
				<td colspan="2" bgcolor="#f6f5f5" align="center">
					<span style="color:red;">允许上传文件类型："jpg", "jpeg", "gif", "txt", "doc", "mp3", "wma", "m4a", "rar", "zip", "xls", "pdf"</span>
				</td>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
		$(document).ready(function(){
			var validator = $("#fileUploadForm").validate({
				errorElement: "em",
				success: function(label) {
					label.text("ok!").addClass("success");
				},
		        rules: {
					
				},
				submitHandler:function(form){
					form.submit();
					window.returnValue = 'refresh';
					window.close();
		        }
			});

			$("#reset").click(function() {
		        validator.resetForm();
		    });
		    $('#submits').click(function(){
		    	if(true== validator.form()){
		        	var msgTip = FHD.opWait({
			  			title : "<spring:message code='fhd.common.prompt'/>",
			  			width : 250,
			  			msg : "<spring:message code='fhd.common.opWait'/>",
			  			progress:true,
			  			wait:true
			  		});
		        	var options={
		        		url:'${ctx}/sys/file/fileUploadSaveTest.do',
		        		type:'post',
		        		success:function(data){
			                msgTip.hide();
			                if("true" == data){
			         			parentWindow().mv_grid.grid.store.reload();
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
			         			closeWindow();
			         		}else{
			         			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
			         			closeWindow();
			         		}
		        		}
		        	};
		        
			        $('#fileUploadForm').ajaxSubmit(options);
			        return false;
		        }else{
		        	return false;
	            }
        	});
		});
	</script>
</body>
</html>