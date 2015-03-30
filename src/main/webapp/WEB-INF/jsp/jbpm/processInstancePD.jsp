<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx }/css/extform.css" />
<title>个人工作列表</title>
<script type="text/javascript">

    var hisDs;
	Ext.onReady(function(){
		Ext.QuickTips.init();
		var toolbar = new Ext.Toolbar( {
			height : 30,
			hideBorders : true,
			buttons : [{
				text : '刷新',
				handler : reflush,
				iconCls:'re'
			},{
				text : '查看流程图',
				handler : viewPic,
				iconCls:'link'
			},{
				text : '删除',
				handler : deleteState,
				iconCls:'icon-del'
			} ]
		});
		var hisCol = new Ext.grid.ColumnModel([ 
		  new Ext.grid.RowNumberer({
			  header : "序号",
			  width : 35,
			  renderer:function(value,metadata,record,rowIndex){
			   return record.store.lastOptions.params.start + 1 + rowIndex;
			  }
			 }), 
		   {id:'link1',header: "名称", dataIndex: 'businessName', sortable: true, width: 820},
		   {id:'activityName',header: "活动名称", dataIndex: 'activityNames', sortable: true, width: 193},
		   {id:'isSub',header: "是否子流程", dataIndex: 'isSub', sortable: true, width: 193,
			   renderer:function(v){
			  
			   return v?"是":"否";
			}},
		   {id:'operater',header: "参与人", dataIndex: 'operater', sortable: true, width: 193},
		   {id:'state',header: "状态", dataIndex: 'state', sortable: true, width: 193}
		    ]); 
		 var hisProxy = new Ext.data.HttpProxy({
			 method:'POST',
			 url:'${ctx}/jbpm/getProcessListToMenuPD.do?id=${param.id}'
			 });
		 var hisReader = new Ext.data.JsonReader({
				idProperty:'executionId',
				root:'datas',
				totalProperty:'totalCount',
				fields: ['id','activityNames','businessName','createTime','index','imageLink','operate','operater','state','isSub']
			 });
		 hisDs = new Ext.data.Store({
			 	autoDestroy: true,
		    	proxy: hisProxy,
		        reader: hisReader,
		        sortInfo:{field:'businessName', direction:'DESC'}    // 排序信息 
			 }); 
			 
		 var grid = new Ext.grid.GridPanel({
			 	id:'hisEventGridId',
				el:'hisGridId',viewConfig: {forceFit:true},
				height:jQuery(window).height(),
				width:'auto',
				stripeRows:true,
				tbar:toolbar,
				ds:hisDs,
				autoscroll:false,
				loadMask:true,
				cm:hisCol,
				bbar: new Ext.PagingToolbar({
		            pageSize: 20,
		            store: hisDs,
		            displayInfo: true, 
		            displayMsg: '显示{0} - {1}条 共{2}条记录', 
		            emptyMsg: "无记录显示"
		        })			
			 }); 
		 grid.render();
		
	  // 数据重新加载
	  hisDs.load({
	  	params:{
	   		start:0,
	   		limit:20		    		
	   	}
		});
		    
	});
	//导出数据
	function exportData(){
		
	}
	
	function reflush()
	{
		window.location.reload();
	}
	
	function queryTask(){
		
		 
	    // 数据重新加载
	    hisDs.load({
	    	params:{
		    		start:0,
		    		limit:20		    		
		    	}
			});
	}
	
	
	function viewSub(){
	
		
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
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
		
		//document.getElementById("id").value = id;
		
	    // 数据重新加载
	    hisDs.load({
	    	params:{
		    		start:0,
		    		limit:20		    		
		    	}
			});
	}
	
	
	
	
	function viewPic()
	{
		var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
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
	
	function keypup(){
		if(event.keyCode==13){
			queryTask();
			return false;
		}
	}
    
    function reloadStore(){
    	hisDs.reload();
    }
    
    function deleteState(){
    
    	var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
			return;
		}
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
	
	function pauseState(){
    
    	var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
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
	function startState(){
    
    	var rows =Ext.getCmp('hisEventGridId').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('警告', '最少选择一条信息，进行修改!'); 
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
	</div>
	<div id="hisGridId" style="overflow:auto;width:100%;">
	</div>
</body>
</html>

