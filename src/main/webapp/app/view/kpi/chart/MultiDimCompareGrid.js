Ext.define('FHD.view.kpi.chart.MultiDimCompareGrid', {
    extend: 'FHD.ux.kpi.KpiGridPanel',
    alias: 'widget.multidimcomparegrid',

    title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
    
    
    initComponent: function() {
        var me = this;
        
        Ext.apply(me, {
        	url : __ctxPath + "/kpi/category/findcategoryrelakpi.f",
            extraParams:{
            	id : me.categoryid,
            	year : FHD.data.yearId,
            	month : FHD.data.monthId,
            	quarter : FHD.data.quarterId,
            	week : FHD.data.weekId,
            	eType : FHD.data.eType,
            	isNewValue : FHD.data.isNewValue,
            	tableType: me.tableType
            },
            type: 'scorecardmainpanel'
        });
        
        me.callParent(arguments);
    }
});