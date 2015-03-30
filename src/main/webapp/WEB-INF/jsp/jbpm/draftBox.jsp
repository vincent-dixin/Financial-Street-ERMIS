<%@page import="com.fhd.fdc.utils.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
		<title>草稿箱</title>
		<script type="text/javascript">
			var mv_grid;
			var dataUrl = '${ctx}/jbpm/draftBox/draftBoxList.do';
			var cols = [
				{dataIndex:'id',hidden:true},
				{dataIndex:'infoTittle',hidden:true},
				{dataIndex:'infoUrl',hidden:true},
				{dataIndex:'commitTittle',hidden:true},
				{dataIndex:'commitUrl',hidden:true},
				{dataIndex:'deleteUrl',hidden:true},
				{header: "名称",dataIndex: 'name',sortable: true,width:100,
					renderer:function(value, metaData, record, rowIndex, colIndex, store){
					return "<a href=\"javascript:void(0);\" onclick=\"show('" + record.get("id") + "','"+ record.get("infoTittle") +"','"+ record.get("infoUrl") +"')\"><div ext:qtitle='' ext:qtip='" + value + "'>" + value + "</div></a>";
   					}
				}, 
				{header: "类型",dataIndex: 'type',sortable: true,width:30},
				{header: "创建时间",dataIndex: 'creatTime',sortable: true,width:30}
			];
			var toolbar = new Ext.Toolbar(
					{height: 30,hideBorders: true,
					buttons: [
					{id:"edit",text: '修改',handler:commit,iconCls:'edit',disabled:true},
					{id:"del",text: '删除',handler:deleteItems,iconCls:'icon-del',disabled:true},
					{id:"query",text: '查询',handler:showAndHide,iconCls:'icon-zoom'}
				]});
			function setToolbarAble(){
				var rows =mv_grid.grid.getSelectionModel().getSelections(); 
				Ext.getCmp("edit").disable();
				Ext.getCmp("del").disable();
			   	if(rows.length==1){
					Ext.getCmp("edit").enable();
			   	}
			   	if(rows.length>0){
					Ext.getCmp("del").enable();
			   	}
			}
			function getParams(){
				var params = {
					draftName:jQuery("[name='draftName']").val(),
					type : document.getElementById("type").value,
					startTime:jQuery("[name='startTime']").val(),
					endTime:jQuery("[name='endTime']").val()
				};
				return params;
			}
			function show(id,tittle,infoUrl){
				FHD.openWindow(tittle,800,500,"${ctx}"+infoUrl+"?id="+id+"&viewFlag=true");
			}
			//提交
			function commit() {
				var rows =mv_grid.grid.getSelectionModel().getSelections(); 
				var id=rows[0].get("id");
				if("评估问卷"==(rows[0].get("type"))){
					openWindow(rows[0].get("commitTittle"), 1000, 530, "${ctx}"+rows[0].get("commitUrl")+"?id="+id);
				}else if("风险控制计划"==(rows[0].get("type"))){
					openWindow(rows[0].get("commitTittle"), 1000, 530, "${ctx}"+rows[0].get("commitUrl")+"?scenarioId="+id);
				}
			}
			
			//删除
			function deleteItems() {
				var rows =mv_grid.grid.getSelectionModel().getSelections(); 
				if (rows.length > 0) {
					Ext.MessageBox.confirm('确定', '确定要删除所选记录?', function(btn) {
						if (btn == 'yes') {
							doDelete(rows);
						}
					});
				}else{
					Ext.MessageBox.alert('警告', '最少选择一条信息，进行删除!'); 
				}
			}
	
			function doDelete(appList) {
				var msgTip = opWait();
				var ids = '';
				var deleteUrl;
				for(var i = 0; i < appList.length; i++){	
					ids += appList[i].get("id") + ',';
					deleteUrl=appList[i].get("deleteUrl");
				}
				var options = {
					url: "${ctx}"+deleteUrl+"?ids="+ids,
					type:'POST',
					success:function(data){
			     		msgTip.hide();
			         	if("true" == data){
			         	    reloadStore();//重新加载数据
			         		parent.Ext.MessageBox.alert('提示', '操作成功!');
			         	}else if("false" == data){
							parent.Ext.MessageBox.alert('提示', '操作失败!');
						}
			     	}
				};
				$('#queryFormId').ajaxSubmit(options);
				return false;		
			}
			//查询
			function query(){
				mv_grid.grid.store.baseParams = getParams();
			    // 数据重新加载
				mv_grid.grid.store.reload();
			}
			function reloadStore(){
				mv_grid.grid.store.reload();
			}
			function keypup(){
				if(event.keyCode==13){
					query();
				}
			}
			//判断时间开始和结束
			function checkDate(startDate,endDate){
				var regS = new RegExp("-","gi");
				date1=startDate.replace(regS,"/");
				date2=endDate.replace(regS,"/");	
				var bd =new Date(Date.parse(date1));
				var ed =new Date(Date.parse(date2));
			    if(bd<=ed){
					return true;
				}
				return false;
			}
			function isRequiredDate(){
				var startDate= document.getElementById("startTime").value;
				var endDate=document.getElementById("endTime").value;	
				if(startDate!=null && startDate!="" && endDate!=null && endDate!=""){
					if(!checkDate(startDate,endDate)){
						Ext.MessageBox.alert("提示", "开始日期不能小于结束日期！");
						return false;			
					}
				}
				return true;	
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
				mv_grid = new FHD.ext.Grid("list",getParams(),cols,null,"",'auto',jQuery(window).height(),dataUrl,false,true,toolbar);
				//设定数据改变监听
				mv_grid.store.on("load",function(store,records,options){
					setToolbarAble();
				});
				mv_grid.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
					setToolbarAble();
				});
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
						<th>名称：</th>
						<td><input type="text" id="draftName" name="draftName" value="" style="width:200px"/></td>
						<th>类型：</th>
						<td>
					        <select id="type" name="type" style='width:200px;' >
								<option value="">请选择</option>
								<option value="评估问卷">评估问卷</option>
								<option value="风险控制计划">风险控制计划</option>
							</select>
						</td>
						
					</tr>
					<tr>
						<th>创建时间：</th>
						<td>
							<input type="text" name="startTime" id="startTime" onfocus="WdatePicker();" class="Wdate"  onchange="isRequiredDate();" style="width:100px"/>~<input type="text" name="endTime" id="endTime" onfocus="WdatePicker();" onchange="isRequiredDate();" class="Wdate"  style="width:100px"/>
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