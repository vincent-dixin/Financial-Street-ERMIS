Ext.define('FHD.view.icm.icsystem.bpm.DefectCleanUpDraft', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.defectcleanupdraft',
	requires: [
       'FHD.view.icm.icsystem.constructplan.form.DefectClearUpGroupForm',
       'FHD.ux.icm.common.FlowTaskBar',
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm'
    ],
//    initParam : function(paramObj){
//         var me = this;
//    	 me.paramObj = paramObj;
//	},
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
		//基本信息
		me.basicInfo=Ext.widget('constructplanpreviewform',{
			columnWidth:1/1,
			businessId:me.businessId,
			border:false
		});
		//评价计划可编辑列表
		me.defectClearUpGroupForm=Ext.widget('defectclearupgroupform',{
			columnWidth:1/1,
			executionId : me.executionId,
			businessId : me.businessId,
			checked:false
		});
//		me.approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
//			executionId: me.executionId,
//			title:'审批意见历史列表',
//			height:200,
//			columnWidth:1
//		});
//    	//fieldSet
//		var fieldSet={
//			xtype : 'fieldset',
//				layout : {
//					type : 'column'
//				},
//				collapsed : true,
//				collapsible : true,
//				columnWidth:1,
//				collapsible : true,
//				title : '审批意见列表',
//				items : [me.approvalIdeaGrid]
//		};
		
		me.bbar={
			items: [
				'->',{
					text: '保存',
    				iconCls: 'icon-control-stop-blue',
    				handler: function () {
					    me.defectClearUpGroupForm.save();
    				}
				},{
       				text: '提交',
    				iconCls: 'icon-operator-submit',
    				handler: function () {
    					if(me.defectClearUpGroupForm.save()){
							FHD.ajax({//ajax调用
								url : __ctxPath+ '/icm/icsystem/submitdefectclearup.f',
							    params : {
							    	constructPlanId:me.businessId,
							    	executionId:me.executionId
								},
								callback : function(data) {
									if(me.winId){
										Ext.getCmp(me.winId).close();
									}
								}
							});
    					}
    				} 
    			}
    		]
		};
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.合规诊断',status:'done'},
	    		{index: 2, context:'2.缺陷反馈',status:'done'},
	    		{index: 3, context:'3.缺陷整理',status:'current'}
	    	]
    	}),me.basicInfo,me.defectClearUpGroupForm]
		});
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		me.basicInfo.initParam({
    		businessId : me.businessId
    	});
    	me.basicInfo.reloadData();
    	me.defectClearUpGroupForm.reloadData();
	}
});