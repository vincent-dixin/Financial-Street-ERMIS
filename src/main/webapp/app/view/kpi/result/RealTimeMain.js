
Ext.define('FHD.view.kpi.result.RealTimeMain', {
	extend : 'Ext.container.Container',
	alias : 'widget.realtimemain',

	requires : ['FHD.view.kpi.result.RealTimeGrid'],
	
	createTabPanel:function(){
		var me = this;
		var grid = Ext.widget('realtimegrid',{
			searchable:false,
			height:531,
			kpiName:me.name,
			kpiId:me.kpiId
		});
		return grid;
		
	},

	// 初始化方法
	initComponent : function() {
		var me = this;
		me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
			type: 'sc',
        	id : '' 
		});
		var naDiv = document.getElementById("realTimeResultNavDiv");
		if(naDiv!=null){
    		$(naDiv).remove();
    	}
		var nav = {
				xtype:'box',
				height:40,
				style : 'border-left: 1px  #99bce8 solid;',
				html:'<div id="realTimeResultNavDiv" class="navigation"></div>',
			    listeners : {
			    	afterrender: function(){
			    		me.navigationBar.renderHtml('realTimeResultNavDiv',me.id, me.name, 'sc');
			    	}
			    }
			};
		var panel = me.createTabPanel();
		Ext.apply(me, {
			border : true,
			layout:{
                align: 'stretch',
                type: 'vbox'
    		},
			items : [	nav
						,
						panel
			         ]
		});
		me.callParent(arguments);
	}
});