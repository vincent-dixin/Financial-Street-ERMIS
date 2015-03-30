/**
 * 计划发布
 */
Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmFour', {
    extend: 'FHD.view.icm.rectify.RectifyPublish',
    alias: 'widget.rectifyimprovebpmfour',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});