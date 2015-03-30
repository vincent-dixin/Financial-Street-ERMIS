/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.effective.EffectBaseFirstLevel', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.effectbasefirstlevel',
    requires: [
    	'FHD.view.risk.effective.EffectBaseSecondLevel'
    ],
    layout : {
    	type : 'vbox',
    	align : 'stretch'
    },
    autoWidth: true,
	initComponent: function() {
		//定量
		var me = this;
		//定量fieldset
        me.firstTextField = Ext.widget('textfield',{
        	value : '',
        	width : 300,
        	name : 'firsttextfield',
        	emptyText : '声誉方面影响' 
        });
        me.addQuantitativeBtn = Ext.widget('button',{
        	text : '增加下级',
        	handler : me.addQuantitative
        });
   		me.secondLevel = Ext.widget('effectbasesecondlevel');
   		var fieldContainer = Ext.widget('fieldcontainer',{
   			layout : {
   				type : 'hbox',
   				align : 'stretch'
   			},
   			items : [me.firstTextField
            		]
   		});
        Ext.apply(me, {
            items: [
            	fieldContainer,
            	me.secondLevel
            ]
        });
        me.callParent(arguments);
    },
    addQuantitative : function(){
    	var formPanel = this.up('effectbasefirstlevel');
    	formPanel.add(Ext.widget('effectbasesecondlevel'));
    }
});