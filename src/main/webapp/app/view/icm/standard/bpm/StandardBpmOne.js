Ext.define('FHD.view.icm.standard.bpm.StandardBpmOne', {
    extend: 'FHD.view.icm.standard.StandardEditStepOne',
    alias: 'widget.standardbpmone',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId, me.executionId);
    }
});