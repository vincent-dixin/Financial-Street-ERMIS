/**
 * 系统菜单主面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.sys.menu.MenuManagementMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.menumanagementmainpanel',
    
    requires: [
           'FHD.view.sys.menu.MenuManagementTreePanel',
           'FHD.view.sys.menu.MenuManagementRightPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	//右侧面板
    	me.treePanel = Ext.widget('menumanagementtreepanel');
    	//菜单树
    	me.rightPanel = Ext.widget('menumanagementrightpanel');
    	

    	Ext.apply(me, {
            border:false,
     		layout: {
     	        type: 'border',
     	        padding: '0 0 5	0'
     	    },
     	    items:[me.treePanel,me.rightPanel]
        });
    	
        me.callParent(arguments);
    }
});