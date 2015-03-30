/**
 * 整改优化主页面
 * 
 */
Ext.define('FHD.view.icm.rectify.RectifyImprovePanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.rectifyimprovepanel',

    requires: [
    	'FHD.view.icm.rectify.RectifyImproveLeftPanel',
    	'FHD.view.icm.rectify.RectifyImproveCenterPanel'
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
       	
        me.recityImproveLeftPanel = Ext.widget('rectifyimproveleftpanel',{
        	region : 'west',
            split:true,
            width: 150,
            collapsible: false
        });
        
        me.rectifyImproveCenterPanel = Ext.widget('rectifyimprovecenterpanel',{
        	region : 'center'
        });
        
        Ext.applyIf(me, {
            items: [me.recityImproveLeftPanel,me.rectifyImproveCenterPanel]
        });

        me.callParent(arguments);
    }
});