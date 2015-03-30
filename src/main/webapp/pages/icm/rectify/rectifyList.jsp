<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
	/***attribute start***/

 Ext.define('rectify_rectifyPanel',{
    	extend:'FHD.ux.GridPanel',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	improveIds:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    		var me=this;
    	me.tbarItems=[{iconCls : 'icon-add',id : 'add${param._dc}',handler :me.addDefect,scope : this}, 
    	    	      //{iconCls : 'icon-edit',id : 'upd',handler :me.editDefect,scope : this}, 
    	    	      {iconCls : 'icon-del',id : 'del${param._dc}',handler :me.delDefect,scope : this}
    	    	     ];
    	me.cols=[ {header : 'ID',dataIndex : 'id',hidden:true,sortable : true},
                  {header : '编号',dataIndex : 'code',sortable : true,flex : 1}, 
                  {header : '名称',dataIndex : 'name',sortable : true,flex : 1},              
                  {header : '计划开始时间',dataIndex :'planStartDate',sortable : true,}, 
                  {header : '计划完成时间',dataIndex : 'planEndDate',sortable : true}, 
                  {header : '处理状态',dataIndex : 'dealStatus',sortable : true},
                  {header : '状态',dataIndex : 'status',sortable : true},
                  {header : '操作',width:170,dataIndex :'id',renderer:function(value){
  					 
   					return "<a onclick='improveEditFunction()' herf='javascript:void(0);' style='cursor:pointer'> 编辑  </a>|<a onclick='improveFileFunction();' herf='javascript:void(0);' style='cursor:pointer'> 上报方案  </a>|<a onclick='improveFollowFunction();' herf='javascript:void(0);' style='cursor:pointer'> 整改跟进 </a>";
   				 }}];
    	
    	me.callParent(arguments);
    	},
    	
    	
    	//添加 的
    	addDefect:function(){
    		rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
    		rectify_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
				autoLoad : {
					url : 'pages/icm/rectify/rectifyEdit.jsp',

					scripts : true
				}
			});
    		rectify_ManagerView.add(rectify_ManagerView.gridPanel);
    	},
    	//编辑
      /*   editDefect:function(){

        	var me=this;
    	 var selection = me.getSelectionModel().getSelection();//得到选中的记录
    	     defectID=selection[0].get('id');
        	rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
    	    rectify_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
            autoLoad : {
            url : 'pages/icm/defect/defectEdit.jsp?defectID='+defectID,
            scripts : true	
                }
          });
    	
    	 rectify_ManagerView.add(rectify_ManagerView.gridPanel);
        	
       }, */
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
    		     url : __ctxPath+ '/icm/improve/removeImproveByIdBatch.f',
    		     params : {
    		    	 improveIds : ids
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
     var id=improveId='';
     var  improveEditFunction=function(){
            rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
	    	rectify_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
					autoLoad : {
						url : 'pages/icm/rectify/rectifyEdit.jsp?improveId='+improveId+'&type=yes',

						scripts : true
					}
				});
	    	rectify_ManagerView.add(rectify_ManagerView.gridPanel);
     };
     var improveFileFunction=function(){
    	    rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
	    	rectify_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
					autoLoad : {
						url : 'pages/icm/rectify/rectifyFile.jsp?improveId='+improveId,

						scripts : true
					}
				});
	    		rectify_ManagerView.add(rectify_ManagerView.gridPanel);
     };
     var improveFollowFunction=function(){
    	    rectify_ManagerView.remove(rectify_ManagerView.gridPanel);
	    	rectify_ManagerView.gridPanel=Ext.create('Ext.container.Container',{
					autoLoad : {
						url : 'pages/icm/rectify/rectifyFollow.jsp?improveId='+improveId,

						scripts : true
					}
				});
	    		rectify_ManagerView.add(rectify_ManagerView.gridPanel);
 
     };
     var rectify_rectifyPanel=null;
	Ext.onReady(function() {
	Ext.QuickTips.init();
	rectify_rectifyPanel= Ext.create('rectify_rectifyPanel',{
			renderTo: 'FHD.icm.rectify.rectifyList${param._dc}',
		    height : FHD.getCenterPanelHeight() - 3,//高度为：获取center-panel的高度
			listeners : {
			}
		});
	rectify_rectifyPanel.on('select',function(){
		var selectionDate=rectify_rectifyPanel.getSelectionModel().getSelection();
		if(null!=selectionDate[0].get('id')){
			improveId=selectionDate[0].get('id');	
		}
	});
	rectify_rectifyPanel.store.proxy.url=__ctxPath+ '/icm/improve/findImproveListBypage.f',//调用后台url
	rectify_rectifyPanel.store.load();
	rectify_rectifyPanel.store.on("load",function(store){
	   // Ext.getCmp('upd').setDisabled(true);//编辑不可用
  	    Ext.getCmp('del${param._dc}').setDisabled(true);
	 });
	rectify_rectifyPanel.on('selectionchange',function(m) {
	var len=rectify_rectifyPanel.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	  // Ext.getCmp('upd').setDisabled(true);//编辑不可用
	   Ext.getCmp('del${param._dc}').setDisabled(true);
	   } else if (len == 1) {
	  // Ext.getCmp('upd').setDisabled(false);//编辑可用
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   } else if (len > 1) {
	 //  Ext.getCmp('upd').setDisabled(true);//编辑不可用
	   Ext.getCmp('del${param._dc}').setDisabled(false);//删除可用
	   }
	 });	 
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.rectify.rectifyList${param._dc}'></div>
</body>
</html>