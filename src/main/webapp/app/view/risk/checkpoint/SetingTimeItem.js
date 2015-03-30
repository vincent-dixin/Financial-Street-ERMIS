/**
 * 风险预览页面，其中嵌套了风险控制矩阵
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.checkpoint.SetingTimeItem', {
	extend: 'Ext.form.FieldSet',
	alias: 'widget.setingtimeitem',
    autoHeight : true,
    autoWidth : true,
    title : '检查点',
    checkboxToggle: true,
    layout: {
        columns: 10,
        type: 'table'
    },
	border : true,
    initComponent: function() {
        var me = this;
        me.delQuantification = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>",
			colspan : 2
        });
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'datefield',
                    colspan: 5,
                    fieldLabel: '预计检查日期'
                },
                {
                    xtype: 'label',
                    colspan: 1,
                    text: '提前'
                },
                {
                    xtype: 'textfield',
                    colspan: 2,
                    width: 30,
                    fieldLabel: ''
                },
                {
                    xtype: 'label',
                    colspan: 2,
                    text: '执行。'
                },
                {
                    xtype: 'textfield',
                    fieldLabel: '主要检查内容'
                },me.delQuantification
            ]
        });
        me.callParent(arguments);
    },
    delSelf : function(){
    	var self = this;
    	upPanel = self.up('fieldcontainer');
    	upPanel.remove(self);
    }

});