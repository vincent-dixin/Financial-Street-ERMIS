/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.icm.assess.AssessPlan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.assessplan',

    requires: [
    	'FHD.view.icm.assess.AssessPlanLeftPanel',
    	'FHD.view.icm.assess.AssessPlanCenterPanel'
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
       	
        me.assessplanleftpanel = Ext.widget('assessplanleftpanel',{
        	region : 'west',
            split:true,
            width: 150,
            collapsible: false
        });
        
        me.assessplancenterpanel = Ext.widget('assessplancenterpanel',{
        	region : 'center'
        });
        
        Ext.applyIf(me, {
            items: [me.assessplanleftpanel,me.assessplancenterpanel]
        });

        me.callParent(arguments);
    }
});