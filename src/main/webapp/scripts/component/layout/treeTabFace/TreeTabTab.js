Ext.define('FHD.ux.layout.treeTabFace.TreeTabTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.treetabtab',
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
            items: me.tabs,
            //添加监听事件
		    listeners: {
		    	tabchange:function(tabPanel, newCard, oldCard, eOpts){
		    		//当点击tab页面回调函数
		    	    if(newCard.onClick){
		    	    	newCard.onClick();
		    	    }
		    	}
		    }
        });
        
        me.callParent(arguments);
        
        me.getTabBar().insert(0,{xtype:'tbfill'});
    }
});