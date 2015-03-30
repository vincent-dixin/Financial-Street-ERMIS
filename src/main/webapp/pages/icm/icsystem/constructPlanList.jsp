<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
	/***attribute start***/

 Ext.define('icsystem_constructPlanPanel',{
    	extend:'FHD.ux.GridPanel',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	constructPlanID:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    		var me=this;
    	me.tbarItems=[{iconCls : 'icon-add',id : 'add${param._dc}',handler :me.addConstructPlan,scope : this}, 
    	    	      {iconCls : 'icon-del',id : 'del${param._dc}',handler :me.delConstructPlan,scope : this}
    	    	     ];
    	me.cols=[{header : 'ID',dataIndex : 'id',hidden:true,sortable : true},
    	         {header : '编号',dataIndex : 'code',sortable : true}, 
 				{header : '名称',dataIndex : 'name',sortable : true,flex : 1}, 
 				{header : '更新依据',dataIndex :'controlRequirement',sortable : true,flex : 1}, 
 				{header : '计划日期',dataIndex : 'planStartDate',sortable : true}, 
 				{header : '状态',dataIndex : 'dealStatus',sortable : true},
				{header : '更新时间',dataIndex : 'lastModifyTime',sortable : true},
				{
					header: '操作',dataIndex:'',width:60,hideable:false, 
					align: 'center',
					xtype:'actioncolumn',
					items: [{
						icon: __ctxPath+'/images/icons/edit.gif',  // Use a URL in the icon config
						tooltip:'编辑',
						handler: function(grid, rowIndex, colIndex) {
							grid.getSelectionModel().deselectAll();
							var rows=[grid.getStore().getAt(rowIndex)];
			 	    		grid.getSelectionModel().select(rows,true);
							var rec = grid.getStore().getAt(rowIndex);
							constructPlan_ManagerView.remove(constructPlan_ManagerView.gridPanel);
				    		constructPlan_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
								autoLoad : {
									url : 'pages/icm/icsystem/constructPlanEdit.jsp?id='+rec.data.id,
									scripts : true
								}
							});
				    		constructPlan_ManagerView.add(constructPlan_ManagerView.gridPanel);
						}
					}]
		      }];
    	
    	me.callParent(arguments);
    	},
    	
    	
    	//添加 的
    	addConstructPlan:function(){
    		constructPlan_ManagerView.remove(constructPlan_ManagerView.gridPanel);
    		constructPlan_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
				autoLoad : {
					url : 'pages/icm/icsystem/constructPlanEdit.jsp',

					scripts : true
				}
			});
    		constructPlan_ManagerView.add(constructPlan_ManagerView.gridPanel);
    	},
    	//编辑
        editConstructPlan:function(){
        },
        //删除
        delConstructPlan:function(){
        	var me=this;
        	var selection = me.getSelectionModel().getSelection();//得到选中的记录
    		Ext.MessageBox.show({
    			title : FHD.locale.get('fhd.common.delete'),
    			width : 260,
    			msg : FHD.locale.get('fhd.common.makeSureDelete'),
    			buttons : Ext.MessageBox.YESNO,
    			icon : Ext.MessageBox.QUESTION,
    			fn : function(btn) {
    		     if (btn == 'yes') {//确认删除
    			 var ids = [];
    			 for ( var i = 0; i < selection.length; i++) {
    			  ids.push(selection[i].get('id'));
    			 }
    			 FHD.ajax({//ajax调用
    		     url : __ctxPath+ '/icsystem/constructPlan/removeconstructPlans.do',
    		     params : {
    		     constructPlanID : ids
    			 },
    			 callback : function(data) {
    			 if (data) {//删除成功！
    			 Ext.ux.Toast.msg(FHD.locale.get('fhd.common.prompt'), FHD.locale.get('fhd.common.operateSuccess'));
    			 me.store.load();
    		   }
    		   }
    		});
    		}
    			}
    		});
        	
        	
        },
    });
    
     var icsystem_constructPlanPanel=null;
	Ext.onReady(function() {
	Ext.QuickTips.init();
	icsystem_constructPlanPanel= Ext.create('icsystem_constructPlanPanel',{
			renderTo: 'FHD.icm.icsystem.constructPlanList${param._dc}',
		    height : FHD.getCenterPanelHeight() - 3,//高度为：获取center-panel的高度
			listeners : {
			}
		});
	icsystem_constructPlanPanel.store.proxy.url=__ctxPath+ '/icsystem/findConstructPlanListBypage.f',//调用后台url
	icsystem_constructPlanPanel.store.load();
	icsystem_constructPlanPanel.store.on("load",function(store){
  	    Ext.getCmp('del${param._dc}').setDisabled(true);
	 });
	icsystem_constructPlanPanel.on('selectionchange',function(m) {
	var len=icsystem_constructPlanPanel.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	   Ext.getCmp('del${param._dc}').setDisabled(true);
	   } else if (len == 1) {
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   } else if (len > 1) {
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   }
	 });	 
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.icsystem.constructPlanList${param._dc}'></div>
</body>
</html>