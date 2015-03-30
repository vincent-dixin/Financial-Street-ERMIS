<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/includes.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>index</title>
</head>
<body> 
	<div id="pd" style="overflow:auto;width:100%;"></div>
	<div id="hisGridId" style="overflow:auto;width:100%;"></div>
</body>
	<script type="text/javascript">
		var grid;
		//Ext初始化（未部署的流程定义）
		Ext.onReady(function(){
		var toolbar = new Ext.Toolbar( {
			height : 30,
			hideBorders : true,
			buttons : [{
				text : '刷新',
				handler : reflush,
				iconCls:'x-tbar-loading'
			},{
				text : '上传',
				handler : upload,
				iconCls:'icon-folder-upload'
			},{
				text : '修改',
				handler : update,
				iconCls:'icon-edit'
			},{
				text : '删除',
				handler : del,
				iconCls:'icon-del'
			}]
		});
	    var cm = new Ext.grid.ColumnModel([
	         new Ext.grid.RowNumberer({
				  header : "序号",
				  width : 35,
				  renderer:function(value,metadata,record,rowIndex){
				   return record.store.lastOptions.params.start + 1 + rowIndex;
				  }
				 }),         
	        {header:'名称',dataIndex:'id',sortable: true, width: 300},
	        {header:'名称',dataIndex:'disName',sortable: true, width: 300},
	        {header:'操作',dataIndex:'operate', sortable: true,width: 200}
	    ]);
	    
	    //-----------------------------------------pd-------------------------------------------
	    var hisProxy = new Ext.data.HttpProxy({
			method:'POST',
			url:'${ctx}/jbpm/processDefinitionDeploy/processDefinitionDeployPage.f'
		});
				 
				
		var hisReader = new Ext.data.JsonReader({
			idProperty:'id',
			root:'datas',
			totalProperty:'totalCount',
			fields: ['id','disName']
		});
		hisDs = new Ext.data.Store({
		 	autoDestroy: true,
	    	proxy: hisProxy,
	        reader: hisReader,
	        sortInfo:{field:'id', direction:'DESC'}    // 排序信息 
		}); 
	
		var grid = new Ext.grid.GridPanel({
		 	id:'pddd',
			el:'pd',viewConfig: {forceFit:true},
			height:200,
			width:document.body.offsetWidth-5,
			stripeRows:true,
			ds:hisDs,
			title:'流程定义库',
			autoscroll:false,
			loadMask:true,
			tbar:toolbar,
			cm:cm,
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
    //-----------------------------------------pd-------------------------------------------
	});

	//Ext初始化（部署的流程定义）
	var hisDs;
	Ext.onReady(function(){
		Ext.QuickTips.init();
		
		var hisCol = new Ext.grid.ColumnModel([ 
			new Ext.grid.RowNumberer({
				header : "序号",
			  	width : 35,
			  	renderer:function(value,metadata,record,rowIndex){
			   		return record.store.lastOptions.params.start + 1 + rowIndex;
			  	}
			}), 
		   {id:'id',header: "id", dataIndex: 'id', sortable: true, width: 150},
		   {id:'disName',header: "流程定义名称", dataIndex: 'disName', sortable: true, width: 300},
		   {id:'pdid',header: "流程ID", dataIndex: 'pdid', sortable: true, width: 100},
		   {id:'executionCount',header: "流程实例数量", dataIndex: 'executionCount', sortable: true, width: 100},
		   {id:'pdversion',header: "版本号", dataIndex: 'pdversion', sortable: true, width: 100},
		   {id:'operate',header: "操作", dataIndex: 'operate', sortable: true, width: 250}
		]); 
		var hisProxy = new Ext.data.HttpProxy({
			method:'POST',
			url:'${ctx}/jbpm/processDefinitionDeploy/vJbpmDeploymentPage.f'
		});
		var hisReader = new Ext.data.JsonReader({
			idProperty:'id',
			root:'datas',
			totalProperty:'totalCount',
			fields: ['id','disName','pdid','executionCount','pdversion']
		});
		hisDs = new Ext.data.Store({
			autoDestroy: true,
		    proxy: hisProxy,
		    reader: hisReader,
		    sortInfo:{field:'id', direction:'DESC'}    // 排序信息 
		}); 
			 
		var grid = new Ext.grid.GridPanel({
		 	id:'hisEventGridId',
			el:'hisGridId',viewConfig: {forceFit:true},
			height:jQuery(window).height()-200,
			width:'auto',
			stripeRows:true,
			ds:hisDs,
			title:'部署的流程',
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
	
	//刷新
	function reflush(){
		window.location.reload();
	}
	
	//上传
	function upload(){
		openWindow('导入流程', 680, 440, '${ctx}/jbpm/processUpload.do');
	}
	
	
	//修改
	function update(){
		var rows =Ext.getCmp('pddd').getSelectionModel().getSelections(); 
		if(rows.length==0){
			Ext.MessageBox.alert('提示', '最少选择一条信息，进行操作!'); 
			return;
		}
		id= rows[0].get("id");
		openWindow('修改流程', 680, 440, '${ctx}/jbpm/processUpload.do?id='+id);
	}


	//删除
	function del() {
		var rows =Ext.getCmp('pddd').getSelectionModel().getSelections(); 		
		if(rows.length==0){
			Ext.MessageBox.alert('提示', '最少选择一条信息，进行操作!'); 
			return false;
		}	
		id= rows[0].get("id");
		Ext.MessageBox.confirm('确定', '确定要删除所选记录?', function(btn) {
			if (btn == 'yes') {
				doDelete(id);
			}
		});
	}

	function doDelete(id) {
		var msgTip = FHD.opWait();
		var options = {
			url:'${ctx}/jbpm/processDelete.do?id='+id,
			type:'POST',
			success:function(data) {
	     		msgTip.hide();
	         	if("true" == data){
	         		Ext.MessageBox.alert('提示', '操作成功.',function refush(){reloadStore1();});
	         }else
	         	Ext.MessageBox.alert('提示', '请求失败！');
	     	}
		};
		$('#processUploadForm').ajaxSubmit(options);
		return false;		
	}
	
	//删除流程定义
	function delPd(url1){
		var url1= '${ctx}/jbpm/'+url1;
		 Ext.MessageBox.confirm('警告','确定删除该流程定义吗？',function () 
			{
			var msgTip = FHD.opWait();
				//document.forms[0].action = url1;
				//document.forms[0].submit();
				
				var options = {
				url:url1,
				type:'POST',
				success:function(data) {
		     		msgTip.hide();
		         	if("true" == data){
		         		Ext.MessageBox.alert('提示', '操作成功.',function refush(){reloadStore();});
		         }else
		         	Ext.MessageBox.alert('提示', '请求失败！');
		     	}
			};
			$('#processUploadForm').ajaxSubmit(options);
		return false;
				
			}
		);
		
	}
	
    //gird 重新加载
    function reloadStore(){
    	hisDs.reload();
    }

    function reloadStore1(){

    	window.location.reload();

    	/*
    	Ext.getCmp(id).grid.getStore().reload({
    		  	params:{
    	   		start:0,
    	   		limit:20		    		
    	   	}
    	});
    	*/
    }
</script>
</html>
