<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form:form id="helpCatalogForm" method="post" commandName="helpCatalogForm">
		<form:hidden path="id"/>
		<form:hidden path="parent.id"/>
		<form:hidden path="parent.catalogName"/>
		<form:hidden path="idSeq"/>
		<form:hidden path="level"/>
		<table  width="100%"  border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
			<tr>	
				<th>知识名称：</th>
				<td>
					<form:input path="catalogName"/>
				</td>
			</tr>
			<tr>	
				<th>排序：</th>
				<td>
					<form:input path="sort"/>
				</td>
			</tr>
			<tr>
				<th colspan="2" align="center">
					<input type="button" id="submits" value="保存" class="fhd_btn" />
					&nbsp;&nbsp;
					<input type="reset" value="重置"  class="fhd_btn" />
				</th>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
	
	var validator = $("#helpCatalogForm").validate({
		errorElement: "em",
		success: function(label) {
			label.text("ok!").addClass("success");
		},
		rules: {
			sort:{
				required:true,
				maxlength:3
			},
			catalogName:{
				required:true
			}
		}
	});
	$("#submits").click(function() {
		if(true == validator.form()){
			//var msgTip = FHD.opWait();
			var options = {
				url:'${ctx}/sys/helponline/addOrEditHelpCatalog.do',
				type:'POST',
				success:function(response) {
	     			//msgTip.hide();
	     			parentWindow().reNode();
	     			closeWindow();
					
	     		}
			};
			$('#helpCatalogForm').ajaxSubmit(options);
		}
    });
		
	</script>
</body>
</html>