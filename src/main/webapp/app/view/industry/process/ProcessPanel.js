/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.process.ProcessPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.processPanel',

    requires: [
    	'FHD.view.industry.process.ProcessGridTree',
    	'FHD.view.industry.process.ProcessEdit'
    ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        me.id = 'processPanelId';
        me.processEdit = Ext.widget('processEdit');
        me.processGridTree = Ext.widget('processGridTree');
        
        Ext.apply(me, {
        	border:false,
        	height:FHD.getCenterPanelHeight()-5,
        	layout: {
                align: 'stretch',
                type: 'vbox'
            },
            items: [me.processGridTree, me.processEdit]
        });

        me.callParent(arguments);
        
    }

});