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
<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
<script type="text/javascript"><!--

var mv_grid;
var heightBody;
var width=Ext.getBody().getWidth();
Ext.onReady(function(){
	heightBody=Ext.getBody().getHeight();
	Ext.QuickTips.init();
	var heightTable=Ext.get('posiForm').getHeight();
	var height=heightBody-heightTable;
	var toolbar = new Ext.Toolbar(
	{height: 30,hideBorders: true,
	buttons: [
	{text: '保存',handler:assignRole,iconCls:'icon-add'}
	
	]});
	
	
	mv_grid = new FHD.ext.Grid('rolelist',{riskId:5},
	[   
		{header: "id", dataIndex: 'id', width: 0},
		{header: "<spring:message code='fhd.sys.auth.role.roleName'/>", dataIndex: 'roleName', sortable: true, width: 260}
	   
	],
	null,false,width,height,'${ctx}/sys/orgstructure/posi/posiAssignRoleList.do'
	,false,true,
	toolbar
	);
	
	mv_grid.grid.store.on('load',function(){
				 selectCheckbox('${param.id}','${posiForm.roleIds}');
			});

});
//////////////////
//初始化checkbox

	function selectCheckbox(idValue,roleIds){
		
		var sm = mv_grid.grid.getSelectionModel();
		var store = mv_grid.grid.getStore();
		
		for(var i = 0; i < store.getTotalCount(); i ++){
			var record = store.getAt(i);
			var id = record.get("id");
			//当前id存在roleIds中
			if(roleIds.indexOf(id)>=0){
				sm.selectRow(i,true);
			}
			
		}
	}
	//保存角色
	function assignRole(){
		var id = document.getElementById("id").value;
		
		
		
		var rows =mv_grid.grid.getSelectionModel().getSelections();
		var selectIds = '';
		for(var i=0;i<rows.length;i++){
			selectIds+=rows[i].get('id')+',';
		}
		
		//alert("选择的角色id="+selectIds);
		if(selectIds == null || selectIds == ""){
			window.top.Ext.ux.Toast.msg('警告', '<spring:message code="fhd.common.updateSelect"/>'); 
			return false;
		}else{
			$.ajax({
				type: "GET",
				url: "${ctx}/sys/orgstructure/posi/posiAssignRoleSubmit.do",
				data: "posiid="+id+"&selectIds="+selectIds,
				success: function(msg){
					if("true"==msg){
						window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", "<spring:message code="fhd.common.operateSuccess"/>");
						closeWindow();
					}else{
						window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>","<spring:message code="fhd.common.operateFailure"/>");
						//closeWindow();
					}
				}
			});
		}
		//window.close();
	}
	Ext.EventManager.onWindowResize(function(width ,height){
			var height1 = heightBody-Ext.get("posiForm").getHeight();
			mv_grid.grid.setWidth(width);
			mv_grid.grid.setHeight(height1);
			
    });
--></script>
</head>
<body>
<div>
<form id="posiForm" name="posiForm" >
	<input type="hidden" name="id" id="id" value="${param.id}"/>
	<input type="hidden" name="roleids" id="roleids" value="${posiForm.roleIds}"/>
	<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;">
		<tr>  
			<th><spring:message code="fhd.sys.orgstructure.posi.posicode"/>：</th>
			<td><input type="text" name="posicode" id="posicode" value="${posiForm.posicode}" readonly="readonly"/></td>
			<th><spring:message code="fhd.sys.orgstructure.posi.posiname"/>：</th>
			<td><input type="text" name="posiname" id="posiname" value="${posiForm.posiname}" readonly="readonly"/></td>
		</tr>
	</table>
</form>
</div>
<div id="rolelist" style="margin:0px;padding:0px;">
</div>






</body>
</html>