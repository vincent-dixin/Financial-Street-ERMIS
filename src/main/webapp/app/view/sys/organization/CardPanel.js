/**
 * 机构管理图片面板
 * 
 * @author 金鹏祥
 */
Ext.define('FHD.view.sys.organization.CardPanel', {
    extend: 'FHD.ux.CardPanel',
    alias: 'widget.cardPanel',
    
    requires: [
               'FHD.view.sys.organization.TabPanel',
               'FHD.view.sys.organization.emp.EmpEditPanel',
               'FHD.view.sys.organization.PositionTabPanel'
    ],
    
    // 初始化方法
    initComponent: function() {
    	var me = this;
    	
    	me.id = 'cardPanel';
    	me.empEditPanel = Ext.widget('empEditPanel');
    	me.tabPanel = Ext.widget('tabPanel');
    	me.positionTabPanel = Ext.widget('positionTabPanel');
    	me.tabPanel.getTabBar().insert(0, {
            xtype: 'tbfill'
       	});
    	me.positionTabPanel.getTabBar().insert(0, {
            xtype: 'tbfill'
       	});
    	
    	Ext.apply(me, {
    		 xtype: 'cardpanel',
             //bodyStyle: 'border-bottom: 1px solid #bec0c0 !important;',
             border:false,
             activeItem : 0,
//             region:'center',
             items:[
               me.tabPanel,me.empEditPanel,me.positionTabPanel
             ],
        });
    	
    	me.empEditPanel.on('resize',function(p){
    		me.empEditPanel.setHeight(FHD.getCenterPanelHeight() - 27);
    	});
    	
        me.callParent(arguments);
    }
});