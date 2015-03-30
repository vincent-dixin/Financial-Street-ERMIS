/**
 * 菜单管理TAB面板
 * 
 * @author 邓广义
 */
Ext.define('FHD.view.sys.menu.MenuManagementTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.menumanagementtabpanel',
    
    requires: [
               	'FHD.view.sys.menu.MenuManagementFormAddPanel'
           
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	//菜单添加
    	me.menuAddPanel = Ext.widget('menumanagementformaddpanel',{title:'基本信息'});
    	
    	
    	
    	Ext.apply(me, {
            deferredRender: false,
            activeTab: 0,  
            items: [me.menuAddPanel],
            plain: true
        });
    	
    	
        me.callParent(arguments);
    }
});