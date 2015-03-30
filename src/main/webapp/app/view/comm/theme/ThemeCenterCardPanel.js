/**
 * 主题分析中部控件
 * 使用card布局
 * 
 * @author haojing
 */
Ext.define('FHD.view.comm.theme.ThemeCenterCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.themecentercardpanel',

    requires: [
        'FHD.view.comm.theme.ThemeMainPanel',
        'FHD.view.comm.theme.AnalysisMainPanel'
    ],

    
    // 初始化方法
    initComponent: function() {
        var me = this;

        me.thememainpanel = Ext.widget('thememainpanel',{id : 'thememainpanel'});
        me.analysismainpanel = Ext.widget('analysismainpanel',{id : 'analysismainpanel'});
     
        
        
        Ext.apply(me, {
            items: [
            	me.thememainpanel,
            	me.analysismainpanel
		]
        });

        me.callParent(arguments);
        
    }

});