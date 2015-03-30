/**
 * 
 * 规章制度数据页面
 * 使用border布局
 * 
 * 左侧是RuleTree,右侧是RuleEditPanel
 * 
 * @author 元杰
 */
Ext.define('FHD.view.process.ProcessMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.processmainpanel',

    requires: [
    	'FHD.view.process.ProcessTree',
    	'FHD.view.process.ProcessEditPanel'
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
        me.processeditpanel = Ext.widget('processeditpanel',{
        	border : false,
            region: 'center'
        });
        
        me.processtree = Ext.widget('processtree',{
        	border : true,
        	width:240,
        	region: 'west'
        });
        
        
        Ext.applyIf(me, {
            items: [me.processtree,me.processeditpanel]
        });

        me.callParent(arguments);
    }

});