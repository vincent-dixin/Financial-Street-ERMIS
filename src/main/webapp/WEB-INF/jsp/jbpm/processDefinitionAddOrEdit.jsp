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
	<input type="hidden" id="id" name="id" value="${riskForm.id}"/>
	<input type="hidden" id="eLevel" name="eLevel" value="${riskForm.eLevel}"/>
	<input type="hidden" id="riskSeq" name="riskSeq" value="${riskForm.riskSeq}"/>
	<input type="hidden" id="isRiskClass" name="isRiskClass" value="${riskForm.isRiskClass }"/>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="fhd_form_table" align="center">
		<tr>
			<th><spring:message code="riskCode"/>：</th>
			<td>
				<input class="textfiled"  id="riskCode" name="riskCode" value="${riskForm.riskCode}" style="width:300px;"/>
				<font style="color:red">*</font>
			</td>			
		</tr>
		<tr>
			<th><spring:message code="riskName"/>：</th>
			<td>
			    <textarea rows="2" cols="1" id="riskName" name="riskName" style="width:300px;">${riskForm.riskName}</textarea>
				<font style="color:red">*</font>
			</td>
		</tr>
		<tr>
			<th><spring:message code="parentRiskName" />：</th>
			<td>
				<fhd:riskSelector multiple="false" fullPath="true" attributeName="parentId" title="选择风险" checkNode="true" riskClass="true"  onlyLeafCheckable="false" checkModel="single" choosedRisks="${riskForm.parentId}"></fhd:riskSelector>
				<font style="color:red">*</font>				
			</td>
		</tr>		
		<tr>
			<th><spring:message code="responseOrg"/>：</th>
			<td>
				<fhd:orgSelector attributeName="responseOrgids" fullPath="true" multiple="true" title="选择部门" checkNode="true" onlyLeafCheckable="false" checkModel="mutiple" choosedOrgs="${riskForm.responseOrgids==null ? orgId:riskForm.responseOrgids }"></fhd:orgSelector>
			</td>
		</tr>
		<tr>
			<th><spring:message code="relateOrg"/>：</th>
			<td>
				<fhd:orgSelector attributeName="relateOrgids" fullPath="true" multiple="true" title="选择部门" checkNode="true" onlyLeafCheckable="false" checkModel="mutiple" choosedOrgs="${riskForm.relateOrgids}"></fhd:orgSelector>
			</td>
		</tr>
		<tr>
			<th><spring:message code="riskEffect"/>：</th>
			<td>
				<textarea id="riskEffect" name="riskEffect" rows="2" cols="1" style="width:300px;">${riskForm.riskEffect}</textarea>
			</td>
		</tr>
		<!-- <tr>
			<th><spring:message code="inceptTime"/>：</th>
			<td>
			 <form:input path="startTime" onfocus="WdatePicker();" onchange="islegal();" cssClass="Wdate" cssStyle="width:146px;"/>~<form:input path="endTime" onfocus="WdatePicker();" onchange="islegal();" cssClass="Wdate" cssStyle="width:146px;"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="sort"/>：</th>
			<td>
				<form:input path="sort" cssStyle="width:300px;"/>
			</td>
		</tr> -->
		
		<!-- JBPM 嵌入页面 这个路径只是直接到/jbpm/jbpmIncluded.jsp	${ctx}/jbpm/jbpmIncluded.do	-->
		 <jsp:include page="/jbpm/jbpmIncluded.do">
			<jsp:param name="id" value="${riskForm.id}"/>
			<jsp:param name="processInstanceId" value="${param.processInstanceId}"/>
			<jsp:param name="taskName" value="${param.taskName}"/>
			<jsp:param name="bType" value="${param.bType}"/>
		</jsp:include>  
		<!--判断只有id为空的时候出现业务按钮   JBPM  史永亮		riskForm.id == null and -->
		
		<c:if test="${param.processInstanceId == null}"> 
		<tr align="center">
			<th colspan="2">
				<input type="button" id="submits" value="<spring:message code="submit_btn"/>" class="fhd_btn"/>
				&nbsp;&nbsp;
				<input type="button" id="save" value="<spring:message code="save_btn"/>" class="fhd_btn"/>
				&nbsp;&nbsp;
				<input type="button" id="close" value="<spring:message code="close"/>" class="fhd_btn"/>
			</th>
		</tr>
		</c:if> 
	</table>
</form:form>
<script type="text/javascript">
//判断时间开始和结束
function checkDate(startDate,endDate){
	var regS = new RegExp("-","gi");
	date1=startDate.replace(regS,"/");
	date2=endDate.replace(regS,"/");	
	var bd =new Date(Date.parse(date1));
	var ed =new Date(Date.parse(date2));
    if(bd<ed){
		return true;
	}
	return false;
}
function islegal(){
	var startDate= document.getElementById("startTime").value;
	var endDate=document.getElementById("endTime").value;			
	if(startDate!=null && startDate!="" && endDate!=null && endDate!=""){
		if(!checkDate(startDate,endDate)){
			Ext.MessageBox.alert("提示", "请选择开始日期小于结束日期！");
			return false;			
		}
	}
	return true;
}

var validator;
function yy(){
	validator = $("#riskForm").validate({
			errorElement: "em",
			success: function(label) {
				label.text("ok!").addClass("success");
			},
			rules: {
				riskCode:{
					required:true,
					remote: {
				        type:"POST",
				        url:"${ctx}/risk/identification/queryRiskCode.do", 
				        data:{
				        	eventCode: function() {
					            return $("#riskCode").val();
					        },
					        id: function() {
					            return $("#id").val();
					        }
				        }
					}
				},
				riskName:{
					required:true
				},
				parentId:{
					required:true
				}
				
			}
		});
}

function addsp(){
	validator = $("#riskForm").validate({
			errorElement: "em",
			success: function(label) {
				label.text("ok!").addClass("success");
			},
			rules: {
				riskCode:{
					required:true,
					remote: {
				        type:"POST",
				        url:"${ctx}/risk/identification/queryRiskCode.do", 
				        data:{
				        	eventCode: function() {
					            return $("#riskCode").val();
					        },
					        id: function() {
					            return $("#id").val();
					        }
				        }
					}
				},
				riskName:{
					required:true
				},
				parentId:{
					required:true
				},
				emp2:{
					required:true
				}
				
			}
		});
}		
		$('#submits').click(function(){
			if(twoNotSameDept() == false){
				return false;
			}
			//if(islegal() == false){
				//return false;
			//}
			addsp();
			if(true == validator.form()){
				$('#submits')[0].disabled = true;
	            var msgTip = FHD.opWait();
				
				var options = {
					url:'${ctx}/risk/identification/rmRiskSave.do?status=1',
					type:'POST',
					success:function(data) {
		     			msgTip.hide();
		     			$('#submits')[0].disabled = false;
		         		if("true" == data){
		         			parentWindow().reloadStore();//重新加载数据
		         			parent.Ext.MessageBox.alert('提示', '操作成功.');
		         			parentWindow().parent.parent.window.selectNodeReload();
		         			cancel();//关闭窗口
		         			
		         		}else
		         			parent.Ext.MessageBox.alert('提示', '请求失败！');

		     		}
				};
				$('#riskForm').ajaxSubmit(options);
				return false;
			}else{
				return false;
			}
	    });

		$('#save').click(function(){
			if(twoNotSameDept() == false){
				return false;
			}
			//if(islegal() == false){
				//return false;
			//}
			var msgTip = FHD.opWait();
			$('#save')[0].disabled = true;
			var options = {
				url:'${ctx}/risk/identification/rmRiskSave.do?status=0',
				type:'POST',
				success:function(data) {
	     			msgTip.hide();
	     			$('#save')[0].disabled = false;
	         		if("true" == data){
	         			parentWindow().reloadStore();//重新加载数据
	         			parent.Ext.MessageBox.alert('提示', '操作成功.');
	         			parentWindow().parent.parent.window.selectNodeReload();
	         			cancel();//关闭窗口
	         		}else
	         			parent.Ext.MessageBox.alert('提示', '请求失败！');

	     		}
			};
			$('#riskForm').ajaxSubmit(options);
			return false;
	    });

		$('#close').click(function(){
			cancel();
            return false;
	    });
	    
	    
		//验证责任部门和相关部门之间不能有重复的
		function twoNotSameDept(){
			var divresponseOrgids = document.getElementById('divresponseOrgids').getElementsByTagName("input");
			var divrelateOrgids = document.getElementById('divrelateOrgids').getElementsByTagName("input");
			if(divresponseOrgids != null && divresponseOrgids != "" && divresponseOrgids.length > 0){
				for(var i  = 0; i < divresponseOrgids.length; i ++){
					if(divrelateOrgids != null && divrelateOrgids != "" && divrelateOrgids.length > 0){
						for (var j = 0; j < divrelateOrgids.length; j ++){
							if(divresponseOrgids[i].value != null && divresponseOrgids[i].value != "" && divresponseOrgids[i].value != "undefined" && divresponseOrgids[i].value == divrelateOrgids[j].value){
								Ext.MessageBox.alert('提示',"相关部门和责任部门里有重复的部门。");
								return false;
								break;
							}
						}
					}
				}
			}
			return true;
		}
		
	function cancel() {
		closeWindow();
	}
</script>	
</body>
</html>