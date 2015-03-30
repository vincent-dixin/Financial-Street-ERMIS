/**
 * 
 * 度量标准树卡片面板
 * 使用card布局
 * 
 * 下级有两个组件 我的文件夹树（id : 'myfoldertree'）、 记分卡树（id : 'scorecardtree'）
 * 
 * @author 胡迪新
 */
Ext.define('FHD.ux.layout.multilayout.MultiLeftCardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.multileftcardpanel',
    requires: [
               
    ],

    /**
	 * public
	 * 接口属性
	 */
    itemArr:[],	//树的数组
    
    initComponent: function() {
        var me = this;

        Ext.apply(me, {
            items: me.itemArr
        });

        me.callParent(arguments);
    }

});