/**
 * 我的数据
 * 我的评价计划
 * @author 吴德福
 */
Ext.define('FHD.view.icm.statics.IcmMyAssessPlanInfo', {
 	extend: 'Ext.container.Container',
 	alias: 'widget.icmmyassessplaninfo',
 	
 	overflowX: 'hidden',
	overflowY: 'auto',
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
    	 
    	 FHD.ajax({
			url : __ctxPath+ '/icm/assess/findAssessPlanChartXmlByCompanyId.f',
			async:false,
		    params : {
		    	companyId: me.orgId
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
			    	
					me.grid = Ext.create('FHD.view.icm.assess.AssessPlanDashboard',{
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
		 
        me.callParent(arguments);
        
        if(me.grid){
        	me.add(me.grid);
        }
    },
    showPlanViewList:function(id,dealStatus){
    	var me = this;
    	
    },
    reloadData:function(orgid){
    	var me=this;
    }
});