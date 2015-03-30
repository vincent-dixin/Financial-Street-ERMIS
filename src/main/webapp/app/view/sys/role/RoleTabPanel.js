/**
 * 角色TAB面板
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RoleTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.roleTabPanel',
    
    requires: [
           'FHD.view.sys.role.RolePersonGridPanel',
           'FHD.view.sys.role.RoleBasicPanel',
           'FHD.view.sys.role.authority.AuthorityPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.id = 'roleTabPanel';
    	//角色下人员GRID
    	me.rolePersonGridPanel = Ext.widget('rolePersonGridPanel');
    	//角色基本信息
    	me.roleBasicPanel = Ext.widget('roleBasicPanel');
    	//角色授权
    	me.authorityPanel = Ext.widget('authorityPanel');
    	
    	me.authorityTree = Ext.getCmp('authorityTree');//得到角色授权树(左)
		me.authorityShowTree = Ext.getCmp('authorityShowTree');//得到权限树展示树(右)
    	
    	Ext.apply(me, {
            deferredRender: false,
            activeTab: 0,     // first tab initially active
            items: [me.rolePersonGridPanel,me.authorityPanel,me.roleBasicPanel],
            plain: true

        });
    	
    	me.rolePersonGridPanel.on('resize',function(p){
    		me.rolePersonGridPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
    	me.roleBasicPanel.on('resize',function(p){
    		me.roleBasicPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    	});
    	
    	me.authorityPanel.on('resize',function(p){
    		me.authorityPanel.setHeight(FHD.getCenterPanelHeight() - 51);
    		me.authorityShowTree.setHeight(FHD.getCenterPanelHeight() - 53);
    		me.authorityTree.setHeight(FHD.getCenterPanelHeight() - 53);
    	});
        me.callParent(arguments);
    }
});