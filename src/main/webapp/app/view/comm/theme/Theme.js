Ext.define('FHD.view.comm.theme.Theme', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.theme',

    requires: [
    	'FHD.view.comm.theme.ThemeCenterCardPanel',
        'FHD.view.comm.theme.ThemeTreeCardPanel'
    ],
//	
    
    frame: false,
    
    // 布局
    layout: {
        type: 'border'
    },

    // 初始化方法
    initComponent: function() {
        var me = this;
        
        
        me.themecentercardpanel = Ext.widget('themecentercardpanel',{
        	border : false,
            region: 'center',
            id:'themecentercardpanel'
        });
        
        me.themetreecardpanel = Ext.widget('themetreecardpanel',{
        	border : false,
        	flex: 1,
        	id:'themetreecardpanel'
        });
        
        // 使用的是vbox布局，一个是metrictreecardpanel,其他是按钮，更改了按钮的样式，使它看见起来想手风琴布局
        me.leftpanel = Ext.widget('panel',{
        	// 右边框样式
        	style:'border-right: 1px  #99bce8 solid !important;',
        	border : false,
            xtype: 'panel',
            region: 'west',
            split:true,
            width: 210,
            defaults: {
                height: 30,
                textAlign: 'left',
                style:'border-top: 1px  #f3f7fb solid !important;border-bottom: 1px  #99bce8 solid !important;',
              	cls:'aaa-btn'
            },
            layout: {
                align: 'stretch',
                type: 'vbox'
            },
            collapsible: true,
            title: '主题分析',
            items: [me.themetreecardpanel
            ]
        });
        
        
        
        Ext.applyIf(me, {
            items: [me.themecentercardpanel,me.leftpanel]
        });

        me.callParent(arguments);
    }


});