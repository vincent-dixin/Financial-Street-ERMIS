/**
 * 
 * 工作计划
 * 
 * @author 胡迪新
 */
Ext.define('FHD.view.riskinput.scheme.SchemeMainPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.schememainpanel',

    requires: [
    	'FHD.view.riskinput.scheme.SchemeEditCardPanel'
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
        me.schemeeditcardpanel = Ext.widget('schemeeditcardpanel',{
        	id:'schemecenterpanel',
        	region : 'center',
        	border:false
        });
        
        Ext.applyIf(me, {
            items: [me.schemeeditcardpanel]
        });

        me.callParent(arguments);
    }
});