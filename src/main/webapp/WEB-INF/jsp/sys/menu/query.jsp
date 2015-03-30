<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><spring:message code="menu"/><spring:message code="list"/></title>
	<script type="text/javascript">
		var mv_grid;
		
		Ext.onReady(function(){
			var heightBody=Ext.getBody().getHeight();
			var width=Ext.getBody().getWidth(); 
			Ext.QuickTips.init();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: '添加',handler:saveMenu,iconCls:'icon-add'},'-',
					{text: '修改', id:'edit',handler:updateMenu,iconCls:'icon-edit',disabled:true},'-',
					{text: '删除',id:'del',handler:removeMenu,iconCls:'icon-del',disabled:true},'-',
					{text: '查询',handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}
				]
			});
			mv_grid = new FHD.ext.Grid('datalist',{},
				[   
					{header: "id", dataIndex: 'id', width: 0},
					{header: '<spring:message code="name"/>', dataIndex: 'name', sortable: true, width: 60, 
					  renderer:function(value, metaData, record, rowIndex, colIndex, store){
		        	   return "<div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div></a>";
					  }
					},
					{header: '排列顺序', dataIndex: 'sort', sortable: true, width: 60},
					{header: '菜单层次', dataIndex: 'rank', sortable: true, width: 60},
			 		{header: '上级菜单', dataIndex: 'pname', sortable: true, width: 60},
			 		{header: '功能名称 ', dataIndex: 'authname', sortable: true, width: 60}
				   
				],
				null,false,'100%',heightBody,'${ctx}/sys/menu/queryList.do?id=${id}'
				,false,true,
				toolbar
			),
			mv_grid.grid.getSelectionModel().on('selectionchange',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==1){
		    		 Ext.getCmp("edit").enable();
		    	}else{
		    		Ext.getCmp("edit").disable();
		    	}
		        Ext.getCmp("del").enable();
		        if(rows.length==0){
		    		 Ext.getCmp("edit").disable();
		    		 Ext.getCmp("del").disable();
		        }
			});
			//打开或关闭高级查询
			function showAndHide(){
				var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
				if(divShows == 'none'){
					document.getElementById("rmRiskTaxisDivId").style.display='';
					mv_grid.grid.getTopToolbar().get("adquery").setText("隐藏");
					var divHeight=Ext.get("rmRiskTaxisDivId").getHeight();
					var height=heightBody-divHeight;
					mv_grid.grid.setHeight(height);
				}else{
					document.getElementById("rmRiskTaxisDivId").style.display='none';
					mv_grid.grid.getTopToolbar().get("adquery").setText("查询");
					mv_grid.grid.setHeight(heightBody);
					
				}
			}
		});
		
		
		function saveMenu(){
			FHD.openWindow('菜单新增', 515,213, "${ctx}/sys/menu/add.do?operation=page&id=${param.id}",'no');
		}
		function updateMenu(){
			var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg('提示', '<spring:message code="updateSelect"/>'); 
				return;
			}
			if(rows.length>1){
				window.top.Ext.ux.Toast.msg('提示','<spring:message code="updateTip"/>');
				return;
			}
			var id=rows[0].get("id");
			var ret = FHD.openWindow('菜单修改', 515,213, "${ctx}/sys/menu/update.do?id="+id,'no');
		}

		function removeMenu(){
			var rows =mv_grid.grid.getSelectionModel().getSelections();
			if(rows.length==0){
				window.top.Ext.ux.Toast.msg('提示', '<spring:message code="removeTip"/>'); 
				return;
			}
			
		Ext.MessageBox.confirm('提示','你确定要删除？',function(btn){
			if(btn=='yes'){
				var ids = '';
				for(var i=0;i<rows.length;i++)
					ids+=rows[i].get('id')+',';  
					FHD.ajaxReq('${ctx}/sys/menu/delete.do',{ids:ids},function(data){
						if(data=='true'){
							window.top.Ext.ux.Toast.msg('提示','操作成功!');
							mv_grid.grid.store.reload();
							window.parent.parent.selectNodeReload();
						}else{
							window.top.Ext.ux.Toast.msg('提示','操作失败!');
						}
				    });
			}else{
					return;
				}
			});
		}
	

		//查询
		function queryData(){
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
	    		name: document.getElementById("name").value,
	    		authorityName: document.getElementById("authorityName").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
		
		function keypup(){
			if(event.keyCode==13){
				queryData();
				return;
			}
		}
	  Ext.EventManager.onWindowResize(function(width ,height){
	   			var height1 = Ext.getBody().getHeight()-Ext.get("rmRiskTaxisDivId").getHeight();
	   			if(Ext.get('showTable')=='none'){
	   				mv_grid.grid.setWidth(width);
	   				mv_grid.grid.setHeight(height);
	   			}else{
	   				mv_grid.grid.setWidth(width);
	   				mv_grid.grid.setHeight(height1);
	   			}
	 });
	</script>
</head>
<body>
	<div id="rmRiskTaxisDivId" style="display: none;">
		<form id="menuForm" name="menuForm" action="${ctx}/sys/menu/query.do" method="post" onkeyup="keypup();">
			<input type="hidden" name="id" value="${param.id}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title" >&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide(this)" width="15" height="15" /><spring:message code="querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0"
				cellspacing="0" class="fhd_query_table" style="display: block;">
				<tr>
					<th><spring:message code="name" />：</th>
					<td><input type="text" name="name" id="name"/></td>
					<th><spring:message code="functionName" />：</th>
					<td><input type="text" name="authorityName" id="authorityName"/></td>
				</tr>
				<tr>
					<td colspan="4" align="center">
					<input type="button" id="submits" onclick="queryData();" value="<spring:message code="search_btn" />" class="fhd_btn" /></td>
				</tr>
			</table>
		</form>
	</div>	
	<div id="datalist" ></div>
</body>
</html>