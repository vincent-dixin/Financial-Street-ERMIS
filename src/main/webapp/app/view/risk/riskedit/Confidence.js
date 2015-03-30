/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.riskedit.Confidence', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.confidence',
    requires: [
    ],
	autoHeight: true,
	layout: {
        type: 'hbox'
    },
    kpiname : '',
	initComponent: function() {
		//定量
		var me = this;
		me.confidence = Ext.widget('kpiselector', {
			name : 'confidence', 
			value: me.kpiname,
			labelText : '指标',
			width:300
		});
        me.delConfidence = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>"
        });
        me.confidenceCheckBoxGroup = Ext.widget('checkboxgroup',{
        	fieldLabel : '预警区间',
        	labelWidth : 50,
        	items : [ {
	                    xtype: 'checkboxfield',
	                    boxLabel: '红'
	                },
	                {
	                    xtype: 'checkboxfield',
	                    boxLabel: '黄'
	                },
	                {
	                    xtype: 'checkboxfield',
	                    boxLabel: '绿'
	                }]
        ,width : 200});
        me.region = Ext.widget('textfield',{
        	value : '',
        	width : 50
        });
        Ext.apply(me, {
            items: [
               me.confidence,
               {
                   xtype: 'hiddenfield',
                   width : 50
               },
               me.confidenceCheckBoxGroup,
               {
                   xtype: 'label',
                   text: '置信区间:',
                   width : 50
               },
               me.region,
               {
                   xtype:'tbspacer',
                   flex : 1
                },
               me.delConfidence
            ]
        });

        me.callParent(arguments);
    },
    delSelf : function(){
    	var self = this;
    	upPanel = self.up('fieldset');
    	upPanel.remove(self);
    }
});