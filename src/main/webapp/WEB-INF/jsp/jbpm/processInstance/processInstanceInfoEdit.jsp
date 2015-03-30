<%@page import="com.fhd.fdc.utils.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>显示流程</title>
		<script type="text/javascript">
		var mv_gridTodo;
		var dataUrlTodo = '${ctx}/jbpm/processInstance/JbpmHistActinstList.f';
		var colsTodo = [
			{dataIndex:'id',hidden:true},
			{header: "任务名称",dataIndex: 'activityName',sortable: false,width:50,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";					
   				}
			}, 
			{header: "状态",dataIndex: 'dbversion',sortable: true,width:20,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";					
   				}
			}, 
			{header: "承办人编码",dataIndex: 'assigneeRealCode',sortable: true,width:60,hidden:true}, 
			{header: "承办人",dataIndex: 'assigneeRealname',sortable: false,width:20,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";
   				}
			},
			{header: "承办人单位",dataIndex: 'assigneeCompanyName',sortable: false,width:40,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";
   				}
			},
			{header: "审批意见",dataIndex: 'ea_Content',sortable: false,width:120,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";					
   				}
			},
			{header: "到达时间",dataIndex: 'startStr',sortable: false,width:60,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";					
   				}
			},
			{header: "操作时间",dataIndex: 'endStr',sortable: false,width:60,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					var color="black";
					if(record.get("dbversion")=="未处理"){
						color="red";
					}
					return "<font style='color:"+color+"'>" + value + "</font>";					
   				}
			}
		];
		function getParamsTodo(){
			var params = {
				jbpmHistProcinstId:"${jbpmHistProcinst.id}",
				dbversion:null
			};
			return params;
		}
		  var toolbar = [
		      {id:'edit',text:'转办',iconCls:'edit',handler:editPerson}
		     	   	];
		function editPerson(){
			var rows=mv_gridTodo.grid.getSelectionModel().getSelections();
		 	var processInstanceId=document.getElementById("processInstanceId").value;
		 	var id = rows[0].get('id');
		 	FHD.openWindow('转办', 700, 296, '${ctx}/jbpm/processInstance/processInstanceInfoPersonSel.f?id='+id+'&processInstanceId='+processInstanceId);
		}
		//按钮状态设定
		function setToolbarAble(){
			var rows =mv_gridTodo.grid.getSelectionModel().getSelections(); 
			Ext.getCmp("edit").disable();
		   	if(rows.length==1){
		   		if(rows[0].get('dbversion')=="未处理"){
				Ext.getCmp("edit").enable();
		   		}
		   	}
		}
		//重新加载store
		function reloadStore(){
			mv_gridTodo.grid.store.reload();
		}
		Ext.onReady(function(){
			Ext.QuickTips.init();
			mv_gridTodo = new FHD.ext.Grid("listTodo",getParamsTodo(),colsTodo,null,null,'auto',jQuery(window).height()-jQuery("#info").height()-42,dataUrlTodo,false,true,toolbar,null,null,"dbversion","DESC");
  			Ext.EventManager.onWindowResize(function(width ,height){
  				mv_gridTodo.grid.setWidth(width);
  				mv_gridTodo.grid.setHeight(height-jQuery("#info").height()-42);
			});
  			mv_gridTodo.grid.store.on("load",function(store,records,options){
				setToolbarAble();
			});
  			mv_gridTodo.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
				setToolbarAble();
			});
 			//复选框被选中
			mv_gridTodo.grid.getSelectionModel().on('rowdeselect',function(sm,rowIndex,record){
				setToolbarAble();
			});
		});
		</script>
	</head>
	<body style="overflow-y:scroll;">
		<fieldset id="info" class="x-fieldset">
			<LEGEND>流程实例信息</LEGEND>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_form_table">
				<tr>
					<th>流程名称：</th>
					<td>
						<input type="hidden" id="processInstanceId" value="${processInstanceId}"/>
						${jbpmHistProcinst.businessWorkFlow.businessName}&nbsp;
					</td>
					<th>流程状态：</th>
					<td>
						${jbpmHistProcinst.endactivity=="end1"?"完成":"进行中"}
					</td>
				</tr>
				<tr>
					<th>流程发起者：</th>
					<td>
						${jbpmHistProcinst.businessWorkFlow.createByEmp.realname }&nbsp;
					</td>
					<th>发起者部门：</th>
					<td>
						<c:forEach items="${jbpmHistProcinst.businessWorkFlow.createByEmp.sysEmpOrgs}" var="sysEmpOrg">
							${sysEmpOrg.sysOrganization.orgname}&nbsp;
						</c:forEach>
						&nbsp;
					</td>
				</tr>
				<tr>
					<th>发起时间：</th>
					<td>
						<fmt:formatDate value="${jbpmHistProcinst.businessWorkFlow.createTime }" type="date" pattern="yyyy年MM月dd日HH时mm分ss秒" />&nbsp;
					</td>
					<th>更新时间：</th>
					<td>
						<fmt:formatDate value="${updateTime}" type="date" pattern="yyyy年MM月dd日HH时mm分ss秒" />&nbsp;
					</td>
				</tr>
			</table>
		</fieldset>
		<!-- <div id="listComplete"></div> -->
		<div id="listTodo"></div>
	</body>
</html>