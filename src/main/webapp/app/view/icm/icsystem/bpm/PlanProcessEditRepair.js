Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessEditRepair', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.planprocesseditrepair',
	requires: [
       'FHD.view.icm.icsystem.bpm.PlanProcessList',
       'FHD.view.icm.icsystem.bpm.PlanProcessEditTabPanel',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	autoScroll:true,
	bodyPadding:'0 3 3 3',
	layout : {
		type : 'column'
	},
	defaults:{
		columnWidth:1/1
	},
	
	initComponent : function() {
		var me=this;
		//评价计划可编辑列表
		me.planprocesslist=Ext.widget('planprocesslist',{
			id:'planprocesslist',
			columnWidth:1/1,
			checked:false,
			border : false
		});
		// 流程基本信息
        me.planprocessedittabpanel = Ext.widget('planprocessedittabpanel',{hidden : true,autoScroll : false});
    	//流程列表
		var fieldSetProcessList={
			xtype:'fieldset',
			//margin:'7 10 0 30',
			title:'1. 请选择要进行的操作',
			layout:{
				type:'column'
			},
			defaults:{
				columnWidth:1/1
			},
			items:[me.planprocesslist]
		};
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[fieldSetProcessList,me.planprocessedittabpanel]
		});
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
    	me.planprocesslist.initParam({
    		constructPlanId : me.paramObj.constructPlanId,
    		executionId : me.paramObj.executionId,
    		orgScoll : 'planprocesslist'
    	});
    	me.planprocesslist.reloadRepairData();
	}
});