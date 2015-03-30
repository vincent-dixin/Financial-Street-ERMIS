/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.kpi.KpiInfoTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.kpiInfoTab',
    
    requires: [
               'FHD.view.industry.kpi.KpiEdit'
              ],
              
    plain: true,
    
    getPanel : function(obj, widgetId){
    	if(obj.items.length == 0){
    		obj.add(Ext.widget(widgetId));
    	}
    },
    
    // 初始化方法
    initComponent: function() {
        var me = this;
        
    	me.kpiEditContainer = Ext.create('Ext.container.Container', {id:'kpiEditContainerId', title : '基础信息'});
    		//Ext.widget('kpiEdit',{title : '基础信息'});
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.kpiEditContainer]
        });
      

        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
        me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight() - 20);
    	});
    }
});