/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.risk.RiskInfoTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.riskInfoTab',
    
    requires: [
               'FHD.view.industry.risk.RiskEdit'
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
        
    	me.riskEditContainer = Ext.create('Ext.container.Container', {id:'riskEditContainerId', title : '基础信息'});
    		//Ext.widget('riskEdit',{title : '基础信息'});
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.riskEditContainer]
        });
      

        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
        me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight() - 20);
    	});
    }
});