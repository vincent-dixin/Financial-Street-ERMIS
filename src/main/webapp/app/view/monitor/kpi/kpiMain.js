Ext.define('FHD.view.monitor.kpi.kpiMain', {
    extend: 'Ext.container.Container',
    layout:'fit',
    
    paramObj:{},
    
    requires: [
              ],
           
    initParam:function(paramObj){
	   	var me = this;
	   	me.paramObj = paramObj;
    },
   
    initComponent: function() {
        var me = this;
        if(Ext.getCmp('kpicardpanel')==null){
        	me.kpicardpanel = Ext.create('FHD.view.monitor.kpi.kpiCard',{id : 'kpicardpanel',height:FHD.getCenterPanelHeight()-40});
        }
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