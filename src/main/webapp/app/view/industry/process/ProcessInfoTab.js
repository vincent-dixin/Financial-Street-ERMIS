/**
 * 
 * 行业主体内容面板
 */

Ext.define('FHD.view.industry.process.ProcessInfoTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.processInfoTab',
    
    requires: [
               'FHD.view.industry.process.ProcessEdit'
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
        
    	me.processEditContainer = Ext.create('Ext.container.Container', {id:'processEditContainerId', title : '基础信息'});
    		//Ext.widget('processEdit',{title : '基础信息'});
        
        Ext.apply(me, {
        	border:false,
        	activeItem : 0,
            items: [me.processEditContainer]
        });
      

        me.callParent(arguments);
        me.getTabBar().insert(0,{xtype:'tbfill'});
        me.on('resize',function(p){
    		me.setHeight(FHD.getCenterPanelHeight() - 20);
    	});
    }
});