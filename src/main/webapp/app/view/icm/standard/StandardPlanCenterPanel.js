/**
 * 
 * 工作计划左侧功能树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.icm.standard.StandardPlanCenterPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.standardplancenterpanel',
	
    layout:'fit',
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       
        FHD.ajax({
			url : __ctxPath+ '/icm/standard/findStandardPlanChartXmlByCompanyId.f',
			async:false,
		    params : {
		    	companyId: __user.companyId
			},
			callback : function(data) {
				if(data){
					if(FusionCharts("standardplan_finish_rate-chart") != undefined){
			 		   	FusionCharts("standardplan_finish_rate-chart").dispose();
			     	}
			    	
			    	if(FusionCharts("standardplan_systemdefectrate_rate-chart") != undefined){
			  		   	FusionCharts("standardplan_systemdefectrate_rate-chart").dispose();
			      	}
			    	
			    	if(FusionCharts("standardplan_performerror_rate-chart") != undefined){
			  		   	FusionCharts("standardplan_performerror_rate-chart").dispose();
			      	}
					me.standardplandashboard = Ext.widget('standardplandashboard',{
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
        	items:[me.standardplandashboard]
        });

        me.callParent(arguments);
    }
});