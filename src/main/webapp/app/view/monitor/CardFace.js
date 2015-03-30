Ext.define('FHD.view.monitor.CardFace', {
	extend : 'FHD.ux.CardPanel',

	requires : [

	],

	itemArr : [],

	// 初始化方法
	initComponent : function() {
		var me = this;

		Ext.apply(me, {
			items : me.itemArr
		});

		me.callParent(arguments);

	}

});