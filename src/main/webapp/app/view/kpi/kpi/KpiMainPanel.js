Ext.define('FHD.view.kpi.kpi.KpiMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.kpimainpanel',
    layout:'fit',
    
    paramObj:{},
    
    requires: [
               'FHD.view.kpi.kpi.KpiCardPanel'
           ],
           
    initParam:function(paramObj){
	   	var me = this;
	   	me.paramObj = paramObj;
    },
   
    initComponent: function() {
        var me = this;
        me.kpicardpanel = Ext.widget('kpicardpanel',{id : 'kpicardpanel',height:FHD.getCenterPanelHeight()-40});
        me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'sc',
        	id : ''
        });
        Ext.apply(me, {
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
    		items:[{
    			xtype:'box',
    			height:40,
    			style : 'border-left: 1px  #99bce8 solid;',
    			html:'<div id="kpimaincontainer" class="navigation"></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.kpicardpanel]
        });
    
    
        me.callParent(arguments);
    
    },
    
    reLoadNav:function(type){
    	var me = this;
    	if("sc"==type){
    		me.navigationBar.renderHtml('kpimaincontainer',Ext.getCmp('scorecardtab').paramObj.categoryid, '', 'sc');
    	}else if("kpitype"==type){
    		me.navigationBar.renderHtml('kpimaincontainer', Ext.getCmp('kpitypemainpanel').paramObj.kpitypeid , '', 'kpi');
    	}else if("sm"==type){
    		me.navigationBar.renderHtml('kpimaincontainer', Ext.getCmp('strategyobjectivemainpanel').paramObj.smid, '', 'sm');
    	}
    }

});