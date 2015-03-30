/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.riskedit.StandardCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.standardcardpanel',
    requires: [
    	'FHD.view.risk.riskedit.HistoryRiskStandardPanel',
    	'FHD.view.risk.riskedit.LatentRiskStandardPanel'
    ],
    autoWidth : true,
    initComponent: function() {
        var me = this;
        me.historyriskstandardpanel = Ext.widget('historyriskstandardpanel');
        me.latentriskstandardpanel = Ext.widget('latentriskstandardpanel');
        
        Ext.apply(me, {
            items: [
                me.historyriskstandardpanel,me.latentriskstandardpanel
            ]
        });

        me.callParent(arguments);
    }

});