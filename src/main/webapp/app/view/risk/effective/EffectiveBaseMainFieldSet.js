/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.effective.EffectiveBaseMainFieldSet',{
	extend : 'Ext.form.FieldSet',
    alias: 'widget.effectivebasemainfieldset',
	requires: [
		'FHD.view.risk.effective.EffectBaseFirstLevel',
		'FHD.view.risk.effective.QuantificationEffectFirstLevel'
    ],
	layout : {
                type: 'vbox',
                align: 'stretch'
              },
    margin: '7 10',
    autoHeight : true,
	title : '风险分类依据',
	initComponent : function() {
		var me = this;
    	me.firstLevel = Ext.widget('effectbasefirstlevel',{autoWidth : true});
        //定性fieldset
        me.quantitativeFieldset = Ext.widget('fieldset', {
        	autoHeight : true,
            title : '定性',
            items : [
            	me.firstLevel
            ]
        });
        me.quantificationeffectfirstlevel = Ext.widget('quantificationeffectfirstlevel');
        me.quantificationFieldset = Ext.widget('fieldset', {
        	checkboxToggle: false,
        	layout : {
                type: 'column'
            },
            title : '定量',
            items : [me.quantificationeffectfirstlevel]
        });
		Ext.applyIf(me,{
			items:[me.quantitativeFieldset,me.quantificationFieldset]
		});
	 	me.callParent(arguments);
	},
	addFirstLevel : function(){
		var quantitativeFieldset = this.up('fieldset');
		var me = this.up('riskclassbasemainfieldset');
		var btn = Ext.widget('button',{
        	text : '增加同级',
        	handler : me.addFirstLevel
        });
		quantitativeFieldset.add(Ext.widget('classbasefirstlevel'));
		quantitativeFieldset.add(btn);
		quantitativeFieldset.remove(this);
	}
});
