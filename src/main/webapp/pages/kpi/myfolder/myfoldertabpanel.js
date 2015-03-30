Ext.define('Ext.kpi.myfolder.myfoldertabpanel', {
    extend: 'Ext.tab.Panel',
    plain: true,
    border: true,
    
    
    initComponent: function () {
    	var me = this;
    	me.grid = Ext.create('FHD.ux.kpi.KpiGridPanel', {
            title: FHD.locale.get('fhd.kpi.kpi.form.kpilist'),
            url: me.kpiListUrl,
            checked: false,
            type:'myfolder'
            
        });
    	
    	Ext.applyIf(me, {
            width: FHD.getCenterPanelWidth() - 270,
            height: FHD.getCenterPanelHeight(),
            activeTab: 1,
            plain: me.plain,
            border: me.border,
            items: [
                    me.grid
            ],

            listeners: {
                afterrender: function (t, e) {
        			
                }
            }

        });

        me.callParent(arguments);
    }
    
    
});