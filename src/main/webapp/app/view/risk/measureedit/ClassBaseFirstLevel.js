/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.ClassBaseFirstLevel', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.classbasefirstlevel',
    requires: [
    	'FHD.view.risk.measureedit.ClassBaseSecondLevel'
    ],
    layout : {
    	type : 'vbox',
    	align : 'stretch'
    },
	initComponent: function() {
		//定量
		var me = this;
		//定量fieldset
        me.firstTextField = Ext.widget('textfield',{
        	name : 'firsttextfield',
        	colspan : 5
        });
        me.addQuantitativeBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>增加下级</a>",
			colspan : 10,
			width : 100
        });
        me.delConfidence = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>",
			colspan : 5
        });
   		me.secondLevel = Ext.widget('classbasesecondlevel');
   		me.fieldContainer = Ext.widget('fieldcontainer',{
   			layout : {
   				type : 'hbox'
   			},
   			items : [me.firstTextField,
            		 me.addQuantitativeBtn,
            		{
                   		xtype:'tbspacer',
                   		flex : 1
                	},
            		 me.delConfidence
            		]
   		});
        Ext.apply(me, {
            items: [
            	me.fieldContainer,
            	me.secondLevel
            ]
        });
        me.callParent(arguments);
    },
    addQuantitative : function(){
    	var formPanel = this;
    	formPanel.add(Ext.widget('classbasesecondlevel'));
    },
    delSelf : function(){
    	var self = this;
    	upPanel = self.up('fieldset');
    	upPanel.remove(self);
    }
});