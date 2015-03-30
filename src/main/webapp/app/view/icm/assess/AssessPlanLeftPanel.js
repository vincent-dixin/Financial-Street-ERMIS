Ext.define('FHD.view.icm.assess.AssessPlanLeftPanel', {
    extend: 'FHD.ux.MenuPanel',
    alias: 'widget.assessplanleftpanel',
    
    requires: [
    	'FHD.view.icm.assess.AssessPlanDashboard',
    	'FHD.view.icm.assess.AssessPlanMainPanel',
    	'FHD.view.comm.report.assess.TestReportList',
    	'FHD.view.comm.report.assess.GroupYearReportList',
    	'FHD.view.comm.report.assess.CompanyYearReportList',
    	'FHD.view.icm.assess.baseset.AssessGuidelinesMainPanel'
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
						url : __ctxPath+ '/icm/assess/findAssessPlanChartXmlByCompanyId.f',
						async:false,
					    params : {
					    	companyId: __user.companyId
						},
						callback : function(data) {
							if(data){
								var assessplancenterpanel = me.up('panel').assessplancenterpanel;
								if(FusionCharts("assessplan_finish_rate-chart") != undefined){
						 		   	FusionCharts("assessplan_finish_rate-chart").dispose();
						     	}
						    	
						    	if(FusionCharts("assessplan_systemdefectrate_rate-chart") != undefined){
						  		   	FusionCharts("assessplan_systemdefectrate_rate-chart").dispose();
						      	}
						    	
						    	if(FusionCharts("assessplan_performerror_rate-chart") != undefined){
						  		   	FusionCharts("assessplan_performerror_rate-chart").dispose();
						      	}
								assessplancenterpanel.removeAll(true);
								assessplancenterpanel.add(Ext.widget('assessplandashboard',{
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
		        text: '评价计划',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var assessplancenterpanel = me.up('panel').assessplancenterpanel;
					assessplancenterpanel.removeAll(true);
					assessplancenterpanel.add(Ext.widget('assessplanmainpanel'));
				}
		    },{
		        text: '测试报告',
		        iconCls:'icon-btn-testReport',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var assessplancenterpanel = me.up('panel').assessplancenterpanel;
					assessplancenterpanel.removeAll(true);
					assessplancenterpanel.add(Ext.widget('testreportlist'));
				}
		    },{
		        text: '公司年度评价报告',
		        iconCls:'icon-btn-testReport',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var assessplancenterpanel = me.up('panel').assessplancenterpanel;
					assessplancenterpanel.removeAll(true);
					assessplancenterpanel.add(Ext.widget('companyyearreportlist'));
				}
		    },{
		        text: '集团年度评价报告',
		        iconCls:'icon-btn-testReport',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var assessplancenterpanel = me.up('panel').assessplancenterpanel;
					assessplancenterpanel.removeAll(true);
					assessplancenterpanel.add(Ext.widget('groupyearreportlist'));
				}
		    },{
		        text: '基础设置',
		        iconCls:'icon-btn-set',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var assessplancenterpanel = me.up('panel').assessplancenterpanel;
					assessplancenterpanel.removeAll(true);
					assessplancenterpanel.add(Ext.widget('assessguidelinesmainpanel'));
				}
		    }]
        });

        me.callParent(arguments);
    }
});