Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmSeven', {
    extend: 'FHD.view.icm.rectify.component.RectifyFollowListGrid',
    alias: 'widget.rectifyimprovebpmseven',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
    	me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});