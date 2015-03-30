<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="dictEntry"/><spring:message code="list"/></title>
<script type="text/javascript">





</script>
</head>
<body >
<div id="rmRiskTaxisDivId" style="text-align: center">
<br/>
<form id="dbbackForm"  method="post" >
	<table id="showTable" width="500px" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
		<tr>
			<th>数据库类型：</th>
			<td>
			 <select name="dbType" id="dbType" onchange="changeVerson(this);">
			  <option value="">请选择</option>
              <option value="b_db2">DB2</option>
              <option value="b_mysql">MYSQL</option>
              <option value="b_oracle">ORACLE</option>
              <option value="b_sqlserver">SQL SERVER</option>
            </select>

			</td>
		</tr>
		<tr>
			<th>数据库版本：</th>
			<td>
			 <select name="dbVersion" id="dbVersion" >
			 


            </select>
			</td>
		</tr>
		<tr>
			<th>数据库地址：</th>
			<td><input type="text" id="dbaddress" name="dbaddress" class="required" ></td>
		</tr>
		<tr>
			<th>数据库名：</th>
			<td><input type="text" id="dbname" name="dbname" class="required"></td>
		</tr>
		
		<tr>
			<th>数据库用户名：</th>
			<td><input type="text" id="dbusername" name="dbusername" class="required"></td>
		</tr>
		<tr>
			<th>数据库用户密码：</th>
			<td><input type="text" id="dbuserpwd" name="dbuserpwd" class="required"></td>
		</tr>
		<tr>
			<th>数据库备份文件名：</th>
			<td><input type="text" id="dbbackfilename" name="dbbackfilename" class="required" ></td>
		</tr>
		
		<tr>
			<th></th>
			<td>
			</td>
		</tr>
		<tr align="center">
			<td colspan="2"  class="fhd_form_bottom">
				<input type="button" id="submits" value="<spring:message code="submit_btn"/>" class="fhd_btn"/>
				&nbsp;&nbsp;
				<input type="reset" value="<spring:message code="reset_btn"/>" class="fhd_btn"  />
			</td>
		</tr>
	</table>
</form>

</div>	

<div id="datalist"></div>	

<script type="text/javascript">

$(document).ready(function(){
	var validator = $("#dbbackForm").validate({
		errorElement: "em",
		success: function(label) {
			label.text("ok!").addClass("success");
		},
		rules: {
			dbType:{
				required:true
			},
			dbVersion:{
				required:true
			}
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
		if(true == validator.form()){
            var msgTip = parent.FHD.opWait( {
				title : '提示',
				width : 250,
				msg : '操作进行中,请稍后......',
				progress:true,
				wait:true
			});
			
			var options = {
				url:'${ctx}/sys/dbbp/dodbbackup.do',
				type:'POST',
				success:function(data) {
	     			msgTip.hide();
	         		if("true" == data){
	         			//parentWindow().mv_grid.grid.store.reload();
	         			parent.Ext.MessageBox.alert('提示', '操作成功.');
	         			//closeWindow();//关闭窗口
	         		}else
	         			parent.Ext.MessageBox.alert('提示', '请求失败！');
	     		}
			};
			$('#dbbackForm').ajaxSubmit(options);
			return false;
		}else{
			return false;
		}
    });
    
    $("#reset").click(function() {
        validator.resetForm();
    });
	 	
});
//清空select
function clearOptions(obj)
{
var modes=document.getElementById('modes');
while(obj.childNodes.length)
obj.removeChild(obj.childNodes[0]);


}


function changeVerson(val){
	var target=document.getElementById("dbVersion");
	clearOptions(target);
	
	
	if(val.value=='b_sqlserver'){
		var newoption = document.createElement("OPTION");   
         newoption.text = "sqlserver2008";   
         newoption.value = "2008";   
         target.add(newoption); 
        var newoption1 = document.createElement("OPTION");   
         newoption1.text = "sqlserver2000";   
         newoption1.value = "2000";   
         target.add(newoption1);    
	
	}
	if(val.value=='b_oracle'){
		var newoption = document.createElement("OPTION");   
         newoption.text = "oracle10g";   
         newoption.value = "10g";   
         target.add(newoption); 
        var newoption1 = document.createElement("OPTION");   
         newoption1.text = "oracle9i";   
         newoption1.value = "9i";   
         target.add(newoption1);    
	
	}
	
	
}
</script>
</body>
</html>