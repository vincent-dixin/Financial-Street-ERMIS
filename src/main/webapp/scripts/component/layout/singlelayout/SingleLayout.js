/**
 * 单表布局  左面tree，右侧主面板panel
 * @author 郑军祥
 */
Ext.define('FHD.ux.layout.singlelayout.SingleLayout', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.singlelayout',
    requires: [
               'FHD.ux.layout.singlelayout.SingleTab',
               'FHD.ux.layout.singlelayout.SingleContainer'
    ],
	
    /**
	 * public
	 * 接口属性
	 */
    tree:null,	//左侧tree
    tabs:[],	//右侧tab页面
    navigationBar:null,	//导航
    
    /**
	 * public
	 * ext属性
	 */
    frame: false,    
    // 布局
    layout: {
        type: 'border'
    },
    
    /**
	 * private
	 * 自定义属性
	 */
    container:null,	//右侧主面板
    tabpanel:null,//主面板下tabpanel
    
	/**
	 * public
	 * 接口方法
	 */
    
    /**
	 * private
	 * 自定义方法
	 */


    // 初始化方法
    initComponent: function() {
        var me = this;
 
        //创建左侧树
        Ext.apply(me.tree,{
        	height:FHD.getCenterPanelHeight(),
        	region: 'west',
        	//width: 210,
        	split:true,
        	collapsible: true
        });
        
        //创建右侧tab
        me.tabpanel = Ext.widget("singletab",{
        	tabs:me.tabs
        });
        
        //创建右侧主面板
        me.container = Ext.widget("singlecontainer",{
        	region: 'center',
         	tabpanel:me.tabpanel,
        	flex:1
        });

        Ext.apply(me, {
        	border : false,
            items: [me.tree,me.container]
		});
        me.callParent(arguments);
        
    }

});