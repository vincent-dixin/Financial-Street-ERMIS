/**
 * 
 * 任务分配
 */

Ext.define('FHD.view.risk.assess.kpiSet.SetMainPanel',{
 	extend: 'Ext.panel.Panel',
 	border:false,
 	requires: [
 	           'FHD.view.risk.assess.kpiSet.SetAssessTask'
],
 	
 	
    initComponent: function () {
    	var me = this;
    		me.p2 = Ext.create('FHD.view.risk.assess.kpiSet.SetAssessTask',{
    		 	navigatorTitle:'评估任务分配',
    		 	border: true,
    		 	last:function(){//保存按钮
    		 	}
    		 });
    		 var btnSubmit = Ext.create('Ext.button.Button',{
 	            text: '提交',//提交按钮
 	            iconCls: 'icon-operator-submit',
 	            handler: function () {
 	            	alert('提交');
 	            }
 	        });
    		 var basicPanel = Ext.create('FHD.ux.layout.StepNavigator',{
 			    hiddenTop:true,	//是否隐藏头部
			    hiddenBottom:false, //是否隐藏底部
			    hiddenUndo:true,	//是否有返回按钮
    			btns:[btnSubmit],
    		 	items:[me.p2],
    		 	undo : function(){
    		 	}
    		 });
    	me.callParent(arguments);
    	me.add(basicPanel);
    	
    	
    	me.on('resize',function(p){
    		/*if(Ext.getCmp('kpisetmaincard') != null){
    			Ext.getCmp('kpisetmaincard').setHeight(FHD.getCenterPanelHeight()-30);
    		}*/
    		if(Ext.getCmp('setAssessTaskId') != null){
    			Ext.getCmp('setAssessTaskId').setHeight(FHD.getCenterPanelHeight()-30);
    		}
    	});
    }

});