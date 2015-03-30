/**
 * 
 * 风险整理主体内容面板
 */

Ext.define('FHD.view.risk.assess.riskTidy.RiskTidyMan', {
    extend: 'Ext.form.Panel',
    alias: 'widget.riskTidyMan',
    
    requires: [
               'FHD.view.risk.assess.utils.AssessTree',
               'FHD.view.risk.assess.riskTidy.RiskTidyPanel'
              ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.assessTree = Ext.widget('assessTree');
        me.riskTidyPanel = Ext.widget('riskTidyPanel');
        
        Ext.apply(me, {
        	border:true,
        	layout: {
                type: 'border'
            },
            items: [me.assessTree, me.riskTidyPanel]
        });

        me.callParent(arguments);
    }

});