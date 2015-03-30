/**
 * 
 * 风险管控措施
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.MeasureCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.measurecardpanel',
    requires: [
    	'FHD.view.risk.measureedit.RiskMeasureBeforePanel',
    	'FHD.view.risk.measureedit.RiskMeasureNowPanel',
    	'FHD.view.risk.measureedit.RiskMeasureLaterPanel'
    ],
    autoWidth : true,
    initComponent: function() {
        var me = this;
        me.riskmeasurebeforepanel = Ext.widget('riskmeasurebeforepanel');
        me.riskmeasurenowpanel = Ext.widget('riskmeasurenowpanel');
        me.riskmeasurelaterpanel = Ext.widget('riskmeasurelaterpanel');
        Ext.apply(me, {
            items: [
                me.riskmeasurebeforepanel,
                me.riskmeasurenowpanel,
                me.riskmeasurelaterpanel
            ]
        });
        me.callParent(arguments);
    }

});