/**
 * 
 * 工作计划左侧功能树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanCenterPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructplancenterpanel',
	
    layout:'fit',
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
					if(FusionCharts("improve_finish_rate-chart") != undefined){
			 		   	FusionCharts("improve_finish_rate-chart").dispose();
			     	}
					me.constructplandashboard = Ext.widget('constructplandashboard',{
						finishRateXml: data.finishRateXml,
						finishRate: data.finishRate
					})
				}
			}
		});
        Ext.applyIf(me, {
        	items:[me.constructplandashboard]
        });
		
        me.callParent(arguments);
    }
});