/**
 * 结果主面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.kpi.result.ResultManPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.resultManPanel',

	requires : [ 'FHD.view.kpi.result.ResultCardPanel' ],
	
	getResultCardPanel : function(id, param){
		var resultCardPanel = null;
		
		if(Ext.getCmp(id) == null){
			resultCardPanel = Ext.widget('resultCardPanel').load(param);
		}else{
			resultCardPanel = Ext.getCmp('resultCardPanel').load(param);
		}
		
		return resultCardPanel;
	},
	
	load : function(param){
		var me = this;
		if(Ext.getCmp('resultCardPanel') != null){
			me.removeAll();
		}
		me.resultCardPanel = me.getResultCardPanel('resultCardPanel', param);
		
		me.add(me.resultCardPanel);
		
		return me;
	},
	
	// 初始化方法
	initComponent : function() {
		var me = this;
		me.id = 'resultManPanel';
		
		Ext.apply(me, {
			border : false,
			layout: {
    	        type: 'fit'
    	    },
			items : []
		});

		me.callParent(arguments);
	}
});