Ext.define('FHD.view.icm.standard.bpm.StandardBpmFour', {
    extend: 'FHD.view.icm.standard.StandardEditStepFour',
    alias: 'widget.standardbpmfour',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId, me.executionId);
    }
});