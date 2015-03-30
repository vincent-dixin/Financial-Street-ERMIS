<%@page import="com.fhd.fdc.utils.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
		<title>流程实例列表</title>
		<script type="text/javascript">
			var mv_grid;
			var dataUrl = '${ctx}/jbpm/processInstance/processInstanceList.do';
			var cols = [
				{dataIndex:'id',hidden:true},
				{dataIndex:'processInstanceId',hidden:true},
				{dataIndex:'url',hidden:true},
				{dataIndex:'businessId',hidden:true},
				{header: "名称",dataIndex: 'businessName',sortable: true,width:40}, 
				{header: "类型",dataIndex: 'jbpmDeploymentName',sortable: true,width:40},
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
				{header: "操作",dataIndex: 'id',sortable: false,align:'center',width:50,
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
						return "<a href=\"javascript:void(0);\" onclick=\"show('" + value + "')\">流程查看</a> <a href=\"javascript:void(0);\" onclick=\"businessShow('"+record.get("url")+"','"+record.get("businessId")+"')\">业务查看</a>";
					}
				}
			];
			var toolbar = new Ext.Toolbar(
				{height: 30,hideBorders: true,
				buttons: [
					{id:"query",text: '查询',handler:showAndHide,iconCls:'icon-zoom'}
			]});
			function getParams(){
				var params = {
					jbpmDeploymentName:jQuery("[name='jbpmDeploymentName']").val(),
					startEmpName:jQuery("[name='startEmpName']").val(),
					dbversion:"${dbversion}",
					assigneeId:"${empid}",
					businessName:jQuery("[name='businessName']").val()
				};
				return params;
			}
			function show(id){
				FHD.openWindow("查看",800,593,"${ctx}/jbpm/processInstance/processInstanceShow.do?id="+id);
			}
			function businessShow(url,businessId){
				if(url!='null'){
					FHD.openWindow("查看",800,593,"${ctx}"+url+businessId);
				}else{
					Ext.Msg.alert("错误","未配置业务查看地址");
				}
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
			Ext.onReady(function(){
				Ext.QuickTips.init();
				mv_grid = new FHD.ext.Grid("list",getParams(),cols,null,"",'auto',jQuery(window).height(),dataUrl,false,true,toolbar,null,null,"createTime","desc");
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
						<th>流程类型：</th>
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