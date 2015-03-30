/**
 * 
 * 行业右侧面板
 */

Ext.define('FHD.view.industry.IndustryRightPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.industryRightPanel',

    requires: [
    	'FHD.view.industry.IndustryCard'
    ],

    load : function(treeId){
    	var me = this;
    	
    	if(Ext.getCmp('industryCardId') != null){
    		me.removeAll();
    	}else{
    		Ext.widget('industryCard');
    	}
    	
    	var navigationBar = Ext.create('Ext.scripts.component.NavigationBars');
        
        var nav = {
    			xtype:'box',
    			height:20,
    			style : 'border-left: 1px  #99bce8 solid;',
    			html:'<div id="industryNavDiv" class="navigation"></div>',
                listeners : {
                	afterrender: function(){
                		navigationBar.renderHtml('industryNavDiv', treeId, '', 'sm');
                	}
                }
    	};
        
        me.add(nav);
        me.add(Ext.getCmp('industryCardId'));
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        
        
        me.id = 'industryRightPanelId';
//        me.industryTab = Ext.widget('industryTab');
        Ext.applyIf(me, {
        	border:false,
        	region:'center',
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
            items: []
        });

        me.callParent(arguments);
    }

});