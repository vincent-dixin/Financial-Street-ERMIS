/*填写整改方案进度*/
Ext.define('FHD.view.icm.rectify.component.RectifyFollowListGrid', {
		extend : 'Ext.form.Panel',
		layout : {
			type : 'column'
		},
		defaults : {
			columnWidth : 1/1
		},
		bodyPadding: '0 3 3 3',
		requires: [
	       'FHD.view.icm.rectify.form.RectifyView',
	       'FHD.view.icm.rectify.form.RectifySchedule',
			'FHD.ux.icm.common.FlowTaskBar'
	    ],
	    autoScroll:true,
		autoWidth:true,
		collapsed : false,
		initComponent :function() {
			var me = this;
			me.rectifyview = Ext.widget('rectifyview');
			me.rectifyschedule = Ext.widget('rectifyschedule');			
			me.items= [
				Ext.widget('flowtaskbar',{
		    		jsonArray:[
			    		{index: 1, context:'1.方案上报',status:'done'},
			    		{index: 2, context:'2.指定复核人',status:'done'},
			    		{index: 3, context:'3.进度汇报',status:'current'},
			    		{index: 4, context:'4.整改复核',status:'undo'},
			    		{index: 5, context:'5.结果审核',status:'undo'}
			    	]
		    	}),
		    	me.rectifyview,
		    	me.rectifyschedule
	    	];
			me.bbar={
				items: [
					'->',{//保存，操作后 执行返回功能 
						text: FHD.locale.get("fhd.strategymap.strategymapmgr.form.save"),
						iconCls: 'icon-control-stop-blue',
						handler: function () {
							me.rectifyschedule.rectifyScheduleSave();
		             	} 
					},
					{//提交，操作后 执行返回功能 
						text: FHD.locale.get("fhd.common.submit"),
						iconCls: 'icon-operator-submit',
						handler: function () {
							var button = this;
							if(me.rectifyschedule.getForm().isValid()){
								FHD.submit({//先保存，后提交
									form: me.rectifyschedule.getForm(),
									url: __ctxPath+'/icm/rectify/saveRectifySchedule.f',
									callback: function (data){ 
										Ext.MessageBox.show({
											title : FHD.locale.get('fhd.common.prompt'),
											width : 260,
											msg : '提交后，系统会将该整改方案提交给复核人进行复核，您确定方案已经执行完成并提交吗？',
											buttons : Ext.MessageBox.YESNO,
											icon : Ext.MessageBox.QUESTION,
											fn : function(btn) {
												if (btn == 'yes') {//确认提交
													button.setDisabled(true);
													FHD.ajax({//ajax调用
								   						url : __ctxPath+ '/icm/rectify/rectifyFollowListGrid.f',
								   					    params : {
								   					    	businessId: me.businessId,
								   					    	executionId: me.executionId
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
								});
							}
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
		me.rectifyschedule.loadData(me.improveId,me.executionId);
	}
});
