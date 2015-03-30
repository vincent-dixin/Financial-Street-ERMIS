/**
 * @author : 邓广义
 *  风险评价报告模板管理
 */
Ext.define('FHD.view.risk.assess.report.RiskAssessReportTemplateTree',{
 	extend: 'FHD.ux.TreePanel',
 	alias : 'widget.riskassessreporttemplatetree',
    requires : [

             ],
	reloadData:function(){},
 	
    initComponent: function () {
    		var me = this;
        Ext.apply(me, {
    		rootVisible: false,
    		width:260,
    		split: true,
           	collapsible : true,
           	border:true,
           	region: 'west',
           	multiSelect: true,
           	rowLines:false,
          	singleExpand: false,
           	checked: false,
        	autoScroll:true,
        	url: __ctxPath + '/app/view/risk/assess/report/report.json',
    	    extraParams: {},
    	    root: {}
        });
        me.callParent(arguments);
    }

});