<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.auth.user.user"/></title>
	<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
	<script type="text/javascript">
		var mv_grid;
		var allIds='';
		Ext.onReady(function(){
			Ext.QuickTips.init();
	
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: "<spring:message code='fhd.common.addEmp'/>",handler:saveEmp,iconCls:'icon-add'},
					{text: "<spring:message code='fhd.common.heightLvlSerch'/>",handler:showAndHide,iconCls:'icon-zoom'}
				]
			});

	
			mv_grid = new FHD.ext.Grid('userlist',{riskId:5},
				[   {header:'id',dataIndex:'id',width:0},
					{header:"<spring:message code='fhd.common.username'/>",dataIndex:'username',width:20},
					{header:"<spring:message code='fhd.sys.auth.role.realName'/>",dataIndex:'realname',width:20},
					{header:"<spring:message code='fhd.common.status'/>",dataIndex:'userStatus',width:10,renderer:function(val){var ops=document.getElementById('userStatus').options;for(var i=0;i<ops.length;i++){if(ops[i].value==val)return ops[i].text;}}},
					{header:"<spring:message code='fhd.common.lockState'/>",dataIndex:'lockstate',width:10,renderer:function(val){if(val=='false')return '<spring:message code="fhd.common.notLocked"/>';if(val=='true')return '<spring:message code="fhd.common.lock"/>'}},
					{header:"<spring:message code='fhd.common.enable'/>",dataIndex:'enable',width:10,renderer:function(val){if(val=='false')return '<spring:message code="fhd.common.false"/>';if(val=='true')return '<spring:message code="fhd.common.true"/>'}},
					{header:"<spring:message code='fhd.common.regdate'/>",dataIndex:'regdate',width:20},
					{header:"<spring:message code='fhd.common.abatedate'/>",dataIndex:'expiryDate',width:20},
					{header:"<spring:message code='fhd.common.credentialsexpiryDate'/>",dataIndex:'credentialsexpiryDate',width:20},
					{header:'attchData',dataIndex:'password',width:0}
				],
				null,false,'95%',460,'${ctx}/sys/auth/userListJSON.do?roleId=${sysRoleForm.id}'
				,false,true,
				toolbar
			);
		});

		//指定人员
		function saveEmp(){
				allIds='';
				var store = mv_grid.grid.getStore();
				var view = mv_grid.grid.getView();
				for(var i = 0; i < view.getRows().length; i ++){
					var record = store.getAt(i);
					var id=record.get("id");
					allIds=id+','+allIds;
				}
				
				//alert(allIds);
				
			var id = document.getElementById("id").value;
			
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			var selectIds = '';
			for(var i=0;i<rows.length;i++){
				selectIds+=rows[i].get('id')+',';
			}
			
			//alert("选择的角色id="+selectIds);
			if(selectIds == null || selectIds == ""){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.alert'/>", '<spring:message code="fhd.common.updateSelect"/>'); 
				return false;
			}else{
				$.ajax({
					type: "GET",
					url: "${ctx}/sys/auth/roleAssignUserSubmit.do",
					data: "roleid="+id+"&selectIds="+selectIds+"&allIds="+allIds,
					success: function(msg){
						if("true"==msg){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.operateSuccess"/>'); 
							closeWindow();
						}else{
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.operateFailure"/>');
							closeWindow();
						}
					}
				});
			}
			//window.close();
			
		}
		
		//打开或关闭高级查询
		function showAndHide(){
			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
			if(divShows == 'none'){
				document.getElementById("rmRiskTaxisDivId").style.display='';			
			}else{
				document.getElementById("rmRiskTaxisDivId").style.display='none';
			}
		}
		
		//查询
		function queryUser(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
	    		userName: document.getElementById("username").value,
	    		realname: document.getElementById("realname").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
		
		//初始checkbox
		function selectCheckbox(userids){
			var records
			var sm = mv_grid.grid.getSelectionModel();
			var store = mv_grid.grid.getStore();
			var view = mv_grid.grid.getView();
			for(var i = 0; i < view.getRows().length; i ++){
				var record = store.getAt(i);
				var flage = record.get("password");
				var id=record.get("id");
				//当前id存在roleIds中
				if(flage=='checked'){
					sm.selectRow(i,true);
					
				//alert(flage);
				}
				
			}
		}
	</script>
</head>
<body onload="showAndHide();">
	<input type="hidden" name="id" value="${sysRoleForm.id}" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="hideTable(this)" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
	</table>
	<span style='display:none'><fhd:dictEntrySelect value="" id="userStatus" dictName="userStatus" path="userStatus" cssClass="required"/></span>
	<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;">
		<tr>  
			<th><spring:message code="fhd.sys.auth.role.roleCode"/>：</th>
			<td><input type="text" name="roleCode" id="roleCode" value="${sysRoleForm.roleCode}" readonly="readonly"/></td>
			<th><spring:message code="fhd.sys.auth.role.realName"/>：</th>
			<td><input type="text" name="roleName" id="roleName" value="${sysRoleForm.roleName}" readonly="readonly"/></td>
		</tr>
	</table>
	<div id="rmRiskTaxisDivId" style="height:48px;background:#f9f9f9;">
		<form id="orgForm" name="orgForm" method="post">
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display: block;">
				<tr>
					<th>${fhd.common.username}：</th>
					<td><input type="text" name="username" id="username"
						value="${username}" /></td>
					<th>${fhd.sys.auth.role.realName}：</th>
					<td><input type="text" name="realname" id="realname"
						value="${realname}" /></td>
				</tr>
				<tr>
					<td class="fhd_query_bottom" colspan="4"><input type="button" onclick="queryUser();"
						value="<spring:message code="fhd.common.search" />" class="fhd_btn" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="userlist"></div>
	<script type="text/javascript">
		//
		/*
		setTimeout(function(){
		    selectCheckbox('${userIdInRole}');
	    }, 1000);
	    */
	    setInterval(function(){
		    selectCheckbox('${userIdInRole}');
	    }, 1000);
	</script>
</body>
</html>