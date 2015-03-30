/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.measureedit.RiskMeasureManage',{
	extend : 'Ext.panel.Panel',
    alias: 'widget.riskmeasuremanage',
	requires: [
       'FHD.view.risk.measureedit.RiskMeasureButtonGroup',
       'FHD.view.risk.measureedit.MeasureCardPanel'
    ],
	layout : {
		type : 'border'
	},
	autoWidth : true,
	border:false,
	initComponent : function() {
		var me = this;
		me.riskmeasurebuttongroup = Ext.widget('riskmeasurebuttongroup',{
            width: 100,
            region : 'west'
		});
		me.measurecardpanel = Ext.widget('measurecardpanel',{
			region : 'center'
		});
		Ext.applyIf(me,{
			items:[me.riskmeasurebuttongroup,me.measurecardpanel]
		})
	 	me.callParent(arguments);
	}
});
