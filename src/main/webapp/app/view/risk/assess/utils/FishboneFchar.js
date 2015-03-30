/**
 * 
 * 鱼骨图FChar
 */

Ext.define('FHD.view.risk.utils.FishboneFchar', {
    extend: 'Ext.form.Panel',
    alias: 'widget.fishboneFchar',

    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
        Ext.apply(me, {
			border:false,
            html : '<img src="images/risk/fishbone.jpg"/>'
        });

        me.callParent(arguments);
    }

});