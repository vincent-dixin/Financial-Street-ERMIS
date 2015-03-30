<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.group.group" /></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody;
		var widthBody;
		Ext.onReady(function(){
			heightBody = Ext.getBody().getHeight();
		    widthBody = Ext.getBody().getWidth();
			Ext.QuickTips.init();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: '添加',handler:saveData,iconCls:'icon-add'},
					{text: '修改',handler:updateData,iconCls:'icon-edit'},
					{text: '删除',handler:removeData,iconCls:'icon-del'},
					{text: '高级查询',handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}
				]
			});
		
			
			mv_grid = new FHD.ext.Grid('datalist',{},
				[   
					{header: "id", dataIndex: 'id', width: 0},
					{header: '工作组编号', dataIndex: 'groupCode', sortable: true, width: 60},
					{header: '<spring:message code="fhd.sys.group.groupName" />', dataIndex: 'groupName', sortable: true, width: 100}, 
					{header: '<spring:message code="fhd.sys.group.groupLevel" />', dataIndex: 'groupLevel', sortable: true, width: 50}, 
					{header: '<spring:message code="fhd.common.startDate" />', dataIndex: 'startDate', sortable: true, width: 40}, 
					{header: '<spring:message code="fhd.common.endDate" />', dataIndex: 'endDate', sortable: true, width: 40},
					{header: '工作组描述', dataIndex: 'groupDesc', sortable: true, width: 120,
				        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
						return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value +'</div>'; 
			        }}
				],
				null,false,widthBody,heightBody,'${ctx}/sys/group/nextGroupList.do?id=${id}'
				,false,true,
				toolbar
			);
		});
		//刷新
		function reloadGrid(){
			mv_grid.grid.store.reload({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});
		}
		//保存
		function saveData(){
			FHD.openWindow('<spring:message code="fhd.common.add" /><spring:message code="fhd.sys.group.group" />', 600, 300, '${ctx}/sys/group/add.do?parentId=${param.id}')
			parent.parent.nodeReload();
		}
		//更新
		function updateData(){
			var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg('提示', '<spring:message code="fhd.common.updateSelect"/>'); 
				return;
			}
			if(rows.length>1){
				window.top.Ext.ux.Toast.msg('提示','<spring:message code="fhd.common.updateTip"/>');
				return;
			}
			var id=rows[0].get("id");
			FHD.openWindow('<spring:message code="fhd.common.add" /><spring:message code="fhd.sys.group.group" />', 600, 300, '${ctx}/sys/group/edit.do?id='+id);
			
		}
		//删除数据
		function removeData(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg('提示', '<spring:message code="fhd.common.removeTip"/>'); 
				return;
			}
			
			Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++){
						ids+=rows[i].get('id')+',';
					}
					FHD.ajaxReq('${ctx}/sys/group/delete.do',{ids:ids},function(data){
						if(data=='true'){
							window.top.Ext.ux.Toast.msg('提示','操作成功!');
							mv_grid.grid.store.reload();
							parent.parent.nodeReload();
						}else{ 
							window.top.Ext.ux.Toast.msg('提示','操作失败!');
						}
					});
				}else{
					return;
				}
			});
		}
		//打开或关闭高级查询
		function showAndHide(){
				var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
				if(divShows == 'none'){
					document.getElementById("rmRiskTaxisDivId").style.display='';
					mv_grid.grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.hide'/>");	
					var divHeight=Ext.get("rmRiskTaxisDivId").getHeight();
					var height=heightBody-divHeight;
					mv_grid.grid.setHeight(height);	
				}else{
					document.getElementById("rmRiskTaxisDivId").style.display='none';
					mv_grid.grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.query'/>");
					mv_grid.grid.setHeight(heightBody);
					}
			}
		//查询
		function queryEmp(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
		    	groupName: document.getElementById("groupName").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
		//工作组分配权限
		function groupAssignAuthority(id){
			FHD.openWindow('<spring:message code="fhd.sys.group.group" /><spring:message code="fhd.sys.auth.authority.assignAuthority" />', 800,500, '${ctx}/sys/group/assignAuthority.do?id='+id);
		}

		function keypup(){
			if(event.keyCode==13){
				queryEmp();
			}
		}
		Ext.EventManager.onWindowResize(function(width ,height){
   			var height1 = heightBody-Ext.get("rmRiskTaxisDivId").getHeight();
   			if(showTable=='none'){
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(height);
   			}else{
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(height1);
   			}
		});
	</script>
</head>
<body onload="showAndHide();">
	<div id="rmRiskTaxisDivId">
		<form id="groupForm" name="groupForm" method="post" action="" onkeypress="keypup();" onsubmit="return false;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0"
				cellspacing="0" class="fhd_query_table" style="display: block;">
				<tr align="left">
					<th><spring:message code="fhd.sys.group.groupName" />：</th>
					<td><input type="text" name="groupName" id="groupName"/><input type="button" onclick="queryEmp();"
						value="<spring:message code="fhd.common.search" />" class="fhd_btn" /></td>
					
				</tr>
				<tr>
					
				</tr>
			</table>
		</form>
	</div>
	<div id="datalist"></div>
</body>
</html>