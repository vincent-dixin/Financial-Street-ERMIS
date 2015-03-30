/**
 * 
 * 流程节点管理器
 * 使用border布局
 * @author 宋佳
 */
Ext.define('FHD.view.icm.icsystem.FlowMainManage', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.flowmainmanage',

    requires: [
    	'FHD.view.icm.icsystem.FlowTree',
        'FHD.view.icm.icsystem.FlowTabMainPanel'
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
        me.flowtabmainpanel = Ext.widget('flowtabmainpanel',{id:'flowtabmainpanel',
			region : 'center',
			title:'操作区'
		});
        me.flowtree = Ext.widget('flowtree',{
        	border : true,
        	width:240,
        	split : true,
        	region: 'west'
        });
        Ext.applyIf(me, {
            items: [me.flowtree,me.flowtabmainpanel]
        });
        me.callParent(arguments);
    }

});