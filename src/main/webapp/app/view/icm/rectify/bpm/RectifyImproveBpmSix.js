Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmSix', {
    extend: 'FHD.view.icm.rectify.component.RectifyFileCheck',
    alias: 'widget.rectifyimprovebpmsix',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});