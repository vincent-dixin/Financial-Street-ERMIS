/**
 * 
 * 工作计划左侧功能树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.icm.assess.AssessPlanCenterPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessplancenterpanel',
	
    layout:'fit',
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       
        FHD.ajax({
			url : __ctxPath+ '/icm/assess/findAssessPlanChartXmlByCompanyId.f',
			async:false,
		    params : {
		    	companyId: __user.companyId
			},
			callback : function(data) {
				if(data){
					if(FusionCharts("assessplan_finish_rate-chart") != undefined){
			 		   	FusionCharts("assessplan_finish_rate-chart").dispose();
			     	}
			    	
			    	if(FusionCharts("assessplan_systemdefectrate_rate-chart") != undefined){
			  		   	FusionCharts("assessplan_systemdefectrate_rate-chart").dispose();
			      	}
			    	
			    	if(FusionCharts("assessplan_performerror_rate-chart") != undefined){
			  		   	FusionCharts("assessplan_performerror_rate-chart").dispose();
			      	}
					me.assessplandashboard = Ext.widget('assessplandashboard',{
						finishRateXml:data.finishRateXml,
						finishRate:data.finishRate,
						systemDefectRateXml:data.systemDefectRateXml,
						systemDefectRate:data.systemDefectRate,
						performErrorRateXml:data.performErrorRateXml,
						performErrorRate:data.performErrorRate,
						defectLevelXml:data.defectLevelXml,
						orgDefectXml:data.orgDefectXml
					});
				}
			}
		});
        Ext.applyIf(me, {
        	items:[me.assessplandashboard]
        });

        me.callParent(arguments);
    }
});