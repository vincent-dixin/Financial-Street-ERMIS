<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<%@ taglib uri="fhd-tag" prefix="fhd"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><spring:message code="fhd.sys.log.businessLog" /></title>
	<script type="text/javascript">
		var mv_grid;
		var heightBody
		Ext.onReady(function(){
			Ext.QuickTips.init();
			heightBody=Ext.getBody().getHeight();
			var toolbar = new Ext.Toolbar({
				height: 30,
				hideBorders: true,
				buttons: [
					{text: '<spring:message code="fhd.common.del" />',id:'del',handler:removeBusinessLog,iconCls:'icon-del',disabled:true},
					{text: '<spring:message code="fhd.common.query" />',id:'adquery',handler:showAndHide,iconCls:'icon-zoom'}
				]
			});
		
			mv_grid = new FHD.ext.Grid('log',null,
				[   
					{header: "id", dataIndex: 'id', width: 0},
					{header: '<spring:message code="fhd.common.operateDesc" />', dataIndex: 'operateRecord', sortable: true, width: 150,
				        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
						return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value +'</div>'; 
			        }}, 
					{header: '<spring:message code="fhd.common.operateTime" />', dataIndex: 'operateTime', sortable: true, width: 50}, 
					{header: '<spring:message code="fhd.common.operateType" />', dataIndex: 'operateType', sortable: true, width: 30}, 
					{header: '<spring:message code="fhd.common.module" />', dataIndex: 'moduleName', sortable: true, width: 35}, 
					{header: '<spring:message code="fhd.common.isSuccess" />', dataIndex: 'isSuccess', sortable: true, width: 30}, 
					{header: '<spring:message code="fhd.common.username" />', dataIndex: 'username', sortable: true, width: 20}, 
					{header: '<spring:message code="fhd.sys.orgstructure.org.orgname" />', dataIndex: 'orgname', sortable: true, width: 40}
				],
				null,false,'100%',heightBody,'${ctx}/sys/log/businessLogALLList.do?id=${id}',false,true,
				toolbar
				);
			  mv_grid.grid.getSelectionModel().on('selectionchange',function(sm,rowIndex,record){
			    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
			        if(rows.length==0){
			    		 Ext.getCmp("del").disable();
			        }else{
			             Ext.getCmp("del").enable();
			        }
			    });
			
			});
			
			function removeBusinessLog(){
				var rows =mv_grid.grid.getSelectionModel().getSelections();
				if(rows.length==0){
					window.top.Ext.ux.Toast.msg('<spring:message code="fhd.common.prompt"/>', '<spring:message code="fhd.common.removeTip"/>'); 
					return;
			}
			
			Ext.MessageBox.confirm('<spring:message code="fhd.common.prompt"/>','<spring:message code="fhd.common.makeSureDelete"/>',function(btn){
				if(btn=='yes'){
					var ids = '';
					for(var i=0;i<rows.length;i++)
						ids+=rows[i].get('id')+',';
						
					FHD.ajaxReq('${ctx}/sys/log/businessLogDel.do?orgId=${id}',{ids:ids},function(data){
							
						if(data=='true')
							window.top.Ext.ux.Toast.msg('<spring:message code="fhd.common.prompt"/>','<spring:message code="fhd.common.operateSuccess"/>');
						else 
							window.top.Ext.ux.Toast.msg('<spring:message code="fhd.common.prompt"/>','<spring:message code="fhd.common.operateFailure"/>');
						mv_grid.grid.store.reload();
					});
				}else{
					return;
				}
			});
		}
		
		//打开或关闭高级查询
		function showAndHide(){
			var divShows = document.getElementById("listdate").style.display;
			if(divShows == 'none'){
				document.getElementById("listdate").style.display='';
				mv_grid.grid.getTopToolbar().get("adquery").setText('<spring:message code="fhd.common.hide"/>');
				var divHeight=Ext.get("listdate").getHeight();
				var height=heightBody-divHeight;
				mv_grid.grid.setHeight(height);					
			}else{
				document.getElementById("listdate").style.display='none';
				mv_grid.grid.getTopToolbar().get("adquery").setText('<spring:message code="fhd.common.query"/>');
				mv_grid.grid.setHeight(heightBody);
			}
		}
		
		//查询
		function queryRole(){
			
			if(!islegal()){
				return false;
			}
			// 数据重新加载
		    mv_grid.grid.store.baseParams = {
	    		userName: document.getElementById("userId").value,
	    		startTimes: document.getElementById("beginTime").value,
	    		endTimes: document.getElementById("endTime").value
			};
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
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
		
		//检查日期
		function islegal(){
			var startDate= document.getElementById("beginTime").value;
			var endDate=document.getElementById("endTime").value;			
			if(startDate!=null && startDate!="" && endDate!=null && endDate!=""){
				if(!checkDate(startDate,endDate)){
					window.top.Ext.ux.Toast.msg('<spring:message code="fhd.common.prompt"/>',"<spring:message code='fhd.common.alertDate'/>");
					return false;			
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
		function queryBusinessLog(){
			if(!islegal()){
				return false;
			}
			var url = '${ctx}/sys/log/businessLogAllList.do';
			var o={};
			//录入起止时间
    		var startTimes = document.getElementById("beginTime").value;
    		if(startTimes != null && startTimes != ""){
				o.startTimes = startTimes;
			}
    		var endTimes = document.getElementById("endTime").value;
    		if(endTimes != null && endTimes != ""){
				o.endTimes = endTimes;
			}
    		
    	    var userName = document.getElementById("userId").value;
    	    if(userName != null && userName != ""){
				o.userName = userName;
			}
    	    
    		o.start=0;
			o.limit=20; 
			mv_grid.grid.store.reload({params:o});
		}
		
		function keypup(){
			if(event.keyCode==13){
				queryRole();
				return false;
			}
		}
		Ext.EventManager.onWindowResize(function(width ,height){
   			var height1 = heightBody-Ext.get("listdate").getHeight();
   			var divShows = document.getElementById("listdate").style.display;
   			if(divShows=='none'){
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(height);
   			}else{
   				mv_grid.grid.setWidth(width);
   				mv_grid.grid.setHeight(height1);
   			}
        });
	</script>
</head>
<body style='overflow:hidden'>
	<div id="listdate" style='display:none'>
		<form:form commandName="businessLogForm"	method="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif" onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td>
				</tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="fhd_query_table" style="display: block;">
				<tr>
					<th><spring:message code="fhd.sys.auth.user.username" />：</th>
					<td><form:input path="userId" onkeypress="keypup();"  /></td>
					<th><spring:message code="fhd.common.operateTime" />：</th>
					<td><form:input path="beginTime" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:146;" />~<form:input path="endTime" onclick="WdatePicker();" cssClass="Wdate" cssStyle="width:147;" /></td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input type="button" onclick="queryRole();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>
		</form:form>
	</div>
	<div id="log"></div>
</body>
</html>