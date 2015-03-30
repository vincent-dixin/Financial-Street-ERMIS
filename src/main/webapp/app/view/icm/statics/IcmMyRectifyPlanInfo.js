/**
 * 我的数据
 * 我的整改计划
 * @author 吴德福
 */
Ext.define('FHD.view.icm.statics.IcmMyRectifyPlanInfo', {
 	extend: 'Ext.container.Container',
 	alias: 'widget.icmmyrectifyplaninfo',
 	
 	overflowX: 'hidden',
	overflowY: 'auto',
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
    	 
    	 FHD.ajax({
    		url : __ctxPath+ '/icm/rectify/findImproveChartXmlByComanyId.f',
			async:false,
		    params : {
		    	companyId: me.orgId
			},
			callback : function(data) {
				if(data){
					if(FusionCharts("improve_finish_rate-chart") != undefined){
			 		   	FusionCharts("improve_finish_rate-chart").dispose();
			     	}
			    	
					me.grid = Ext.create('FHD.view.icm.rectify.RectifyImproveDashboard',{
						finishRateXml: data.finishRateXml,
						finishRate: data.finishRate,
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