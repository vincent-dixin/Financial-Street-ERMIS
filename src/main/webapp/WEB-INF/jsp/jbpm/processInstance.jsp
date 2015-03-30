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
//Ext初始化
    var hisDs;
	Ext.onReady(function(){
		Ext.QuickTips.init();

		//按钮栏
		var toolbar = new Ext.Toolbar( {
			height : 30,
			hideBorders : true,
			buttons : [{
				text : '刷新',
				handler : reflush,
				iconCls:'re'
			},{
				text : '子流程',
				handler : viewSub,
				iconCls:'sub'
			},{
				text : '启动',
				handler : startState,
				iconCls:'start'
			},{
				text : '暂停',
				handler : pauseState,
				iconCls:'pause'
			},{
				text : '查看流程图',
				handler : viewPic,
				iconCls:'link'
			},{
				text : '高级查询>>',
				handler : showAndHide,
				iconCls:'icon-zoom'
			},{
				text : '删除',
				handler : deleteState,
				iconCls:'icon-del'
			} ]
		});
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
		//列表表头
		var hisCol = new Ext.grid.ColumnModel([ 
			sm,
		  new Ext.grid.RowNumberer({
			  header : "序号",
			  width : 35,
			  renderer:function(value,metadata,record,rowIndex){
			   return record.store.lastOptions.params.start + 1 + rowIndex;
			  }
			 }), 
		   {id:'link1',header: "名称", dataIndex: 'businessName', sortable: true, width: 520},
		   {id:'btype',header: "类型", dataIndex: 'btype', sortable: true, width: 300},
		   {id:'activityName',header: "活动名称", dataIndex: 'activityNames', sortable: true, width: 193},
		   {id:'isSub',header: "是否子流程", dataIndex: 'isSub', sortable: true, width: 193,
			   renderer:function(v){
			  
			   return v?"是":"否";
			}},
//		   {id:'operater',header: "参与人", dataIndex: 'operater', sortable: true, width: 193},
		   {id:'state',header: "状态", dataIndex: 'state', sortable: true, width: 193}
		    ]); 

	    //取数据
		 var hisProxy = new Ext.data.HttpProxy({
			 method:'POST',
			 url:'${ctx}/jbpm/getProcessListToMenu.do'
			 });

	    //装载数据
		 var hisReader = new Ext.data.JsonReader({
				idProperty:'executionId',
				root:'datas',
				totalProperty:'totalCount',
				fields: ['id','activityNames','businessName','btype','createTime','index','imageLink','operate','operater','state','isSub']
			 });

		 //排序信息
		 /**
		 hisDs = new Ext.data.Store({
			 	autoDestroy: true,
		    	proxy: hisProxy,
		        reader: hisReader,
		        sortInfo:{field:'index', direction:'DESC'}  
			 }); 
        */
        
		//分组数据
		hisDs = new Ext.data.GroupingStore({
				autoDestroy: true,
		    	proxy: hisProxy,
		        reader: hisReader,
	            sortInfo:{field:'businessName', direction:'ASC'},
	            groupField:'btype'
	        });

		 //	 
		 var grid = new Ext.grid.GridPanel({
			    id:'hisEventGridId',
				el:'hisGridId',
				viewConfig: {forceFit:true},
				height:jQuery(window).height(),
				width:'auto',
				stripeRows:true,
				tbar:toolbar,
				ds:hisDs,
				loadMask:true,
				autoscroll:false,
				cm:hisCol,
				sm: sm,				
				bbar: new Ext.PagingToolbar({
		            pageSize: 20,
		            store: hisDs,
		            displayInfo: true, 
		            displayMsg: '显示{0} - {1}条 共{2}条记录', 
		            emptyMsg: "无记录显示"
		        }),
				view: new Ext.grid.GroupingView({
		            forceFit:true,//Ture表示自动扩展或缩小列的宽度以适应grid的宽度,从而避免出现水平滚动条
		            showGroupName: false,//是否在分组行上显示分组字段的名字，默认为true 
	                enableNoGroups:false,//是否允许用户关闭分组功能，默认为true 
	       			enableGroupingMenu:false,//是否在表头菜单中进行分组控制，默认为true 
	                hideGroupedColumn: true,//是否隐藏分组列，默认为false
	                startCollapsed:true,//初次显示时分组是否处于收缩状态，默认为false 
		            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
		        })		
			 }); 
		 
		grid.render();

		//
		hisDs.baseParams = {
					name: document.getElementById("name").value,
		  			type: document.getElementById("tasktype").value,		
		    		isSub: document.getElementById("isSub").value
		  			
		};
		
	    // 数据重新加载
	    hisDs.load({
	  	  params:{
	   		  start:0,
	   		  limit:20		    		
	   	  }
		});
		    
	});
	
	//刷新
	function reflush()
	{
		window.location.reload();
	}
	
	//条件查询
	function queryTask(){
		
	   hisDs.baseParams = {
	  			id: document.getElementById("id").value,
	  			name: document.getElementById("name").value,
	    		type: document.getElementById("tasktype").value,		
	    		isSub: document.getElementById("isSub").value
			};

	    // 数据重新加载
	    hisDs.load({
	    	params:{
		    		start:0,
		    		limit:20		    		
		    	}
			});
	}
	
	//查看子流程
	function viewSub(){
	
		
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		id= rows[0].get("id");
		
		isSub = rows[0].get("isSub");
		//alert("a"+isSub+"a");
		if(isSub == true)
		{
			Ext.MessageBox.alert('警告', '子流程业务无法再查看子流程！'); 
			return;
		}
		document.getElementById("isSub").value = "true";
		document.getElementById("id").value = id;
		hisDs.baseParams = {
					id: document.getElementById("id").value,
		  			name: document.getElementById("name").value,
		    		type: document.getElementById("tasktype").value,		
		    		isSub: document.getElementById("isSub").value
				};
	    // 数据重新加载
	    hisDs.load({
	    	params:{
		    		start:0,
		    		limit:20		    		
		    	}
			});
	}
	
	
	
	//查看流程图
	function viewPic()
	{
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		var link2=rows[0].get("imageLink");
		//alert(link2);
		eval(link2);
	}
	
	//打开或关闭高级查询
	function showAndHide(){
		var divShows = document.getElementById("hisEventDivId").style.display;
		if(divShows == 'none'){
			document.getElementById("hisEventDivId").style.display='';			
		}else{
			document.getElementById("hisEventDivId").style.display='none';
		}
	}
	
	//回车事件
	function keypup(){
		if(event.keyCode==13){
			queryTask();
			return false;
		}
	}
    
    //重新加载extgird
    function reloadStore(){
    	hisDs.reload();
    }
    
    //删除流程定义
    function deleteState(){
    
    	var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}else{ 
			Ext.MessageBox.confirm('提示框', '您确定要删除？',function(btn){ 
				if(btn=='yes'){
					if(rows){
						id= rows[0].get("id");
						$.ajax({
							type: "POST",
							async: false,
							url: "${ctx}/jbpm/deleteProcessInstance.do",
							data: "id="+id,
							success: function(msg){
								if("true"==msg){
									queryTask();
								}else{
									Ext.MessageBox.alert("<spring:message code="fhd.common.operateFailure"/>");
								}
							}
						});
					}
				}
			});
		}
	}
	
	//使流程实例暂停
	function pauseState(){
    
    	var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		id= rows[0].get("id");
		$.ajax({
			type: "POST",
			async: false,
			url: "${ctx}/jbpm/pausePorcessInstance.do",
			data: "id="+id,
			success: function(msg){
				if("true"==msg){
					queryTask();
				}else{
					Ext.MessageBox.alert("<spring:message code="fhd.common.operateFailure"/>");
				}
			}
		});
		
	}
	
	//使流程实例运行
	function startState(){
    
    	var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行操作!'); 
			return;
		}
		id= rows[0].get("id");
		$.ajax({
			type: "POST",
			async: false,
			url: "${ctx}/jbpm/startPorcessInstance.do",
			data: "id="+id,
			success: function(msg){
				if("true"==msg){
					queryTask();
				}else{
					Ext.MessageBox.alert("<spring:message code="fhd.common.operateFailure"/>");
				}
			}
		});
		
	}

</script>
<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/style.css"/> 
<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/table.css"/> 
</head>
<body onload="showAndHide();">
	<div id="hisEventDivId" style="height:6px; background:#f9f9f9;">
		<form id="riskFormId" onkeypress="return keypup();" action="${ctx}/jbpm/getTaskListToMenu.do" method="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr><td class="fhd_query_title">&nbsp;<img src="${ctx}/images/plus.gif"  onclick="showAndHide();" width="15" height="15" />
				<spring:message code="fhd.common.querycondition"></spring:message>
				</td></tr>
			</table>
			<input type="hidden" id="id" name="id" value="${param.id}" style="width:150px"/>
			<table id="hisEventTable" border="0" width="100%" cellpadding="0" cellspacing="0"
					class="fhd_query_table" style="display:block;" >
					<tr>
						<th>名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：</th>
						<td><input type="text" id="name" name="name" value="${param.name}" style="width:150px"/></td>
						<th>流程实例类型：</th>
						<td>
							<select style="width:160;" id="tasktype" name="type" >
					    		<option value="all"				>所有</option>
					    		<option	value="risk"			>风险</option>
					    		<option value="riskhistory"		>风险案例</option>
					    		<option	value="riskevent"		>风险事件</option>
					    		<option value="question"		>评估问卷</option>
					    		<option value="kpi_plan"		>监控计划</option>
					    		<option value="kpi_target"		>指标采集</option>
					    	</select>
						</td>
		
		            </tr>
		            <tr>
						<th>子流程：</th>
						<td colspan="3">
							<select style="width:150px;" id="isSub" name="isSub" >
					    		<option value="false"			>否</option>
					    		<option	value="true"			>是</option>					    		
					    	</select>
					    </td>
					    
						
		
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

