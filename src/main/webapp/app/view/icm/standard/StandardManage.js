/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.icm.standard.StandardManage',{
	extend : 'Ext.panel.Panel',
    alias: 'widget.standardmanage',
	
	requires: [
       'FHD.view.icm.standard.StandardMainPanel',
       'FHD.view.icm.standard.StandardTreeLeftPanel'
    ],
	layout : 'border',
	border:false,
	
	initComponent : function() {
		var me = this;
		
		me.standardTree = Ext.widget('standardtreeleftpanel',{
			collapsible: true,
            width: 265,
            split:true,
			region : 'west'
		});
		me.standardMainPanel = Ext.widget('standardmainpanel',{
			region : 'center',
			title:'操作区'
		});
		Ext.applyIf(me,{
			items:[me.standardTree,me.standardMainPanel]
		})
		
	 	me.callParent(arguments);
	}
});
