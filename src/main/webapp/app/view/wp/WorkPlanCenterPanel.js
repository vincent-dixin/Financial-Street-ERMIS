/**
 * 
 * 工作计划左侧功能树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.wp.WorkPlanCenterPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workplancenterpanel',
	
    layout:'fit',
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       
        Ext.applyIf(me, {
        	items:[Ext.widget('workplandashboard')]
        });

        me.callParent(arguments);
    }

});