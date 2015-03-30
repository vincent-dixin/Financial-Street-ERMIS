/*上报方案*/
Ext.define('FHD.view.icm.rectify.component.RectifyFileView', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyfileview',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding: '0 3 3 3',
	requires: [
       'FHD.view.icm.rectify.form.RectifyView',
       'FHD.view.icm.rectify.component.ImprovePlanEditGrid',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    autoScroll:true,
	autoWidth:true,
	collapsed : false,
	initComponent :function() {
		var me = this;
		me.rectifyview = Ext.widget('rectifyview');
		me.improveplaneditgrid = Ext.widget('improveplaneditgrid',{border: false});			
		me.items= [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.方案上报',status:'current'},
	    		{index: 2, context:'2.指定复核人',status:'undo'},
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
			title : '方案编辑',
			items:[me.improveplaneditgrid]
            }
		];
		me.bbar={
			items: [
				'->',{//保存，操作后 执行返回功能 
					text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),
					iconCls: 'icon-control-stop-blue',
					handler: function () {
						me.improveplaneditgrid.mergeImprovePlan()
	             	} 
				},
				{//提交，操作后 执行返回功能 
					text: FHD.locale.get("fhd.common.submit"),
					iconCls: 'icon-operator-submit',
					handler: function () {
						var jsonArray = new Array();
						var rows = me.improveplaneditgrid.improvePlanGrid.store.getModifiedRecords();
						Ext.each(rows,function(item){
							jsonArray.push(item.data);
						});
						var isReturnFalse = false;
						me.improveplaneditgrid.improvePlanGrid.store.each(function(value){
							if(!(value.data.content && value.data.content!='')){
								isReturnFalse = true;
								return;
							}
						});
						if(isReturnFalse){
							Ext.Msg.alert(FHD.locale.get('fhd.common.prompt'), '方案内容为必须输项');
							return false;
						}
						this.setDisabled(true);
						FHD.ajax({//ajax调用
	   						url : __ctxPath+ '/icm/rectify/rectifyDraftFile.f',
	   					    params : {
	   					    	businessId:me.businessId,
	   					    	executionId:me.executionId,
	   					    	modifiedRecord:(jsonArray.length>0)?Ext.encode(jsonArray):null
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
		me.improveplaneditgrid.loadData(me.improveId);
	}
});