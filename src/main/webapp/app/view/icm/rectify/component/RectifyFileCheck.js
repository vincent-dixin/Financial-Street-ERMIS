/*汇总并指定复核人*/
Ext.define('FHD.view.icm.rectify.component.RectifyFileCheck', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyfilecheck',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding: '0 3 3 3',
	requires: [
       'FHD.view.icm.rectify.form.RectifyView',
       'FHD.view.icm.rectify.component.ImprovePlanSetReviewerEditGrid',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    autoScroll:true,
	autoWidth:true,
	collapsed : false,
	initComponent :function() {
		var me = this;
		var me = this;
		me.rectifyview = Ext.widget('rectifyview');
		me.improveplansetreviewereditgrid = Ext.widget('improveplansetreviewereditgrid',{border: false});			
		me.items= [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.方案上报',status:'done'},
	    		{index: 2, context:'2.指定复核人',status:'current'},
	    		{index: 3, context:'3.进度汇报',status:'undo'},
	    		{index: 4, context:'4.整改复核',status:'undo'},
	    		{index: 5, context:'5.结果审核',status:'undo'}
	    	]
    	}),me.rectifyview,{
			xtype : 'fieldset',
			layout : {
				type : 'fit'
			},
			collapsed : false,
			collapsible : false,
			title : '指定复核人',
			items:[me.improveplansetreviewereditgrid]
            }
		];
				me.bbar={
			items: [
				'->',{//提交，操作后 执行返回功能 
					text: FHD.locale.get("fhd.common.submit"),
					iconCls: 'icon-operator-submit',
					handler: function () {
						var isReturnFalse = false;
						me.improveplansetreviewereditgrid.improvePlanGrid.store.each(function(value){
							if(!(value.data.empId && value.data.empId!='')){
								isReturnFalse = true;
								return;
							}
						});
						if(isReturnFalse){
							Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '复核人为必须选项');
							return false;
						}
						
						this.setDisabled(true);
						FHD.ajax({//ajax调用
	   						url : __ctxPath+ '/icm/rectify/rectifyFileCheck.f',
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
		Ext.applyIf(me,{
			layout:{
        		align: 'stretch',
    			type: 'vbox'
        	},
			items:me.items
		});
		me.callParent(arguments);
	},
	loadData: function(improveId,executionId){
		var me = this;
		me.improveId = improveId;
		me.executionId = executionId;
		me.reloadData();
	},
	reloadData:function(){
		var me=this;
		me.rectifyview.loadData(me.improveId);
		me.improveplansetreviewereditgrid.loadData(me.improveId);
	}
});
