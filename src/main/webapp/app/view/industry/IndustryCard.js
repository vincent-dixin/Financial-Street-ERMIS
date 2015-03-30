/**
 * 
 * 行业卡片面板
 */

Ext.define('FHD.view.industry.IndustryCard',{
	extend: 'FHD.ux.CardPanel',
    alias: 'widget.industryCard',
    
    requires: [
               'FHD.view.industry.IndustryTab',
               'FHD.view.industry.kpi.KpiInfoTab',
               'FHD.view.industry.process.ProcessInfoTab',
               'FHD.view.industry.risk.RiskInfoTab'
              ],
              
    showIndustryTab : function(){
  		var me = this;
  		me.getLayout().setActiveItem(me.items.items[0]);
  	},         
              
	showKpiInfoTab : function(){
		var me = this;
		me.getLayout().setActiveItem(me.items.items[1]);
	},
	
	showProcessInfoTab : function(){
		var me = this;
		me.getLayout().setActiveItem(me.items.items[2]);
	},
	
	showRiskInfoTab : function(){
		var me = this;
		me.getLayout().setActiveItem(me.items.items[3]);
	},
              
    initComponent: function () {
        var me = this;
        
        me.id = 'industryCardId';
        me.industryTab = Ext.widget('industryTab');
        me.kpiInfoTab = Ext.widget('kpiInfoTab');
        me.processInfoTab = Ext.widget('processInfoTab');
        me.riskInfoTab = Ext.widget('riskInfoTab');
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.industryTab, me.kpiInfoTab, me.processInfoTab, me.riskInfoTab]
        });
        
        me.callParent(arguments);
    }

});