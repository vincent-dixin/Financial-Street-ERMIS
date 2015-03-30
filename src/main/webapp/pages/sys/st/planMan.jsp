<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>计划任务列表</title>
<script type="text/javascript">
/***attribute start***/

Ext.Loader.setPath({
	'Ext.plan' : 'pages/sys/st'
});
Ext.require(['Ext.plan.PlanEditFind']);
Ext.require(['Ext.plan.PlanSelect']);

var planManPanel;
var grid,isAdd,formwindow,viewwindow;
var planAddUrl = 'pages/sys/st/planEdit.jsp';//保存url
var planDelUrl = 'sys/st/removePlanBatch.f'; //删除url
var planQueryUrl = 'sys/st/queryPlanPage.f'; //查询分页url
var planStatusUrl = 'sys/st/mergePlanBatch.f';//启用/停用url
var planViewUrl = 'sys/st/viewPlan.f';//查看url
var logGridUrl = 'pages/sys/st/planDealLogMan.jsp';
var planEditFind = null;
var planSelect = null;

	/***attribute start***/
	var gridColums =[
		{header:'',dataIndex: 'planEmpId', sortable: true, flex : 1,hidden : true},
		{header: FHD.locale.get('fhd.sys.planEdit.name') ,dataIndex: 'name', defaultSortable:true, sortable: true,flex : 1,
			renderer:function(value,metaData,record,colIndex,store,view) { 
				metaData.tdAttr = 'data-qtip="'+value+'" data-qwidth="'+100+'"'; 
	    		return "<a href=\"javascript:void(0);\" onclick=\"view('" + record.get('id') + "')\">"+value+"</a>";  
		}},
		{header: FHD.locale.get('fhd.sys.planMan.triggerType') ,dataIndex: 'triggerName',sortable: true,flex : 1},
		{header: FHD.locale.get('fhd.sys.planMan.triggerDataSet') ,dataIndex: 'triggerDateText', sortable: true, flex : 1},
		{header: FHD.locale.get('fhd.sys.planMan.task') ,dataIndex: 'zq', sortable: true, flex : 1},
		{header: FHD.locale.get('fhd.sys.planMan.operate') ,dataIndex: 'statusName', sortable: true, flex : 1,
			renderer:function(value,metaData,record,colIndex,store,view) { 
			if(record.get('status') == 1){
				//启用
				return "<a href=\"javascript:void(0);\" onclick='stop()'>" + 
						"<image src='images/icons/stop.gif' width='15' height='15'/></a>&nbsp;&nbsp;&nbsp;" + 
						"<a href=\"javascript:void(0);\" onclick=\"viewLog()\">" +
						"<image src='images/icons/table_go.png' width='15' height='15'/></a>";
			}else{
				//停用
				return "<a href=\"javascript:void(0);\" onclick='start()'>" + 
						"<image src='images/icons/start.gif' width='15' height='15'/></a>&nbsp;&nbsp;&nbsp;" + 
						"<a href=\"javascript:void(0);\" onclick=\"viewLog()\">" +
						"<image src='images/icons/table_go.png' width='15' height='15'/></a>";
						
			}
		}},
		{header: FHD.locale.get('fhd.sys.planMan.status') ,dataIndex: 'status', sortable: true, flex : 1,hidden : true}
	];
	var tbar =[//菜单项
				{text : FHD.locale.get('fhd.common.add'), iconCls: 'icon-add',id:'planMan_add',handler:edit, scope : this},'-',
				{text : FHD.locale.get('fhd.common.modify'), iconCls: 'icon-edit',id:'planMan_edit', handler:edit, disabled : true, scope : this},'-',
				{text : FHD.locale.get('fhd.common.del'), iconCls: 'icon-del',id:'planMan_del', handler:del, disabled : true, scope : this},'-',
				{text : FHD.locale.get('fhd.sys.planMan.start'), iconCls: 'icon-plan-start',id:'planMan_start', handler:start, disabled : true, scope : this},'-',
				{text : FHD.locale.get('fhd.sys.planMan.stop'), iconCls: 'icon-plan-stop',id:'planMan_stop', handler:stop, disabled : true, scope : this}
	];
	/***attribute end***/

	/***function start***/
	function edit(button){//新增方法
		var selection = grid.getSelectionModel().getSelection()[0];//得到选中的记录
		formwindow = new Ext.Window({
			layout:'fit',
			iconCls: 'icon-edit',//标题前的图片
			modal:true,//是否模态窗口
			collapsible:true,
			width:600,
			height:600,
			maximizable:true,//（是否增加最大化，默认没有）
			constrain:true,
			autoLoad:{url:planAddUrl,nocache:true,scripts:true}
		});
		formwindow.show();
		if(button.id=='planMan_add'){
			formwindow.setTitle(FHD.locale.get('fhd.common.add'));
			isAdd = true;
		}else{
			formwindow.setTitle(FHD.locale.get('fhd.common.modify'));
			formwindow.initialConfig.id = selection.get('id') + ',' + selection.data.planEmpId;
			isAdd = false;
		}
	} 

	function close(){
		viewwindow.close();
	}

	function del(){//删除方法
		var selection = grid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.common.delete'),
			width : 260,
			msg : FHD.locale.get('fhd.common.makeSureDelete'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
				if (btn == 'yes') {//确认删除
					var ids = [];
					
					for(var i=0;i<selection.length;i++){
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({//ajax调用
						url : planDelUrl,
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){//删除成功！
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								grid.store.load();
							}
						}
					});
				}
			}
		});
	}

	function start(){//启用
		var selection = grid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.sys.planMan.start'),
			width : 260,
			msg : FHD.locale.get('fhd.sys.planMan.isStart'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
			
				if (btn == 'yes') {
					
					var ids = [];
					for(var i=0;i<selection.length;i++){
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({//ajax调用
						url : planStatusUrl + '?status=1',
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								grid.store.load();
							}
						}
					});
				}
			}
		});
	}

	function stop(){//停用
		var selection = grid.getSelectionModel().getSelection();//得到选中的记录
		Ext.MessageBox.show({
			title : FHD.locale.get('fhd.sys.planMan.stop'),
			width : 260,
			msg : FHD.locale.get('fhd.sys.planMan.isStop'),
			buttons : Ext.MessageBox.YESNO,
			icon : Ext.MessageBox.QUESTION,
			fn : function(btn) {
			
				if (btn == 'yes') {
					var ids = [];
					for(var i=0;i<selection.length;i++){
						ids.push(selection[i].get('id'));
					}
					FHD.ajax({//ajax调用
						url : planStatusUrl + '?status=0',
						params : {
							ids:ids.join(',')
						},
						callback : function(data){
							if(data){
								Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'),FHD.locale.get('fhd.common.operateSuccess'));
								grid.store.load();
							}
						}
					});
				}
			}
		});
	}

	function viewLog(){
		var selection = grid.getSelectionModel().getSelection();//得到选中的记录
		
		formwindow = new Ext.Window({
			layout:'fit',
			iconCls: 'icon-table-go',//标题前的图片
			modal:true,//是否模态窗口
			collapsible:true,
			width:800,
			height:600,
			maximizable:true,//（是否增加最大化，默认没有）
			autoLoad:{url:logGridUrl,nocache:true,scripts:true}
		});
		formwindow.show();
		formwindow.setTitle(FHD.locale.get('fhd.sys.planDealLogMan.log'));
		formwindow.initialConfig.id = selection[0].data.planEmpId;
	}
	
	function view(id){
		FHD.ajax({//ajax调用
			url : planViewUrl,
			params : {
				id:id
			},
			callback : function(data){
				viewwindow = new Ext.Window({
					title:FHD.locale.get('fhd.sys.planMan.look'),
			    	layout:'fit',
					iconCls: 'icon-report',//标题前的图片
					modal:true,//是否模态窗口
					collapsible:true,
					width:450,
					height:350,
					maximizable:true,//（是否增加最大化，默认没有）
					items:[Ext.create('Ext.panel.Panel',{
	            		width: 300,
	                    bodyStyle: "padding:5px;font-size:12px;"
	            	})],
					listeners: {
		                afterlayout: function() {
							var panel = this.down('panel');
							var content = '';
							var content2 = '';
							var contentTime = '';
							if(data.triggerTypes == 'st_trigger_type_time'){
							  content = '<tr>' +
					  '<td class="alt">' + FHD.locale.get('fhd.sys.planMan.triggerType') + '：</td>' +
					  '<td>{triggerName}</td>' +
					  '</tr>' +
					  '<tr>' +
					    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.lookType') + '：</td>' +
					    '<td>{lookName}</td>'+
					    '</tr>' +
					  '<tr>' +
					    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.createBy') + '：</td>' +
					    '<td>{tempName}</td>' +
					  '</tr>';
						if(data.recycle){
							content2 =
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.triggerTime') + '：</td>' +
						    '<td>{triggetTime}</td>' +
						    '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.model') + '：</td>' +
						    '<td>{triggerDateText}</td>' +
						  '</tr>';

						  if(data.timeType == '1'){
							  contentTime = '<tr>' +
							    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.startTime') + '：</td>' +
							    '<td>{startTime}</td>' +
							  '</tr>' +
							  '<tr>' +
							    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.entTime') + '：</td>' +
							    '<td>{endTime}</td>' + '</tr>';
						  }else{
							  contentTime = '<tr>' +
							    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.periodScope') + '：</td>' +
							    '<td>' + FHD.locale.get('fhd.sys.planEdit.timeForever') + '</td>' + '</tr>';
						  }
						}else{
							content2 = '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.triggerData') + '：</td>' +
						    '<td>{triggerData}</td>' +
						    '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.triggerTime') + '：</td>' +
						    '<td>{triggetTime}</td>' +
						    '</tr>';
						    }
							}else{
								content = '<td class="alt">' + FHD.locale.get('fhd.sys.planMan.triggerType') + '：</td>' +
						  '<td>{triggerName}</td>' +
						  '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.lookType') + '：</td>' +
						    '<td>{lookName}</td>'+
						    '</tr>' +
						  '<tr>' +
						    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.createBy') + '：</td>' +
						    '<td>{tempName}</td>' +
						  '</tr>';
							}
							
			                tpl = Ext.create('Ext.Template', 
			                		'<fieldset class="x-fieldset x-fieldset-with-title x-fieldset-with-legend x-fieldset-default">',
									'<legend class="x-fieldset-header x-fieldset-header-default">' + FHD.locale.get('fhd.common.details') + '</legend>',
									'<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="0" class="fhd_add">',
									  '<tr >',
									    '<td class="alt" width="15%">' + FHD.locale.get('fhd.sys.planEdit.name') + '：</td>',
									    '<td width="35%">{name}</td>',
									  '</tr>' + content + content2 + contentTime,
									  '<tr>',
									    '<td class="alt">' + FHD.locale.get('fhd.sys.planEdit.status') + '：</td>',
									    '<td>{statusName}</td>',
									  '</tr>',
									 '</table>',
									 '</fieldset>'
			                );
			
			                tpl.overwrite(panel.body, data);
		                }
		            },
		            buttons:[
		            	{text: FHD.locale.get('fhd.common.cancel'),handler:close}
		            ]
			    }).show();
			}
		});
	}

	function setstatus(){//设置你按钮可用状态
		
		grid.down('#planMan_edit').setDisabled(grid.getSelectionModel().getSelection().length === 0);
		grid.down('#planMan_del').setDisabled(grid.getSelectionModel().getSelection().length === 0);
		grid.down('#planMan_start').setDisabled(grid.getSelectionModel().getSelection().length === 0);
		grid.down('#planMan_stop').setDisabled(grid.getSelectionModel().getSelection().length === 0);
	}
	/***function end***/
	
/***Ext.onReady start***/
Ext.onReady(function(){
	planEditFind = new Ext.plan.PlanEditFind();
	planSelect = new Ext.plan.PlanSelect();
	Ext.QuickTips.init(); 
	grid = Ext.create('FHD.ux.GridPanel',{//实例化一个grid列表
		url: planQueryUrl,//调用后台url
		region: 'center',
		cols:gridColums,//cols:为需要显示的列
		tbarItems:tbar,
		listeners:{itemdblclick:edit}//双击执行修改方法
	});
	grid.store.on('load',setstatus);
	grid.on('selectionchange',setstatus);
	
	planManPanel = Ext.create('Ext.panel.Panel',{
	    renderTo: 'planMan${param._dc}', 
	    height:FHD.getCenterPanelHeight(),
	    border:false,
		layout: {
	        type: 'fit'
	    },
	    items:[grid]
	});
	FHD.componentResize(planManPanel,0,0);//参数为：需要自适应的对象，需要减去的宽度，需要减去的高度
});

/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='planMan${param._dc}'></div>
</body>
</html>