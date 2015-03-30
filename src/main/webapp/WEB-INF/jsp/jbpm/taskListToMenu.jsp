<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
<title>待办工作</title>
<script type="text/javascript">

    var hisDs;
    var grid;
	Ext.onReady(function(){
		Ext.QuickTips.init();
		var toolbar = new Ext.Toolbar( {
			height: 30,
			hideBorders: true,
			buttons: [
					{
					id:'adquery',
					text : '查询',
					handler : showAndHide,
					iconCls:'icon-zoom'
				}
			]
		});
		var hisCol = new Ext.grid.ColumnModel([                        			
		  new Ext.grid.RowNumberer({			  
			  header : "序",
			  width : 20,
			  renderer:function(value,metadata,record,rowIndex){
			   return record.store.lastOptions.params.start + 1 + rowIndex;
			  }
			 }), 

//		   {id:'link1',header: "操作",xtype: 'actioncolumn',dataIndex: 'fileId',width: 100,renderer:function(value){return '执行';	}},
		   
		   {id:'businessName',header: "名称", dataIndex: 'businessName', sortable: true, width: 400},
		   {id:'businessType',header: "业务类型", dataIndex: 'businessType', sortable: true, width: 200},
		   {id:'activityName',header: "状态", dataIndex: 'beforActivityName', sortable: true, width: 200},
		   {id:'createTime',header: "到达时间", dataIndex: 'createTime', sortable: true, width: 100},
		   {id:'operate',header: "操作", dataIndex: 'url', width: 100,align:'center',
			   renderer:function(value,metadata,record){
				   return '<a href="#" onclick="javascript:FHD.openWindowSimple(\''+ record.get("businessName") +'\',\''+ value + 
						   '?piid=' + record.get("executionId") + '&bsid=' + record.get("businessId") +'\',true);">执行</a>';	
				}
		   }
		    ]); 
		var conn=new Ext.data.Connection({  
		        url: '${ctx}/jbpm/getTaskListToMenu.do',  
		        timeout : 600000, //自定义超时时间，这里是600秒 (默认30s)  
		        autoAbort : false,  
		        disableCaching : true ,  
		        method : "POST"  
		    });  

		
		 var hisProxy = new Ext.data.HttpProxy(conn);
		 var hisReader = new Ext.data.JsonReader({
				idProperty:'executionId',
				root:'datas',
				totalProperty:'totalCount',
				fields: ['executionId','activityName','businessName','createTime','index',
				         'link1','link2','businessType','id','isXb','operate','beforActivityName',
				         'url','businessId']
			 });

		 //分组数据
		 hisDs = new Ext.data.GroupingStore({
				autoDestroy: true,
		    	proxy: hisProxy,
		        reader: hisReader,
	            sortInfo:{field:'createTime', direction:'DESC'},
	            groupField:'businessType'
	        });

		 
			 
		 grid = new Ext.grid.GridPanel({
			 	id:'hisEventGridId',
				el:'hisGridId',viewConfig: {forceFit:true},
				height:jQuery(window).height(),
				width:'',
				stripeRows:true,
				tbar:toolbar,
				ds:hisDs,
				autoscroll:false,
				cm:hisCol,
				loadMask:true,
				bbar: new Ext.PagingToolbar({
		            pageSize: 20,
		            store: hisDs,
		            displayInfo: true, 
		            displayMsg: '显示{0} - {1}条 共{2}条记录', 
		            emptyMsg: "无记录显示"
		        }),
		        view:new Ext.grid.GroupingView({
		            forceFit:true,//Ture表示自动扩展或缩小列的宽度以适应grid的宽度,从而避免出现水平滚动条
		            showGroupName: false,//是否在分组行上显示分组字段的名字，默认为true 
	                enableNoGroups:false,//是否允许用户关闭分组功能，默认为true 
	       			enableGroupingMenu:false,//是否在表头菜单中进行分组控制，默认为true 
	                hideGroupedColumn: true,//是否隐藏分组列，默认为false 
	                startCollapsed:false,//初次显示时分组是否处于收缩状态，默认为false 
		            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "项" : "项"]})'
		        })					
			 }); 
		 	grid.render();
		 	hisDs.baseParams = { name: document.getElementById("name").value, type: document.getElementById("tasktype").value};
			// 数据重新加载
		  	hisDs.load({
				params:{
			 		start:0,
			 		limit:20		    		
			 	}
		  	});
		  
	});
		
	function commission(){
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		var taskId=rows[0].get("id");
		var executionId=rows[0].get("executionId");
		var isXb = rows[0].get("isXb");
		if(	!isXb)
		{
			openWindow('代办/协办',800,150,'jbpm/toCommission.do?taskId='+taskId+"&processInstanceId="+executionId);
		}
		else
		{
			Ext.MessageBox.alert('警告','当前协办状态，不能再次代办或协办！');
		}
	}
	//导出数据
	function exportData(){
		
	}
	
	function reflush()
	{
		window.location.reload();
	}
	
	function queryTask(){
		
		  hisDs.baseParams = {
		  			name: document.getElementById("name").value,
		    		type: document.getElementById("tasktype").value	
		    		
				};
	    // 数据重新加载
	    hisDs.load({
	    	params:{
		    		start:0,
		    		limit:20		    		
		    	}
			});
	}
	
	
	function operateTask() {
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		var link1=rows[0].get("link1");
		eval(link1);
	}
	
	function viewPic()
	{
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){	dq
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		var link2=rows[0].get("link2");
		//alert(link2);
		eval(link2);
	}
	
	//打开或关闭高级查询
	function showAndHide(){
		var divShows = document.getElementById("hisEventDivId").style.display;
		if(divShows == 'none'){
			document.getElementById("hisEventDivId").style.display='';	
			grid.getTopToolbar().get("adquery").setText("隐藏");
			var divHeight=Ext.get("hisEventDivId").getHeight();
			grid.setHeight(grid.getHeight()+divHeight);
			
		}else{
			document.getElementById("hisEventDivId").style.display='none';
			grid.getTopToolbar().get("adquery").setText("查询");
			var divHeight=Ext.get("hisEventDivId").getHeight();
			grid.setHeight(grid.getHeight()-divHeight);
		}
	}
	
	function keypup(){
		if(event.keyCode==13){
			queryTask();
			return false;
		}
	}
    
    function reloadStore(){
    	hisDs.reload();
    }

    </script>
<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/style.css"/> 
<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/table.css"/> 
</head>
<body>
	<div id="hisEventDivId" style="background:#f9f9f9;display: none;">
		<form id="riskFormId" onkeypress="return keypup();" action="${ctx}/jbpm/getTaskListToMenu.do" method="post">
			<input type="hidden" id="tasktype" value="all" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" />
				<spring:message code="fhd.common.querycondition"></spring:message>
				</td></tr>
			</table>
			<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0"
					class="fhd_query_table" style="display:block;" >
					<tr>
						<th>名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</th>
						<td><input type="text" id="name" name="name" value="${param.name}" style="width:350px"/></td>
					<!-- 
				 		<th>代办任务类型：</th>
						<td>
							<select style="width:160;" id="tasktype" name="type" >
					    		<option value="all"				>所有</option>
					    		<option	value="risk"			>风险</option>
					    		<option value="riskhistory"		>风险案例</option>
					    		<option	value="riskevent"		>风险事件</option>
					    		<option value="question"		>问卷</option>
					    		<option value="kpi_plan"		>监控计划</option>
					    		<option value="kpi_target"		>指标采集</option>
					    	</select>
						</td>
		 			 -->
		            </tr>
					<tr>
						<td colspan="4" align="center">
							<input type="button" onclick="queryTask();" value="<spring:message code="fhd.common.query" />" class="fhd_btn" />
						</td>
						
					</tr>
			</table>
		</form>
	</div>
	<div id="hisGridId" style="overflow:auto;width:100%;">
	</div>	
</body>
</html>

