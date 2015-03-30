/**
 * 角色管理主面板
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RoleManPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.roleManPanel',
   
    requires: [
           'FHD.view.sys.role.RoleTreePanel',
           'FHD.view.sys.role.RoleRightPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	//me.id = 'roleManPanel';
    	//右侧面板
    	me.roleRightPanel = Ext.widget('roleRightPanel');
    	//角色树
    	me.roleTreePanel = Ext.widget('roleTreePanel');
    	Ext.apply(me, {
            border:false,
     		layout: {
     	        type: 'border',
     	        padding: '0 0 5	0'
     	    },
     	    items:[me.roleTreePanel,me.roleRightPanel]
        });
    	
        me.callParent(arguments);
    }
});