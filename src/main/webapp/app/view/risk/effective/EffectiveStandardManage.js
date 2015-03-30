/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.effective.EffectiveStandardManage', {
    extend: 'Ext.Panel',
    alias: 'widget.effectivestandardmanage',
    requires: [
    	'FHD.view.risk.effective.EffectiveBaseMainFieldSet'
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
        me.effectivebasemainfieldset = Ext.widget('effectivebasemainfieldset');
        Ext.apply(me, {
            items: [
               me.effectivebasemainfieldset
               ]
        });

        me.callParent(arguments);
    }
});