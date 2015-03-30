/**
 *    @description 内控标准数据主页面，框架包括了 tree 和 列表
 *    @author 宋佳
 *    @since 2013-3-5
 */
Ext.define('FHD.view.risk.responseplan.RiskResponsePlanManage',{
	extend : 'Ext.panel.Panel',
    alias: 'widget.riskresponseplanmanage',
	requires: [
		'FHD.view.risk.responseplan.RiskResponsePlanForm'
    ],
	layout : {
                type: 'vbox',
                align: 'stretch'
              },
    margin: '7 10',
    autoHeight : true,
    autoScroll : true,
    border : false,
	initComponent : function() {
		var me = this;
    	me.riskresponseplanform = Ext.widget('riskresponseplanform');
	 	Ext.applyIf(me,{
	 		items : [me.riskresponseplanform]
	 	});
	 	me.callParent(arguments);
	}
});
