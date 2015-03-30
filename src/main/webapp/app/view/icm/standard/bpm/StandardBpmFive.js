Ext.define('FHD.view.icm.standard.bpm.StandardBpmFive', {
    extend: 'FHD.view.icm.standard.StandardEditStepFive',
    alias: 'widget.standardbpmfive',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId, me.executionId);
    }
});