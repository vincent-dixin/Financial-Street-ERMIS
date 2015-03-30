Ext.define('FHD.ux.layout.multilayout.MultiTabPanel',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.multitabpanel',
    requires: [

              ],
              
    /**
	 * public
	 * 接口属性
	 */
    tabs:null,
    
    /**
	 * public
	 * ext属性
	 */
    plain: true,	//控制tab样式，右侧显示
    
    initComponent: function () {
        var me = this;
        
        Ext.apply(me, {
        	border : false,
        	tabBar:{//控制右侧显示
        		style : 'border-left: 1px  #99bce8 solid;'
        	},
            items: me.tabs
        });
        
        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
    }
});