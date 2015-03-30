/*领导审批*/
Ext.define('FHD.view.icm.rectify.RectifyLeadApprove', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifyleadapprove',
	layout : {
		type : 'column'
	},
	defaults : {
		columnWidth : 1/1
	},
	bodyPadding: '0 3 3 3',
	requires: [
       'FHD.view.icm.rectify.form.RectifyView',
       'FHD.view.comm.bpm.ApprovalIdea',
       'FHD.view.icm.defect.component.DefectRelaImproveGrid',
       'FHD.ux.icm.common.FlowTaskBar'
    ],
    autoScroll:true,
	autoWidth:true,
	collapsed : false,
	initComponent :function() {
		var me = this;
		me.rectifyview = Ext.widget('rectifyview');
		me.defectrelaimprovegrid = Ext.widget('defectrelaimprovegrid',{
			editable:false
		});
		me.approvalidea = Ext.widget('approvalidea',{executionId:me.executionId}); 
		me.items= [Ext.widget('flowtaskbar',{
    		jsonArray:[
	    		{index: 1, context:'1.计划制定',status:'done'},
	    		{index: 2, context:'2.计划审核',status:'done'},
	    		{index: 3, context:'3.计划审批',status:'current'},
	    		{index: 4, context:'4.计划发布',status:'undo'}
	    	]
    	}),me.rectifyview,{
			xtype : 'fieldset',
			layout : {
				type : 'fit'
			},
			collapsed : false,
			collapsible : false,
			title : '整改范围',
			items:[me.defectrelaimprovegrid]
        },me.approvalidea
		];
		me.bbar={
			items: [
				'->',{
					text: FHD.locale.get('fhd.common.submit'),
					iconCls: 'icon-operator-submit',
					disabled:false,
					handler: function () {
						this.setDisabled(true);
						FHD.ajax({//ajax调用
	   						url : __ctxPath+ '/icm/rectify/rectifyRectifyLeadApprove.f',
	   					    params : {
	   					    	businessId:me.businessId,
	   					    	executionId:me.executionId,
	   					    	isPass:me.approvalidea.isPass,
								examineApproveIdea:me.approvalidea.getValue()
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
		me.defectrelaimprovegrid.loadData(me.improveId);
		me.approvalidea.reloadData();
	}
});