/**
 * 
 * 风险整理操作面板
 */

Ext.define('FHD.view.risk.assess.riskTidy.RiskTidyOpe', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.riskTidyOpe',
    
    requires: [
				'FHD.view.risk.assess.riskTidy.RiskTidyGrid',
				'FHD.view.risk.assess.riskTidy.RiskTidyEdit'
              ],
    /**
     * 初始化方法
     * */
    initComponent: function() {
        var me = this;
        
        me.riskTidyGrid = Ext.widget('riskTidyGrid');
        me.riskTidyEdit = Ext.widget('riskTidyEdit');
        
        Ext.apply(me, {
        	border:false,
        	layout:{
                align: 'stretch',
                type: 'vbox'
    		},
            items: [me.riskTidyGrid, me.riskTidyEdit]
        });

        me.callParent(arguments);
        
        me.on('resize',function(p){
        	me.riskTidyGrid.setHeight(FHD.getCenterPanelHeight() - 330);
        	me.riskTidyGrid.setWidth(FHD.getCenterPanelWidth() - 200);
    	});
    }

});