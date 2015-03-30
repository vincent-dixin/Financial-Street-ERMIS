Ext.define('FHD.view.icm.standard.bpm.StandardBpmThree', {
    extend: 'FHD.view.icm.standard.StandardEditStepThree',
    alias: 'widget.standardbpmthree',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
        me.callParent(arguments);
        me.loadData(me.businessId, me.executionId);
    }
});