Ext.define('FHD.view.icm.icsystem.bpm.ConstructPlanApprove', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.constructplanapprove',
	requires: [
       'FHD.view.icm.icsystem.constructplan.form.ConstructPlanPreviewForm',
       'FHD.view.icm.icsystem.constructplan.ConstructPlanRelaStandardViewGrid',
	   'FHD.ux.icm.common.FlowTaskBar'
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
		//评价计划预览表单
		me.basicInfo=Ext.widget('constructplanpreviewform',{
			columnWidth:1/1,
			businessId:me.businessId
		});
		//评价计划可编辑列表
		me.constructPlanGrid=Ext.widget('constructplanrelastandardviewgrid',{
			columnWidth:1/1,
			checked:false,
			tbarItems : [],
			searchable : false
		});
		//fieldSet
		me.childItems={
			xtype : 'fieldset',
			//margin: '7 10 0 30',
			layout : {
				type : 'column'
			},
			collapsed: false,
			columnWidth:1/1,
			collapsible : false,
			title : '建设范围',
			items : [me.constructPlanGrid]
		};
		//审批意见
		me.ideaApproval=Ext.create('FHD.view.comm.bpm.ApprovalIdea',{
			columnWidth:1/1,
			executionId:me.executionId
		});
    	//fieldSet
//		var fieldSet={
//			xtype:'fieldset',
//			//margin:'7 10 0 30',
//			title:'审批',
//			collapsed: false,
//			collapsible : false,
//			layout:{
//				type:'column'
//			},
//			defaults:{
//				columnWidth:1/1
//			},
//			items:[me.ideaApproval]
//		};
		
		me.bbar=[
		    '->',
		    {
				text:'提交',
				iconCls: 'icon-operator-submit',
				handler: function () {
					//提交工作流
					me.submit(me.ideaApproval.isPass,me.ideaApproval.getValue());
					this.setDisabled(true);
	            }
			}
		];
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.计划制定',status:'done'},
	    		{index: 2, context:'2.计划审批',status:'current'},
	    		{index: 3, context:'3.计划发布',status:'undo'}
	    		
	    	]
    	}),me.basicInfo,me.childItems,me.ideaApproval]
		});
		me.callParent(arguments);
	},
	submit:function(isPass,examineApproveIdea){
		var me=this;
		FHD.ajax({//ajax调用
			url : __ctxPath+ '/icm/icsystem/constructplanapproval.f',
		    params : {
		    	businessId:me.businessId,
		    	executionId:me.executionId,
		    	isPass:isPass,
		    	examineApproveIdea:examineApproveIdea
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
		//基本信息和详细信息刷新
		me.basicInfo.initParam({
			executionId : me.executionId,
			businessId :  me.businessId
		});
		me.basicInfo.reloadData();
		//选择范围刷新
		me.constructPlanGrid.extraParams={
			executionId : me.executionId,
			businessId :  me.businessId
		};
    	me.constructPlanGrid.reloadData();
	}
});