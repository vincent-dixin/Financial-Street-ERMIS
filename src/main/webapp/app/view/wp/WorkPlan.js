/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.wp.WorkPlan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workplan',

    requires: [
    	'FHD.view.wp.WorkPlanLeftPanel',
    	'FHD.view.wp.WorkPlanCenterPanel'
    ],
	
    
    frame: false,
    
    // 布局
    layout: {
        type: 'border'
    },
    
    border : false,
    
    // 初始化方法
    initComponent: function() {
        var me = this;
       	
        me.workplanleftpanel = Ext.widget('workplanleftpanel',{
        	region : 'west',
            split:true,
            width: 150,
            collapsible: false
        });
        
        me.workplancenterpanel = Ext.widget('workplancenterpanel',{
        	region : 'center'
        });
        
        Ext.applyIf(me, {
            items: [me.workplanleftpanel,me.workplancenterpanel]
        });

        me.callParent(arguments);
    }

});