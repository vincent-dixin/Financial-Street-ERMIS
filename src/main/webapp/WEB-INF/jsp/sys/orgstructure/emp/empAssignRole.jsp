<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.orgstructure.emp.employee"/><spring:message code="fhd.sys.auth.user.assignRole"/></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody;
		var width;
		var height;
		Ext.onReady(function(){
			heightBody=Ext.getBody().getHeight();
			width=Ext.getBody().getWidth();
			Ext.QuickTips.init();
			var heightTable=Ext.get('empForm').getHeight();
				height=heightBody-heightTable;
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: "<spring:message code='fhd.common.save'/>",handler:assignRole,iconCls:'icon-add'}
				]
			});
			
			mv_grid = new FHD.ext.Grid('rolelist',{riskId:5},
			[   
				{header: "id", dataIndex: 'id', width: 0},
				{header: "<spring:message code='fhd.sys.auth.role.roleName'/>", dataIndex: 'roleName', sortable: true, width: 260}
			],
			null,false,width,height,'${ctx}/sys/orgstructure/posi/posiAssignRoleList.do'
			,false,true,
			toolbar
			);
		});

		//初始化
		function selectCheckbox(idValue,roleIds){
			var sm = mv_grid.grid.getSelectionModel();
			var rows = new Array();
			var store = mv_grid.grid.getStore();
			var view = mv_grid.grid.getView();
			for(var i = 0; i < view.getRows().length; i ++){
				var record = store.getAt(i);
				var id = record.get("id");
				//当前id存在roleIds中
				if(roleIds.indexOf(id)>=0){
					rows.push(i);
					//sm.selectRow(i);
					//sm.selectRecords(record,true);
				}
			}
			sm.selectRows(rows);
		}
		
		//保存角色
		function assignRole(){
			var id = document.getElementById("id").value;
			
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			
			//var s = document.getElementsByName("rid");
			var selectIds = '';
			for(var i=0;i<rows.length;i++){
				selectIds+=rows[i].get('id')+',';
			}
			//alert("选择的角色id="+selectIds);
			if(selectIds == null || selectIds == ""){
				window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.updateSelect"/>');
				return false;
			}else{
				$.ajax({
					type: "GET",
					url: "${ctx}/sys/orgstructure/emp/empAssignRoleSubmit.do",
					data: "empid="+id+"&selectIds="+selectIds,
					success: function(msg){
						if("true"==msg){
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.operateSuccess"/>'); 
						}else{
							window.top.Ext.ux.Toast.msg("<spring:message code='fhd.common.prompt'/>", '<spring:message code="fhd.common.operateFailure"/>');
						}
					}
				});
			}
		}
		Ext.EventManager.onWindowResize(function(width ,height){
			var height1 = heightBody-Ext.get("empForm").getHeight();
			mv_grid.grid.setWidth(width);
			mv_grid.grid.setHeight(height1);
			
        });
	</script>
</head>
<body>
	<div>
		<form id="empForm" name="empForm" action="${ctx}/sys/orgstructure/emp/empAssignRoleSubmit.do" method="post">
			<input type="hidden" name="id" id="id" value="${param.id}"/>
			<input type="hidden" name="roleids" id="roleids" value="${empForm.roleIds}"/>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;">
				<tr>  
					<th><spring:message code="fhd.sys.orgstructure.emp.empcode"/>：</th>
					<td><input type="text" name="empcode" id="empcode" value="${empForm.empcode}" readonly="readonly"/></td>
					<th><spring:message code="fhd.sys.orgstructure.emp.empname"/>：</th>
					<td><input type="text" name="empname" id="empname" value="${empForm.empname}" readonly="readonly"/></td>
				</tr>
				<tr>  
					<th><spring:message code="fhd.sys.auth.user.username"/>：</th>
					<td><input type="text" name="username" id="username" value="${empForm.username}" readonly="readonly"/></td>
					<th><spring:message code="fhd.sys.auth.role.realName"/>：</th>
					<td><input type="text" name="fhd.sys.auth.role.realName" id="realname" value="${empForm.realname}" readonly="readonly"/></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="rolelist" ></div>
	<script type="text/javascript">
		setTimeout(function(){
		    selectCheckbox('${param.id}','${empForm.roleIds}');
	    }, 2000);
	</script>
</body>
</html>