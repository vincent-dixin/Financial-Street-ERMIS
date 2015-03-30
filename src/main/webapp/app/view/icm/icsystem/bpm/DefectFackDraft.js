Ext.define('FHD.view.icm.icsystem.bpm.DefectFackDraft', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.defectfackdraft',
	requires: [
       'FHD.view.icm.icsystem.constructplan.form.DiagnosesGroupForm',
       'FHD.ux.icm.common.FlowTaskBar',
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm'
    ],
//    initParam : function(paramObj){
//         var me = this;
//    	 me.paramObj = paramObj;
//	},
	autoScroll:true,
	autoHeight : true,
	autoWidth : true,
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
		me.diagnosesGroupForm=Ext.widget('diagnosesgroupform',{
			columnWidth:1/1,
			executionId : me.executionId,
			businessId : me.businessId,
			checked:false,
			border : false,
			autoHeight: true,
			autoWidth : true
		});
//		//fieldSet
//		me.childItems={
//			xtype : 'fieldset',
//			//margin: '7 10 0 30',
//			layout : {
//				type : 'column'
//			},
//			autoHeight: true,
//			collapsed: false,
//			columnWidth:1/1,
//			collapsible : true,
//			title : '缺陷反馈',
//			items : [me.diagnosesGroupForm]
//		};
		me.bbar={
			items: [
				'->',{
					text: '保存',
    				iconCls: 'icon-control-stop-blue',
    				handler: function () {
					    me.diagnosesGroupForm.save();
    				} 
				},{
       				text: '提交',
    				iconCls: 'icon-operator-submit',
    				handler: function () {
    					if(me.diagnosesGroupForm.save()){
							FHD.ajax({//ajax调用
								url : __ctxPath+ '/icm/icsystem/submitdiagnosesdefect.f',
							    params : {
							    	constructPlanId : me.businessId,
							    	executionId : me.executionId
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
	    		{index: 2, context:'2.缺陷反馈',status:'current'},
	    		{index: 3, context:'3.缺陷整理',status:'undo'}
	    	]
    	}),me.basicInfo,me.diagnosesGroupForm]
		});
		me.callParent(arguments);
	},
	reloadData:function(){
		var me=this;
		me.basicInfo.initParam({
    		businessId : me.businessId
    	});
    	me.basicInfo.reloadData();
    	me.diagnosesGroupForm.reloadData();
	}
});