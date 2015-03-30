Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessEditDraft', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.planprocesseditdraft',
	requires: [
       'FHD.view.icm.icsystem.bpm.PlanProcessList',
       'FHD.view.icm.icsystem.bpm.PlanProcessEditTabPanel'
    ],
    initParam : function(paramObj){
         var me = this;
    	 me.paramObj = paramObj;
	},
	autoScroll:true,
	autoHeight : true,
	bodyPadding:'0 3 3 3',
	layout : {
		type : 'column'
	},
	defaults:{
		columnWidth:1/1
	},
	border:false,
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
			title:'请选择操作',
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
	submit:function(){
		var me=this;
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/icsystem/constructplanrelaprocesssubmit.f',
		    params : {
		    	businessId : me.paramObj.constructPlanId,
		    	executionId : me.paramObj.executionId
			},
			callback : function(data) {
				if(me.winId){
					Ext.getCmp(me.winId).close();
				}
			}
		});
	},
	reloadData:function(){
		var me=this;
    	me.planprocesslist.initParam({
    		constructPlanId : me.paramObj.constructPlanId,
    		executionId : me.paramObj.executionId,
    		orgScoll : 'planprocesslist'
    	});
    	me.planprocesslist.reloadData();
	}
});