/**
 * 风险中心card面板
 * @author zhengjunxiang
 */
Ext.define('FHD.ux.layout.multilayout.MultiRightCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.multirightcardpanel',

    requires: [
        
    ],
    
    /**
	 * public
	 * 接口属性
	 */
    itemArr:[],	//mainpanel的数组
    
    // 初始化方法
    initComponent: function() {
        var me = this;

        Ext.apply(me, {
            items: me.itemArr
        });

        me.callParent(arguments);
        
    }

});