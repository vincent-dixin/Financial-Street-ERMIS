/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.kpi.KpiPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.kpiPanel',

    requires: [
    	'FHD.view.industry.kpi.KpiGridTree',
    	'FHD.view.industry.kpi.KpiEdit'
    ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.id = "kpiPanelId";
        me.kpiEdit = Ext.widget('kpiEdit');
        me.kpiGridTree = Ext.widget('kpiGridTree');
        
        Ext.apply(me, {
        	border:false,
        	height:FHD.getCenterPanelHeight()-5,
        	layout: {
                align: 'stretch',
                type: 'vbox'
            },
            items: [me.kpiGridTree, me.kpiEdit]
        });

        me.callParent(arguments);
        
    }

});