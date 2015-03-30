/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.checkpoint.CheckPointManage',{
	extend : 'Ext.container.Container',
    alias: 'widget.checkpointmanage',
	requires: [
		'FHD.view.risk.checkpoint.CheckPointForm'
    ],
	layout : {
                type: 'vbox',
                align: 'stretch'
              },
    margin: '7 10',
    autoHeight : true,
    autoScroll : true,
    border : false,
	initComponent : function() {
		var me = this;
    	me.checkpointform = Ext.widget('checkpointform');
	 	Ext.applyIf(me,{
	 		items : [me.checkpointform]
	 	});
	 	me.callParent(arguments);
	}
});
