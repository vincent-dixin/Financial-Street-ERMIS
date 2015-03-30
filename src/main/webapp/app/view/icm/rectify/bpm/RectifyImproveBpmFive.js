/**
 * 上报方案
 */
Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmFive', {
    extend: 'FHD.view.icm.rectify.component.RectifyFileView',
    alias: 'widget.rectifyimprovebpmfive',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});