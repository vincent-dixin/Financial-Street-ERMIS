/**
 * 
 * GRID目标、指标、风险图
 */

Ext.define('FHD.view.risk.assess.utils.GridTreeStr', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.gridTreeStr',

    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        Ext.apply(me, {
			border:false,
            html : '<img src="images/risk/GridFhart.jpg"/>'
        });

        me.callParent(arguments);
    }

});