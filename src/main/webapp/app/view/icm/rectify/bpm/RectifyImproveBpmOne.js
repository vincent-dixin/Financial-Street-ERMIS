Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmOne', {
    extend: 'FHD.view.icm.rectify.RectifyImproveContainer',
    alias: 'widget.rectifyimprovebpmone',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});