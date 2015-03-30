/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.riskedit.RiskStandardManage',{
	extend : 'Ext.panel.Panel',
    alias: 'widget.riskstandardmanage',
	requires: [
       'FHD.view.risk.riskedit.RiskStandardButtonGroup',
       'FHD.view.risk.riskedit.StandardCardPanel'
    ],
	layout : 'border',
	autoWidth : true,
	border:false,
	initComponent : function() {
		var me = this;
		me.riskstandardbuttongroup = Ext.widget('riskstandardbuttongroup',{
            width: 100,
			region : 'west'
		});
		me.standardcardpanel = Ext.widget('standardcardpanel',{
			region : 'center'
		});
		Ext.applyIf(me,{
			items:[me.riskstandardbuttongroup,me.standardcardpanel]
		});
		me.riskstandardbuttongroup.items.items[0].addCls('menu-selected-btn');
	 	me.callParent(arguments);
	}
});
