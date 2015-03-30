/**
 * 
 * 行业主面板
 */

Ext.define('FHD.view.industry.IndustryMain', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.industryMain',

    requires: [
    	'FHD.view.industry.IndustryTree',
    	'FHD.view.industry.IndustryRightPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        me.id = 'industryMainId';
        me.industryRightPanel = Ext.widget('industryRightPanel');
        me.industryTree = Ext.widget('industryTree');
        
        Ext.apply(me, {
        	border:false,
        	layout: {
                type: 'border'
            },
            items: [me.industryTree, me.industryRightPanel]
        });

        me.callParent(arguments);
//        Ext.getCmp('industryCardId').industryTab.infoPanel.load(me.industryTree.treeId)
    }

});