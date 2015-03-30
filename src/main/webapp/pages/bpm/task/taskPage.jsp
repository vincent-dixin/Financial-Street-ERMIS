<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctx}/css/extform.css"/>
	<title>FHD.locale.get('fhd.bpm.task.taskPage.title')</title>
	<script type="text/javascript">
	    var grid;
	    var queryUrl = 'jbpm/getTaskListToMenu.do';
		var gridColums =[
			{dataIndex: 'executionId', sortable: true, width: 0,flex : 0},
			{dataIndex: 'businessId', sortable: true, width: 0,flex : 0},
			{dataIndex: 'url', sortable: true, width: 0,flex : 0},
			{header: FHD.locale.get('fhd.pages.test.field.name'), dataIndex: 'businessName', sortable: true, width: 60,flex : 1},
			{header: FHD.locale.get('fhd.bpm.task.taskPage.businessType'), dataIndex: 'businessType', sortable: true, width: 60,flex : 1},
			{header: FHD.locale.get('fhd.sys.planEdit.status'), dataIndex: 'beforActivityName', sortable: true, width: 60,flex : 1},
			{header: FHD.locale.get('fhd.bpm.task.taskPage.createTime'), dataIndex: 'createTime', sortable: true, width: 60,flex : 1},
			{header: FHD.locale.get('fhd.common.operate'), dataIndex: 'operate', sortable: true, width: 60,flex : 1,renderer:function(value,metadata,record){
				return "<a href='javascript:void(0);' onclick='execute(\""+record.get("url")+"\",\""+record.get("executionId")+"\",\""+record.get("businessId")+"\");'>执行</a>";
			}}
		];
		var tbar =[//菜单项
		];
	    function reloadStore(){
	    	grid.store.load();
	    }
		function execute(url,executionId,businessId){
			var winId = "win" + Math.random()+"$ewin";
			var formwindow = new Ext.Window({
				id:winId,
				title:FHD.locale.get('fhd.common.execute'),
				constrain:true,
				layout:'fit',
				iconCls: 'icon-edit',//标题前的图片
				modal:true,//是否模态窗口
				collapsible:true,
				scroll:'auto',
				width:800,
				height:550,
				maximizable:true,//（是否增加最大化，默认没有）
				autoLoad:{ url: url+"?executionId="+executionId+"&businessId="+businessId+"&winId="+winId,nocache:true,scripts:true},
				listeners:{
					close : function(){
						reloadStore();
					}
				}
			});
			formwindow.show();
		}

		
		function setStatus(){//设置你按钮可用状态
		}
		Ext.onReady(function(){
			Ext.QuickTips.init(); 
			grid = Ext.create('FHD.ux.GroupGridPanel',{//实例化一个grid列表
				renderTo: 'hisGridId', //渲染到id为demo的div里
				url: queryUrl,//调用后台url
				height:FHD.getCenterPanelHeight(),//高度为：获取center-panel的高度
				cols:gridColums,//cols:为需要显示的列
				tbarItems:tbar,
				storeGroupField:'businessType'
			});
			grid.store.on('load',setStatus);
			grid.on('selectionchange',setStatus);
			FHD.componentResize(grid,0,0);
		});
    </script>
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/style.css"/> 
	<link rel="stylesheet" type="text/css" media="all" href="${ctx}/css/table.css"/> 
</head>
<body>
	<div id="hisGridId" style="overflow:auto;width:100%;">
	</div>	
</body>
</html>

