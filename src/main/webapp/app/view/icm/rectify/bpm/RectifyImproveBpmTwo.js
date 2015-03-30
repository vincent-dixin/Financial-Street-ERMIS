Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmTwo', {
    extend: 'FHD.view.icm.rectify.RectifyApprove',
    alias: 'widget.rectifyimprovebpmtwo',
    initComponent: function() {
    	debugger;
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});