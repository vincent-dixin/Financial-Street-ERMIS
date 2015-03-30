<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.group.group" /></title>
	<script type="text/javascript">
		var grid;
		function roleCallback(data){
			var ids = '';
			if(data.length==0)return;
			for(var i=0;i<data.length;i++)
				ids += ','+data[i].id;
			FHD.ajaxReq('${ctx}/sys/group/addRoles.do',{id:'${param.id}',ids:ids},function(reqText){
				if(reqText=='true'){
					grid.store.reload();
					window.top.Ext.ux.Toast.msg('提示','操作成功!');
				}else
					window.top.Ext.ux.Toast.msg('提示','操作失败!');
			});
		}
		function add(){
			var rows = grid.getStore().data;
				var ids = '';
				for(var i=0;i<rows.length;i++)
					ids += ','+grid.getStore().getAt(i).get('id');
			FHD.selectRoleWindow('roleCallback',"selects="+ids);
		}
		function deleteItem(){
			if (grid.getSelectionModel().hasSelection()){
				var rows = grid.getSelectionModel().getSelections();
				var ids = "";
				for(var i=0;i<rows.length;i++){
					ids += rows[i].get("id") + ",";
				}
				Ext.MessageBox.confirm('提示框', '您确定要删除？', function(btn){
					if(btn=='no')return;
					var rows = grid.getSelectionModel().getSelections();
					var ids = '';
					for(var i=0;i<rows.length;i++)
						ids += ','+rows[i].get('id');
					FHD.ajaxReq('${ctx}/sys/group/deleteRoles.do',{id:'${param.id}',ids:ids},function(reqText){
						if(reqText=='true'){
							grid.store.reload();window.top.Ext.ux.Toast.msg('提示','操作成功!');
						}else
							window.top.Ext.ux.Toast.msg('提示','操作失败!');
					});
				});
			}else
				window.top.Ext.ux.Toast.msg("提示","请选择要删除的记录!");
		}
		function reloadGrid(){
			grid.store.reload();
		}
		Ext.onReady(function(){
			var cols=[{dataIndex:'id',width:0},
					{header:'<spring:message code="fhd.sys.auth.role.roleCode" />',dataIndex:'roleCode',width:20},
					{header:'<spring:message code="fhd.sys.auth.role.roleName" />',dataIndex:'roleName',width:20}];
			var toolbar=[{text:'<spring:message code="fhd.common.add" />',handler:add,iconCls:'icon-add'},
						{text:'<spring:message code="fhd.common.del" />',handler:deleteItem,iconCls:'icon-del'}];
			grid = new FHD.ext.Grid('list',{id:'${param.id}'},cols,null,'',Ext.getBody().getWidth(),Ext.getBody().getHeight()-5,'${ctx}/sys/group/roleList.do',true,true,toolbar);
			grid = grid.getGrid();
		});
	</script>
</head>
<body>
	<div id='list'></div>
</body>
</html>