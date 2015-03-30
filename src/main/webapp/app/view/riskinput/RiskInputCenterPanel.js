/**
 * 
 * 工作计划左侧功能树
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.riskinput.RiskInputCenterPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.riskinputcenterpanel',
	
    layout:'fit',
    // 初始化方法
    initComponent: function() {
        var me = this;
       
        Ext.applyIf(me, {
        	border:true,
        	items:[Ext.widget('riskeventeditcardpanel',{id:'riskeventeditcardpanel'})]
        });

        me.callParent(arguments);
    }
});