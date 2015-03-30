/**
 * 整改优化左侧树
 * 
 */
Ext.define('FHD.view.icm.rectify.RectifyImproveLeftPanel', {
    extend: 'FHD.ux.MenuPanel',
    alias: 'widget.rectifyimproveleftpanel',
    
    requires: [
    	'FHD.view.icm.rectify.RectifyImproveDashboard',
    	'FHD.view.icm.rectify.RectifyImproveMainPanel',
    	'FHD.view.icm.defect.DefectList'
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
						url : __ctxPath+ '/icm/rectify/findImproveChartXmlByComanyId.f',
						async:false,
					    params : {
					    	companyId: __user.companyId
						},
						callback : function(data) {
							if(data){
								var rectifyImproveCenterPanel = me.up('panel').rectifyImproveCenterPanel;
								if(FusionCharts("improve_finish_rate-chart") != undefined){
						 		   	FusionCharts("improve_finish_rate-chart").dispose();
						     	}
								rectifyImproveCenterPanel.removeAll(true);
								rectifyImproveCenterPanel.add(Ext.widget('rectifyimprovedashboard',{
									finishRateXml: data.finishRateXml,
									finishRate: data.finishRate,
									defectLevelXml:data.defectLevelXml,
									orgDefectXml:data.orgDefectXml
								}));
							}
						}
					});
				}
		    },{
		        text: '整改计划',
		        iconCls:'icon-btn-assessPlan',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){	
					var rectifyImproveCenterPanel = me.up('panel').rectifyImproveCenterPanel;
					rectifyImproveCenterPanel.removeAll(true);
					rectifyImproveCenterPanel.add(Ext.widget('rectifyimprovemainpanel'));
				}
		    }/*,{
		        text: '整改执行',
		        iconCls:'icon-btn-execute',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){
					
				}
		    }*/,{
		        text: '缺陷管理',
		        iconCls:'icon-btn-defect',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){
					var rectifyImproveCenterPanel = me.up('panel').rectifyImproveCenterPanel;
					rectifyImproveCenterPanel.removeAll(true);
					rectifyImproveCenterPanel.add(Ext.widget('defectlist'));
				}
		    },{
		        text: '基础设置',
		        iconCls:'icon-btn-set',
		        scale: 'large',
				iconAlign: 'top',
				handler:function(){
					var rectifyImproveCenterPanel = me.up('panel').rectifyImproveCenterPanel;
					rectifyImproveCenterPanel.removeAll(true);
					rectifyImproveCenterPanel.add(Ext.widget('panel',{title:'暂无'}));
				}
		    }]
        });

        me.callParent(arguments);
    }
});