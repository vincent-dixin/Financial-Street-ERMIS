<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>面板信息管理</title>
	<script type="text/javascript"><!--
		var pageSize=20;
		var grid;
		var cols = [{header: "ID", dataIndex: 'id', sortable: true, width: 0},
		    		{header: "标题", dataIndex: 'title', sortable: true, width: 120},
					{header: "高度", dataIndex: 'height', sortable: true, width: 40},
					{header: "url", dataIndex: 'url', sortable: true, width: 400},
					{header: "操作人", dataIndex: 'operator', sortable: true, width: 100},
					{header: "操作时间", dataIndex: 'updateTime', sortable: true, width: 200}];
		var toolbar = new Ext.Toolbar( {
			height : 30,
			hideBorders : true,
			buttons : [{
				text : '添加面板',
				handler : save,iconCls:'icon-add'
			},{
				text : '修改',
				handler : edit,iconCls:'icon-edit'
			}, {
				text : '删除',
				handler : remove,iconCls:'icon-del'
			}, {
				text : '高级查询',
				handler : showAndHide,iconCls:'icon-zoom',id:'adquery'
			} ]
		});

		function keypup(){
			if(event.keyCode==13){
				query();
			}
		}
		//打开或关闭高级查询
		function showAndHide(){
			var divShows = document.getElementById("questSetDiv").style.display;
			if(divShows == 'none'){
				document.getElementById("questSetDiv").style.display='';
				grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.hide'/>");	
				var divHeight=Ext.get("questSetDiv").getHeight();
				var height=heightBody-divHeight;
				grid.setHeight(height);	
			}else{
				document.getElementById("questSetDiv").style.display='none';
				grid.getTopToolbar().get("adquery").setText("<spring:message code='fhd.common.query'/>");
				grid.setHeight(heightBody);
				}
		}
		
		
		function query(){
			hisDs=grid.store;
			hisDs.baseParams = {
				title: document.getElementById("title").value,
				url: document.getElementById("url").value
			};
			hisDs.load({
				params:{
					start:0,
					limit:20
				}
			});
		}
		function save() {
			var rows = grid.getSelectionModel().getSelections(); 
			openWindow('新增面板', 450, 130, '${ctx}/sys/portlet/updateShow.do');
			query();
		}
		function edit() {
			var rows = grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
			}else{
				var id=rows[0].get("id");
				openWindow('修改面板', 450, 130, '${ctx}/sys/portlet/updateShow.do?id='+id);
			}
			query();
		}
		function remove() {
			var rows = grid.getSelectionModel().getSelections(); 
			if(rows.length==0){
				Ext.MessageBox.alert('警告', '最少选择一条信息，进行删除!'); 
			}else{
				Ext.Msg.confirm("警告","确认删除该信息吗？",function(btn,text){
					if(btn=="yes"){
						var id=rows[0].get("id");
						Ext.Ajax.request({
							type: "POST",
							url: '${ctx}/sys/portlet/delete.do',
							params:'id='+id,
							success: function(response) {
								Ext.Msg.alert('成功', "删除成功");
								query();
							},
							failure: function(response) {
								Ext.Msg.alert('失败', "删除失败");
							}
						});
					}
					if(btn=="no"){
					
					}
				});
			}
		}
		var heightBody ;
		Ext.onReady(function(){
		heightBody  = Ext.getBody().getHeight();
			grid =new FHD.ext.Grid(
				'showList',
				{},
				cols,
				null,
				'',
				Ext.getBody().getWidth(),
				heightBody,
				'${ctx}/sys/portlet/portletSelect.do?status=2',
				null,
				true,
				toolbar
			).grid;
		});
		
		Ext.EventManager.onWindowResize(function(width,height){
			
   			var height1 = heightBody-Ext.get("questSetDiv").getHeight();
   			if(hisEventTable=='none'){
   				grid.setWidth(width);
   				grid.setHeight(height);
   			}else{
   				grid.setWidth(width);
   				grid.setHeight(height1);
   			}
		});
	--></script>
</head>
<body onload="showAndHide();">
	<div id="questSetDiv">
		<form id="questSetForm" name="questSetForm" method="post" onkeypress="keypup();">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
				</tr>
			</table>
			<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;" >
				<tr>
					<th>标题：</th>
					<td><input type="text" id="title" name="title" value="${title}" style="width:200px"/></td>
					<th>url：</th>
					<td>
						<input type="text" id="url" name="url" value="${title}" style="width:200px"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" onclick="query();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="showList" >
	</div>
</body>
</html>