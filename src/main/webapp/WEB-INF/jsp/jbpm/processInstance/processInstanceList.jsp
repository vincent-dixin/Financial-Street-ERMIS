<%@page import="com.fhd.fdc.utils.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
		<title>流程监控</title>
		<script type="text/javascript">
			var mv_grid;
			var dataUrl = '${ctx}/jbpm/processInstance/processInstanceList.do';
			var cols = [
				{dataIndex:'id',hidden:true},
				{dataIndex:'processInstanceId',hidden:true},
				{dataIndex:'url',hidden:true},
				{dataIndex:'businessId',hidden:true},
				{header: "流程名称",dataIndex: 'businessName',sortable: true,width:60}, 
				{header: "类型",dataIndex: 'jbpmDeploymentName',sortable: true,width:60},
				{header: "发起人",dataIndex: 'createByRealname',sortable: true,width:60},
				{header: "发起时间",dataIndex: 'createTime',sortable: true,width:60},
				{header: "执行情况",dataIndex: 'endactivity',sortable: true,width:40,
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						var showValue="执行中";
						if(value=='end1'){
							showValue="已完成";
						}else if(value=='remove1'){
							showValue="已删除";
						}
						return showValue;
					}
				},
				{header: "操作",dataIndex: 'id',sortable: false,align:'center',
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<a href=\"javascript:void(0);\" onclick=\"show('" + value + "','"+record.get("processInstanceId")+"')\">流程查看</a> <a href=\"javascript:void(0);\" onclick=\"businessShow('"+record.get("url")+"','"+record.get("businessId")+"')\">业务查看</a>";
					}
				}
			];
			var toolbar = new Ext.Toolbar(
				{height: 30,hideBorders: true,
				buttons: [
					{id:"query",text: '查询',handler:showAndHide,iconCls:'icon-zoom'},
					{id:"del",text: '流程删除',handler:del,iconCls:'icon-del'}
			]});
			function getParams(){
				var params = {
					jbpmDeploymentName:jQuery("[name='jbpmDeploymentName']").val(),
					startEmpName:jQuery("[name='startEmpName']").val(),
					businessName:jQuery("[name='businessName']").val()
				};
				return params;
			}
			//流程查看
			function show(id,processInstanceId){
				FHD.openWindow("流程查看",800,593,"${ctx}/jbpm/processInstance/processInstanceEdit.do?id="+id+"&processInstanceId="+processInstanceId);
			}
			//业务查看
			function businessShow(url,businessId){
				if(url!='null'){
					FHD.openWindow("业务查看",800,593,"${ctx}"+url+businessId);
				}else{
					Ext.Msg.alert("错误","未配置业务查看地址");
				}
			}
			//流程删除
			function del(){
				var rows = mv_grid.grid.getSelectionModel().getSelections();
				Ext.MessageBox.confirm('提示框', '您确定要删除？',function(btn){ 
					if(btn=='yes'){
						if(rows){
							var processInstanceIds="";
							for(var i=0;i<rows.length;i++){
								processInstanceIds += rows[i].get('processInstanceId');
								if(i!=rows.length-1){
									processInstanceIds += ",";
								}
							}
						}
						$.ajax({
							type: "POST",
							async: false,
							url: "${ctx}/jbpm/deleteProcessInstance.do",
							data: "ids="+processInstanceIds,
							success: function(msg){
								if("true"==msg){
									query();
									Ext.MessageBox.alert('提示', '操作成功!');
								}else{
									Ext.MessageBox.alert('提示',"<spring:message code="fhd.common.operateFailure"/>");
								}
							}
						});
					}
				});
			}
			//查询
			function query(){
				mv_grid.grid.store.baseParams = getParams();
			    // 数据重新加载
				mv_grid.grid.store.reload();
			}
			function opReload(){
				mv_grid.grid.store.reload();
			}
			function keypup(){
				if(event.keyCode==13){
					query();
				}
			}
			//隐藏div
			function showAndHide(){
			    var queryDiv = jQuery("#queryDiv");
			    if(queryDiv.is(":visible")){
			        mv_grid.grid.getTopToolbar().get("query").setText("查询");
			        var divHeight=Ext.get("queryDiv").getHeight();
			        mv_grid.grid.setHeight(mv_grid.grid.getHeight()+divHeight);
			        queryDiv.hide();
			    }else{
			        queryDiv.show();
			        mv_grid.grid.getTopToolbar().get("query").setText("隐藏");        
			        var divHeight=Ext.get("queryDiv").getHeight();
			        mv_grid.grid.setHeight(mv_grid.grid.getHeight()-divHeight);
			    }
			}
			//按钮状态设定
			function setToolbarAble(){
				var rows =mv_grid.grid.getSelectionModel().getSelections(); 
				Ext.getCmp("del").disable();
			   	if(rows.length>0){
			   		var b="false";
			   		for(var i=0;i<rows.length;i++){
			   			if(rows[i].get('endactivity')=="end1" || rows[i].get('endactivity')=="remove1"){
							b="true";
			   			}
			   		}
			   		if( b=="false"){
			   			Ext.getCmp("del").enable();
			   		}
			   	}
			}
			Ext.onReady(function(){
				Ext.QuickTips.init();
				mv_grid = new FHD.ext.Grid("list",getParams(),cols,null,"",'auto',jQuery(window).height(),dataUrl,false,true,toolbar,null,null,"createTime","DESC");
				mv_grid.grid.store.on("load",function(store,records,options){
					setToolbarAble();
				});
				mv_grid.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
					setToolbarAble();
				});
	 			//复选框被选中
				mv_grid.grid.getSelectionModel().on('rowdeselect',function(sm,rowIndex,record){
					setToolbarAble();
				});
			});
		</script>
	</head>
	<body style="overflow: hidden;">
		<div id="queryDiv" style="display: none;">
			<form id="queryFormId" method="post" onkeypress="keypup();" onsubmit="return false;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
					</tr>
				</table>
				<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display:block;" >
					<tr>
						<th>流程名称：</th>
						<td>
							<input type="text" id="businessName" name="businessName" value="" style="width:200px;"/>
						</td>
						<th>发&nbsp;起&nbsp;人：</th>
						<td>
							<input type="text" id="startEmpName" name="startEmpName" value="" style="width:200px;"/>
						</td>
		            </tr>
					<tr>
						<th>类&nbsp;&nbsp;&nbsp;&nbsp;型：</th>
						<td>
							<input type="text" id="jbpmDeploymentName" name="jbpmDeploymentName" value="" style="width:200px;"/>
						</td>
						<th>&nbsp;</th>
						<td>
							&nbsp;
						</td>
		            </tr>
					<tr>
						<td colspan="4" align="center">
							<input type="button" onclick="query();" value="<spring:message code="fhd.common.query" />" class="fhd_btn" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="list"></div>
	</body>
</html>