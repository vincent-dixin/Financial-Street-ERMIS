Ext.define('FHD.view.monitor.TreeCardFace', {
	extend : 'Ext.panel.Panel',
	requires : [ 'FHD.view.monitor.CardFace' ],

	tree : null, // 左侧tree

	rightContainer : null, // 右侧面板

	frame : false,
	// 布局
	layout : {
		type : 'border'
	},

	// 初始化方法
	initComponent : function() {
		var me = this;

		// 创建左侧树
		Ext.apply(me.tree, {
			height : FHD.getCenterPanelHeight(),
			region : 'west',
			// width: 210,
			split : true,
			collapsible : true
		});

		// 右侧card布局面板
		me.rightContainer = Ext.create("FHD.view.monitor.CardFace", {
			region : 'center',
			itemArr : [],
			flex : 1
		});

		Ext.apply(me, {
			border : false,
			items : [ me.tree, me.rightContainer ]
		});
		me.callParent(arguments);

	}

});