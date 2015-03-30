/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.measureedit.ClassBaseSecondLevel', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.classbasesecondlevel',
    requires: [
    ],
	autoHeight: true,
	layout: {
        type: 'hbox'
    },
	initComponent: function() {
		//定量
		var me = this;
		me.addSetPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>设定预案/</a>"
        });
		me.addLookPlanBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantitative()'>查看预案/</a>"
        });
		me.delMySelfBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delMySelf()'>删除</a>"
        });
		//定量fieldset
        me.secondTextField = Ext.widget('textfield',{
        	value : '',
        	name : 'secondtextfield'
        });
        Ext.apply(me, {
            items: [
               {
               		xtype : 'image',
               		src : __ctxPath+'/images/makegrid.png'
               },
               me.secondTextField,
               {
                   xtype:'tbspacer',
                   flex : 1
                },
               me.addSetPlanBtn,
               me.addLookPlanBtn,
               me.delMySelfBtn
            ]
        });
        me.callParent(arguments);
    },
    delMySelf : function(){
    	this.up('fieldcontainer').remove(this);
    }
});