/**
 * 角色管理图片面板
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RoleCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.roleCardPanel',
    requires: [
         'FHD.view.sys.role.RoleTabPanel'
    ],
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.id = 'roleCardPanel';
    	me.roleTabPanel = Ext.widget('roleTabPanel');
    	me.roleTabPanel.getTabBar().insert(0, {
            xtype: 'tbfill'
       	});
    	Ext.apply(me, {
    		 xtype: 'cardpanel',
             border:false,
             activeItem : 0,
             items:[
               me.roleTabPanel
             ],
        });
        me.callParent(arguments);
    }
});