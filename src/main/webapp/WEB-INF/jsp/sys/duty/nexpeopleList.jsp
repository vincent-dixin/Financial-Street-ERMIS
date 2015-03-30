<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="fhd.sys.duty.duty" /></title>
	<script type="text/javascript">
		var mv_grid;
		var grid;
		var heightBody
		Ext.onReady(function(){
			Ext.QuickTips.init();
			heightBody=Ext.getBody().getHeight();
			var toolbar = new Ext.Toolbar(
			{height: 30,hideBorders: true,
			 buttons: [
			{text: '添加',handler:add,iconCls:'icon-add'},
			{text: '删除',id:'del',handler:deleteItem,iconCls:'icon-del',disabled:true},
			{text: '查询',handler:showAndHide,iconCls:'icon-zoom',id:'adquery'}]});
			
			mv_grid = new FHD.ext.Grid('datalist',{riskId:5},
			[   
				{header: "id", dataIndex: 'id', width: 0},
				{header: '员工姓名', dataIndex: 'empname', sortable: true, width: 60}, 
				{header: '用户名', dataIndex: 'username', sortable: true, width: 60}
				
			],
			null,false,'100%',heightBody,'${ctx}/sys/duty/nexPeople.do?id=${param.id}'
			,false,true,
			toolbar
			);
			grid=mv_grid.grid;
			mv_grid.grid.getSelectionModel().on('rowselect',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length!=0){
		    		 Ext.getCmp("del").enable();
		    	}
			});
			mv_grid.grid.getSelectionModel().on('rowdeselect',function(sm,rowIndex,record){
		    	var rows =mv_grid.grid.getSelectionModel().getSelections(); 
		    	if(rows.length==0){
		    		 Ext.getCmp("del").disable();
		    	}
			});
			
		});

		//回车事件
		function keypup(){
			if(event.keyCode==13){
				queryData();
			}
		}

		//高级查询
		function queryData(){
			mv_grid.grid.store.baseParams = {
				empname: document.getElementById("empname").value
			};      
		    mv_grid.grid.store.load({
		    	params:{		    			 
		    		start:0,
		    		limit:20			    		
		    	}
			});	
		}
		
		var interval;
		function addPeople(){
			var cs = divemps.getElementsByTagName('input');
			var index = 0;
			var els = new Array();
			var ids = '';
			for(var i=0;i<cs.length;i++){
				if(cs[i].checked){
					els[index++]=cs[i];
				}
			}
			for(var i=0;i<els.length;i++){
				ids += els[i].value + ',';
			}
			if(index>0){
				ajaxRequest('${ctx}/sys/duty/addPeople.do',{id:'${param.id}',ids:ids},function(){window.location.reload();},function(){window.location.reload();});
				clearInterval(interval);
			}
		}
		//添加人员
		function add(){
			var rows = grid.getStore().data;
			var ids = '';
			for(var i=0;i<rows.length;i++){
				ids += ','+grid.getStore().getAt(i).get('id');
			}
			FHD.selectEmpWindow('roleCallback',"selects="+ids);
		}
		//添加人员回调函数
		function roleCallback(selects){
			var orgIds = '';
			for(var j=0; j<selects.length; j++){
				orgIds += selects[j].id+",";
			}
			if('' == orgIds){
				window.top.Ext.ux.Toast.msg('提示', '请选择要添加的职务的下级人员!');
				return false;
			}
          	//后台操作数据
            Ext.Ajax.request({
				url:'${ctx}/sys/duty/addPeople.do',
			    method:'post',
			    params:{
					ids: orgIds.substring(0,orgIds.length-1),
					id: $("#id").attr("value")
			    },
			    success:function(response){
			      	if("true" == response.responseText){
			      		mv_grid.grid.store.load({
					    	params:{		    			 
					    		start:0,
					    		limit:20			    		
					    	}
						});
			      		window.top.Ext.ux.Toast.msg('提示','操作成功!');
				    }else if("false" == response.responseText){
				    	window.top.Ext.ux.Toast.msg('提示', '操作失败!');
	         			closeWindow();
	         		}else{
	         			window.top.Ext.ux.Toast.msg('提示', response.responseText);
	         			closeWindow();
	         		}
			    }
			});
		}
		
		function deleteItem(){
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
					FHD.ajaxReq('${ctx}/sys/duty/deletePeople.do',{ids:ids},function(data){
						if(data!='true'){
							window.top.Ext.ux.Toast.msg('提示','操作失败!');
						}else{ 
							window.top.Ext.ux.Toast.msg('提示','操作成功!');
						}
						mv_grid.grid.store.reload();
					});
				}else{
					return;
				}
			});
		}
		interval = setInterval(addPeople,500);

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
		Ext.EventManager.onWindowResize(function(width ,height){
	   			var height1 = heightBody-Ext.get("rmRiskTaxisDivId").getHeight();
	   			var divShows = document.getElementById("rmRiskTaxisDivId").style.display;
	   			if(divShows=='none'){
	   				mv_grid.grid.setWidth(width);
	   				mv_grid.grid.setHeight(height);
	   			}else{
	   				mv_grid.grid.setWidth(width);
	   				mv_grid.grid.setHeight(height1);
	   			}
	 });
	</script>
	<script type="text/javascript" src="${ctx}/scripts/orgtag.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/tags.css"/>
</head>
<body >
	<div id="rmRiskTaxisDivId" style="display:none;">
		<form id="empForm" name="empForm" onkeypress="keypup();" onsubmit="return false;">
			<input type="hidden" id="id" name="id" value="${param.id}"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" /><spring:message code="fhd.common.querycondition"></spring:message></td></tr>
			</table>
			<table id="showTable" width="100%" border="0" cellpadding="0" cellspacing="0"
				class="fhd_query_table" style="display:block;">
				<tr>  
					<th>员工姓名：</th>
					<td><input type="text" name="empname" id="empname" /></td>                                             
					<th>&nbsp;</th>
					<td>&nbsp;</td>                                             
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input type="button" onclick="queryData();" value="<spring:message code="fhd.common.search" />" class="fhd_btn" />
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<div id='divemps' class='fhd_selected_orgs' style='display:none'></div>
	<div id='containerDIV' style='height:600px' style='display:none'></div>
	<div id="datalist"></div>
</body>
</html>