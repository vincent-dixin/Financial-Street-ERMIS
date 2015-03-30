/**
 * 风险预览页面，其中嵌套了风险控制矩阵
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.checkpoint.SetingTimeForm', {
	extend: 'Ext.form.FieldContainer',
	alias: 'widget.setingtimeform',
	requires : [
		'FHD.view.risk.checkpoint.SetingTimeItem'
    ],
    autoHeight : true,
    autoScroll : true,
    autoWidth : true,
    layout: {
        align : 'stretch',
        type: 'vbox'
    },
    border : false,
    bodyPadding: 10,
    initComponent: function() {
        var me = this;
        me.addSetingItem = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addSelf()'>增加检查点</a>",
			colspan : 2
        });
        me.setingtimeitem = Ext.widget('setingtimeitem',{title : '检查点1'});
        Ext.applyIf(me, {
            items: [
                me.setingtimeitem,
                me.addSetingItem
            ]
        });
        me.callParent(arguments);
    },
    addSelf : function(){
    	var me = this;
    	var num = me.items.length;   /* 获取容器内item数量 */
    	me.insert(num-1,Ext.widget('setingtimeitem',{title : '检查点' + num}));
    }
});
