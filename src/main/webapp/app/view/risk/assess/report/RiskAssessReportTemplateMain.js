/**
 * @author : 邓广义
 *  风险评价报告模板管理主面板
 */
Ext.define('FHD.view.risk.assess.report.RiskAssessReportTemplateMain',{
 	extend: 'Ext.panel.Panel',
 	alias : 'widget.riskassessreporttemplatemain',
    requires : [
                'FHD.view.risk.assess.report.RiskAssessReportTemplateTree',
                'FHD.view.risk.assess.report.RiskAssessReportTemplateRight'
             ],
	reloadData:function(){},
 	
    initComponent: function () {
    	var me = this;
    	
    	me.treePanel = Ext.widget('riskassessreporttemplatetree');
    	me.rightPanel = Ext.widget('riskassessreporttemplateright');
    	
    	Ext.apply(me, {
            border:false,
     		layout: {
     	        type: 'border',
     	        padding: '0 0 5	0'
     	    },
     	    items:[me.treePanel,me.rightPanel]
        });
    	
        me.callParent(arguments);
    }

});