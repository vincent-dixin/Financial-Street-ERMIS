Ext.define('FHD.view.icm.standard.bpm.StandardBpmTwo', {
    extend: 'FHD.view.icm.standard.StandardEditStepTwo',
    alias: 'widget.standardbpmtwo',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId, me.executionId);
    }
});