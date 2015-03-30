<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="fhd.sys.auth.user.user"/><spring:message code="fhd.sys.auth.user.assignRole"/></title>
<script type="text/javascript">
	function selectCheckbox(idValue,roleIds){
		//alert(idValue);
		//alert(roleIds);
		if(roleIds != null && roleIds != ""){
			var ids = roleIds.substring(0,roleIds.length-1);
			var ida = ids.split(',');
			for(var i=0;i<ida.length;i++){
				var s = document.getElementsByName("rid");
			    for(var j=0;j<s.length;j++){ 
					if(ida[i] == s[j].value){
						//选中用户
						s[j].checked=true;
					}
			    }
			}
		}
	}
	//保存角色
	function assignRole(){
		var id = document.getElementById("id").value;
		
		var s = document.getElementsByName("rid");
		var selectIds = '';
		for(var i=0;i<s.length;i++){
			if(s[i].checked){
				selectIds = selectIds + s[i].value + ",";
			}
		}
		//alert("选择的角色id="+selectIds);
		if(selectIds == null || selectIds == ""){
			window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.chooseRole'/>");
			return false;
		}else{
			var msgTip = opWait();
			$.ajax({
				type: "GET",
				url: "${ctx}/sys/auth/userAssignRoleSubmit.do",
				data: "userid="+id+"&selectIds="+selectIds,
				success: function(msg){
					msgTip.hide();
					if("true"==msg){
						window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateSuccess'/>");
						closeWindow();
					}else{
						window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code='fhd.common.operateFailure'/>");
					}
				}
			});
		}
	}
</script>
</head>
<body onload="selectCheckbox('${param.id}','${sysUserForm.roleIds}')">
<form id="sysUserForm" name="sysUserForm" action="${ctx}/sys/auth/userAssignRoleSubmit.do" method="post">
	<input type="hidden" name="id" id="id" value="${param.id}"/>
	<input type="hidden" name="roleids" id="roleids" value="${sysUserForm.roleIds}"/>
	<table id="showTable" width="800" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;">
		<tr>  
			<th><spring:message code="fhd.sys.auth.user.username"/>：</th>
			<td><input type="text" name="username" id="username" value="${sysUserForm.username}" readonly="readonly"/></td>
			<th><spring:message code="fhd.sys.auth.role.realName"/>：</th>
			<td><input type="text" name="realname" id="realname" value="${sysUserForm.realname}" readonly="readonly"/></td>
		</tr>
	</table>
	<table width="800" border="0" cellpadding="0" cellspacing="0" bgcolor="#f4faff" style="border-collapse: collapse;border:1px solid #d0cfcd;">
		<tr>
			<td style="width:100px;text-align:center"><spring:message code="fhd.sys.auth.role.role"/><spring:message code="fhd.common.list"/></td>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_result">
					<tr>
						<td><spring:message code="fhd.common.select"/></td>
						<td><spring:message code="fhd.sys.auth.role.roleName"/></td>
					</tr>
					<c:forEach items="${roleList}" var="v">
						<tr>
							<td>
								<input type="checkbox" name="rid" id="rid" value="${v.id}"/>
							</td>
							<td>
								${v.roleName}
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
		<tr align="center">
			<td colspan="2" class="right">
				<input type="button" onclick="javascript:assignRole();" name="save" id="save" class="fhd_btn" value="<spring:message code="fhd.common.submit" />" />
			</td>
		</tr>
	</table>
</form>
</body>
</html>