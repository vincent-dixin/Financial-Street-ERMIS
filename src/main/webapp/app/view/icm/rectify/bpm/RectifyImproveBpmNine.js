Ext.define('FHD.view.icm.rectify.bpm.RectifyImproveBpmNine', {
    extend: 'FHD.view.icm.rectify.RectifyApproveJudge',
    alias: 'widget.rectifyimprovebpmnine',
    initComponent: function() {
        var me = this;
        Ext.apply(me,{
			isWindow: true
		});
		me.callParent(arguments);
        me.loadData(me.businessId,me.executionId);
    }
});