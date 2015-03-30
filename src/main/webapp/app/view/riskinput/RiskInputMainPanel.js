/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.riskinput.RiskInputMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.riskinputmainpanel',

    requires: [
    	'FHD.view.riskinput.RiskInputCenterPanel',
    	'FHD.view.riskinput.RiskInputMenuPanel'
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
        me.riskinputmenupanel = Ext.widget('riskinputmenupanel',{
        	region : 'west',
            split:true,
            width: 150,
            collapsible: false
        });
        me.riskinputmenupanel.items.items[0].addCls('menu-selected-btn');
        me.riskinputcenterpanel = Ext.widget('riskinputcenterpanel',{
        	id:'riskinputcenterpanel',
        	region : 'center',
        	border:false
        });
        
        Ext.applyIf(me, {
            items: [me.riskinputmenupanel,me.riskinputcenterpanel]
        });

        me.callParent(arguments);
    }
});