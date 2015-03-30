Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmEight', {
    extend: 'FHD.view.icm.rectify.RectifyPlanCheck',
    alias: 'widget.rectifyimprovebpmeight',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});