Ext.define('FHD.view.icm.icsystem.bpm.ConstructPlanPublish', {
	/*
 * 内控评价计划，审批页面
 * businessId:评价计划Id
 */
	extend:'Ext.panel.Panel',
	aligs:'widget.constructplanpublish',
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
		me.approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			title:'审批意见历史列表',
			height:200,
			columnWidth:1
		});
    	//fieldSet
		var fieldSet={
			xtype : 'fieldset',
				layout : {
					type : 'column'
				},
				collapsed : true,
				collapsible : true,
				columnWidth:1,
				collapsible : true,
				title : '审批意见列表',
				items : [me.approvalIdeaGrid]
		};
		
		me.bbar={
			items: [
				'->',{
       				text: '提交',
    				iconCls: 'icon-operator-submit',
    				handler: function () {
    					this.setDisabled(true);
					    FHD.ajax({//ajax调用
	   						url : __ctxPath+ '/icm/icsystem/constructplanpublish.f',
	   					    params : {
	   					    	businessId:me.businessId,
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
    		]
		};
		Ext.applyIf(me, {
        	layout:{
        		align: 'stretch',
        		type: 'vbox'
        	},
        	items:[Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.计划制定',status:'done'},
	    		{index: 2, context:'2.计划审批',status:'done'},
	    		{index: 3, context:'3.计划发布',status:'current'}
	    		
	    	]
    	}),me.basicInfo,me.childItems,fieldSet]
		});
		me.callParent(arguments);
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