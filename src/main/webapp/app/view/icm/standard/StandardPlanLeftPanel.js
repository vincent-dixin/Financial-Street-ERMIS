Ext.define('FHD.view.icm.standard.StandardPlanLeftPanel', {
    extend: 'FHD.ux.MenuPanel',
    alias: 'widget.standardplanleftpanel',
    
    requires: [
    	'FHD.view.icm.standard.StandardPlanDashboard',
    	'FHD.view.icm.standard.StandardManage',
    	'FHD.view.icm.standard.bpm.StandardBpmList'
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
						url : __ctxPath+ '/icm/standard/findStandardPlanChartXmlByCompanyId.f',
						async:false,
					    params : {
					    	companyId: __user.companyId
						},
						callback : function(data) {
							if(data){
								var standardplancenterpanel = me.up('panel').standardplancenterpanel;
								if(FusionCharts("standardplan_finish_rate-chart") != undefined){
						 		   	FusionCharts("standardplan_finish_rate-chart").dispose();
						     	}
						    	
						    	if(FusionCharts("standardplan_systemdefectrate_rate-chart") != undefined){
						  		   	FusionCharts("standardplan_systemdefectrate_rate-chart").dispose();
						      	}
						    	
						    	if(FusionCharts("standardplan_performerror_rate-chart") != undefined){
						  		   	FusionCharts("standardplan_performerror_rate-chart").dispose();
						      	}
								standardplancenterpanel.removeAll(true);
								standardplancenterpanel.add(Ext.widget('standardplandashboard',{
									finishRateXml:data.finishRateXml,
									finishRate:data.finishRate,
									systemDefectRateXml:data.systemDefectRateXml,
									systemDefectRate:data.systemDefectRate,
									performErrorRateXml:data.performErrorRateXml,
									performErrorRate:data.performErrorRate,
									defectLevelXml:data.defectLevelXml,
									orgDefectXml:data.orgDefectXml
								}));
							}
						}
					});
				}
		    },{
		        text: '内控标准列表',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var standardplancenterpanel = me.up('panel').standardplancenterpanel;
					standardplancenterpanel.removeAll(true);
					standardplancenterpanel.add(Ext.widget('standardbpmlist'));
				}
		    },{
		        text: '内控标准维护',
		        iconCls:'icon-btn-testReport',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var standardplancenterpanel = me.up('panel').standardplancenterpanel;
					standardplancenterpanel.removeAll(true);
					standardplancenterpanel.add(Ext.widget('standardmanage'));
				}
		    }]
        });

        me.callParent(arguments);
    }
});