/**
 * 
 * 规章制度数据页面
 * 使用border布局
 * 
 * 左侧是RuleTree,右侧是RuleEditPanel
 * 
 * @author 元杰
 */
Ext.define('FHD.view.icm.rule.RuleMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.rulemainpanel',

    requires: [
    	'FHD.view.icm.rule.RuleTree',
    	'FHD.view.icm.rule.RuleEditPanel'
    ],
    
    frame: false,
    // 布局
    layout: {
        type: 'border'
    },
    border : false,
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        
        me.ruleeditpanel = Ext.widget('ruleeditpanel',{
        	border : true,
            region: 'center'
        });
        
        me.ruletree = Ext.widget('ruletree',{
        	border : true,
        	width:240,
        	split : true,
        	region: 'west'
        });
        
        
        Ext.applyIf(me, {
            items: [me.ruletree,me.ruleeditpanel]
        });

        me.callParent(arguments);
    }

});