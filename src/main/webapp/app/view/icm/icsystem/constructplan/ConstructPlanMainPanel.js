/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.icm.icsystem.constructplan.ConstructPlanMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.constructplanmainpanel',

    requires: [
    	'FHD.view.icm.icsystem.constructplan.ConstructPlanCenterPanel',
    	'FHD.view.icm.icsystem.constructplan.ConstructPlanMenuPanel'
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
        	id:'constructplancenterpanel',
        	region : 'center',
        	border:false
        });
        
        Ext.applyIf(me, {
            items: [me.constructplanmenupanel,me.constructplancenterpanel]
        });

        me.callParent(arguments);
    }
});