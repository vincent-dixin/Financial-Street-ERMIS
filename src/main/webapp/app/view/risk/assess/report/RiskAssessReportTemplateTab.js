/**
 * @author : 邓广义
 *  风险评价报告模板管理
 */
Ext.define('FHD.view.risk.assess.report.RiskAssessReportTemplateTab',{
 	extend: 'Ext.tab.Panel',
 	alias : 'widget.riskassessreporttemplatetab',
    requires : [
               'FHD.view.risk.assess.report.RiskAssessReportTemplateForm'
             ],
	reloadData:function(){},
 	
    initComponent: function () {
    	var me = this;
    	me.formPanel = Ext.widget('riskassessreporttemplateform',{title:'模板信息'});
    	Ext.apply(me, {
            deferredRender: false,
            activeTab: 0,  
            items: [me.formPanel],
            plain: true
        });
    	me.callParent(arguments);
    }

});