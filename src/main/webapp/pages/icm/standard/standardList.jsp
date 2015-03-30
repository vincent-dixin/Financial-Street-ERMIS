<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>普通grid列表</title>
<script type="text/javascript">
	/***attribute start***/
 Ext.define('standard_standardGridPanel',{
    	extend:'FHD.ux.GridPanel',
    	cols:[],
    	tbar:[],
    	tbarItems:[],
    	standardId:'',
    	idSeq:'',
    	upName:'',
    	initComponent:function(){
    		var me=this;
    	me.tbarItems=[{iconCls : 'icon-add',id : 'add',handler :me.addStandard,scope : this},'-', 
    	    	      {iconCls : 'icon-del',id : 'del',handler :me.delStandard,scope : this}];
    	me.cols=[{header : '编号',dataIndex : 'code',sortable : true,flex : 1}, 
 				 {header : '名称',dataIndex : 'name',sortable : true,flex : 1}, 
 				 {header : '内部控制要求',dataIndex :'controlRequirement',sortable : true,}, 
 				 {header : '管理责任部门',dataIndex : 'dept',sortable : true}, 
 				 {header : '状态',dataIndex : 'status',sortable : true},
 				 {
 			     xtype : 'templatecolumn',text : $locale('fhd.common.edit'),align : 'center',dataIndex : 'id',
				 tpl : '<font class="icon-edit" ">&nbsp&nbsp&nbsp</font>',
				 listeners : {
					    click :function(){
					    	Ext.getCmp('standardCreateCodeButtonId').setDisabled(true);
				        	standardTree_ManagerView.tabEditPanel.setActiveTab(0);
				    		standardTree_ManagerView.addType="0";
				    		FHD_icm_standard_standardEdit.controlType='listEdit';
				    		var selection = me.getSelectionModel().getSelection();//得到选中的记录
				    		FHD_icm_standard_standardEdit.standardId=selection[0].get('id');
				    	    var form=FHD_icm_standard_standardEdit.getForm();
				    		form.reset();
				    		form.load({
				                 waitMsg:'信息读取中.....',
				                 url: __ctxPath + '/standard/standardTree/findStandardByIdToJson.do',
				                 params: {
				               	  standardId:selection[0].get('id')
				                 },
				                 success: function (form, action) {
				                     return true;
				                 },
				                 failure: function (form, action) {
				                 }
				             });
							}
						} 
 				 }
 				 ];
    	
    	me.callParent(arguments);
    	},
    	//添加 type=0的standard
    	addStandard:function(){
    		var me=this;
    		var form=FHD_icm_standard_standardEdit.getForm();
      		form.reset(); 
      		form.setValues({'upName':me.upName});
    		standardTree_ManagerView.tabEditPanel.setActiveTab(0);
    		standardTree_ManagerView.addType="0";
    		FHD_icm_standard_standardEdit.standardId=this.standardId;
    		FHD_icm_standard_standardEdit.controlType='';
    		FHD_icm_standard_standardEdit.idSeq=this.idSeq;
    		Ext.getCmp('standardCreateCodeButtonId').setDisabled(false);
    	},
    	//编辑
        editStandard:function(){
        	var me=this;
        	
        },
        //删除type=0的数据
        delStandard:function(){
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
    		     url : __ctxPath+ '/standard/standardTree/removeStandards.do',
    		     params : {
    		     standardIds : ids
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
    
     var standard_standardGridPanel=null;
	Ext.onReady(function() {
	standard_standardGridPanel= Ext.create('standard_standardGridPanel',{
			title:'标准列表',
		    height : FHD.getCenterPanelHeight() - 3,//高度为：获取center-panel的高度
			listeners : {
			}
		});
	standard_standardGridPanel.store.on("load",function(store){
  	    Ext.getCmp('del').setDisabled(true);
	 });
	standard_standardGridPanel.on('selectionchange',function(m) {
	var len=standard_standardGridPanel.getSelectionModel().getSelection().length; 
	 if (len== 0) {
	   Ext.getCmp('del').setDisabled(true);
	   } else if (len == 1) {
	   Ext.getCmp('del').setDisabled(false);//删除可用
	   } else if (len > 1) {
	   Ext.getCmp('del').setDisabled(false);//删除可用
	   }
	 });
});
	/***Ext.onReady end***/
</script>
</head>
<body>
	<div id='FHD.icm.standard.standardList${param._dc}'></div>
</body>
</html>