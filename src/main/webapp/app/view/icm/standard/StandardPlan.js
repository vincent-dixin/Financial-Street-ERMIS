/**
 * 
 * 内控标准
 * 
 * @author 元杰
 */
Ext.define('FHD.view.icm.standard.StandardPlan', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.standardplan',

    requires: [
    	'FHD.view.icm.standard.StandardPlanLeftPanel',
    	'FHD.view.icm.standard.StandardPlanCenterPanel'
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
       	
        me.standardplanleftpanel = Ext.widget('standardplanleftpanel',{
        	region : 'west',
            split:true,
            width: 150,
            collapsible: false
        });
        
        me.standardplancenterpanel = Ext.widget('standardplancenterpanel',{
        	id:'standardplancenterpanel',
        	region : 'center'
        });
        
        Ext.applyIf(me, {
            items: [me.standardplanleftpanel,me.standardplancenterpanel]
        });

        me.callParent(arguments);
    }
});