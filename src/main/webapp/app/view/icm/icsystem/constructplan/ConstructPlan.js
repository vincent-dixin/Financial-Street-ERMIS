/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructplan',
    requires: [
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
       	
        me.constructplanmenupanel = Ext.widget('constructplanmenupanel',{
        	region : 'west',
            split:true,
            width: 150,
            collapsible: false
        });
        
        me.constructplancenterpanel = Ext.widget('constructplancenterpanel',{
        	region : 'center'
        });
        
        Ext.applyIf(me, {
            items: [me.assessplanleftpanel,me.assessplancenterpanel]
        });

        me.callParent(arguments);
    }
});