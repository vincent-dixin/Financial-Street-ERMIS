Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessEditApprove', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.planprocesseditapprove',
	requires: [
       'FHD.view.icm.icsystem.bpm.PlanProcessListForView',
       'FHD.view.icm.icsystem.bpm.PlanProcessEditTabPanelForView'
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
		me.planprocesslistforview=Ext.widget('planprocesslistforview',{
			id:'planprocesslistforview',
			columnWidth:1/1,
			checked:false,
			border : false,
			plain : true
		});
		// 流程基本信息
        me.planprocessedittabpanelforview = Ext.widget('planprocessedittabpanelforview',{hidden : true,readOnly:true,border:false});
        //流程列表
		var fieldSetProcessList={
			xtype:'fieldset',
			//margin:'7 10 0 30',
			title:'请选择操作',
			collapsible : false,
			layout:{
				type:'column'
			},
			defaults:{
				columnWidth:1/1
			},
			items:[me.planprocesslistforview]
		};
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[fieldSetProcessList,me.planprocessedittabpanelforview]
		});
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
    	me.planprocesslistforview.initParam({
    		constructPlanId : me.paramObj.constructPlanId,
    		executionId : me.paramObj.executionId,
    		orgScoll : 'planprocesslistforview'
    	});
    	me.planprocesslistforview.reloadData();
	}
});