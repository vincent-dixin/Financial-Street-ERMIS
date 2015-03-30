Ext.define('FHD.view.icm.icsystem.bpm.PlanProcessEditPublish', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.planprocesseditpublish',
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
			height:140,
			columnWidth:1/1,
			checked:false
		});
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
			items:[me.planprocesslistforview]
		};
		// 流程基本信息
        me.planprocessedittabpanelforview = Ext.widget('planprocessedittabpanelforview',{readOnly:true});
    	 //审批意见
		me.grid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			title:'审批意见历史列表',
			margin:'5 3 5 0',
			autoScroll:true,
			collapsible:true,
			collapsed:true,
			height:200,
			columnWidth:1
		});
    	//fieldSet
		var fieldSet={
			xtype:'fieldset',
			//margin:'7 10 0 30',
			title:'审批意见',
			collapsible : true,
			layout:{
				type:'column'
			},
			defaults:{
				columnWidth:1/1
			},
			items:[me.grid]
		};
		
		me.bbar=[
		    '->',
		    {
				text:'提交',
				iconCls: 'icon-operator-submit',
				handler: function () {
					//提交工作流
					me.submit();
					this.setDisabled(true);
	            }
			}
		];
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[fieldSet,fieldSetProcessList,me.planprocessedittabpanelforview]
		});
		me.callParent(arguments);
	},
	submit:function(){
		var me=this;
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/icsystem/constructplanresultspublish.f',
		    params : {
		    	businessId : me.paramObj.constructPlanId,
		    	executionId : me.paramObj.executionId
			},
			callback : function(data) {
				if(me.up('constructplanresultspublish').winId){
					Ext.getCmp(me.up('constructplanresultspublish').winId).close();
				}
			}
		});
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