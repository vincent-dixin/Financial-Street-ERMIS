/**
 * 角色管理右面板
 * 
 * @author 翟辉
 */
Ext.define('FHD.view.sys.role.RoleRightPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.roleRightPanel', 
    requires: [
			'FHD.view.sys.role.RoleCardPanel'
    ],
   
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	me.id = 'roleRightPanel';
    	//图片面板
    	me.roleCardPanel = Ext.widget('roleCardPanel');
    	me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'role',
        });
        Ext.apply(me, {
        	region:'center',
        	border:false,
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[
    		    {
	    			xtype:'box',
	    			height:20,
	    			style : 'border-left: 1px  #99bce8 solid;',
	    			html:'<div id="rolerightPanelDiv"></div>',
		            
    		    },me.roleCardPanel]
        });
        me.callParent(arguments);
    }
});