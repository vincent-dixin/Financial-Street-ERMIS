/**
 * 
 * 风险上报标准
 * 使用card布局
 * 
 * 下级有两个组件 潜在风险上报标准、历史风险上报标准
 * 
 * @author 宋佳
 */
Ext.define('FHD.view.risk.riskedit.Quantification', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.quantification',
    requires: [
    	'FHD.ux.kpi.KpiSelector'
    ],
    autoHeight: true,
    autoWidth : true,
	layout: {
        type: 'hbox',
        align : 'stretch'
    },
    kpiname : '',
	initComponent: function() {
		/*定性 qualitative  定量quantification*/
		var me = this;
		//定性
		//定量
		me.quantification = Ext.widget('kpiselector', {
			name : 'quantification', 
			value: me.kpiname,
			labelText : '指标',
			width:300
		});
        me.delQuantification = Ext.widget('label',{
			html : "<a href='javascript:void(0)' onclick='Ext.getCmp(\""+me.id+"\").delSelf()'>删除</a>"
        });
        me.checkBoxGroup = Ext.widget('checkboxgroup',{
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
        Ext.apply(me, {
            items : [
            	me.quantification,
            	{
                   xtype: 'hiddenfield',
                   width : 50
               },
            	me.checkBoxGroup,
            	{
                   xtype:'tbspacer',
                   flex:1
                },
            	me.delQuantification
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