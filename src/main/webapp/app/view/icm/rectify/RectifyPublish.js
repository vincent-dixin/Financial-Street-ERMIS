/*整改计划发布*/
Ext.define('FHD.view.icm.rectify.RectifyPublish', {
	extend : 'Ext.form.Panel',
	alias: 'widget.rectifypublish',
	autoScroll:true,
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
	autoWidth:true,
	collapsed : false,
	initComponent :function() {
		var me = this;
		me.defectrelaimprovegrid = Ext.widget('defectrelaimprovegrid',{
			editable:false
		});
		me.approvalidea = Ext.widget('approvalidea',{executionId:me.executionId}); 
		me.rectifyview = Ext.widget('rectifyview');
		me.approvalIdeaGrid = Ext.create('FHD.view.comm.bpm.ApprovalIdeaGrid',{
			executionId: me.executionId,
			title:'审批意见历史列表',
			height:200,
			columnWidth:1
		});
		me.items= [Ext.widget('flowtaskbar',{
	    		jsonArray:[
		    		{index: 1, context:'1.计划制定',status:'done'},
		    		{index: 2, context:'2.计划审核',status:'done'},
		    		{index: 3, context:'3.计划审批',status:'done'},
		    		{index: 4, context:'4.计划发布',status:'current'}
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
			},{
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
			}
		];
		me.bbar={
			items: [
				'->',{
       				text: '提交',
    				iconCls: 'icon-operator-submit',
    				handler: function () {
    					var button = this;
    					Ext.MessageBox.show({
							title : FHD.locale.get('fhd.common.prompt'),
							width : 260,
							msg : '计划发布后，系统会将该计划分发给各个缺陷的整改责任部门，您确定发布该计划吗？',
							buttons : Ext.MessageBox.YESNO,
							icon : Ext.MessageBox.QUESTION,
							fn : function(btn) {
								if (btn == 'yes') {//确认提交
									button.setDisabled(true);
									FHD.ajax({//ajax调用
				   						url : __ctxPath+ '/icm/rectify/rectifyPublish.f',
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
		var me = this;
		me.rectifyview.loadData(me.improveId);
		me.defectrelaimprovegrid.loadData(me.improveId);
	}
});