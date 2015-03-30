/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.measureedit.RiskClassBaseMainFieldSet',{
	extend : 'Ext.form.FieldSet',
    alias: 'widget.riskclassbasemainfieldset',
	requires: [
      'FHD.view.risk.measureedit.ClassBaseFirstLevel',
      'FHD.view.risk.measureedit.QuantificationFirstLevel'
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
    	me.firstLevel = Ext.widget('classbasefirstlevel');
        me.addFirstLevelBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addFirstLevel()'>增加同级</a>"
        });
        //定性fieldset
        me.quantitativeFieldset = Ext.widget('fieldset', {
        	autoHeight : true,
        	checkboxToggle: true,
            title : '定性',
            items : [
            	me.firstLevel,
            	me.addFirstLevelBtn
            ]
        });
        me.addQuantificationFirstLevelBtn = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").addQuantificationFirstLevel()'>增加同级</a>"
        });
        me.quantificationfirstlevel = Ext.widget('quantificationfirstlevel');
        me.quantificationFieldset = Ext.widget('fieldset', {
        	checkboxToggle: true,
            title : '定量',
            items : [me.quantificationfirstlevel,me.addQuantificationFirstLevelBtn]
        });
		Ext.applyIf(me,{
			items:[me.quantitativeFieldset,me.quantificationFieldset]
		});
	 	me.callParent(arguments);
	},
	addFirstLevel : function(){
		var me = this;
		var quantitativeFieldset = me.quantitativeFieldset;
		quantitativeFieldset.insert(0,Ext.widget('classbasefirstlevel'));
	},
	addQuantificationFirstLevel : function(){
		var me = this;
		var quantificationFieldset = me.quantificationFieldset;
		quantificationFieldset.insert(0,Ext.widget('quantificationfirstlevel'));
	}
});
