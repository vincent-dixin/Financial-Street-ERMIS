<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

<div style="height:6px; background:#f9f9f9;">
</div>
<form:form commandName="riskForm" enctype="multipart/form-data">
	
	
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="fhd_form_table" align="center">
		<tr>
			<th>审批人：</th>
			<td>
				<fhd:empSelector multiple="false" attributeName="emp2" title="选择员工" choosedEmps=""></fhd:empSelector>
				<font style="color:red">*</font>		
			</td>		
		</tr>
		<tr>
			<th>类型：</th>
			<td>
			    <select style="widtd:100" name="bType">
			    	<option value="1">代办</option>
			    	<option value="2">协办</option>
			    </select>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<input type="button" id="ty" value="提交" class="fhd_btn"/>
				<input type="button" id="close" value="关闭" class="fhd_btn"/>
			</td>
		</tr>
		
	</table>
</form:form>
<script type="text/javascript">


var validator;


function addsp(){
	validator = $("#riskForm").validate({
			errorElement: "em",
			success: function(label) {
				label.text("ok!").addClass("success");
			},
			rules: {
							
				emp2:{
					required:true
				}
				
			}
		});
}		$("#ty").click(function() {
	    	addsp();
	    	if(true == validator.form()){
		            $("#limit").attr("value",$("#limit").attr("value")-1);
					document.body.style.overflow='hidden';
					document.forms[0].style.display='none';
					new Ext.LoadMask(Ext.getBody(), {msg:'正在操作...',removeMask:true}).show();
					document.forms[0].action = "${ctx}/jbpm/commission.do?processInstanceId=${param.processInstanceId}&taskId=${param.taskId}&windowId=${param.windowId}";
					document.forms[0].method ="POST";
					document.forms[0].submit();
					return false;
				}else{
					return false;
				}
			
		});
		

		$('#close').click(function(){
			cancel();
            return false;
	    });
	    
	    
		
		
	function cancel() {
		closeWindow();
	}
</script>	
</body>
</html>