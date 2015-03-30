/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.RiskMeasureNowPanel', {
    extend: 'Ext.Panel',
    alias: 'widget.riskmeasurenowpanel',
    requires: [
    	'FHD.view.risk.measureedit.MeaSureEditField'
    ],
    border : 0,
    autoScroll : true,
	autoWidth: true,
	autoHeight: true,
	margin: '7 10',
	layout: {
		type: 'vbox',
		align : 'stretch'
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
        me.measureeditfield = Ext.widget('measureeditfield');
        Ext.apply(me, {
            items: [
               me.measureeditfield
               ]
        });
        me.callParent(arguments);
    }
});