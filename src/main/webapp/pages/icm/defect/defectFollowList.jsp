<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
	/***attribute start***/

 Ext.define('defectFollow_defectPanel',{
    	extend:'FHD.ux.GridPanel',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	defectIds:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    		var me=this;
    	me.tbarItems=[//{iconCls : 'icon-add',id : 'add${param._dc}',handler :me.addDefect,scope : this}, 
    	    	      {iconCls : 'icon-edit',id : 'upd${param._dc}',handler :me.editDefect,scope : this}, 
    	    	      {iconCls : 'icon-del',id : 'del${param._dc}',handler :me.delDefect,scope : this}
    	    	     ];
    	me.cols=[ {header : 'ID',dataIndex : 'id',hidden:true,sortable : true},
                  {header : '缺陷描述',dataIndex : 'defect',sortable : true,flex : 1}, 
                  {header : '整改计划',dataIndex : 'improve',sortable : true,flex : 1},
                  {header : '整改结果',dataIndex : 'improveResult',sortable : true,flex : 1}, 
                  {header : '整改测试',dataIndex :'improveTest',sortable : true,}, 
                  {header : '测试分析',dataIndex : 'testAnalyze',sortable : true}
                  ];
    	
    	me.callParent(arguments);
    	},
    	
 /*    	
    	//添加 的
    	addDefect:function(){
    	 	//选择编辑评价计划，首先初始化form               	  
	     	 var selection = this.getSelectionModel().getSelection();//得到选中的记录
	     	 var defectRelaImproveId=selection[0].get('id');
    		defectFollow_ManagerView.remove(defectFollow_ManagerView.gridPanel);
    		defectFollow_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
				autoLoad : {
					url : 'pages/icm/defect/defectFollowEdit.jsp?defectRelaImproveId='+defectRelaImproveId,

					scripts : true
				}
			});
    		defectFollow_ManagerView.add(defectFollow_ManagerView.gridPanel);
    	}, */
    	//编辑
        editDefect:function(){
        	//选择编辑评价计划，首先初始化form               	  
	     	 var selection = this.getSelectionModel().getSelection();//得到选中的记录
	     	 var defectRelaImproveId=selection[0].get('id');
   		defectFollow_ManagerView.remove(defectFollow_ManagerView.gridPanel);
   		defectFollow_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
				autoLoad : {
					url : 'pages/icm/defect/defectFollowEdit.jsp?defectRelaImproveId='+defectRelaImproveId,

					scripts : true
				}
			});
   		defectFollow_ManagerView.add(defectFollow_ManagerView.gridPanel);
        	
        },
        //删除
        delDefect:function(){
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
    		     url : __ctxPath+ '/icm/defect/removeDefectFollowByIdBatch.f',
    		     params : {
    		     defectIds : ids
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
    
     var defectFollow_defectPanel=null;
	Ext.onReady(function() {
	Ext.QuickTips.init();
	defectFollow_defectPanel= Ext.create('defectFollow_defectPanel',{
			renderTo: 'FHD.icm.defect.defectFollowList${param._dc}',
		    height : FHD.getCenterPanelHeight() - 3,//高度为：获取center-panel的高度
			listeners : {
			}
		});
	defectFollow_defectPanel.store.proxy.url=__ctxPath+ '/icm/defect/findDefectFollowListBypage.f?defectId=79fac09f-3ec4-40e0-9c83-197158a6d60a&improveId=b24c10ab-2e5d-470f-923f-03cc6b321430',//调用后台url
	defectFollow_defectPanel.store.load();
	defectFollow_defectPanel.store.on("load",function(store){
	    Ext.getCmp('upd${param._dc}').setDisabled(true);//编辑不可用
  	    Ext.getCmp('del${param._dc}').setDisabled(true);
	 });
	defectFollow_defectPanel.on('selectionchange',function(m) {
	var len=defectFollow_defectPanel.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	   Ext.getCmp('upd${param._dc}').setDisabled(true);//编辑不可用
	   Ext.getCmp('del${param._dc}').setDisabled(true);
	   } else if (len == 1) {
	   Ext.getCmp('upd${param._dc}').setDisabled(false);//编辑可用
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   } else if (len > 1) {
	   Ext.getCmp('upd${param._dc}').setDisabled(true);//编辑不可用
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   }
	 });	 
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.defect.defectFollowList${param._dc}'></div>
</body>
</html>