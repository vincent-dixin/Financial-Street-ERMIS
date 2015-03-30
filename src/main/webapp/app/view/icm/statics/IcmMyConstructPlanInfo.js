/**
 * 我的数据
 * 我的体系计划
 * @author 吴德福
 */
Ext.define('FHD.view.icm.statics.IcmMyConstructPlanInfo', {
 	extend: 'Ext.container.Container',
 	alias: 'widget.icmmyconstructplaninfo',
 	
 	overflowX: 'hidden',
	overflowY: 'auto',
	layout: 'fit',
    // 初始化方法
    initComponent: function() {
    	 var me = this;
    	 
    	 FHD.ajax({
    		url : __ctxPath+ '/icm/icsystem/findconstructplanchartxmlbycomanyid.f',
			async:false,
		    params : {
		    	companyId: __user.companyId
			},
			callback : function(data) {
				if(data){
					if(FusionCharts("constructpaln_finish_rate-chart") != undefined){
			 		   	FusionCharts("constructpaln_finish_rate-chart").dispose();
			     	}
			    	
					me.grid = Ext.create('FHD.view.icm.icsystem.constructplan.ConstructPlanDashboard',{
						finishRateXml:data.finishRateXml,
						finishRate:data.finishRate,
						processXml:data.processXml,
						diagnosisXml:data.diagnosisXml
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