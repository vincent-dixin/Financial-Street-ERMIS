/**
 * 
 * 行业标签面板
 */

Ext.define('FHD.view.industry.IndustryTab',{
	extend: 'Ext.tab.Panel',
    alias: 'widget.industryTab',
    
    plain: true,
    
    requires: [
               'FHD.view.industry.InfoPanel',
               'FHD.view.industry.kpi.KpiGridTree',
               'FHD.view.industry.process.ProcessGridTree',
               'FHD.view.industry.risk.RiskGridTree'
              ],
              
    getPanel : function(obj, widgetId){
    	if(obj.items.length == 0){
    		debugger;
    		obj.add(Ext.widget(widgetId));
    	}
    },
         
    initComponent: function () {
        var me = this;
        
        me.id = 'industryTabId';
        me.infoPanelContainer = Ext.create('Ext.container.Container', {id:'infoPanelContainerId', title : '基础信息'});
		me.kpiGridTreeContainer =  Ext.create('Ext.container.Container', {id:'kpiGridTreeContainerId', title : '指标'});
		me.processGridTreeContainer = Ext.create('Ext.container.Container', {id:'processGridTreeContainerId', title : '流程'});
		me.riskGridTreeContainer =  Ext.create('Ext.container.Container', {id:'riskGridTreeContainerId', title : '风险'});
        
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
        	listeners:{
        		afterrender : function(obj){
        			me.getPanel(obj.items.items[0], 'infoPanel');
        		},
        		
                tabchange:function(tp, obj){
                    if(obj.id == 'infoPanelContainerId'){
                    	me.getPanel(obj, 'infoPanel');
                    }else if(obj.id == 'kpiGridTreeContainerId'){
                    	me.getPanel(obj, 'kpiGridTree');
                    	Ext.getCmp('industryCardId').kpiInfoTab.getPanel(Ext.getCmp('industryCardId').kpiInfoTab.kpiEditContainer, 'kpiEdit');
                    }else if(obj.id == 'processGridTreeContainerId'){
                    	me.getPanel(obj, 'processGridTree');
                    	Ext.getCmp('industryCardId').processInfoTab.getPanel(Ext.getCmp('industryCardId').processInfoTab.processEditContainer, 'processEdit');
                    }else if(obj.id == 'riskGridTreeContainerId'){
                    	me.getPanel(obj, 'riskGridTree');
                    	Ext.getCmp('industryCardId').riskInfoTab.getPanel(Ext.getCmp('industryCardId').riskInfoTab.riskEditContainer, 'riskEdit');
                    }
                } 
            },
            items: [me.infoPanelContainer, me.kpiGridTreeContainer, me.processGridTreeContainer, me.riskGridTreeContainer]
        });
        
        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
    }
});