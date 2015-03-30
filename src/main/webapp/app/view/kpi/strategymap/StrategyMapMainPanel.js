Ext.define('FHD.view.kpi.strategymap.StrategyMapMainPanel', {
    extend: 'Ext.Component',
    alias: 'widget.strategymapmainpanel',
    
    initComponent: function() {
    	var me = this;
    	
    	Ext.applyIf(me,{
    		html:'<iframe frameborder="0" height="100%" width="100%" noresize  src="' + __ctxPath + '/pages/kpi/test.jsp"/>'
    	});
    	
    	me.callParent(arguments);
    }
    
});
    