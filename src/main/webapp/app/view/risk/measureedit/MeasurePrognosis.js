/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.measureedit.MeasurePrognosis',{
	extend : 'Ext.form.Panel',
    alias: 'widget.measureprognosis',
    border : false,
    width : 700,
    defaultType: 'textfield',
    bodyPadding: 5,
    margin: '7 10',
	initComponent : function() {
		var me = this;
		me.noteradio = Ext.create('FHD.ux.org.CommonSelector', {
			fieldLabel : '责任人',
			labelAlign : 'left',
			name:'empId',
			allowBlank : false,
			type : 'emp',
			multiSelect : false,
			anchor: '50%'
		});
		Ext.applyIf(me,{
			items :[{
            xtype: 'radiogroup',
            fieldLabel: '定期综合性预判',
            labelWidth: 100,
            items: [
                {
                    xtype: 'radiofield',
                    boxLabel: '是',
                    name : 'name'
                },
                {
                    xtype: 'radiofield',
                    boxLabel: '否',
                    name : 'name'
                }
            ],
            anchor : '50%'
        },{
            xtype: 'datefield',
            anchor: '50%',
            fieldLabel: '时间',
            labelWidth: 100
        }, {
            xtype: 'textfield',
            anchor: '50%',
            labelWidth: 100,
            fieldLabel: '模版'
        },me.noteradio]
		});
		me.callParent(arguments);
	}
});
