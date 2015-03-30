/**
 * 主面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.kpi.result.ManPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.manPanel',

	requires : [ 'FHD.view.kpi.result.ResultManPanel'],
	
	getResultManPanel : function(id, param){
		var resultManPanel = null;
		
		if(Ext.getCmp(id) == null){
			resultManPanel = Ext.widget('resultManPanel').load(param);
		}else{
			resultManPanel = Ext.getCmp('resultManPanel').load(param);
		}
		
		return resultManPanel;
	},
	
	load : function(param){
		var me = this;
		
		if(Ext.getCmp('resultManPanel') != null){
			me.removeAll();
			
		}
		
		me.resultManPanel = me.getResultManPanel('resultManPanel', param);
		me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars');
		
		var nav = {
			xtype:'box',
			height:40,
			style : 'border-left: 1px  #99bce8 solid;',
			html:'<div id="gatherresulttableNavDiv" class="navigation"></div>',
            listeners : {
            	afterrender: function(){
            		if("sm" == param.type){
            			me.navigationBar.renderHtml('gatherresulttableNavDiv', param.sm.smId, param.sm.smName, 'sm');
		        	}else if("kpitype" == param.type){
		        		me.navigationBar.renderHtml('gatherresulttableNavDiv', param.kpiType.kpiTypeId, param.kpiType.kpiTypeName, 'kpi');
		        	}else if("myfolder" == param.type){
		        	}else{
		        		me.navigationBar.renderHtml('gatherresulttableNavDiv', param.category.categoryId, param.category.categoryName, 'sc')
		        	}
            	}
            }
		};
		
		me.add(nav);
		me.add(me.resultManPanel);
		
		me.resultManPanel.on('resize',function(p){
			me.resultManPanel.setHeight(FHD.getCenterPanelHeight() - 47);
		});
		
		return me;
	},
	
	// 初始化方法
	initComponent : function() {
		var me = this;
		me.id = 'manPanel';
		
		Ext.apply(me, {
			border : true,
			layout:{
                align: 'stretch',
                type: 'vbox'
    		},
			items : []
		});

		me.callParent(arguments);
	}
});