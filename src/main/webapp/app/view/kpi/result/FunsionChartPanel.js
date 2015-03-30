/**
 * 图表面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.kpi.result.FunsionChartPanel', {
	extend : 'FHD.ux.FusionChartPanel',
	alias : 'widget.funsionChartPanel',

	// 初始化方法
	initComponent : function() {
		var me = this;
		me.id = 'funsionChartPanel';
		
		Ext.apply(me, {
			border : false,
			chartType : 'MSColumnLine3D',
			xmlData : me.xml
		});

		me.callParent(arguments);
	}
});