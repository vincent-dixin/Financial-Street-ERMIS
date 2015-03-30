/**
 * @author : 邓广义
 *  风险评价报告模板管理
 */
Ext.define('FHD.view.risk.assess.report.RiskAssessReportTemplateRight',{
 	extend: 'Ext.panel.Panel',
 	alias : 'widget.riskassessreporttemplateright',
    requires : [
                'FHD.view.risk.assess.report.RiskAssessReportTemplateTab',
                'FHD.view.risk.assess.report.RiskAssessReportTemplateForm'
             ],
	reloadData:function(){},
 	
    initComponent: function () {
    	var me = this;
    	me.formPanel = Ext.widget('riskassessreporttemplateform');
        Ext.apply(me, {
        	region:'center',
        	layout:{
                align: 'stretch',
                 type: 'vbox'
    		},
    		border: false,
    		items:[me.formPanel]
        });
    	me.callParent(arguments);
    }

});