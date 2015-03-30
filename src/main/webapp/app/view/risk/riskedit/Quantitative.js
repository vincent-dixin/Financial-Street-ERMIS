/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.riskedit.Quantitative', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.quantitative',
    requires: [
    ],
    border : 0,
	autoWidth: true,
	autoHeight: true,
	layout: {
		type: 'hbox'
	},
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		//定性
		me.quantitative = Ext.widget('textfield', {
			name : 'quantitative', 
			value: '',
			width:300
		});
		me.delQuantitative = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>"
        });
        Ext.apply(me, {
            items : [
            	me.quantitative,
            	{
                   xtype:'tbspacer',
                   flex:1
                },
                me.delQuantitative
            	]
        });
        me.callParent(arguments);
    },
    delSelf : function(){
    	debugger;
    	var me = this;
    	var self = me.up('fieldcontainer');
    	upPanel = me.up('fieldset');
    	upPanel.remove(me);
    }
});