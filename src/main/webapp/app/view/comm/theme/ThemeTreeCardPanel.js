/**
 * 
 * 主题分析树卡片面板
 * 使用card布局
 * 
 * 
 * @author haojing
 */
Ext.define('FHD.view.comm.theme.ThemeTreeCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.themetreecardpanel',

    requires: [
        'FHD.view.comm.theme.ThemeTree'
    ],

    initComponent: function() {
        var me = this;

        me.themetree = Ext.widget('themetree',{id:'themetree'});
        
        
        Ext.apply(me, {
            items: [
                me.themetree
            ]
        });

        me.callParent(arguments);
    }

});