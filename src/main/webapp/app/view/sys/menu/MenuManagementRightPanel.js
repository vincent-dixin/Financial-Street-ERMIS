/**
 * 菜单管理右面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.sys.menu.MenuManagementRightPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.menumanagementrightpanel',
    
    requires: [
			'FHD.view.sys.menu.MenuManagementTabPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.tabPanel = Ext.widget('menumanagementtabpanel');
    	
    	
        Ext.apply(me, {
        	region:'center',
        	layout:{
        		padding:'1 0 0 0',
                align: 'stretch',
                 type: 'vbox'
    		},
    		items:[me.tabPanel]
        });

        me.callParent(arguments);
    }
});