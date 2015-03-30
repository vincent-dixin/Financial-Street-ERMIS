/**
 * 
 * 风险整理上下面板
 */

Ext.define('FHD.view.risk.assess.riskTidy.RiskTidyPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.riskTidyPanel',
    
    requires: [
               'FHD.view.risk.assess.utils.InfoNav',
               'FHD.view.risk.assess.riskTidy.RiskTidyCard'
              ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.infoNav = Ext.widget('infoNav',{height : 40});
        me.riskTidyCard = Ext.widget('riskTidyCard');
        
        Ext.apply(me, {
        	border:false,
        	region:'center',
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
            items: [me.infoNav, me.riskTidyCard]
        });

        me.callParent(arguments);
        
        me.on('resize',function(p){
        	me.riskTidyCard.setHeight(FHD.getCenterPanelHeight() - 40);
    	});
    }

});