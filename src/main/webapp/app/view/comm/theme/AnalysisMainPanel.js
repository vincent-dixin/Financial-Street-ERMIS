Ext.define('FHD.view.comm.theme.AnalysisMainPanel', {
    extend: 'Ext.container.Container',
    alias: 'widget.analysismainpanel',
    layout:'fit',
    
    paramObj:{},
    
    requires: [
               'FHD.view.comm.theme.AnalysisCardPanel'
           ],
           
    initParam:function(paramObj){
	   	var me = this;
	   	me.paramObj = paramObj;
    },
   
    initComponent: function() {
        var me = this;
        me.analysiscardpanel = Ext.widget('analysiscardpanel',{id : 'analysiscardpanel',height:FHD.getCenterPanelHeight()-40});
        me.navigationBar = Ext.create('Ext.scripts.component.NavigationBars',{
        	type: 'theme',
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
    			html:'<div id="analysismaincontainer" class="navigation"></div>',
	            listeners : {
	            	afterrender: function(){
	            	}
	            }
    		},
    		me.analysiscardpanel]
        });
    
    
        me.callParent(arguments);
    
    },
    
    reLoadNav:function(){
    	var me = this;
    	me.navigationBar.renderHtml('analysismaincontainer','主题分析', '', 'theme');
    	
    }

});