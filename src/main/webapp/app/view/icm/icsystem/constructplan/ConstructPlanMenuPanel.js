Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanMenuPanel', {
    extend: 'FHD.ux.MenuPanel',
    alias: 'widget.constructplanmenupanel',
    
    requires: [
    	'FHD.view.icm.icsystem.constructplan.ConstructPlanEditPanel',
    	'FHD.view.icm.icsystem.constructplan.PlanStandardDiagnosesEditGrid',
    	'FHD.view.icm.icsystem.constructplan.ConstructPlanDashboard',
    	'FHD.view.comm.report.icsystem.ConstructPlanTestReportList'
    ],
    // 初始化方法
    initComponent: function() {
        var me = this;
		
        Ext.applyIf(me, {
        	items:[{
		        text: '驾驶舱',
		        iconCls:'icon-btn-home',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					FHD.ajax({
						url : __ctxPath+ '/icm/icsystem/findconstructplanchartxmlbycomanyid.f',
						
						async:false,
					    params : {
					    	companyId: __user.companyId
						},
						callback : function(data) {
							if(data){
								var constructplancenterpanel = me.up('panel').constructplancenterpanel;
								if(FusionCharts("constructpaln_finish_rate-chart") != undefined){
						 		   	FusionCharts("constructpaln_finish_rate-chart").dispose();
						     	}
								constructplancenterpanel.removeAll(true);
								constructplancenterpanel.add(Ext.widget('constructplandashboard',{
									finishRateXml: data.finishRateXml,
									finishRate: data.finishRate,
									processXml:data.processXml,
									diagnosisXml:data.diagnosisXml
								}));
							}
						}
					});
					
				
				}
		    },{
		        text: '建设计划',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var constructplancenterpanel = me.up('panel').constructplancenterpanel;
					constructplancenterpanel.removeAll(true);
					var constructplaneditpanel = Ext.widget('constructplaneditpanel');
					constructplancenterpanel.add(constructplaneditpanel);
					constructplaneditpanel.reloadData();
				}
		    },{
		        text: '测试报告',
		        iconCls:'icon-btn-execute',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var constructplancenterpanel = me.up('panel').constructplancenterpanel;
					constructplancenterpanel.removeAll(true);
					var constructplantestreportlist = Ext.widget('constructplantestreportlist');
//					constructplantestreportlist.reloadData();
					constructplancenterpanel.add(constructplantestreportlist);
				}
		    },{
		        text: '基础设置',
		        iconCls:'icon-btn-set',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){
					
				}
		    }]
        });

        me.callParent(arguments);
    }
});