/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.risk.RiskPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.riskPanel',

    requires: [
    	'FHD.view.industry.risk.RiskGridTree',
    	'FHD.view.industry.risk.RiskEdit'
    ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.id = 'riskPanelId';
        me.riskEdit = Ext.widget('riskEdit');
        me.riskGridTree = Ext.widget('riskGridTree');
        
        
        Ext.apply(me, {
        	border:false,
        	height:FHD.getCenterPanelHeight()-5,
        	layout: {
                align: 'stretch',
                type: 'vbox'
            },
            items: [me.riskGridTree, me.riskEdit]
        });

        me.callParent(arguments);
        
    }

});