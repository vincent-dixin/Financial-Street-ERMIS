/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.RiskMeasureLaterPanel', {
    extend: 'Ext.Panel',
    alias: 'widget.riskmeasurelaterpanel',
    requires: [
    	'FHD.view.risk.measureedit.RiskClassBaseMainFieldSet'
    ],
    border : 0,
    autoScroll : true,
	autoWidth: true,
	autoHeight: true,
	layout: {
		type: 'vbox',
		align : 'stretch'
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
        me.riskclassbasemainfieldset = Ext.widget('riskclassbasemainfieldset');
        Ext.apply(me, {
            items: [
               me.riskclassbasemainfieldset
               ]
        });

        me.callParent(arguments);
    }
});